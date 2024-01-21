package com.github.dfauth.game.theory;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.github.dfauth.game.theory.utils.ExceptionalRunnable.tryCatch;

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

}
