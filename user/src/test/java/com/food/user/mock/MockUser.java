package com.food.user.mock;

import com.food.common.user.business.internal.dto.UserDto;
import lombok.Builder;

public class MockUser extends UserDto {

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public MockUser(Long id, String nickname) {
        super();

        this.id = id;
        this.nickname = nickname;
    }

    public static MockUser create() {
        return MockUser.testBuilder()
                .nickname("TestUser")
                .build();
    }
}
