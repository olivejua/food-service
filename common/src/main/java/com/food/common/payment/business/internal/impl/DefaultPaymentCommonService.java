package com.food.common.payment.business.internal.impl;

import com.food.common.order.business.internal.impl.OrderEntityService;
import com.food.common.order.domain.Order;
import com.food.common.payment.business.internal.PaymentCommonService;
import com.food.common.payment.business.internal.model.PaymentDto;
import com.food.common.payment.domain.Payment;
import com.food.common.payment.enumeration.PaymentActionType;
import com.food.common.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class DefaultPaymentCommonService implements PaymentCommonService {
    private final PaymentRepository paymentRepository;
    private final OrderEntityService orderEntityService;

    @Override
    public PaymentDto save(PaymentDto paymentDto) {
        Order order = orderEntityService.findById(paymentDto.getOrderId());
        Payment payment = Payment.create(order, paymentDto.getActionType());

        return new PaymentDto(paymentRepository.save(payment));
    }

    @Override
    public void updateActionType(Long paymentId, PaymentActionType actionType) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("결제 내역이 존재하지 않습니다. paymentId=" + paymentId));

        payment.update(actionType);
    }

    @Override
    public boolean existsById(Long id) {
        return paymentRepository.existsById(id);
    }

    @Override
    public Optional<PaymentDto> findById(Long id) {
        return paymentRepository.findById(id)
                .map(PaymentDto::new);
    }
}
