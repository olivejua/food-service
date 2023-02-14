package com.food.order;

import com.food.common.order.business.internal.dto.OrderDto;
import com.food.common.payment.business.internal.model.PaymentLogDto;
import com.food.common.payment.enumeration.PaymentMethod;
import com.food.order.error.DuplicatedPaymentException;
import com.food.order.error.InvalidPaymentException;
import com.food.order.error.NotFoundOrderException;
import com.food.order.error.PaymentErrors;
import com.food.order.mock.MockOrder;
import com.food.order.mock.MockPayment;
import com.food.order.mock.MockRequestUser;
import com.food.order.presentation.dto.request.PaymentDoRequest;
import com.food.order.stubrepository.StubOrderService;
import com.food.order.stubrepository.StubPaymentLogService;
import com.food.order.stubrepository.StubPaymentService;
import com.food.order.stubrepository.StubPointService;
import com.food.order.temp.DefaultPayService;
import com.food.order.temp.PayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.food.common.utils.CollectionUtils.mappedBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentDoTest {
    /**
     * 결제하기 기능 요구사항
     * - 결제 금액, 결제수단과 함께 통보한다.
     * - 결제 금액과 결제수단은 필수값이다. (v)
     * - 결제 시 두개 이상의 결제 수단으로 요청할 수 있다. (포인트, 카드, 계좌이체) (v)
     * - DB에 결제요청한 주문서가 존재해야한다 (MSG: 유효하지 않은 주문서입니다.) (v)
     * - 주문서의 금액과 총 결제금액이 일치해야한다. (MSG: 잘못된 결제금액입니다.) (v)
     * - 중복 결제내역이 있으면 안된다. (v)
     * - 결제수단에 포인트가 존재하면 잔여포인트를 차감한다. (잔여포인트가 충분해야한다. MSG: 잔여포인트가 부족합니다.) (point service 호출)
     * - DB에 결제 내역과 로그가 저장되어야한다. (v)
     * - 결제가 완료되면 주문 상태가 변경된다. (order service 호출)
     * - 결제가 완료되면 포인트가 적립된다. (point service 호출)
     *
     * - 구조 리팩토링한다. (v)
     * - service 기능구현 완료하면 주석 작성한다. (v)
     * - 리팩토링시 EntityDto와 Entity명칭 좀 더 상징적인 것으로 변경한다.
     */

    private PayService payService;
    private StubOrderService stubOrderService;
    private StubPaymentService stubPaymentService;
    private StubPaymentLogService stubPaymentLogService;
    private StubPointService stubPointService;
    private MockRequestUser mockRequestUser;

    @BeforeEach
    void setup() {
        stubOrderService = new StubOrderService();
        stubPaymentService = new StubPaymentService();
        stubPaymentLogService = new StubPaymentLogService();
        stubPointService = new StubPointService();
        mockRequestUser = new MockRequestUser(1L);

        payService = new DefaultPayService(stubOrderService, stubPaymentService, stubPaymentLogService, stubPointService);
    }

    @Test
    void 결제요청한_주문서_데이터가_존재하지_않으면_실패한다() {
        //given
        PaymentDoRequest.Item requestItem = new PaymentDoRequest.Item(PaymentMethod.CARD, 30_000);
        Long orderId = givenOrderIdNotPresent();
        PaymentDoRequest request = new PaymentDoRequest(orderId, requestItem);

        //when & then
        NotFoundOrderException error = assertThrows(NotFoundOrderException.class, () -> payService.pay(request, null));
        assertTrue(error.getMessage().contains("orderId="+orderId));
    }

    private Long givenOrderIdNotPresent() {
        Long result = 1L;
        if (stubOrderService.existsById(result)) {
            throw new IllegalArgumentException();
        }

        return result;
    }

    @Test
    void 주문서의_금액과_결제금액이_일치하지_않으면_실패한다() {
        //given
        OrderDto mockOrder = savedMockOrder(30_000);

        PaymentDoRequest.Item requestItem = new PaymentDoRequest.Item(PaymentMethod.CARD, 40_000);
        PaymentDoRequest request = new PaymentDoRequest(mockOrder.getId(), requestItem);

        //when & then
        assertThrows(InvalidPaymentException.class, () -> payService.pay(request, null));

        //given
        PaymentDoRequest.Item requestItem2 = new PaymentDoRequest.Item(PaymentMethod.CARD, 20_000);
        PaymentDoRequest request2 = new PaymentDoRequest(mockOrder.getId(), requestItem2);

        InvalidPaymentException exception = assertThrows(InvalidPaymentException.class, () -> payService.pay(request2, null));
        assertEquals(PaymentErrors.WRONG_PAYMENT_AMOUNT.getMessage(), exception.getMessage());
    }

    @Test
    void 중복_결제데이터가_존재하면_실패한다() {
        //given
        OrderDto mockOrder = savedMockOrder();
        stubPaymentService.save(MockPayment.create(mockOrder));

        PaymentDoRequest.Item requestItem = new PaymentDoRequest.Item(PaymentMethod.CARD, mockOrder.getAmount());
        PaymentDoRequest request = new PaymentDoRequest(mockOrder.getId(), requestItem);

        //when & then
        DuplicatedPaymentException exception = assertThrows(
                DuplicatedPaymentException.class,
                () -> payService.pay(request, null));
        assertTrue(exception.getMessage().contains("해당 주문서에 대한 결제내역이 존재합니다."));
    }

    @Test
    void 결제데이터와_디테일로그데이터를_저장한다() {
        //given
        OrderDto mockOrder = savedMockOrder();

        PaymentDoRequest.Item requestItem1 = new PaymentDoRequest.Item(PaymentMethod.CARD, 10_000);
        PaymentDoRequest.Item requestItem2 = new PaymentDoRequest.Item(PaymentMethod.ACCOUNT_TRANSFER, mockOrder.getAmount() - 10_000);
        PaymentDoRequest request = new PaymentDoRequest(mockOrder.getId(), requestItem1, requestItem2);

        //when
        Long savedPaymentId = payService.pay(request, null);

        //then
        boolean exists = stubPaymentService.existsById(savedPaymentId);
        assertTrue(exists);

        assertFalse(stubPointService.isCalledToUse()); //Point 사용 요청이 없었으니 호출도 없어야한다.

        List<PaymentLogDto> paymentLogs = stubPaymentLogService.findAllByPaymentId(savedPaymentId);
        assertEquals(request.getItems().size(), paymentLogs.size());
        assertThat(mappedBy(request.getItems(), PaymentDoRequest.Item::getMethod))
                .containsExactlyInAnyOrder(PaymentMethod.CARD, PaymentMethod.ACCOUNT_TRANSFER);

        assertEquals(request.totalPaymentAmount(), getTotalAmountOfPaymentLogs(paymentLogs));


        //given: 포인트로 결제한다.
        OrderDto mockOrder2 = stubOrderService.save(MockOrder.create(12_000));
        PaymentDoRequest.Item requestItem3 = new PaymentDoRequest.Item(PaymentMethod.POINT, mockOrder2.getAmount());
        PaymentDoRequest request2 = new PaymentDoRequest(mockOrder2.getId(), requestItem3);

        //when
        Long savedPaymentId2 = payService.pay(request2, mockRequestUser);

        //then
        assertTrue(stubPointService.isCalledToUse());

        List<PaymentLogDto> paymentLogs2 = stubPaymentLogService.findAllByPaymentId(savedPaymentId2);
        assertEquals(1, paymentLogs2.size());
        PaymentLogDto pointPaymentLog = paymentLogs2.get(0);
        assertEquals(savedPaymentId2, pointPaymentLog.getPaymentId());
        assertNotNull(pointPaymentLog.getPointId());
    }

//    @Test
//    void 결제가_완료되면_포인트사용금액을_제외한_총금액으로_포인트적립_요청한다() {
//        OrderDto mockOrder = stubOrderService.save(MockOrder.create());
//        PaymentDoRequest.Item requestItem1 = new PaymentDoRequest.Item(PaymentMethod.CARD, 10_000);
//
//
//        assertTrue(stubPointService.isCalledToCollect());
//    }

    private OrderDto savedMockOrder(int amount) {
        return stubOrderService.save(MockOrder.create(amount));
    }

    private OrderDto savedMockOrder() {
        return stubOrderService.save(MockOrder.create());
    }

    private int getTotalAmountOfPaymentLogs(List<PaymentLogDto> paymentLogs) {
        return paymentLogs.stream()
                .mapToInt(PaymentLogDto::getAmount)
                .sum();
    }
}
