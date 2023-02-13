package com.food.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class CollectionUtils {
    public static <T, R> List<R> mappedBy(Collection<T> targetCollection, Function<? super T, ? extends R> extractAction) {
        return targetCollection.stream()
                .map(extractAction)
                .collect(Collectors.toList());
    }
}
