package com.food.common.utils;

import org.springframework.util.Assert;

public class UsedPoints extends Amount {

    private UsedPoints(int value) {
        super(value);
    }

    public static UsedPoints won(int value) {
        Assert.isTrue(value > 0, "The value must be greater than zero.");
        Assert.isTrue(value % 10 == 0, "The value must in units of 10.");

        return new UsedPoints(value);
    }
}
