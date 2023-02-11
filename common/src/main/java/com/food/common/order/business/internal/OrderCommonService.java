package com.food.common.order.business.internal;

import com.food.common.order.business.internal.dto.OrderDto;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Repository에서 별다른 비즈니스 로직을 포함시키지 않은 채
 * Entity를 직접접근을 방지하기 위해 DTO로만 리턴해준다.
 */

public interface OrderCommonService {
    Optional<OrderDto> findById(@NotNull Long id);

    boolean existsById(@NotNull Long id);

    OrderDto save(OrderDto order);
}
