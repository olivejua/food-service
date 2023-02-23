package com.food.order.stubrepository;

import com.food.common.user.business.external.PointService;
import com.food.common.user.business.external.model.RequestUser;
import com.food.common.utils.UsedPoints;

public class StubPointService implements PointService {
    private boolean calledToUsePoints = false;
    private boolean calledToCollectPoints = false;
    private boolean calledToRecollectPoints = false;
    private boolean calledToRetrievePoints = false;
    private Long autoIncrementKey = -1L;

    public boolean isCalledToCollect() {
        return calledToCollectPoints;
    }

    public boolean isCalledToUse() {
        return calledToUsePoints;
    }

    public boolean isCalledToRecollect() {
        return calledToRecollectPoints;
    }

    public boolean isCalledToRetrieve() {
        return calledToRetrievePoints;
    }

    @Override
    public void collect(Long paymentId, RequestUser requestUser) {
        calledToCollectPoints = true;

    }

    @Override
    public Long use(UsedPoints amount, RequestUser requestUser) {
        calledToUsePoints = true;

        return autoIncrementKey--;
    }

    @Override
    public void recollectUsedPoint(Long pointId) {
        calledToRecollectPoints = true;
    }

    @Override
    public void retrieveCollectedPoint(Long paymentId) {
        calledToRetrievePoints = true;
    }
}
