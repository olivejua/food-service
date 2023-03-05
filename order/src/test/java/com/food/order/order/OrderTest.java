package com.food.order.order;

import com.food.common.error.CommonErrors;
import com.food.common.error.exception.InvalidRequestParameterException;
import com.food.common.store.domain.type.OpenStatus;
import com.food.common.utils.Amount;
import com.food.order.common.stub.StubOrderService;
import com.food.order.order.mock.MockMenu;
import com.food.order.order.mock.MockStore;
import com.food.order.order.stubrepository.StubStoreCommonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private OrderService orderService;
    private StubOrderService stubOrderCommonService;
    private StubStoreCommonService stubStoreCommonService;
    private StubMenuCommonService stubMenuCommonService;

    @BeforeEach
    void setup() {
        stubOrderCommonService = new StubOrderService();
        stubStoreCommonService = new StubStoreCommonService();
        stubMenuCommonService = new StubMenuCommonService();
        orderService = new DefaultOrderService(stubOrderCommonService, stubStoreCommonService, stubMenuCommonService);
    }

    @Test
    void 주문메뉴는_하나이상_존재해야한다() {
        Long mockStoreId = 1L;

        InvalidRequestParameterException error = assertThrows(InvalidRequestParameterException.class, () -> new OrderDoRequest(mockStoreId, Collections.emptyList()));
        assertEquals(CommonErrors.INVALID_REQUEST_PARAMETERS.getCode(), error.getErrorCode());
        assertTrue(error.getMessage().contains("주문메뉴는 하나이상 존재해야 합니다."));

        OrderMenuRequest orderMenuRequest = new OrderMenuRequest(1L, 1);
        assertDoesNotThrow(() -> new OrderDoRequest(mockStoreId, List.of(orderMenuRequest)));
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
    void 주문요청한_가게ID의_가게데이터가_존재해야한다() {
        //given
        Long mockStoreIdNotPresent = givenStoreIdNotPresent();

        //when & then
        NotFoundStoreException error = assertThrows(NotFoundStoreException.class, () -> orderService.order(new OrderDoRequest(mockStoreIdNotPresent, List.of(new OrderMenuRequest(1L, 1)))));
        assertTrue(error.getMessage().contains(String.valueOf(mockStoreIdNotPresent)));
    }

    @Test
    void 주문요청한_가게는_Open상태여아한다() {
        //given
        MockStore mockStore = MockStore.testBuilder()
                .name("A Restaurant")
                .minOrderAmount(Amount.won(10_000))
                .status(OpenStatus.CLOSED)
                .build();
        Long mockStoreId = stubStoreCommonService.save(mockStore).getId();

        //when & then
        assertThrows(InvalidStoreOpenStatusException.class, () -> orderService.order(new OrderDoRequest(mockStoreId, List.of(new OrderMenuRequest(1L, 1)))));
    }

    @Test
    void 요청한_메뉴ID_목록의_데이터_전부_가게메뉴_내에_존재해야한다() {
        //given
        Long mockStoreId1 = givenStoreIdPresent();
        Long mockStoreId2 = givenStoreIdPresent();

        Long aMenuId = stubMenuCommonService.save(MockMenu.testBuilder()
                .storeId(mockStoreId1)
                .name("A Menu")
                .amount(Amount.won(12_000))
                .build()).getId();

        Long bMenuId = stubMenuCommonService.save(MockMenu.testBuilder()
                .storeId(mockStoreId2)
                .name("B Menu")
                .amount(Amount.won(7_000))
                .build()).getId();

        //when & then
        assertThrows(NotFoundMenuException.class,
                () -> orderService.order(new OrderDoRequest(mockStoreId1,
                        List.of(new OrderMenuRequest(aMenuId, 1), new OrderMenuRequest(bMenuId, 1)))));

        //given
        Long mockStoreId3 = givenStoreIdPresent();

        Long cMenuId = stubMenuCommonService.save(MockMenu.testBuilder()
                .storeId(mockStoreId3)
                .name("C Menu")
                .amount(Amount.won(14_000))
                .build()).getId();

        Long dMenuId = stubMenuCommonService.save(MockMenu.testBuilder()
                .storeId(mockStoreId3)
                .name("D Menu")
                .amount(Amount.won(5_000))
                .build()).getId();

        assertDoesNotThrow(() -> orderService.order(new OrderDoRequest(mockStoreId3,
                        List.of(new OrderMenuRequest(cMenuId, 1), new OrderMenuRequest(dMenuId, 2)))));

    }

    private Long givenStoreIdNotPresent() {
        Long storeId = 1L;

        if (stubOrderCommonService.existsById(storeId)) {
            throw new IllegalArgumentException();
        }

        return storeId;
    }

    private Long givenStoreIdPresent() {
        MockStore mockStore = MockStore.testBuilder()
                .name("A Restaurant")
                .minOrderAmount(Amount.won(10_000))
                .status(OpenStatus.OPEN)
                .build();

        return stubStoreCommonService.save(mockStore).getId();
    }


}
