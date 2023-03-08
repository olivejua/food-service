package com.food.common.menu.business.internal;

import com.food.common.menu.business.internal.dto.MenuOptionDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Validated
public interface MenuOptionCommonService {

    /**
     * 주어진 메뉴ID 목록의 연관된 옵션을 조회한다.
     * @param menuIds 메뉴데이터 ID 목록
     * @return 주어진 메뉴 ID를 가진 메뉴옵션의 데이터 목록.
     *         {Key: MenuId, Value: MenuOption 목록}
     *         null이거나 null인 요소는 포함하지 않는다.
     *         Menu에 해당하는 메뉴옵션이 존재하지 않을 경우 Map에 요소를 포함시키지 않는다.
     */
    Map<Long, List<MenuOptionDto>> findAllByMenuIds(@NotEmpty Set<@NotNull Long> menuIds);
}
