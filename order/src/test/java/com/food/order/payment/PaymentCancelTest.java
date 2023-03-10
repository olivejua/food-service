package com.food.order.payment;

import com.food.common.order.business.internal.dto.OrderDto;
import com.food.common.payment.business.internal.model.PaymentDto;
import com.food.order.payment.mock.MockOrder;
import com.food.order.payment.mock.MockPayment;
import com.food.order.payment.mock.MockPaymentLog;
import com.food.order.payment.mock.MockRequestUser;
import com.food.order.common.stub.StubOrderService;
import com.food.order.payment.stubrepository.StubPaymentLogService;
import com.food.order.payment.stubrepository.StubPaymentService;
import com.food.order.payment.stubrepository.StubPointService;
import com.food.order.business.DefaultPayService;
import com.food.order.error.InvalidPaymentActionTypeException;
import com.food.order.error.NotFoundPaymentException;
import com.food.common.payment.business.external.PayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.food.common.payment.enumeration.PaymentActionType.CANCELLATION;
import static com.food.common.payment.enumeration.PaymentActionType.PAYMENT;
import static com.food.common.payment.enumeration.PaymentMethod.CARD;
import static com.food.common.payment.enumeration.PaymentMethod.POINT;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentCancelTest {
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
    void paymentId???_???????????????_????????????_?????????_?????????_????????????() {
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
    void paymentId???_???????????????_actionType???_??????_??????????????????_?????????_????????????() {
        //given
        PaymentDto paymentCanceled = PaymentDto.builder()
                .actionType(CANCELLATION)
                .build();
        Long paymentId1 = stubPaymentService.save(paymentCanceled).getId();

        //when & then
        assertThrows(InvalidPaymentActionTypeException.class, () -> payService.cancel(paymentId1, mockRequestUser));

        //given
        PaymentDto paymentNotCanceled = PaymentDto.builder()
                .actionType(PAYMENT)
                .build();
        Long paymentId2 = stubPaymentService.save(paymentNotCanceled).getId();

        //when & then
        assertDoesNotThrow(() -> payService.cancel(paymentId2, mockRequestUser));
    }

    @Test
    void ???????????????_?????????_????????????() {
        //given
        PaymentDto payment = PaymentDto.builder()
                .actionType(PAYMENT)
                .build();
        Long paymentId = stubPaymentService.save(payment).getId();

        //when
        payService.cancel(paymentId, mockRequestUser);

        //then
        Optional<PaymentDto> findPaymentOptional = stubPaymentService.findById(paymentId);
        assertTrue(findPaymentOptional.isPresent());

        PaymentDto findPayment = findPaymentOptional.get();
        assertEquals(CANCELLATION, findPayment.getActionType());
    }

    @Test
    void ?????????_???????????????_??????????????????_????????????????????????_????????????_?????????_?????????_????????????() {
        //given
        OrderDto mockOrder = stubOrderService.save(MockOrder.create(30_000));
        PaymentDto mockPayment = stubPaymentService.save(MockPayment.create(mockOrder, PAYMENT));
        stubPaymentLogService.save(MockPaymentLog.create(mockPayment, POINT, 10_000));
        stubPaymentLogService.save(MockPaymentLog.create(mockPayment, CARD, 20_000));

        //when
        payService.cancel(mockPayment.getId(), mockRequestUser);

        //then
        assertTrue(stubPointService.isCalledToRecollect());
    }

    @Test
    void ?????????_???????????????_??????????????????_????????????????????????_????????????_?????????_?????????_?????????_????????????_?????????() {
        //given
        OrderDto mockOrder2 = stubOrderService.save(MockOrder.create(20_000));
        PaymentDto mockPayment2 = stubPaymentService.save(MockPayment.create(mockOrder2, PAYMENT));
        stubPaymentLogService.save(MockPaymentLog.create(mockPayment2, CARD, 20_000));

        //when
        payService.cancel(mockPayment2.getId(), mockRequestUser);

        //then
        assertFalse(stubPointService.isCalledToRecollect());
    }

    @Test
    void ?????????_???????????????_?????????_??????????????????_????????????_???????????????_?????????_????????????() {
        //given
        OrderDto mockOrder = stubOrderService.save(MockOrder.create(30_000));
        PaymentDto mockPayment = stubPaymentService.save(MockPayment.create(mockOrder, PAYMENT));
        stubPaymentLogService.save(MockPaymentLog.create(mockPayment, POINT, 10_000));
        stubPaymentLogService.save(MockPaymentLog.create(mockPayment, CARD, 20_000));

        //when
        payService.cancel(mockPayment.getId(), mockRequestUser);

        //then
        assertTrue(stubPointService.isCalledToRetrieve());
    }

    @Test
    void ?????????_???????????????_?????????_??????????????????_????????????_?????????_???????????????_?????????_????????????_?????????() {
        //given
        OrderDto mockOrder = stubOrderService.save(MockOrder.create(15_000));
        PaymentDto mockPayment = stubPaymentService.save(MockPayment.create(mockOrder, PAYMENT));
        stubPaymentLogService.save(MockPaymentLog.create(mockPayment, POINT, 15_000));

        //when
        payService.cancel(mockPayment.getId(), mockRequestUser);

        //then
        assertFalse(stubPointService.isCalledToRetrieve());
    }
}
