package com.food.order;

import com.food.common.payment.business.internal.model.PaymentDto;
import com.food.common.payment.enumeration.PaymentActionType;
import com.food.order.mock.MockRequestUser;
import com.food.order.stubrepository.StubOrderService;
import com.food.order.stubrepository.StubPaymentLogService;
import com.food.order.stubrepository.StubPaymentService;
import com.food.order.stubrepository.StubPointService;
import com.food.order.temp.DefaultPayService;
import com.food.order.temp.InvalidPaymentActionTypeException;
import com.food.order.temp.NotFoundPaymentException;
import com.food.order.temp.PayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentCancelTest {
    /**
     * 요청정보인 PaymentId의 결제정보가 존재해야한다. (v)
     * 결제 정보의 ActionType이 '취소'이면 취소처리에 실패한다. (v)
     * 결제 정보의 상태를 '취소'로 변경한다.
     * 포인트 사용 금액이 있다면 재적립한다.
     * 포인트 적립 금액이 있다면 회수한다.
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
    void paymentId의_결제정보가_존재하지_않으면_예외가_발생한다() {
        //given
        Long paymentId = givenPaymentIdNotPresent();

        //when & then
        NotFoundPaymentException error = assertThrows(NotFoundPaymentException.class, () -> payService.cancel(paymentId, mockRequestUser));
        assertTrue(error.getMessage().contains("paymentId="+paymentId));

        //given & when & then
        assertDoesNotThrow(() -> payService.cancel(givenPaymentIdPresent(), mockRequestUser));
    }

    private Long givenPaymentIdNotPresent() {
        Long result = 1L;
        boolean exists = stubPaymentService.existsById(result);
        if (exists) {
            throw new IllegalArgumentException();
        }

        return result;
    }

    private Long givenPaymentIdPresent() {
        PaymentDto payment = PaymentDto.builder()
                .build();
        return stubPaymentService.save(payment).getId();
    }

    @Test
    void paymentId의_결제정보의_actionType이_이미_취소상태이면_예외가_발생한다() {
        //given
        PaymentDto paymentCanceled = PaymentDto.builder()
                .actionType(PaymentActionType.CANCELLATION)
                .build();
        Long paymentId1 = stubPaymentService.save(paymentCanceled).getId();

        //when & then
        assertThrows(InvalidPaymentActionTypeException.class, () -> payService.cancel(paymentId1, mockRequestUser));

        //given
        PaymentDto paymentNotCanceled = PaymentDto.builder()
                .actionType(PaymentActionType.PAYMENT)
                .build();
        Long paymentId2 = stubPaymentService.save(paymentNotCanceled).getId();

        //when & then
        assertDoesNotThrow(() -> payService.cancel(paymentId2, mockRequestUser));
    }

    @Test
    void 결제상태를_취소로_변경한다() {
        //given
        PaymentDto payment = PaymentDto.builder()
                .actionType(PaymentActionType.PAYMENT)
                .build();
        Long paymentId = stubPaymentService.save(payment).getId();

        //when
        payService.cancel(paymentId, mockRequestUser);

        //then
        Optional<PaymentDto> findPaymentOptional = stubPaymentService.findById(paymentId);
        assertTrue(findPaymentOptional.isPresent());

        PaymentDto findPayment = findPaymentOptional.get();
        assertEquals(PaymentActionType.CANCELLATION, findPayment.getActionType());
    }
}
