package io.github.dipo33.gtmapaddon.utils;

@FunctionalInterface
public interface ThrowingBiConsumer<T, U, E extends Exception> {
    void acceptThrows(T t, U u) throws E;
}