package com.food.user;

import com.food.common.user.business.external.PointService;
import com.food.common.user.business.internal.dto.PointDto;
import com.food.common.user.business.internal.dto.UserDto;
import com.food.common.utils.Amount;
import com.food.user.business.DefaultPointService;
import com.food.user.error.NotFoundPointOwnerException;
import com.food.user.mock.MockRequestUser;
import com.food.user.mock.MockUser;
import com.food.user.stub.StubPaymentAmountService;
import com.food.user.stub.StubPointService;
import com.food.user.stub.StubUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PointCollectTest {
    /**
     * [적립]
     *
     * 포인트 사용금액을 제외한 실제 결제금액의 5%를 적립한다. 1번째 자리수는 내림처리한다.
     * 적립 금액은 0 이상이고, 10원단위여야 한다. (v) -> 제거
     * 적립 대상자가 존재하지 않으면 예외가 발생한다. (v)
     * 결제 ID는 존재해야한다. (v)
     * 포인트 적립 데이터를 저장한다. -> 추가 (v)
     * 결제금액의 5%를 적립해준다. 적립금액이 소수점이라면 내림처리한다. (사용자의 포인트 잔여금액이 그만큼 늘었는지 확인) -> 추가 (v)
     */

    private StubUserService stubUserService;
    private StubPointService stubPointService;
    private StubPaymentAmountService paymentAmountService;
    private PointService pointService;
    private MockRequestUser mockRequestUser;

    @BeforeEach
    void setup() {
        stubUserService = new StubUserService();
        stubPointService = new StubPointService();
        paymentAmountService = new StubPaymentAmountService();
        pointService = new DefaultPointService(stubUserService, stubPointService, paymentAmountService);
        mockRequestUser = new MockRequestUser(givenOwnerIdPresent());
    }

    private Long givenOwnerIdNotPresent() {
        Long ownerId = 1L;

        if (stubUserService.existsById(ownerId)) {
            throw new IllegalArgumentException();
        }

        return ownerId;
    }

    private Long givenOwnerIdPresent() {
        UserDto savedUser = stubUserService.save(MockUser.create());

        return savedUser.getId();
    }

    @Test
    void 포인트_적립_대상자가_존재하지_않으면_예외가_발생한다() {
        //given
        mockRequestUser = new MockRequestUser(givenOwnerIdNotPresent());

        //when & then
        NotFoundPointOwnerException error = assertThrows(NotFoundPointOwnerException.class, () -> pointService.collect(1L, mockRequestUser));
        assertTrue(error.getMessage().contains("ownerId=" + mockRequestUser.getUserId()));
    }

    @Test
    void 결제금액의_5퍼센트를_포인트_적립한다() {
        //given
        Amount givenAmount = Amount.won(25_000);
        paymentAmountService.remember(givenAmount);

        Long givenOwnerId = givenOwnerIdPresent();

        Amount expectedChangedAmount = givenAmount.multiply(0.05f);
        Amount expectedCurrentAmount = currentAmount(givenOwnerId).plus(expectedChangedAmount);

        //when
        pointService.collect(1L, new MockRequestUser(givenOwnerId));

        //then
        Optional<PointDto> findPoint = stubPointService.findLatestPointByUserId(givenOwnerId);
        assertTrue(findPoint.isPresent());
        PointDto actualPoint = findPoint.get();
        assertEquals(expectedChangedAmount, actualPoint.getChangedAmount());
        assertEquals(expectedCurrentAmount, actualPoint.getCurrentAmount());

        assertTrue(stubPointService.isCalledToSave());
    }

    @Test
    void 적립금액에서_소수점이_발생한다면_내림처리하여_적립한다() {
        //given
        Amount givenAmount = Amount.won(30_550);
        paymentAmountService.remember(givenAmount);

        Long givenOwnerId = givenOwnerIdPresent();

        //when
        pointService.collect(1L, new MockRequestUser(givenOwnerId));

        //then
        Optional<PointDto> findPoint = stubPointService.findLatestPointByUserId(givenOwnerId);
        assertTrue(findPoint.isPresent());
        PointDto actualPoint = findPoint.get();
        assertEquals(Amount.won(1527), actualPoint.getChangedAmount());

    }

    private Amount currentAmount(Long ownerId) {
        return stubPointService.findLatestPointByUserId(ownerId)
                .map(PointDto::getChangedAmount)
                .orElse(Amount.zero());
    }
}
