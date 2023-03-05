package com.food.store;

import com.food.common.menu.business.external.MenuService;
import com.food.common.store.domain.type.OpenStatus;
import com.food.common.utils.Amount;
import com.food.store.business.DefaultMenuService;
import com.food.store.error.NotFoundStoreException;
import com.food.store.mock.MockStore;
import com.food.store.stub.StubMenuCommonService;
import com.food.store.stub.StubStoreCommonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MenuFindTest {
    private MenuService menuService;
    private StubStoreCommonService storeCommonService;
    private StubMenuCommonService menuCommonService;

    @BeforeEach
    void setup() {
        storeCommonService = new StubStoreCommonService();
        menuCommonService = new StubMenuCommonService();
        menuService = new DefaultMenuService(storeCommonService, menuCommonService);

    }

    @Test
    void 요청StoreId의_Store데이터가_존재해야한다() {
        Long mockStoreId = givenStoreIdNotPresent();

        NotFoundStoreException error = assertThrows(NotFoundStoreException.class, () -> menuService.findAllMenusByStoreId(mockStoreId));
        assertTrue(error.getMessage().contains(String.valueOf(mockStoreId)));

        assertDoesNotThrow(() -> menuService.findAllMenusByStoreId(givenStoreIdPresent()));
    }

    private Long givenStoreIdPresent() {
        MockStore mockStore = MockStore.testBuilder()
                .name("A Restaurant")
                .status(OpenStatus.OPEN)
                .minOrderAmount(Amount.won(12_000))
                .build();
        return storeCommonService.save(mockStore).getId();
    }

    private Long givenStoreIdNotPresent() {
        Long storeId = 1L;

        if (storeCommonService.existsById(storeId)) {
            throw new IllegalArgumentException();
        }

        return storeId;
    }

//    @Test
//    void 가게Id로_전체_메뉴목록을_조회한다() {
//        MockMenu aMenu = MockMenu.testBuilder()
//                .storeId(mockStoreId)
//                .name("A Menu")
//                .amount(Amount.won(15_000))
//                .cookingMinutes(40)
//                .build();
//        Long mockAMenuId = menuCommonService.save(aMenu).getId();
//
//        MockMenu bMenu = MockMenu.testBuilder()
//                .storeId(mockStoreId)
//                .name("B Menu")
//                .amount(Amount.won(7_000))
//                .cookingMinutes(10)
//                .build();
//        Long mockBMenuId = menuCommonService.save(bMenu).getId();
//
//        //given
//        Long storeId = 1L;
//
//        //when
//        StoreMenus storeMenus = menuService.findAllMenusByStoreId(storeId);
//
//        //then
//        assertEquals(storeId, storeMenus.getStoreId());
//    }
}
