package com.food.common.menu.business.internal;

import com.food.common.menu.business.internal.dto.MenuDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface MenuCommonService {
    /**
     * 주어진 메뉴데이터 객체들을 모두 저장한다.
     * @param menus 저장할 메뉴데이터 목록. null인 요소를 포함할 수 없으며, 비어있을 수 없다.
     * @return 저장된 ID가 주입된 메뉴 목록. 순서는 요청목록 순서와 동일하며, null이거나 null인 요소를 포함하지 않는다.
     */
    List<MenuDto> saveAll(@NotEmpty List<@NotNull MenuDto> menus);
}
