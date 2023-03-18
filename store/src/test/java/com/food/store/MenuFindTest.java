package com.food.store;

import com.food.common.menu.business.external.MenuService;
import com.food.common.menu.business.external.dto.StoreMenuItem;
import com.food.common.menu.business.external.dto.StoreMenuOptionItem;
import com.food.common.menu.business.external.dto.StoreMenus;
import com.food.common.menu.business.internal.dto.MenuDto;
import com.food.common.menu.business.internal.dto.MenuOptionDto;
import com.food.common.menu.business.internal.dto.MenuSelectionDto;
import com.food.common.store.domain.type.OpenStatus;
import com.food.common.utils.Amount;
import com.food.common.utils.ByteUtils;
import com.food.store.business.DefaultMenuService;
import com.food.store.error.NotExistMenusException;
import com.food.store.error.NotFoundStoreException;
import com.food.store.mock.MockMenu;
import com.food.store.mock.MockMenuOption;
import com.food.store.mock.MockMenuSelection;
import com.food.store.mock.MockStore;
import com.food.store.stub.StubMenuCommonService;
import com.food.store.stub.StubMenuOptionCommonService;
import com.food.store.stub.StubMenuSelectionCommonService;
import com.food.store.stub.StubStoreCommonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MenuFindTest {
    private MenuService menuService;
    private StubStoreCommonService storeCommonService;
    private StubMenuCommonService menuCommonService;
    private StubMenuOptionCommonService menuOptionCommonService;
    private StubMenuSelectionCommonService menuSelectionCommonService;

    @BeforeEach
    void setup() {
        storeCommonService = new StubStoreCommonService();
        menuCommonService = new StubMenuCommonService();
        menuOptionCommonService = new StubMenuOptionCommonService();
        menuSelectionCommonService = new StubMenuSelectionCommonService();
        menuService = new DefaultMenuService(storeCommonService, menuCommonService, menuOptionCommonService, menuSelectionCommonService);
    }

    @Test
    void 요청StoreId의_Store데이터가_존재해야한다() {
        Long mockStoreId = givenStoreIdNotPresent();

        NotFoundStoreException error = assertThrows(NotFoundStoreException.class, () -> menuService.findAllMenusByStoreId(mockStoreId));
        assertTrue(error.getMessage().contains(String.valueOf(mockStoreId)));
    }

    @Test
    void 가게메뉴는_한가지_이상_존재해야한다() {
        Long mockStoreId = givenStoreIdPresent();

        assertThrows(NotExistMenusException.class, () -> menuService.findAllMenusByStoreId(mockStoreId));
    }

    @Test
    void 메뉴_안에_옵션이_존재할_경우_옵션목록도_함께_가져온다() {
        //given
        Long mockStoreId = givenStoreIdPresent();

        assertThrows(NotExistMenusException.class, () -> menuService.findAllMenusByStoreId(mockStoreId));

        MenuDto mockMenuA = givenMenuPresent(mockStoreId, "A Menu", 15_000, 40);
        MenuDto mockMenuB = givenMenuPresent(mockStoreId, "B Menu", 7_000, 10);

        MenuOptionDto mockMenuOptionA = givenMenuOptionPresent(mockMenuA.getId(), "A MenuOption", 1, 1);

        MenuSelectionDto mockMenuSelectionA = givenMenuSelectionPresent(mockMenuOptionA.getId(), "A Selection", 3_000);
        MenuSelectionDto mockMenuSelectionB = givenMenuSelectionPresent(mockMenuOptionA.getId(), "B Selection", 1_000);

        //when
        StoreMenus result = menuService.findAllMenusByStoreId(mockStoreId);

        //then
        assertEquals(2, result.getMenus().size());
        List<StoreMenuItem> menus = result.getMenus();

        StoreMenuItem findAMenu = menus.stream()
                .filter(menu -> menu.getId().equals(mockMenuA.getId())).findFirst()
                .orElseThrow(IllegalArgumentException::new);
        assertEquals(mockMenuA.getAmount(), findAMenu.getAmount());
        assertEquals(mockMenuA.getCookingMinutes(), findAMenu.getCookingMinutes());

        assertEquals(1, findAMenu.getOptions().size());

        StoreMenuOptionItem findAOption = findAMenu.getOptions().get(0);
        assertEquals(mockMenuOptionA.getId(), findAOption.getId());
        assertEquals(mockMenuOptionA.getName(), findAOption.getName());
        assertEquals(mockMenuOptionA.getMinSize(), findAOption.getMinSize());
        assertEquals(mockMenuOptionA.getMaxSize(), findAOption.getMaxSize());

        List<MenuSelectionDto> findSelections = findAOption.getSelections();
        assertEquals(2, findSelections.size());
        MenuSelectionDto findAMenuSelection = findSelections.stream()
                .filter(selection -> selection.getId().equals(mockMenuSelectionA.getId())).findFirst()
                .orElseThrow(IllegalArgumentException::new);

        assertEquals(mockMenuSelectionA.getId(), findAMenuSelection.getId());
        assertEquals(mockMenuSelectionA.getName(), findAMenuSelection.getName());
        assertEquals(mockMenuSelectionA.getAmount(), findAMenuSelection.getAmount());
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

    private MenuDto givenMenuPresent(Long storeId, String name, Integer amount, Integer cookingMinutes) {
        MockMenu mockMenu = MockMenu.testBuilder()
                .storeId(storeId)
                .name(name)
                .amount(Amount.won(amount))
                .cookingMinutes(cookingMinutes)
                .build();
        return menuCommonService.save(mockMenu);
    }

    private MenuOptionDto givenMenuOptionPresent(Long menuId, String name, Integer minSize, Integer maxSize) {
        MockMenuOption mockMenuOption = MockMenuOption.testBuilder()
                .menuId(menuId)
                .name(name)
                .minSize(ByteUtils.byteValue(minSize))
                .maxSize(ByteUtils.byteValue(maxSize))
                .build();
        return menuOptionCommonService.save(mockMenuOption);
    }

    private MenuSelectionDto givenMenuSelectionPresent(Long optionId, String name, Integer amount) {
        MockMenuSelection mockMenuSelection = MockMenuSelection.testBuilder()
                .optionId(optionId)
                .name(name)
                .amount(Amount.won(amount))
                .build();

        return menuSelectionCommonService.save(mockMenuSelection);
    }
}
