package com.food.order.order;

import com.food.common.menu.business.internal.MenuCommonService;
import com.food.common.menu.business.internal.dto.MenuDtos;
import com.food.common.order.business.internal.OrderCommonService;
import com.food.common.store.business.internal.StoreCommonService;
import com.food.common.store.business.internal.dto.StoreDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultOrderService implements OrderService {
    private final OrderCommonService orderCommonService;
    private final StoreCommonService storeCommonService;
    private final MenuCommonService menuCommonService;

    @Override
    public Long order(OrderDoRequest request) {
        StoreDto store = storeCommonService.findById(request.getStoreId())
                .orElseThrow(() -> new NotFoundStoreException(request.getStoreId()));

        if (store.isClosed()) {
            throw new InvalidStoreOpenStatusException();
        }

        MenuDtos menus = menuCommonService.findAllByStoreId(request.getStoreId());
        if (!menus.containsAll(request.getMenuIds())) {
            throw new NotFoundMenuException();
        }

        return null;
    }
}
