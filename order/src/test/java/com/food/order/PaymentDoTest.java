package com.food.order;

import com.food.common.order.repository.OrderRepository;
import com.food.common.payment.enumeration.PaymentMethod;
import com.food.order.mock.MockOrder;
import com.food.order.presentation.dto.request.PaymentDoRequest;
import com.food.order.temp.DefaultPayService;
import com.food.order.temp.MemoryOrderRepository;
import com.food.order.temp.NotFoundOrderException;
import com.food.order.temp.PayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentDoTest {
    /**
     * 결제하기 기능 요구사항
     * - 결제 금액, 결제수단과 함께 통보한다.
     * - 결제 금액과 결제수단은 필수값이다. (v)
     * - 결제 시 두개 이상의 결제 수단으로 요청할 수 있다. (포인트, 카드, 계좌이체) (v)
     * - DB에 결제요청한 주문서가 존재해야한다 (MSG: 유효하지 않은 주문서입니다.) (v)
     * - 주문서의 금액과 총 결제금액이 일치해야한다. (MSG: 잘못된 결제금액입니다.)
     * - DB에 결제 내역과 로그가 저장되어야한다.
     * - 결제가 완료되면 주문 상태가 변경된다. (order service 호출)
     * - 결제가 완료되면 포인트가 적립된다. (point service 호출)
     */

    private PayService payService;
    private OrderRepository orderRepository;

    @BeforeEach
    void setup() {
        orderRepository = new MemoryOrderRepository();

        payService = new DefaultPayService(orderRepository);
    }

    @Test
    void 결제요청한_주문서_데이터가_존재하지_않으면_실패한다() {
        PaymentDoRequest.Item requestItem = new PaymentDoRequest.Item(PaymentMethod.CARD, 30_000);
        Long orderId = givenOrderIdNotPresent();
        PaymentDoRequest request = new PaymentDoRequest(orderId, requestItem);

        NotFoundOrderException error = assertThrows(NotFoundOrderException.class, () -> payService.pay(request));
        assertTrue(error.getMessage().contains("orderId="+orderId));

        orderRepository.save(MockOrder.create(orderId));
        assertDoesNotThrow(() -> payService.pay(request));
    }

    @Test
    void 주문서의_금액과_결제금액이_일치하지_않으면_실패한다() {

    }

    private Long givenOrderIdNotPresent() {
        Long result = 1L;
        if (orderRepository.existsById(result)) {
            throw new IllegalArgumentException();
        }

        return result;
    }
}
