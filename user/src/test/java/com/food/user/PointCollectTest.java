package com.food.user;

import com.food.common.error.exception.InvalidRequestParameterException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PointCollectTest {
    /**
     * [적립]
     * 적립 금액은 0 이상이고, 10원단위여야 한다. (v)
     * 적립 대상자가 존재하지 않으면 예외가 발생한다.
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



    @Test
    void 포인트_적립_금액은_0원_이상이어야한다() {
        //given
        int collectedAmount1 = 0;

        //when & then
        InvalidRequestParameterException error1 = assertThrows(InvalidRequestParameterException.class,
                () -> new PointCollectRequest(1L, collectedAmount1));

        assertTrue(error1.getMessage().contains("포인트 적립금액은 0이상의 금액이어야 합니다."));

        //given
        int collectedAmount2 = 1_000;

        //when & then
        assertDoesNotThrow(() -> new PointCollectRequest(1L, collectedAmount2));

        int collectedAmount3 = -1_000;
        assertThrows(InvalidRequestParameterException.class,
                () -> new PointCollectRequest(1L, collectedAmount3));
    }

    @Test
    void 포인트_적립금액이_10원_단위여야_한다() {
        //given
        int collectedAmount1 = 1_111;

        //when
        InvalidRequestParameterException error1 = assertThrows(InvalidRequestParameterException.class,
                () -> new PointCollectRequest(1L, collectedAmount1));

        //then
        assertTrue(error1.getMessage().contains("포인트 적립금액은 10원 단위의 금액이어야 합니다."));

        //given
        int collectedAmount2 = 1_110;

        //when & then
        assertDoesNotThrow(() -> new PointCollectRequest(1L, collectedAmount2));
    }

//    private Long givenOwnerIdNotPresent() {
//        Long ownerId = 1L;
//
//        if (stubUserService.existsById(ownerId)) {
//            throw new IllegalArgumentException();
//        }
//
//        return ownerId;
//    }
}
