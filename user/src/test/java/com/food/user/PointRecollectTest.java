package com.food.user;

import com.food.common.user.business.external.PointService;
import com.food.common.user.business.internal.dto.PointDto;
import com.food.common.user.business.internal.dto.UserDto;
import com.food.common.user.enumeration.PointType;
import com.food.common.utils.Amount;
import com.food.common.utils.UsedPoints;
import com.food.user.business.DefaultPointService;
import com.food.user.error.DoesNotMatchPointOwnerException;
import com.food.user.error.NotFoundPointException;
import com.food.user.mock.MockPoint;
import com.food.user.mock.MockRequestUser;
import com.food.user.mock.MockUser;
import com.food.user.stub.StubPaymentAmountService;
import com.food.user.stub.StubPointService;
import com.food.user.stub.StubUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PointRecollectTest {
    private StubUserService stubUserService;
    private StubPointService stubPointService;
    private PointService pointService;
    private MockRequestUser mockRequestUser;

    @BeforeEach
    void setup() {
        stubUserService = new StubUserService();
        stubPointService = new StubPointService();
        pointService = new DefaultPointService(stubUserService, stubPointService, new StubPaymentAmountService());
        mockRequestUser = new MockRequestUser(givenUserIdPresent());
    }

    @Test
    void 취소할_pointId가_존재하지_않을경우_예외가_발생한다() {
        assertThrows(NotFoundPointException.class, () -> pointService.recollect(givenUsedMockPointIdNotPresent(), mockRequestUser));

        assertDoesNotThrow(() -> pointService.recollect(givenUsedMockPointIdPresent(), mockRequestUser));
    }

    @Test
    void 요청자와_사용포인트_데이터의_유저는_일치해야한다() {
        Long requestUserId = givenUserIdPresent();
        Long usedPointOwnerId = givenUserIdPresent();

        Assert.isTrue(requestUserId != usedPointOwnerId, "must be different users");

        MockPoint mockPoint = MockPoint.testBuilder()
                .userId(usedPointOwnerId)
                .type(PointType.USE)
                .changedAmount(UsedPoints.won(2000))
                .currentAmount(Amount.won(1000))
                .build();
        Long usedPointId = stubPointService.save(mockPoint).getId();

        assertThrows(DoesNotMatchPointOwnerException.class, () -> pointService.recollect(usedPointId, new MockRequestUser(requestUserId)));

        assertDoesNotThrow(() -> pointService.recollect(usedPointId, new MockRequestUser(usedPointOwnerId)));
    }

    @Test
    void 사용포인트를_재적립하고_저장한다() {
        Amount pointBalance = Amount.won(1000);
        UsedPoints usedPoints = UsedPoints.won(2000);
        Long mockPointId = givenUsedMockPointIdPresent(usedPoints, pointBalance);

        Long recollectedPointId = pointService.recollect(mockPointId, mockRequestUser);
        Optional<PointDto> findPoint = stubPointService.findByPointId(recollectedPointId);
        assertTrue(findPoint.isPresent());

        PointDto recollectedPoint = findPoint.get();
        assertEquals(usedPoints, recollectedPoint.getChangedAmount());
        assertEquals(pointBalance.plus(usedPoints), recollectedPoint.getCurrentAmount());
    }

    private Long givenUserIdPresent() {
        UserDto mockUser = MockUser.create();
        return stubUserService.save(mockUser).getId();
    }

    private Long givenUsedMockPointIdPresent() {
        MockPoint mockPoint = MockPoint.testBuilder()
                .userId(mockRequestUser.getUserId())
                .type(PointType.USE)
                .changedAmount(UsedPoints.won(2000))
                .currentAmount(Amount.won(1000))
                .build();

        return stubPointService.save(mockPoint).getId();
    }

    private Long givenUsedMockPointIdPresent(UsedPoints usedPoints, Amount pointBalance) {
        MockPoint mockPoint = MockPoint.testBuilder()
                .userId(mockRequestUser.getUserId())
                .type(PointType.USE)
                .changedAmount(usedPoints)
                .currentAmount(pointBalance)
                .build();

        return stubPointService.save(mockPoint).getId();
    }

    private Long givenUsedMockPointIdNotPresent() {
        Long pointId = -1L;

        if (stubPointService.findByPointId(pointId).isPresent()) {
            throw new IllegalArgumentException();
        }

        return pointId;
    }
}
