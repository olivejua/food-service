package com.food.order;

import com.food.common.order.business.internal.dto.OrderDto;
import com.food.common.payment.business.external.PaymentAmountService;
import com.food.common.payment.business.internal.model.PaymentDto;
import com.food.common.payment.business.internal.model.PaymentLogDto;
import com.food.common.payment.enumeration.PaymentActionType;
import com.food.common.payment.enumeration.PaymentMethod;
import com.food.common.utils.Amount;
import com.food.order.business.DefaultPaymentAmountService;
import com.food.order.error.NotFoundPaymentException;
import com.food.order.mock.MockOrder;
import com.food.order.mock.MockPaymentLog;
import com.food.order.stubrepository.StubOrderService;
import com.food.order.stubrepository.StubPaymentLogService;
import com.food.order.stubrepository.StubPaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentAmountTest {
    private PaymentAmountService paymentAmountService;
    private StubOrderService stubOrderService;
    private StubPaymentService stubPaymentService;
    private StubPaymentLogService stubPaymentLogService;

    @BeforeEach
    void setup() {
        stubOrderService = new StubOrderService();
        stubPaymentService = new StubPaymentService();
        stubPaymentLogService = new StubPaymentLogService();

        paymentAmountService = new DefaultPaymentAmountService(stubPaymentService, stubPaymentLogService);
    }

    @Test
    void 요청결제ID의_결제데이터가_존재하지_않으면_예외가_발생한다() {
        assertThrows(NotFoundPaymentException.class, () -> paymentAmountService.sumOfPaymentAmountWithoutPoints(givenPaymentIdNotPresent()));

        assertDoesNotThrow(() -> paymentAmountService.sumOfPaymentAmountWithoutPoints(givenPaymentPresent()));
    }

    @Test
    void 결제수단_중_포인트_금액을_제외한_금액의_합을_리턴한다() {
        //given
        PaymentDto mockPayment1 = stubPaymentService.findById(givenPaymentPresent(20_000)).get();
        PaymentLogDto mockPointPayment1 = MockPaymentLog.create(mockPayment1, PaymentMethod.POINT, 20_000);
        stubPaymentLogService.saveAll(mockPointPayment1);

        //when
        Amount result1 = paymentAmountService.sumOfPaymentAmountWithoutPoints(mockPayment1.getId());

        //then
        Assertions.assertEquals(Amount.zero(), result1);


        //given
        PaymentDto mockPayment2 = stubPaymentService.findById(givenPaymentPresent(30_000)).get();
        PaymentLogDto mockCardPayment2 = MockPaymentLog.create(mockPayment2, PaymentMethod.CARD, 25_000);
        PaymentLogDto mockPointPayment2 = MockPaymentLog.create(mockPayment2, PaymentMethod.POINT, 5_000);
        stubPaymentLogService.saveAll(mockCardPayment2, mockPointPayment2);

        // when
        Amount result2 = paymentAmountService.sumOfPaymentAmountWithoutPoints(mockPayment2.getId());

        //then
        Assertions.assertEquals(Amount.won(25_000), result2);
    }

    private Long givenPaymentIdNotPresent() {
        Long result = 1L;
        boolean exists = stubPaymentService.existsById(result);
        if (exists) {
            throw new IllegalArgumentException();
        }

        return result;
    }

    private Long givenPaymentPresent() {
        PaymentDto payment = PaymentDto.builder()
                .orderId(MockOrder.create().getId())
                .actionType(PaymentActionType.PAYMENT)
                .build();
        return stubPaymentService.save(payment).getId();
    }

    private Long givenPaymentPresent(int amount) {
        OrderDto mockOrder = MockOrder.create(amount);
        stubOrderService.save(mockOrder);

        PaymentDto payment = PaymentDto.builder()
                .orderId(mockOrder.getId())
                .actionType(PaymentActionType.PAYMENT)
                .build();
        return stubPaymentService.save(payment).getId();
    }
}
