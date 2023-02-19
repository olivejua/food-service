package com.food.user.stub;

import com.food.common.user.business.internal.PointCommonService;
import com.food.common.user.business.internal.dto.PointDto;
import com.food.common.user.business.internal.dto.PointSaveDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        PointDto newOne = PointDto.builder()
                .id(autoIncrementKey--)
                .userId(point.getUserId())
                .type(point.getType())
                .changedAmount(point.getChangedAmount())
                .currentAmount(point.getCurrentAmount())
                .paymentId(point.getPaymentId())
                .build();
        data.put(newOne.getId(), newOne);

        return newOne;
    }

    @Override
    public Optional<PointDto> findLatestPointByUserId(Long userId) {
        return Optional.empty();
    }

    @Override
    public Long save(PointSaveDto request) {
        return null;
    }

    @Override
    public Optional<PointDto> findByPointId(Long pointId) {
        return Optional.empty();
    }

    @Override
    public List<PointDto> findAllByPaymentId(Long paymentId) {
        return null;
    }

    public boolean isCalledToSave() {
        return calledToSave;
    }
}
