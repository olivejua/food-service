package com.food.order.business;

import com.food.common.order.business.internal.OrderCommonService;
import com.food.common.order.business.internal.dto.OrderDto;
import com.food.common.payment.business.external.PayService;
import com.food.common.payment.business.external.model.PayRequest;
import com.food.common.payment.business.external.model.payrequest.PointPayment;
import com.food.common.payment.business.internal.PaymentCommonService;
import com.food.common.payment.business.internal.PaymentLogCommonService;
import com.food.common.payment.business.internal.model.PaymentDto;
import com.food.common.payment.business.internal.model.PaymentLogDto;
import com.food.common.payment.enumeration.PaymentActionType;
import com.food.common.payment.enumeration.PaymentMethod;
import com.food.common.user.business.external.PointService;
import com.food.common.user.business.external.model.PointCollectRequest;
import com.food.common.user.business.external.model.RequestUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class DefaultPayService implements PayService {
    private final OrderCommonService orderCommonService;
    private final PaymentCommonService paymentCommonService;
    private final PaymentLogCommonService paymentLogCommonService;
    private final PointService pointService;

    public Long pay(final PayRequest payment) {
        OrderDto order = orderCommonService.findById(payment.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문내역이 존재하지 않습니다. orderId=" + payment.getOrderId()));

        if (payment.hasDifferentTotalAmountAs(order)) {
            throw new IllegalArgumentException(
                    String.format("주문금액과 결제금액이 일치하지 않습니다. 주문금액: %d, 결제금액: %d", order.getAmount(), payment.getTotalAmount()));
        }

        usePoints(payment);

        PaymentDto paymentDto = PaymentDto.builder()
                .orderId(payment.getOrderId())
                .actionType(payment.getActionType())
                .build();
        Long savedPaymentId = paymentCommonService.save(paymentDto).getId();
        paymentLogCommonService.saveAll(savedPaymentId, payment.getPayments());

        collectPoints(payment, savedPaymentId);

        return savedPaymentId;
    }

    private void usePoints(PayRequest payment) {
        Optional<PointPayment> findPointPayment = payment.findPointPayment();
        if(findPointPayment.isPresent()) {
            PointPayment paymentPoint = findPointPayment.get();
            Long usedPointId = pointService.use(paymentPoint.toPointsUseRequest(payment.getPayerId()));
            paymentPoint.updateUsedPointId(usedPointId);
        }
    }

    private void collectPoints(PayRequest payment, Long paymentId) {
        int actualPaymentAmount = payment.getActualPaymentAmount();
        if (actualPaymentAmount == 0) return;

        PointCollectRequest request = new PointCollectRequest(payment.getPayerId(), paymentId, actualPaymentAmount);
        pointService.collect(request);
    }

    @Override
    public void cancelPayment(Long paymentId, RequestUser requestUser) {
        paymentCommonService.updateActionType(paymentId, PaymentActionType.CANCELLATION);

        List<PaymentLogDto> paymentLogs = paymentLogCommonService.findAllByPaymentId(paymentId);
        Optional<PaymentLogDto> pointPaymentLog = filterPointPaymentLog(paymentLogs);

        pointPaymentLog
                .ifPresent(paymentLog -> pointService.recollectUsedPoint(paymentLog.getPointId()));

        pointService.retrieveCollectedPoint(paymentId);
    }

    private Optional<PaymentLogDto> filterPointPaymentLog(List<PaymentLogDto> paymentLogs) {
        return paymentLogs.stream()
                .filter(log -> log.getMethod() == PaymentMethod.POINT)
                .findAny();
    }

}
