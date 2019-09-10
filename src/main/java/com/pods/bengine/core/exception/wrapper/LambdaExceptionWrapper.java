package com.pods.bengine.core.exception.wrapper;

import java.util.function.Function;

public class LambdaExceptionWrapper {

    public static <T, R> Function<T, R> wrap(CheckedFunction<T, R> checkedFunction) {
        return t -> {
            try {
                return checkedFunction.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
