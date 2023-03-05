package com.food.store.stub;

import com.food.common.store.business.internal.StoreCommonService;
import com.food.common.store.business.internal.dto.StoreDto;
import com.food.store.mock.MockStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StubStoreCommonService implements StoreCommonService {
    private final Map<Long, StoreDto> data = new HashMap<>();
    private Long autoIncrementKey = -1L;

    public StoreDto save(MockStore store) {
        if (data.containsKey(store.getId())) {
            data.put(store.getId(), store);
            return store;
        }

        MockStore newOne = MockStore.testBuilder()
                .id(autoIncrementKey--)
                .name(store.getName())
                .status(store.getStatus())
                .minOrderAmount(store.getMinOrderAmount())
                .build();
        data.put(newOne.getId(), newOne);

        return newOne;
    }

    @Override
    public Optional<StoreDto> findById(Long storeId) {
        return Optional.ofNullable(data.get(storeId));
    }

    @Override
    public boolean existsById(Long storeId) {
        return data.containsKey(storeId);
    }
}
