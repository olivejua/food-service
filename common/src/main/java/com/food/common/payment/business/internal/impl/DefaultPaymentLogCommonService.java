package com.food.common.payment.business.internal.impl;

import com.food.common.payment.business.external.model.payrequest.PaymentElement;
import com.food.common.payment.business.internal.PaymentLogCommonService;
import com.food.common.payment.business.internal.model.PaymentLogDto;
import com.food.common.payment.domain.Payment;
import com.food.common.payment.domain.PaymentLog;
import com.food.common.payment.repository.PaymentLogRepository;
import com.food.common.user.business.internal.impl.PointEntityService;
import com.food.common.user.domain.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class DefaultPaymentLogCommonService implements PaymentLogCommonService {
    private final PaymentEntityService paymentEntityService;
    private final PointEntityService pointEntityService;
    private final PaymentLogRepository paymentLogRepository;

    @Override
    public void saveAll(Long paymentId, Set<PaymentElement> elements) {
        Payment payment = paymentEntityService.findById(paymentId);

        Set<PaymentLog> paymentLogs = elements.stream()
                .map(logRequest -> {
                    PaymentLog paymentLog = PaymentLog.create(payment, logRequest.method(), logRequest.getAmount());
                    logRequest.getPointId().ifPresent(pointId -> updatePoint(paymentLog, pointId));

                    return paymentLog;
                })
                .collect(Collectors.toSet());

        paymentLogRepository.saveAll(paymentLogs);
    }

    private void updatePoint(PaymentLog paymentLog, Long pointId) {
        Point point = pointEntityService.findById(pointId);
        paymentLog.update(point);
    }

    @Override
    public List<PaymentLogDto> findAllByPaymentId(Long paymentId) {
        Payment payment = paymentEntityService.findById(paymentId);
        return paymentLogRepository.findAllByPayment(payment).stream()
                .map(PaymentLogDto::new)
                .collect(Collectors.toList());
    }
}
