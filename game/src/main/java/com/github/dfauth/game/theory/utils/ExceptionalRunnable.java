package com.github.dfauth.game.theory.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.function.Function;

@Slf4j
public class ExceptionalRunnable {

    public static <T> Function<Exception,T> propagate() {
        return e -> {
            log.error(e.getMessage(),e);
            throw new RuntimeException(e);
        };
    }

    public static <T> T tryCatch(Callable<T> callable) {
        return tryCatch(callable, propagate(), () -> {});
    }

    public static <T> T tryCatch(Callable<T> callable, Function<Exception,T> exceptionHandler) {
        return tryCatch(callable, exceptionHandler, () -> {});
    }

    public static <T> T tryCatch(Callable<T> callable, Function<Exception,T> exceptionHandler, Runnable finallyRunnable) {
        try {
            return callable.call();
        } catch (Exception e) {
            return exceptionHandler.apply(e);
        } finally {
            finallyRunnable.run();
        }
    }
}
