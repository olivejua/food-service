package com.food.user.stub;

import com.food.common.payment.business.internal.PaymentCommonService;
import com.food.common.user.business.internal.PointCommonService;
import com.food.common.user.business.internal.UserCommonService;
import com.food.common.user.business.internal.dto.UserDto;
import com.food.user.PointCollectRequest;

public class DefaultPointService implements PointService {
    private final UserCommonService userCommonService;
    private final PointCommonService pointCommonService;
    private final PaymentCommonService paymentCommonService;

    public DefaultPointService(UserCommonService userCommonService, PointCommonService pointCommonService, PaymentCommonService paymentCommonService) {
        this.userCommonService = userCommonService;
        this.pointCommonService = pointCommonService;
        this.paymentCommonService = paymentCommonService;
    }

    @Override
    public void collect(PointCollectRequest request) {
        UserDto owner = userCommonService.findById(request.getOwnerId())
                .orElseThrow(() -> new NotFoundPointOwnerException(request.getOwnerId()));


    }
}
