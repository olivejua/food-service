package com.food.user.stub;

import com.food.common.user.business.internal.PointCommonService;
import com.food.common.user.business.internal.dto.PointDto;
import com.food.common.user.business.internal.dto.PointSaveDto;
import com.food.user.mock.MockPoint;

import java.util.*;

public class StubPointService implements PointCommonService {
    private final Map<Long, PointDto> data = new HashMap<>();
    private Long autoIncrementKey = -1L;

    private boolean calledToSave = false;

    @Override
    public PointDto save(PointDto point) {
        calledToSave = true;

        if (data.containsKey(point.getId())) {
            data.put(point.getId(), point);
            return point;
        }

        MockPoint newOne = MockPoint.testBuilder()
                .id(autoIncrementKey--)
                .userId(point.getUserId())
                .type(point.getType())
                .changedAmount(point.getChangedAmount())
                .currentAmount(point.getCurrentAmount())
                .paymentId(point.getPaymentId())
                .build();
        newOne.updateCreatedDate();

        data.put(newOne.getId(), newOne);

        return newOne;
    }

    @Override
    public Optional<PointDto> findLatestPointByUserId(Long userId) {
        PointDto latestPoint = null;

        for (PointDto eachPoint : data.values()) {
            if (eachPoint.getUserId().equals(userId) && hasMoreRecentCreatedDateThan(eachPoint, latestPoint)) {
                latestPoint = eachPoint;
            }
        }

        return Optional.ofNullable(latestPoint);
    }

    private boolean hasMoreRecentCreatedDateThan(PointDto targetPoint, PointDto compared) {
        if (compared == null) return true;

        return targetPoint.getCreatedDate().isAfter(compared.getCreatedDate());
    }

    @Override
    public Optional<PointDto> findByPointId(Long pointId) {
        return Optional.ofNullable(data.get(pointId));
    }

    @Override
    public Optional<PointDto> findByPaymentId(Long paymentId) {
        return data.values().stream()
                .filter(targetPoint -> targetPoint.getPaymentId().equals(paymentId))
                .max(Comparator.comparing(PointDto::getCreatedDate));
    }

    public boolean isCalledToSave() {
        return calledToSave;
    }
}
