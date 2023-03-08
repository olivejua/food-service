package com.food.common.menu.repository;

import com.food.common.menu.domain.Menu;
import com.food.common.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByStore(Store store);
}
