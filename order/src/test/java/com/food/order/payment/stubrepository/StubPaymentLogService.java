package com.food.order.payment.stubrepository;

import com.food.common.payment.business.external.model.payrequest.PaymentElement;
import com.food.common.payment.business.internal.PaymentLogCommonService;
import com.food.common.payment.business.internal.model.PaymentLogDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StubPaymentLogService implements PaymentLogCommonService {
    private final Map<Long, PaymentLogDto> data = new HashMap<>();
    private Long autoIncrementKey = -1L;

    public PaymentLogDto save(PaymentLogDto paymentLog) {
        if (data.containsKey(paymentLog.getId())) {
            data.put(paymentLog.getId(), paymentLog);
            return paymentLog;
        }

        PaymentLogDto newOne = PaymentLogDto.builder()
                .id(autoIncrementKey--)
                .paymentId(paymentLog.getPaymentId())
                .method(paymentLog.getMethod())
                .amount(paymentLog.getAmount())
                .pointId(paymentLog.getPointId())
                .build();
        data.put(newOne.getId(), newOne);

        return newOne;
    }

    @Override
    public void saveAll(Long paymentId, Set<PaymentElement> elements) {
        for (PaymentElement element : elements) {
            PaymentLogDto paymentLogDto = PaymentLogDto.builder()
                    .paymentId(paymentId)
                    .method(element.method())
                    .amount(element.getAmount())
                    .pointId(element.getPointId().orElse(null))
                    .build();

            save(paymentLogDto);
        }
    }

    public void saveAll(PaymentLogDto... paymentLogs) {
        for (PaymentLogDto paymentLog : paymentLogs) {
            save(paymentLog);
        }
    }

    @Override
    public List<PaymentLogDto> findAllByPaymentId(Long paymentId) {
        return data.values().stream()
                .filter(log -> log.getPaymentId().equals(paymentId))
                .collect(Collectors.toList());
    }
}
