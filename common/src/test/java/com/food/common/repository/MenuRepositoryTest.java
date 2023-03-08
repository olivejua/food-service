package com.food.common.repository;

import com.food.common.menu.domain.Menu;
import com.food.common.menu.domain.MenuOption;
import com.food.common.menu.domain.MenuSelection;
import com.food.common.menu.repository.MenuOptionRepository;
import com.food.common.menu.repository.MenuRepository;
import com.food.common.menu.repository.MenuSelectionRepository;
import com.food.common.store.domain.Store;
import com.food.common.store.domain.StoreOwner;
import com.food.common.store.domain.type.OpenStatus;
import com.food.common.store.repository.StoreRepository;
import com.food.common.user.domain.User;
import com.food.common.user.repository.StoreOwnerRepository;
import com.food.common.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;

import static com.food.common.utils.ByteUtils.byteValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class MenuRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreOwnerRepository storeOwnerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuOptionRepository menuOptionRepository;

    @Autowired
    private MenuSelectionRepository menuSelectionRepository;

    @Autowired
    private EntityManager em;

    @Test
    void Batch사이즈없이_일대다조회시_쿼리가_몇개실행되는지_테스트한다() {
        //given
        User mockUser = userRepository.save(User.create("testNick"));
        StoreOwner mockStoreOwner = storeOwnerRepository.save(StoreOwner.create(mockUser));
        Store mockStore = storeRepository.save(Store.create("A Restaurant", mockStoreOwner, 12_000, OpenStatus.OPEN));

        Menu mockMenuA = menuRepository.save(Menu.create(mockStore, "A Menu", 14_000, 30));
        Menu mockMenuB = menuRepository.save(Menu.create(mockStore, "B Menu", 7_000, 20));
        Menu mockMenuC = menuRepository.save(Menu.create(mockStore, "A Menu", 3_000, 10));

        MenuOption mockOptionA = menuOptionRepository.save(MenuOption.menuOption(mockMenuA, "A Option - A Menu", byteValue(1), byteValue(1)));
        MenuOption mockOptionB = menuOptionRepository.save(MenuOption.menuOption(mockMenuB, "B Option - B Menu", byteValue(0), byteValue(2)));

        MenuSelection mockMenuSelectionA = menuSelectionRepository.save(MenuSelection.create(mockOptionA, "A Selection - A Option - A Menu", 3_000));
        MenuSelection mockMenuSelectionB = menuSelectionRepository.save(MenuSelection.create(mockOptionA, "B Selection - A Option - A Menu", 2_000));
        MenuSelection mockMenuSelectionC = menuSelectionRepository.save(MenuSelection.create(mockOptionA, "C Selection - A Option - A Menu", 1_000));
        MenuSelection mockMenuSelectionD = menuSelectionRepository.save(MenuSelection.create(mockOptionB, "D Selection - B Option - B Menu", 1_000));
        MenuSelection mockMenuSelectionE = menuSelectionRepository.save(MenuSelection.create(mockOptionB, "E Selection - B Option - B Menu", 1_000));

        em.flush();
        em.clear();

        //when
        List<Menu> menus = menuRepository.findAllByStore(mockStore);

        //then
        assertEquals(3, menus.size());
        assertEquals(1, menus.get(0).getOptions().size());
        assertEquals(3, menus.get(0).getOptions().get(0).getSelections().size());
    }
}
