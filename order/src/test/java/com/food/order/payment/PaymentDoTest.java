package com.food.order.payment;

import com.food.common.order.business.internal.dto.OrderDto;
import com.food.common.payment.business.internal.model.PaymentLogDto;
import com.food.common.payment.enumeration.PaymentMethod;
import com.food.order.error.DuplicatedPaymentException;
import com.food.order.error.InvalidPaymentException;
import com.food.order.error.NotFoundOrderException;
import com.food.order.error.PaymentErrors;
import com.food.order.payment.mock.MockOrder;
import com.food.order.payment.mock.MockPayment;
import com.food.order.payment.mock.MockRequestUser;
import com.food.common.payment.business.external.model.PaymentDoRequest;
import com.food.order.payment.stubrepository.StubOrderService;
import com.food.order.payment.stubrepository.StubPaymentLogService;
import com.food.order.payment.stubrepository.StubPaymentService;
import com.food.order.payment.stubrepository.StubPointService;
import com.food.order.business.DefaultPayService;
import com.food.common.payment.business.external.PayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.food.common.utils.CollectionUtils.mappedBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentDoTest {

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
        assertThrows(InvalidPaymentException.class, () -> payService.pay(request, mockRequestUser));

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
                () -> payService.pay(request, mockRequestUser));
        assertTrue(exception.getMessage().contains("해당 주문서에 대한 결제내역이 존재합니다."));
    }

    @Test
    void 카드와_계좌이체로_결제시_결제데이터와_디테일로그데이터를_정상적으로_저장한다() {
        //given
        OrderDto mockOrder = savedMockOrder();

        PaymentDoRequest.Item requestItem1 = new PaymentDoRequest.Item(PaymentMethod.CARD, 10_000);
        PaymentDoRequest.Item requestItem2 = new PaymentDoRequest.Item(PaymentMethod.ACCOUNT_TRANSFER, mockOrder.getAmount() - 10_000);
        PaymentDoRequest request = new PaymentDoRequest(mockOrder.getId(), requestItem1, requestItem2);

        //when
        Long savedPaymentId = payService.pay(request, mockRequestUser);

        //then
        boolean exists = stubPaymentService.existsById(savedPaymentId);
        assertTrue(exists);

        assertFalse(stubPointService.isCalledToUse()); //Point 사용 요청이 없었으니 호출도 없어야한다.

        List<PaymentLogDto> paymentLogs = stubPaymentLogService.findAllByPaymentId(savedPaymentId);
        assertEquals(request.getItems().size(), paymentLogs.size());
        assertThat(mappedBy(request.getItems(), PaymentDoRequest.Item::getMethod))
                .containsExactlyInAnyOrder(PaymentMethod.CARD, PaymentMethod.ACCOUNT_TRANSFER);

        assertEquals(request.totalPaymentAmount(), getTotalAmountOfPaymentLogs(paymentLogs));
    }

    @Test
    void 포인트로_결제시_포인트아이디가_추가로_부여된다() {
        //given: 포인트로 결제한다.
        OrderDto mockOrder = stubOrderService.save(MockOrder.create(12_000));
        PaymentDoRequest.Item requestItem = new PaymentDoRequest.Item(PaymentMethod.POINT, mockOrder.getAmount());
        PaymentDoRequest request = new PaymentDoRequest(mockOrder.getId(), requestItem);

        //when
        Long savedPaymentId = payService.pay(request, mockRequestUser);

        //then
        assertTrue(stubPointService.isCalledToUse());

        List<PaymentLogDto> paymentLogs = stubPaymentLogService.findAllByPaymentId(savedPaymentId);
        assertEquals(1, paymentLogs.size());
        PaymentLogDto pointPaymentLog = paymentLogs.get(0);
        assertNotNull(pointPaymentLog.getPointId());
    }

    @Test
    void 포인트로_결제하지않으면_포인트아이디가_추가로_부여되지_않는다() {
        //given
        OrderDto mockOrder2 = savedMockOrder();

        PaymentDoRequest.Item requestItem1 = new PaymentDoRequest.Item(PaymentMethod.CARD, 10_000);
        PaymentDoRequest.Item requestItem2 = new PaymentDoRequest.Item(PaymentMethod.ACCOUNT_TRANSFER, mockOrder2.getAmount() - 10_000);
        PaymentDoRequest request = new PaymentDoRequest(mockOrder2.getId(), requestItem1, requestItem2);

        //when
        Long savedPaymentId2 = payService.pay(request, mockRequestUser);

        //then
        assertFalse(stubPointService.isCalledToUse()); //Point 사용 요청이 없었으니 호출도 없어야한다.

        List<PaymentLogDto> paymentLogs = stubPaymentLogService.findAllByPaymentId(savedPaymentId2);
        assertEquals(request.getItems().size(), paymentLogs.size());
        for (PaymentLogDto paymentLogDto : paymentLogs) {
            assertNull(paymentLogDto.getPointId());
        }
    }

    @Test
    void 결제가_완료되면_포인트적립_요청한다() {
        OrderDto mockOrder = stubOrderService.save(MockOrder.create());
        PaymentDoRequest.Item requestItem = new PaymentDoRequest.Item(PaymentMethod.CARD, mockOrder.getAmount());
        PaymentDoRequest request = new PaymentDoRequest(mockOrder.getId(), requestItem);

        payService.pay(request, mockRequestUser);

        assertTrue(stubPointService.isCalledToCollect());
    }


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
