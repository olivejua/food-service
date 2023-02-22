package com.food.order.business;

import com.food.common.payment.business.external.PaymentAmountService;
import com.food.common.payment.business.internal.PaymentCommonService;
import com.food.common.payment.business.internal.PaymentLogCommonService;
import com.food.common.payment.business.internal.model.PaymentDto;
import com.food.common.payment.business.internal.model.PaymentLogDto;
import com.food.common.utils.Amount;
import com.food.order.error.NotFoundPaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultPaymentAmountService implements PaymentAmountService {
    private final PaymentCommonService paymentCommonService;
    private final PaymentLogCommonService paymentLogCommonService;

    @Override
    public Amount sumOfPaymentAmountWithoutPoints(Long paymentId) {
        PaymentDto findPayment = paymentCommonService.findById(paymentId)
                .orElseThrow(() -> new NotFoundPaymentException(paymentId));

        int totalAmount = paymentLogCommonService.findAllByPaymentId(findPayment.getId())
                .stream()
                .filter(paymentLog -> !paymentLog.hasMethodOfPoint())
                .mapToInt(PaymentLogDto::getAmount)
                .sum();

        return Amount.won(totalAmount);
    }
}
