package com.food.user.stub;

import com.food.user.PointCollectRequest;

public interface PointService {
    /**
     * 포인트를 적립힌다.
     * @param request 적립금액과 적립대상유저 정보를 포함하고 있는 적립 요청 객체
     */
    void collect(PointCollectRequest request);
}
