package com.food.order.payment.stubrepository;

import com.food.common.payment.business.internal.PaymentCommonService;
import com.food.common.payment.business.internal.model.PaymentDto;
import com.food.common.payment.enumeration.PaymentActionType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StubPaymentService implements PaymentCommonService {
    private final Map<Long, PaymentDto> data = new HashMap<>();
    private Long autoIncrementKey = -1L;

    @Override
    public PaymentDto save(PaymentDto payment) {
        if (data.containsKey(payment.getId())) {
            data.put(payment.getId(), payment);
            return payment;
        }

        PaymentDto newOne = PaymentDto.builder()
                .id(autoIncrementKey--)
                .orderId(payment.getOrderId())
                .actionType(payment.getActionType())
                .build();
        data.put(newOne.getId(), newOne);

        return newOne;
    }

    @Override
    public void updateActionType(Long paymentId, PaymentActionType actionType) {
        PaymentDto payment = findById(paymentId)
                .map(findPayment -> new PaymentDto(findPayment.getId(), findPayment.getOrderId(), actionType))
                .orElseThrow(IllegalArgumentException::new);

        data.put(paymentId, payment);
    }

    @Override
    public boolean existsById(Long id) {
        return data.containsKey(id);
    }

    @Override
    public Optional<PaymentDto> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }
}
