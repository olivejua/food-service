package com.food.order.stubrepository;

import com.food.common.user.business.external.PointService;
import com.food.common.user.business.external.model.PointCollectRequest;
import com.food.common.user.business.external.model.PointUseRequest;

public class StubPointService implements PointService {
    private boolean calledToUsePoints = false;
    private boolean calledToCollectPoints = false;
    private Long autoIncrementKey = -1L;

    public boolean isCalledToCollect() {
        return calledToCollectPoints;
    }

    public boolean isCalledToUse() {
        return calledToUsePoints;
    }

    @Override
    public Long use(PointUseRequest request) {
        calledToUsePoints = true;

        return autoIncrementKey--;
    }

    @Override
    public void collect(PointCollectRequest request) {
        calledToCollectPoints = true;
    }

    @Override
    public void recollectUsedPoint(Long pointId) {

    }

    @Override
    public void retrieveCollectedPoint(Long paymentId) {

    }
}
