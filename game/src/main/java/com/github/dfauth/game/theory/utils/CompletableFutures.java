package com.github.dfauth.game.theory.utils;

import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;

public class CompletableFutures {

    public static <T,R> BinaryOperator<CompletableFuture<T>> compose(BinaryOperator<T> f2) {
        return (fut1,fut2) -> fut1.thenCompose(t1 -> fut2.thenApply(t2 -> f2.apply(t1,t2)));
    }
}
