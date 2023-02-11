package com.food.order;

import com.food.common.order.domain.Order;
import com.food.common.order.repository.OrderRepository;
import com.food.common.payment.domain.Payment;
import com.food.common.payment.domain.PaymentLog;
import com.food.common.payment.enumeration.PaymentMethod;
import com.food.common.payment.repository.PaymentLogRepository;
import com.food.common.payment.repository.PaymentRepository;
import com.food.order.mock.MockOrder;
import com.food.order.mock.MockPayment;
import com.food.order.presentation.dto.request.PaymentDoRequest;
import com.food.order.temp.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentDoTest {
    /**
     * 결제하기 기능 요구사항
     * - 결제 금액, 결제수단과 함께 통보한다.
     * - 결제 금액과 결제수단은 필수값이다. (v)
     * - 결제 시 두개 이상의 결제 수단으로 요청할 수 있다. (포인트, 카드, 계좌이체) (v)
     * - DB에 결제요청한 주문서가 존재해야한다 (MSG: 유효하지 않은 주문서입니다.) (v)
     * - 주문서의 금액과 총 결제금액이 일치해야한다. (MSG: 잘못된 결제금액입니다.) (v)
     * - 중복 결제내역이 있으면 안된다 (v)
     * - DB에 결제 내역과 로그가 저장되어야한다. (v)
     * - 결제가 완료되면 주문 상태가 변경된다. (order service 호출)
     * - 결제가 완료되면 포인트가 적립된다. (point service 호출)
     *
     * - 구조 리팩토링한다.
     * - service 기능구현 완료하면 주석 작성한다.
     */

    private PayService payService;
    private OrderRepository orderRepository;
    private PaymentRepository paymentRepository;
    private PaymentLogRepository paymentLogRepository;

    @BeforeEach
    void setup() {
        orderRepository = new MemoryOrderRepository();
        paymentRepository = new MemoryPaymentRepository();
        paymentLogRepository = new MemoryPaymentLogRepository();

        payService = new DefaultPayService(orderRepository, paymentRepository, paymentLogRepository);
    }

    @Test
    void 결제요청한_주문서_데이터가_존재하지_않으면_실패한다() {
        //given
        PaymentDoRequest.Item requestItem = new PaymentDoRequest.Item(PaymentMethod.CARD, 30_000);
        Long orderId = givenOrderIdNotPresent();
        PaymentDoRequest request = new PaymentDoRequest(orderId, requestItem);

        //when & then
        NotFoundOrderException error = assertThrows(NotFoundOrderException.class, () -> payService.pay(request));
        assertTrue(error.getMessage().contains("orderId="+orderId));
    }

    private Long givenOrderIdNotPresent() {
        Long result = 1L;
        if (orderRepository.existsById(result)) {
            throw new IllegalArgumentException();
        }

        return result;
    }

    @Test
    void 주문서의_금액과_결제금액이_일치하지_않으면_실패한다() {
        //given
        Order mockOrder = MockOrder.builder()
                .id(1L)
                .amount(30_000)
                .build();
        orderRepository.save(mockOrder);

        PaymentDoRequest.Item requestItem = new PaymentDoRequest.Item(PaymentMethod.CARD, 40_000);
        PaymentDoRequest request = new PaymentDoRequest(mockOrder.getId(), requestItem);

        //when & then
        assertThrows(InvalidPaymentException.class, () -> payService.pay(request));

        //given
        PaymentDoRequest.Item requestItem2 = new PaymentDoRequest.Item(PaymentMethod.CARD, 20_000);
        PaymentDoRequest request2 = new PaymentDoRequest(mockOrder.getId(), requestItem2);

        assertThrows(InvalidPaymentException.class, () -> payService.pay(request2));
    }

    @Test
    void 중복_결제데이터가_존재하면_실패한다() {
        //given
        Order mockOrder = orderRepository.save(MockOrder.with(1L));
        Payment payment = MockPayment.builder()
                .id(1L)
                .order(mockOrder)
                .build();
        paymentRepository.save(payment);

        PaymentDoRequest.Item requestItem = new PaymentDoRequest.Item(PaymentMethod.CARD, mockOrder.getAmount());
        PaymentDoRequest request = new PaymentDoRequest(mockOrder.getId(), requestItem);

        //when & then
        DuplicatedPaymentException exception = assertThrows(
                DuplicatedPaymentException.class,
                () -> payService.pay(request));
        assertTrue(exception.getMessage().contains("해당 주문서에 대한 결제내역이 존재합니다."));
    }

    @Test
    void 결제데이터와_디테일로그데이터를_저장한다() {
        Order mockOrder = orderRepository.save(MockOrder.with(1L));
        PaymentDoRequest.Item requestItem1 = new PaymentDoRequest.Item(PaymentMethod.CARD, 10_000);
        PaymentDoRequest.Item requestItem2 = new PaymentDoRequest.Item(PaymentMethod.ACCOUNT_TRANSFER, mockOrder.getAmount() - 10_000);
        PaymentDoRequest request = new PaymentDoRequest(mockOrder.getId(), requestItem1, requestItem2);

        Long savedPaymentId = payService.pay(request);

        Optional<Payment> findPayment = paymentRepository.findById(savedPaymentId);
        assertTrue(findPayment.isPresent());
        Payment payment = findPayment.get();

        assertEquals(mockOrder.getId(), payment.getOrder().getId());

        List<PaymentLog> paymentLogs = paymentLogRepository.findAllByPaymentId(savedPaymentId);

        assertEquals(request.totalPaymentAmount(), getTotalAmountOfPaymentLogs(paymentLogs));
    }

    private int getTotalAmountOfPaymentLogs(List<PaymentLog> paymentLogs) {
        return paymentLogs.stream()
                .mapToInt(PaymentLog::getAmount)
                .sum();
    }
}
