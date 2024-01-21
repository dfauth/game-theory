package com.github.dfauth.game.theory.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class Function2 {

    public static <T> Consumer<T> noOp() {
        return ignored -> {};
    }

    public static <T> Function<T,T> peek(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return t;
        };
    }

}
