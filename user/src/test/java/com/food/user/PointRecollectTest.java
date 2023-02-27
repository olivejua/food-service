package com.food.user;

import com.food.common.user.business.external.PointService;
import com.food.common.user.business.internal.dto.UserDto;
import com.food.common.user.enumeration.PointType;
import com.food.common.utils.Amount;
import com.food.common.utils.UsedPoints;
import com.food.user.business.DefaultPointService;
import com.food.user.error.DoesNotMatchPointOwnerException;
import com.food.user.error.DuplicatePointRecollectException;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PointRecollectTest {
    /**
     * 취소할 사용 pointId는 null일 수 없다.
     * 요청자와 사용포인트 대상유저는 일치해야한다.
     * 포인트 ID로 된 데이터가 존재해야한다.
     * 이미 사용 취소처리가 된 포인트 건이라면 예외가 발생한다.
     * 사용자 포인트 잔액을 변경하고, 취소 포인트 데이터를 저장한다.
     */

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
    void 이미_재적립이_된_포인트라면_예외가_발생한다() {
        assertThrows(DuplicatePointRecollectException.class, () -> pointService.recollect(givenUsedMockPointIdPresent(), mockRequestUser));
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

    private Long givenUsedMockPointIdNotPresent() {
        Long pointId = -1L;

        if (stubPointService.findByPointId(pointId).isPresent()) {
            throw new IllegalArgumentException();
        }

        return pointId;
    }
}
