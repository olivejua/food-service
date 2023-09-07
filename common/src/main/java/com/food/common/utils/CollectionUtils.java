package com.food.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class CollectionUtils {
    public static <T, R> List<R> mappedBy(Collection<T> target, Function<T, R> mapper) {
        return target.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
}
