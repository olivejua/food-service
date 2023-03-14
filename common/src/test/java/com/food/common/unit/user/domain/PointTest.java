package com.food.common.unit.user.domain;

import com.food.common.mock.user.MockPoint;
import com.food.common.mock.user.MockUser;
import com.food.common.unit.SuperValidationTests;
import com.food.common.user.domain.Point;
import com.food.common.user.enumeration.PointType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.food.common.user.UserValidationFailureMessages.Point.TYPE_CANNOT_BE_NULL;
import static com.food.common.user.UserValidationFailureMessages.Point.USER_CANNOT_BE_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PointTest extends SuperValidationTests<Point> {
    @Test
    void validateUserInPoint() {
        Point mockPointWithNullUser = MockPoint.builder()
                .user(null)
                .build();

        Point mockPointWithNormalUser = MockPoint.builder()
                .user(MockUser.builder().build())
                .build();

        assertAll(
                () -> assertThat(failureMessagesOf(mockPointWithNullUser)).containsExactlyInAnyOrder(USER_CANNOT_BE_NULL),
                () -> assertThat(failureMessagesOf(mockPointWithNormalUser)).isEmpty()
        );
    }

    @Test
    void validateTypeInPoint() {
        Point mockPointWithNullType = MockPoint.builder()
                .type(null)
                .build();

        Set<Point> mockPointsWithEnumTypes = Arrays.stream(PointType.values()).map(
                        type -> MockPoint.builder()
                                .type(type)
                                .build())
                .collect(Collectors.toSet());

        assertAll(
                () -> assertThat(failureMessagesOf(mockPointWithNullType)).containsExactlyInAnyOrder(TYPE_CANNOT_BE_NULL),
                () -> assertThat(failureMessagesOf(mockPointsWithEnumTypes)).isEmpty()
        );
    }
}
