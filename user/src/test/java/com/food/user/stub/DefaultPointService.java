package com.food.user.stub;

import com.food.common.user.business.internal.PointCommonService;
import com.food.common.user.business.internal.UserCommonService;
import com.food.common.user.business.internal.dto.PointDto;
import com.food.common.user.business.internal.dto.UserDto;
import com.food.user.PointCollectRequest;
import org.springframework.validation.annotation.Validated;

@Validated
public class DefaultPointService implements PointService {
    private final UserCommonService userCommonService;
    private final PointCommonService pointCommonService;

    public DefaultPointService(UserCommonService userCommonService, PointCommonService pointCommonService) {
        this.userCommonService = userCommonService;
        this.pointCommonService = pointCommonService;
    }

    @Override
    public void collect(PointCollectRequest request) {
        UserDto owner = userCommonService.findById(request.getOwnerId())
                .orElseThrow(() -> new NotFoundPointOwnerException(request.getOwnerId()));

        PointDto collectPoint = basePoint(owner.getId())
                .collect(request.getAmount(), request.getPaymentId());

        pointCommonService.save(collectPoint);
    }

    private PointDto basePoint(Long ownerId) {
        return pointCommonService.findLatestPointByUserId(ownerId)
                .orElse(PointDto.createBasePoint(ownerId));
    }
}
