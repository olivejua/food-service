package com.food.common.utils;

import org.springframework.util.Assert;

import java.util.Objects;

public class Amount {
    protected final int value;

    protected Amount(int value) {
        this.value = value;
    }

    public static Amount won(int value) {
        Assert.isTrue(value >= 0, "The value must be positive.");

        if (value == 0) return zero();

        return new Amount(value);
    }

    public static Amount zero() {
        return new Amount(0);
    }

    public Amount subtract(Amount amount) {
        Assert.notNull(amount, "The paramter amount must not be null.");
        Assert.isTrue(this.value >= amount.value, "The value of parameter amount must be greater than the value of field amount.");

        return Amount.won(this.value - amount.value);
    }

    public Amount multiply(float value) {
        float result = this.value * value;
        return new Amount((int) result);
    }

    public Amount plus(Amount amount) {
        Assert.notNull(amount, "The paramter amount must not be null.");

        return Amount.won(this.value + amount.value);
    }

    public boolean isGreaterThanOrEqualTo(Amount compared) {
        return this.value >= compared.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Amount amount = (Amount) o;
        return this.value == amount.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
