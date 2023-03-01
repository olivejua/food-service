package com.food.user;

import com.food.common.user.business.external.PointService;
import com.food.common.user.business.internal.dto.PointDto;
import com.food.common.user.business.internal.dto.UserDto;
import com.food.common.user.enumeration.PointType;
import com.food.common.utils.Amount;
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

public class PointRetrieveTest {
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
    void 요청결제ID로_적립포인트_데이터가_존재하지_않으면_예외가_발생한다() {
        assertThrows(NotFoundPointException.class, () -> pointService.retrieve(givenPaymentIdNotPresent(), mockRequestUser));
    }

    @Test
    void 적립포인트_유저와_요청자는_일치해야한다() {
        Long pointOwnerId = givenUserIdPresent();
        Long requestUserId = givenUserIdPresent();
        Assert.isTrue(!requestUserId.equals(pointOwnerId));

        assertThrows(DoesNotMatchPointOwnerException.class, () -> pointService.retrieve(givenPaymentIdPresent(pointOwnerId), new MockRequestUser(requestUserId)));
    }

    @Test
    void 사용자의_포인트_잔액을_적립금액만큼_차감하고_회수포인트_데이터를_저장한다() {
        //given
        MockPoint collectMockPoint = MockPoint.testBuilder()
                .paymentId(1L)
                .userId(mockRequestUser.getUserId())
                .type(PointType.COLLECT)
                .changedAmount(Amount.won(1000))
                .currentAmount(Amount.won(3000))
                .build();
        stubPointService.save(collectMockPoint);

        //when
        Long pointId = pointService.retrieve(collectMockPoint.getPaymentId(), mockRequestUser);

        //then
        Optional<PointDto> retrievePointOptional = stubPointService.findByPointId(pointId);
        assertTrue(retrievePointOptional.isPresent());
        PointDto findPoint = retrievePointOptional.get();
        assertEquals(PointType.RETRIEVE, findPoint.getType());
        assertEquals(collectMockPoint.getChangedAmount(), findPoint.getChangedAmount());
        assertEquals(Amount.won(2000), findPoint.getCurrentAmount());
    }

    private Long givenUserIdPresent() {
        UserDto mockUser = MockUser.create();
        return stubUserService.save(mockUser).getId();
    }

    private Long givenPaymentIdNotPresent() {
        Long paymentId = 1L;

        if (stubPointService.findByPaymentId(paymentId).isPresent()) {
            throw new IllegalArgumentException();
        }

        return paymentId;
    }

    private Long givenPaymentIdPresent(Long userId) {
        MockPoint mockPoint = MockPoint.testBuilder()
                .paymentId(1L)
                .userId(userId)
                .type(PointType.COLLECT)
                .build();

        return stubPointService.save(mockPoint).getPaymentId();
    }
}
