package com.food.user;

import com.food.common.error.exception.InvalidRequestParameterException;
import com.food.common.user.business.internal.dto.UserDto;
import com.food.user.mock.MockUser;
import com.food.user.stub.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PointCollectTest {
    /**
     * [적립]
     * 적립 금액은 0 이상이고, 10원단위여야 한다. (v)
     * 적립 대상자가 존재하지 않으면 예외가 발생한다. (v)
     * 결제 ID는 존재해야한다. (v)
     * 포인트 적립 데이터를 저장한다.
     *
     * [사용]
     * 사용 대상자가 존재하지 않으면 예외가 발생한다.
     * 포인트 잔액이 사용 포인트보다 부족하면 예외가 발생한다.
     * 사용포인트는 0이상이고, 10원단위여야 한다.
     * 잔액포인트를 차감한다.
     *
     * 재적립
     *
     * 회수
     */

    private StubUserService stubUserService;
    private StubPointService stubPointService;
    private PointService pointService;

    @BeforeEach
    void setup() {
        stubUserService = new StubUserService();
        stubPointService = new StubPointService();
        pointService = new DefaultPointService(stubUserService, stubPointService);
    }

    @Test
    void 포인트_적립_금액은_0원_이상이어야한다() {
        //given
        int collectedAmount1 = 0;

        //when & then
        InvalidRequestParameterException error1 = assertThrows(InvalidRequestParameterException.class,
                () -> new PointCollectRequest(1L, collectedAmount1, 1L));

        assertTrue(error1.getMessage().contains("포인트 적립금액은 0이상의 금액이어야 합니다."));

        //given
        int collectedAmount2 = 1_000;

        //when & then
        assertDoesNotThrow(() -> new PointCollectRequest(1L, collectedAmount2, 1L));

        int collectedAmount3 = -1_000;
        assertThrows(InvalidRequestParameterException.class,
                () -> new PointCollectRequest(1L, collectedAmount3, 1L));
    }

    @Test
    void 포인트_적립금액이_10원_단위여야_한다() {
        //given
        int collectedAmount1 = 1_111;

        //when
        InvalidRequestParameterException error1 = assertThrows(InvalidRequestParameterException.class,
                () -> new PointCollectRequest(1L, collectedAmount1, 1L));

        //then
        assertTrue(error1.getMessage().contains("포인트 적립금액은 10원 단위의 금액이어야 합니다."));

        //given
        int collectedAmount2 = 1_110;

        //when & then
        assertDoesNotThrow(() -> new PointCollectRequest(1L, collectedAmount2, 1L));
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
    void 요청데이터로_결제ID는_null이면_예외가_발생한다() {
        //when & then
        InvalidRequestParameterException error = assertThrows(InvalidRequestParameterException.class,
                () -> new PointCollectRequest(givenOwnerIdPresent(), 1_000, null));

        assertTrue(error.getMessage().contains("PaymentId는 비어있을 수 없습니다."));

        //given & when & then
        assertDoesNotThrow(() -> new PointCollectRequest(givenOwnerIdPresent(), 1_000, 1L));
    }

    @Test
    void 포인트_적립_대상자가_존재하지_않으면_예외가_발생한다() {
        //given
        PointCollectRequest request1 = new PointCollectRequest(givenOwnerIdNotPresent(), 1_000, 1L);

        //when & then
        NotFoundPointOwnerException error = assertThrows(NotFoundPointOwnerException.class, () -> pointService.collect(request1));
        assertTrue(error.getMessage().contains("ownerId=" + request1.getOwnerId()));

        //given
        PointCollectRequest request2 = new PointCollectRequest(givenOwnerIdPresent(), 1_000, 1L);

        //when & then
        assertDoesNotThrow(() -> pointService.collect(request2));
    }

    @Test
    void 포인트_적립_요청하면_포인트데이터가_저장함수를_호출한다() {
        //given
        PointCollectRequest request = new PointCollectRequest(givenOwnerIdPresent(), 1_000, 1L);

        //when
        pointService.collect(request);

        //then
        assertTrue(stubPointService.isCalledToSave());
    }
}
