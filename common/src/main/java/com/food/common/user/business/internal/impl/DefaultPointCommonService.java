package com.food.common.user.business.internal.impl;

import com.food.common.payment.business.internal.impl.PaymentEntityService;
import com.food.common.payment.domain.Payment;
import com.food.common.user.business.internal.PointCommonService;
import com.food.common.user.business.internal.dto.PointDto;
import com.food.common.user.business.internal.dto.PointSaveDto;
import com.food.common.user.domain.Point;
import com.food.common.user.domain.User;
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
        Payment payment = paymentEntityService.findById(request.getPaymentId());

        Point entity = Point.create(user, request.getType(), request.getChangedAmount(), request.getCurrentAmount(), payment);
        return new PointDto(pointRepository.save(entity));
    }

    @Override
    public Optional<PointDto> findLatestPointByUserId(Long userId) {
        Optional<Point> optionalPoint = findLatestPointByUser(findUser(userId));

        if (optionalPoint.isEmpty()) return Optional.empty();

        return Optional.of(new PointDto(optionalPoint.get()));
    }

    private Point createChangedPoint(PointSaveDto request, Point basePoint) {
        Point result;
        switch (request.getType()) {
            case COLLECT -> {
                Payment payment = paymentEntityService.findById(request.getPaymentId());
                result = basePoint.collect(request.getAmount(), payment);
            }
            case USE -> {
                result = basePoint.use(request.getAmount());
            }
            case RETRIEVE -> {
                Payment payment = paymentEntityService.findById(request.getPaymentId());
                result = basePoint.retrieve(request.getAmount(), payment);
            }
            case RECOLLECT -> {
                result = basePoint.recollect(request.getAmount());
            }
            default -> throw new IllegalStateException("Unexpected value: " + request.getType());
        }
        return result;
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
