package com.food.order.temp;

import com.food.common.order.domain.Order;
import com.food.common.order.repository.OrderRepository;
import com.food.common.payment.domain.Payment;
import com.food.common.payment.domain.PaymentLog;
import com.food.common.payment.enumeration.PaymentActionType;
import com.food.common.payment.repository.PaymentLogRepository;
import com.food.common.payment.repository.PaymentRepository;
import com.food.order.error.PaymentErrors;
import com.food.order.presentation.dto.request.PaymentDoRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DefaultPayService implements PayService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentLogRepository paymentLogRepository;

    @Override
    public Long pay(PaymentDoRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new NotFoundOrderException(request.getOrderId()));

        if (!request.hasSameTotalAmountAs(order.getAmount())) {
            throw new InvalidPaymentException(PaymentErrors.WRONG_PAYMENT_AMOUNT);
        }

        if (paymentRepository.existsById(order.getId())) {
            throw new DuplicatedPaymentException();
        }

        Payment payment = paymentRepository.save(Payment.create(order, PaymentActionType.PAYMENT));

        List<PaymentLog> paymentLogs = request.getItems().stream()
                .map(item -> PaymentLog.create(payment, item.getMethod(), item.getAmount()))
                .toList();
        paymentLogRepository.saveAll(paymentLogs);

        return payment.getId();
    }
}
