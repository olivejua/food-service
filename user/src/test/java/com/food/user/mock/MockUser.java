package com.food.user.mock;

import com.food.common.user.business.internal.dto.UserDto;

public class MockUser {
    public static Builder builder() {
        return new Builder();
    }

    public static UserDto create() {
        return builder().build();
    }

    public static class Builder {
        private Long id;
        private String nickname = "TestUser";

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public UserDto build() {
            return UserDto.builder()
                    .id(id)
                    .nickname(nickname)
                    .build();
        }
    }
}
