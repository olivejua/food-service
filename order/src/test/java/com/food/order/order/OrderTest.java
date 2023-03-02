package com.food.order.order;

import com.food.common.error.CommonErrors;
import com.food.common.error.exception.InvalidRequestParameterException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    /**
     * 요구사항
     *
     * 요청 정보거
     * 메뉴 목록 (NotEmpty)
     * (메뉴 Unit: 메뉴 id, 수량, 선택지 목록) (NotNull)
     * (메뉴 선택지 Unit: 메뉴 선택지 id, 수량) (NotNull)
     *
     * 주문메뉴는 하나이상 존재해야한다. (v)
     * 주문메뉴의 수량은 1개 이상이어야한다. (v)
     * 메뉴와 상점이 존재해야한다.
     * 한 상점안의 메뉴만 주문이 가능하다.
     * 상점은 Open 상태여야한다.
     * 상점의 최소 주문금액보다 주문금액이 같거나 커야한다.
     * 상태가 '요청'인 주문데이터를 저장하고, 저장된 주문 ID를 반환한다.
     */

    @Test
    void 주문메뉴는_하나이상_존재해야한다() {
        InvalidRequestParameterException error = assertThrows(InvalidRequestParameterException.class, () -> new OrderDoRequest(Collections.emptyList()));
        assertEquals(CommonErrors.INVALID_REQUEST_PARAMETERS.getCode(), error.getErrorCode());
        assertTrue(error.getMessage().contains("주문메뉴는 하나이상 존재해야 합니다."));

        OrderMenuRequest orderMenuRequest = new OrderMenuRequest(1L, 1);
        assertDoesNotThrow(() -> new OrderDoRequest(List.of(orderMenuRequest)));
    }

    @Test
    void 주문메뉴의_수량은_1개_이상이어야_한다() {
        int count = 0;

        InvalidRequestParameterException error = assertThrows(InvalidRequestParameterException.class, () -> new OrderMenuRequest(1L, count));
        assertTrue(error.getMessage().contains("주문메뉴의 수량은 1개 이상이어야 합니다."));
    }

    @Test
    void 주문메뉴선택지의_수량은_1개_이상이어야_한다() {
        int count = 0;

        InvalidRequestParameterException error = assertThrows(InvalidRequestParameterException.class, () -> new OrderMenuSelectionRequest(1L, count));
        assertTrue(error.getMessage().contains("주문메뉴의 수량은 1개 이상이어야 합니다."));
    }

    @Test
    void 주문요청한_메뉴ID의_메뉴데이터가_존재해야한다() {

    }
}
