package com.github.dfauth.game.theory.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface ExceptionalRunnable extends Runnable {

    @Slf4j
    class Logger {};

    Runnable noOp = () -> {};

    static UnaryOperator<Exception> log() {
        return e -> {
            Logger.log.error(e.getMessage(),e);
            return e;
        };
    }

    static <T> Function<Exception,T> propagate() {
        return e -> {
            throw new RuntimeException(e);
        };
    }

    static <T> T tryCatch(Callable<T> callable) {
        return tryCatch(callable, log().andThen(propagate()), () -> {});
    }

    static <T> T tryCatch(Callable<T> callable, Function<Exception, T> exceptionHandler) {
        return tryCatch(callable, exceptionHandler, () -> {});
    }

    static <T> T tryCatch(Callable<T> callable, Function<Exception, T> exceptionHandler, Runnable finallyRunnable) {
        try {
            return callable.call();
        } catch (Exception e) {
            return exceptionHandler.apply(e);
        } finally {
            finallyRunnable.run();
        }
    }

    static void tryCatchRunnable(ExceptionalRunnable runnable) {
        tryCatch(toCallable(runnable), log().andThen(propagate()), noOp);
    }

    static Callable<Void> toCallable(ExceptionalRunnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }

}
