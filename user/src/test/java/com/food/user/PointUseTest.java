package com.food.user;

import com.food.common.user.business.external.PointService;
import com.food.common.user.business.internal.dto.PointDto;
import com.food.common.user.business.internal.dto.UserDto;
import com.food.common.utils.Amount;
import com.food.common.utils.UsedPoints;
import com.food.user.business.DefaultPointService;
import com.food.user.error.InsufficientPointBalanceException;
import com.food.user.error.NotFoundPointOwnerException;
import com.food.user.mock.MockPoint;
import com.food.user.mock.MockRequestUser;
import com.food.user.mock.MockUser;
import com.food.user.stub.StubPaymentAmountService;
import com.food.user.stub.StubPointService;
import com.food.user.stub.StubUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PointUseTest {
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
    void 사용포인트는_0보다_커야하고_10원_단위여야_한다() {
        assertThrows(IllegalArgumentException.class, () -> UsedPoints.won(0));
        assertThrows(IllegalArgumentException.class, () -> UsedPoints.won(11));
        assertThrows(IllegalArgumentException.class, () -> UsedPoints.won(-1_000));
        assertDoesNotThrow(() -> UsedPoints.won(1_000));
        assertDoesNotThrow(() -> UsedPoints.won(10));
    }

    @Test
    void 사용대상_유저가_존재하지_않으면_예외가_발생한다() {
        mockRequestUser = new MockRequestUser(givenUserIdNotPresent());
        assertThrows(NotFoundPointOwnerException.class, () -> pointService.use(UsedPoints.won(1_000), mockRequestUser));
    }

    @Test
    void 포인트_잔액이_사용_포인트_금액보다_적으면_예외가_발생한다() {
        mockRequestUser = new MockRequestUser(givenUserIdPresent());

        stubPointService.save(givenMockPoint(500, mockRequestUser.getUserId()));
        UsedPoints usedPoints = UsedPoints.won(1_000);

        assertThrows(InsufficientPointBalanceException.class, () -> pointService.use(usedPoints, mockRequestUser));
    }

    @Test
    void 포인트_잔액이_사용_포인트_금액보다_많으면_예외가_발생하지_않는다() {
        mockRequestUser = new MockRequestUser(givenUserIdPresent());

        stubPointService.save(givenMockPoint(1_500, mockRequestUser.getUserId()));
        UsedPoints usedPoints = UsedPoints.won(1_000);

        assertDoesNotThrow(() -> pointService.use(usedPoints, mockRequestUser));
    }

    @Test
    void 잔액포인트를_사용포인트만큼_차감한다() {
        //given
        mockRequestUser = new MockRequestUser(givenUserIdPresent());

        stubPointService.save(givenMockPoint(1_500, mockRequestUser.getUserId()));
        UsedPoints usedPoints = UsedPoints.won(1_000);

        PointDto basePoint = stubPointService.findLatestPointByUserId(mockRequestUser.getUserId()).get();
        Amount expectedCurrentAmount = basePoint.getCurrentAmount().subtract(usedPoints);

        //when
        Long usedPointId = pointService.use(usedPoints, mockRequestUser);

        //then
        Optional<PointDto> findPointOptional = stubPointService.findByPointId(usedPointId);
        assertTrue(findPointOptional.isPresent());
        PointDto findPoint = findPointOptional.get();
        assertEquals(expectedCurrentAmount, findPoint.getCurrentAmount());
        assertEquals(usedPoints, findPoint.getChangedAmount());
    }

    private Long givenUserIdNotPresent() {
        Long userId = 1L;

        if (stubUserService.existsById(userId)) {
            throw new IllegalArgumentException();
        }

        return userId;
    }

    private Long givenUserIdPresent() {
        UserDto mockUser = MockUser.create();
        return stubUserService.save(mockUser).getId();
    }

    private MockPoint givenMockPoint(int amount, Long userId) {
        return MockPoint.testBuilder()
                .currentAmount(Amount.won(amount))
                .userId(userId)
                .build();
    }
}
