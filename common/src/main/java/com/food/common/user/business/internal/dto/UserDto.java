package com.food.common.user.business.internal.dto;

import com.food.common.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class UserDto {
    protected Long id;
    protected String nickname;

    public UserDto(@NotNull final User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
    }


}
