package com.pods.bengine.core.exception.wrapper;

@FunctionalInterface
public interface CheckedFunction<T, R> {

    R apply(T t) throws Exception;
}
