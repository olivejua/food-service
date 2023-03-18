package com.food.order.order.temp;

import com.food.common.menu.business.external.MenuService;
import com.food.common.menu.business.external.dto.StoreMenus;
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
    private final MenuService menuService;

    @Override
    public Long order(OrderDoRequest request) {
        StoreDto store = storeCommonService.findById(request.getStoreId())
                .orElseThrow(() -> new NotFoundStoreException(request.getStoreId()));

        if (store.isClosed()) {
            throw new InvalidStoreOpenStatusException();
        }

        StoreMenus menus = menuService.findAllMenusByStoreId(request.getStoreId());
        if (!menus.containsAll(request.getMenuIds())) {
            throw new NotFoundMenuException();
        }

        /**
         * 주문금액을 계산한다.
         * 메뉴Id(has OptionId, SelectionId)의 가격
         * 수량
         */

        throw new NotEnoughOrderAmountException(null);
    }
}
