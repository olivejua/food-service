package com.food.order.business;

import com.food.common.order.business.internal.OrderCommonService;
import com.food.common.order.business.internal.dto.OrderDto;
import com.food.common.payment.business.external.PayService;
import com.food.common.payment.business.external.model.PaymentDoRequest;
import com.food.common.payment.business.external.model.payrequest.PaymentElement;
import com.food.common.payment.business.external.model.payrequest.PointPayment;
import com.food.common.payment.business.internal.PaymentCommonService;
import com.food.common.payment.business.internal.PaymentLogCommonService;
import com.food.common.payment.business.internal.model.PaymentDto;
import com.food.common.payment.business.internal.model.PaymentLogDto;
import com.food.common.payment.enumeration.PaymentActionType;
import com.food.common.payment.enumeration.PaymentMethod;
import com.food.common.user.business.external.PointService;
import com.food.common.user.business.external.model.RequestUser;
import com.food.common.utils.UsedPoints;
import com.food.order.error.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultPayService implements PayService {
    private final OrderCommonService orderCommonService;
    private final PaymentCommonService paymentCommonService;
    private final PaymentLogCommonService paymentLogCommonService;
    private final PointService pointService;

    @Override
    public Long pay(PaymentDoRequest request, RequestUser requestUser) {
        OrderDto order = orderCommonService.findById(request.getOrderId())
                .orElseThrow(() -> new NotFoundOrderException(request.getOrderId()));

        if (!request.hasSameTotalAmountAs(order.getAmount())) {
            throw new InvalidPaymentException(PaymentErrors.WRONG_PAYMENT_AMOUNT);
        }

        if (paymentCommonService.existsById(order.getId())) {
            throw new DuplicatedPaymentException();
        }

        PaymentDto paymentDto = PaymentDto.builder()
                .orderId(order.getId())
                .actionType(PaymentActionType.PAYMENT)
                .build();
        Long paymentId = paymentCommonService.save(paymentDto).getId();

        Set<PaymentElement> paymentElements = getPaymentElements(request, requestUser);
        paymentLogCommonService.saveAll(paymentId, paymentElements);

        collectPoint(requestUser, paymentId, paymentElements);

        return paymentId;
    }

    private Set<PaymentElement> getPaymentElements(PaymentDoRequest request, RequestUser requestUser) {
        return request.getItems().stream()
                .map(item -> {
                    PaymentElement element = PaymentElement.findPaymentElement(item.getMethod(), item.getAmount());
                    usePointsIfPaymentMethodIsPoint(requestUser, element);

                    return element;
                })
                .collect(Collectors.toSet());
    }

    private void usePointsIfPaymentMethodIsPoint(RequestUser requestUser, PaymentElement element) {
        if (element instanceof PointPayment pointPayment) {
            pointPayment.updateUsedPointId(pointService.use(UsedPoints.won(pointPayment.getAmount()), requestUser));
        }
    }

    private void collectPoint(RequestUser requestUser, Long paymentId, Set<PaymentElement> paymentElements) {
        pointService.collect(paymentId, requestUser);
    }

    @Override
    public void cancel(Long paymentId, RequestUser requestUser) {
        PaymentDto payment = paymentCommonService.findById(paymentId)
                .orElseThrow(() -> new NotFoundPaymentException(paymentId));

        if (payment.isCanceled()) {
            throw new InvalidPaymentActionTypeException("이미 취소된 주문정보입니다.");
        }

        paymentCommonService.updateActionType(paymentId, PaymentActionType.CANCELLATION);

        List<PaymentLogDto> paymentLogs = paymentLogCommonService.findAllByPaymentId(paymentId);

        recollectPointsIfUsedPointsExist(paymentLogs, requestUser);
        retrievePointsIfCollectedPointsExist(paymentId, paymentLogs);
    }

    private void recollectPointsIfUsedPointsExist(List<PaymentLogDto> paymentLogs, RequestUser requestUser) {
        paymentLogs
                .stream()
                .filter(paymentLog -> paymentLog.getMethod() == PaymentMethod.POINT)
                .findAny()
                .ifPresent(pointPayment -> pointService.recollect(pointPayment.getPointId(), requestUser));
    }

    private void retrievePointsIfCollectedPointsExist(Long paymentId, List<PaymentLogDto> paymentLogs) {
        int paymentAmount = paymentLogs.stream()
                .filter(paymentLog -> paymentLog.getMethod() != PaymentMethod.POINT)
                .mapToInt(PaymentLogDto::getAmount)
                .sum();

        if (paymentAmount > 0) {
            pointService.retrieve(paymentId);
        }
    }
}
