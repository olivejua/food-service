package com.food.order.order;

import com.food.common.error.CommonErrors;
import com.food.common.error.exception.InvalidRequestParameterException;
import com.food.common.menu.business.external.dto.StoreMenuItem;
import com.food.common.menu.business.internal.dto.MenuSelectionDto;
import com.food.common.store.domain.type.OpenStatus;
import com.food.common.utils.Amount;
import com.food.order.common.stub.StubOrderService;
import com.food.order.order.mock.*;
import com.food.order.order.stubrepository.StubMenuService;
import com.food.order.order.stubrepository.StubStoreCommonService;
import com.food.order.order.temp.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.food.order.order.mock.MockMenuFactory.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private OrderService orderService;
    private StubOrderService stubOrderCommonService;
    private StubStoreCommonService stubStoreCommonService;
    private StubMenuService stubMenuService;

    @BeforeEach
    void setup() {
        stubOrderCommonService = new StubOrderService();
        stubStoreCommonService = new StubStoreCommonService();
        stubMenuService = new StubMenuService();
        orderService = new DefaultOrderService(stubOrderCommonService, stubStoreCommonService, stubMenuService);
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

        MockMenu mockMenuA = mockMenu(mockStoreId1, "A Menu", 12_000);
        MockMenu mockMenuB = mockMenu(mockStoreId1, "B Menu", 7_000);

        MockMenuOption mockOptionA = mockOption(mockMenuA.getId(), "Option A", 1, 1);

        MenuSelectionDto mockSelectionA = mockSelection(mockOptionA.getId(), "Selection A", 2_000);
        MenuSelectionDto mockSelectionB = mockSelection(mockOptionA.getId(), "Selection B", 1_000);

        StoreMenuItem menuA = MockStoreMenuItem.mock(mockMenuA, List.of(MockStoreMenuOptionItem.mock(mockOptionA, List.of(mockSelectionA, mockSelectionB))));
        StoreMenuItem menuB = MockStoreMenuItem.mock(mockMenuB, Collections.emptyList());

        stubMenuService.remember(MockStoreMenus.create(mockStoreId1, List.of(menuA, menuB)));

        MockMenu mockMenuC = mockMenu(mockStoreId2, "C Menu", 20_000);

        //when & then
        assertThrows(NotFoundMenuException.class,
                () -> orderService.order(new OrderDoRequest(mockStoreId1,
                        List.of(new OrderMenuRequest(mockMenuA.getId(), 1), new OrderMenuRequest(mockMenuC.getId(), 1)))));

    }

    @Disabled
    @Test
    void 주문금액이_가게의_최소주문금액보다_커야한다() {
        Long mockStoreId1 = givenStoreIdPresent(Amount.won(30_000));

        MockMenu mockMenuA = mockMenu(mockStoreId1, "A Menu", 12_000);
        MockMenu mockMenuB = mockMenu(mockStoreId1, "B Menu", 3_000);

        MockMenuOption mockOptionA = mockOption(mockMenuA.getId(), "Option A", 1, 1);

        MockMenuSelection mockSelectionA = mockSelection(mockOptionA.getId(), "Selection A", 2_000);
        MockMenuSelection mockSelectionB = mockSelection(mockOptionA.getId(), "Selection B", 1_000);

        StoreMenuItem menuA = MockStoreMenuItem.mock(mockMenuA, List.of(MockStoreMenuOptionItem.mock(mockOptionA, List.of(mockSelectionA, mockSelectionB))));
        StoreMenuItem menuB = MockStoreMenuItem.mock(mockMenuB, Collections.emptyList());

        stubMenuService.remember(MockStoreMenus.create(mockStoreId1, List.of(menuA, menuB)));

        assertThrows(NotEnoughOrderAmountException.class,
                () -> orderService.order(new OrderDoRequest(mockStoreId1,
                        List.of(new OrderMenuRequest(mockMenuA.getId(), 1, List.of(new OrderMenuSelectionRequest(mockSelectionA.getId(), 1)))))));


        assertDoesNotThrow(() -> orderService.order(new OrderDoRequest(mockStoreId1,
                List.of(new OrderMenuRequest(mockMenuA.getId(), 2, List.of(new OrderMenuSelectionRequest(mockSelectionA.getId(), 1)))))));
    }

    private Long givenStoreIdNotPresent() {
        Long storeId = 1L;

        if (stubOrderCommonService.existsById(storeId)) {
            throw new IllegalArgumentException();
        }

        return storeId;
    }

    private Long givenStoreIdPresent() {
        return this.givenStoreIdPresent(Amount.won(12_000));
    }

    private Long givenStoreIdPresent(Amount minOrderAmount) {
        MockStore mockStore = MockStore.testBuilder()
                .name("A Restaurant")
                .minOrderAmount(minOrderAmount)
                .status(OpenStatus.OPEN)
                .build();

        return stubStoreCommonService.save(mockStore).getId();
    }
}
