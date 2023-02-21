package com.food.user.stub;

import com.food.common.user.business.internal.UserCommonService;
import com.food.common.user.business.internal.dto.UserDto;
import com.food.common.user.enumeration.Role;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StubUserService implements UserCommonService {
    private final Map<Long, UserDto> data = new HashMap<>();
    private Long autoIncrementKey = -1L;

    public boolean existsById(Long userId) {
        return data.containsKey(userId);
    }

    public UserDto save(UserDto user) {
        if (data.containsKey(user.getId())) {
            data.put(user.getId(), user);
            return user;
        }

        UserDto newOne = UserDto.builder()
                .id(autoIncrementKey--)
                .nickname(user.getNickname())
                .build();
        data.put(newOne.getId(), newOne);

        return newOne;
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Role findRoleById(Long id) {
        return null;
    }
}
