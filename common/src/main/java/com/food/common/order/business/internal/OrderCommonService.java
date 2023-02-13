package com.food.common.order.business.internal;

import com.food.common.order.business.internal.dto.OrderDto;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface OrderCommonService {
    /**
     * OrderId로 주문데이터를 단건 조회한다.
     * @param id 검색할 ID. null일 수 없다.
     * @return ID로 조회해온 주문데이터가 담긴 Dto 객체
     */
    Optional<OrderDto> findById(@NotNull Long id);

    /**
     * OrderId로 주문데이터의 존재유무를 리턴한다.
     * @param id 검색할 ID. null일 수 없다.
     * @return 데이터 존재 유무
     */
    boolean existsById(@NotNull Long id);

    /**
     * 주문데이터를 저장하고, 새로운 ID가 부여된 Dto 객체를 리턴한다.
     * @param order 저장될 주문데이터. null일 수 없다.
     * @return 저장된 주문 데이터가 담긴 Dto 객체. 리턴객체는 null일 수 없다.
     */
    OrderDto save(OrderDto order);
}
