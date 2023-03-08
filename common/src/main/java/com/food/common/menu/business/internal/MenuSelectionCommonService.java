package com.food.common.menu.business.internal;

import com.food.common.menu.business.internal.dto.MenuSelectionDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MenuSelectionCommonService {

    /**
     * 주어진 메뉴옵션ID 목록의 연관된 선택지을 조회한다.
     * @param menuOptionIds 메뉴 옵션데이터 ID 목록.
     * @return 주어진 메뉴 ID를 가진 메뉴선택지의 데이터 목록.
     *         {Key: MenuOptionId, Value: MenuSelection 목록}
     *         null이거나 null인 요소는 포함하지 않는다.
     * @throws NotExistMenuSelectionException MenuOption에 해당하는 메뉴선택지가 1개 이상 존재하지 않을 경우 발생한다.
     */
    Map<Long, List<MenuSelectionDto>> findAllByMenuOptionIds(@NotEmpty Set<@NotNull Long> menuOptionIds);
}
