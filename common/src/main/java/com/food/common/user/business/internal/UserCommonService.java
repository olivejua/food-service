package com.food.common.user.business.internal;

import com.food.common.user.business.internal.dto.UserDto;
import com.food.common.user.enumeration.Role;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Validated
public interface UserCommonService {
    /**
     * UserId로 유저데이터의 존재유무를 리턴한다.
     * @param userId 검색할 ID. null일 수 없다.
     * @return 데이터 존재 유무
     */
    boolean existsById(@NotNull Long userId);

    /**
     * ID로 유저데이터를 리턴한다.
     * @param id 검색할 ID. null일 수 없다.
     * @return 주어진 ID를 보유한 유저데이터 또는 찾을 수 없는 경우 Optional#empty()
     */
    Optional<UserDto> findById(@NotNull Long id);


    Role findRoleById(@NotNull Long id);
}
