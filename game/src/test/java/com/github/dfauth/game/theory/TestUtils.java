package com.github.dfauth.game.theory;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestUtils {

    public static <T> T waitOn(CompletableFuture<T> f) {
        return waitOn(f, 1000, TimeUnit.MILLISECONDS);
    }

    public static <T> T waitOn(CompletableFuture<T> f, long l) {
        return waitOn(f, l, TimeUnit.MILLISECONDS);
    }

    public static <T> T waitOn(CompletableFuture<T> f, long l, TimeUnit u) {
        return tryCatch(() -> f.get(l,u));
    }

    public static <T> T tryCatch(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
