package com.food.order.temp;

import com.food.common.order.business.internal.OrderCommonService;
import com.food.common.order.business.internal.dto.OrderDto;
import com.food.common.payment.business.external.model.payrequest.PaymentElement;
import com.food.common.payment.business.internal.PaymentCommonService;
import com.food.common.payment.business.internal.PaymentLogCommonService;
import com.food.common.payment.business.internal.model.PaymentDto;
import com.food.common.payment.enumeration.PaymentActionType;
import com.food.common.user.business.external.PointService;
import com.food.common.user.business.external.model.PointUseRequest;
import com.food.common.user.business.external.model.RequestUser;
import com.food.order.error.DuplicatedPaymentException;
import com.food.order.error.InvalidPaymentException;
import com.food.order.error.NotFoundOrderException;
import com.food.order.error.PaymentErrors;
import com.food.order.presentation.dto.request.PaymentDoRequest;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultPayService implements PayService {
    private final OrderCommonService orderCommonService;
    private final PaymentCommonService paymentCommonService;
    private final PaymentLogCommonService paymentLogRepository;
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

        usePoints(request, requestUser);

        Set<PaymentElement> paymentElements = request.getItems().stream()
                .map(item -> PaymentElement.findPaymentElement(item.getMethod(), item.getAmount()))
                .collect(Collectors.toSet());
        paymentLogRepository.saveAll(paymentId, paymentElements);

        return paymentId;
    }

    private void usePoints(PaymentDoRequest request, RequestUser requestUser) {
        if (request.hasItemWithPointMethod()) {
            PointUseRequest pointUseRequest = new PointUseRequest(request.getUsedPointAmount(), requestUser.getUserId());

            pointService.use(pointUseRequest);
        }
    }
}
