package com.food.user.business;

import com.food.common.payment.business.external.PaymentAmountService;
import com.food.common.user.business.external.PointService;
import com.food.common.user.business.external.model.RequestUser;
import com.food.common.user.business.internal.PointCommonService;
import com.food.common.user.business.internal.UserCommonService;
import com.food.common.user.business.internal.dto.PointDto;
import com.food.common.user.business.internal.dto.UserDto;
import com.food.common.utils.Amount;
import com.food.common.utils.UsedPoints;
import com.food.user.error.DoesNotMatchPointOwnerException;
import com.food.user.error.InsufficientPointBalanceException;
import com.food.user.error.NotFoundPointException;
import com.food.user.error.NotFoundPointOwnerException;
import org.springframework.stereotype.Service;

@Service
public class DefaultPointService implements PointService {
    private final UserCommonService userCommonService;
    private final PointCommonService pointCommonService;
    private final PaymentAmountService paymentAmountService;

    public DefaultPointService(UserCommonService userCommonService, PointCommonService pointCommonService, PaymentAmountService paymentAmountService) {
        this.userCommonService = userCommonService;
        this.pointCommonService = pointCommonService;
        this.paymentAmountService = paymentAmountService;
    }

    @Override
    public void collect(Long paymentId, RequestUser requestUser) {
        UserDto owner = userCommonService.findById(requestUser.getUserId())
                .orElseThrow(() -> new NotFoundPointOwnerException(requestUser.getUserId()));

        Amount paymentAmount = paymentAmountService.sumOfPaymentAmountWithoutPoints(paymentId);
        Amount collectedAmount = paymentAmount.multiply(0.05f);

        PointDto collectPoint = basePoint(owner.getId())
                .collect(collectedAmount, paymentId);

        pointCommonService.save(collectPoint);
    }

    @Override
    public Long use(UsedPoints amount, RequestUser requestUser) {
        if (!userCommonService.existsById(requestUser.getUserId())) {
            throw new NotFoundPointOwnerException(requestUser.getUserId());
        }

        PointDto basePoint = basePoint(requestUser.getUserId());
        if (!basePoint.getCurrentAmount().isGreaterThanOrEqualTo(amount)) {
            throw new InsufficientPointBalanceException();
        }

        PointDto usePoint = basePoint.use(amount);
        return pointCommonService.save(usePoint).getId();
    }

    @Override
    public void recollect(Long pointId, RequestUser requestUser) {
        PointDto usedPoint = pointCommonService.findByPointId(pointId)
                .orElseThrow(() -> new NotFoundPointException(pointId));

        if (!usedPoint.hasSameOwnerIdAs(requestUser.getUserId())) {
            throw new DoesNotMatchPointOwnerException();
        }


    }

    @Override
    public void retrieveCollectedPoint(Long paymentId) {

    }

    private PointDto basePoint(Long ownerId) {
        return pointCommonService.findLatestPointByUserId(ownerId)
                .orElse(PointDto.createBasePoint(ownerId));
    }
}
