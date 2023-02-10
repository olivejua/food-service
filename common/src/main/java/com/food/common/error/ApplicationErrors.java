package com.food.common.error;

public interface ApplicationErrors {
    String getCode();
    String getMessage();

    default String appendMessage(String extraMessage) {
        return String.format("%s \n%s", getMessage(), extraMessage);
    }
}
