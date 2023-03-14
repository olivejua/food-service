package com.food.common.user.business.internal.impl;

import com.food.common.payment.business.internal.impl.PaymentEntityService;
import com.food.common.payment.domain.Payment;
import com.food.common.user.business.internal.PointCommonService;
import com.food.common.user.business.internal.dto.PointDto;
import com.food.common.user.domain.Point;
import com.food.common.user.domain.User;
import com.food.common.user.enumeration.PointType;
import com.food.common.user.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class DefaultPointCommonService implements PointCommonService {
    private final PointRepository pointRepository;
    private final UserEntityService userEntityService;
    private final PaymentEntityService paymentEntityService;

    @Override
    public PointDto save(PointDto request) {
        User user = userEntityService.findById(request.getUserId());
        Point entity = request.getType() == PointType.COLLECT ?
                Point.create(user, request.getType(), request.getChangedAmount(), request.getCurrentAmount(), paymentEntityService.findById(request.getPaymentId())) :
                Point.create(user, request.getType(), request.getChangedAmount(), request.getCurrentAmount());

        return new PointDto(pointRepository.save(entity));
    }

    @Override
    public Optional<PointDto> findLatestPointByUserId(Long userId) {
        Optional<Point> optionalPoint = findLatestPointByUser(findUser(userId));

        if (optionalPoint.isEmpty()) return Optional.empty();

        return Optional.of(new PointDto(optionalPoint.get()));
    }


    @Override
    public Optional<PointDto> findByPointId(Long pointId) {
        return pointRepository.findById(pointId)
                .map(PointDto::new);
    }

    @Override
    public Optional<PointDto> findByPaymentId(Long paymentId) {
        Payment payment = paymentEntityService.findById(paymentId);

        return pointRepository.findFirstByPaymentOrderByCreatedDateDesc(payment)
                .map(PointDto::new);
    }

    private Optional<Point> findLatestPointByUser(User user) {
        return pointRepository.findFirstByUserOrderByCreatedDateDesc(user);
    }

    private User findUser(Long userId) {
        return userEntityService.findById(userId);
    }
}
