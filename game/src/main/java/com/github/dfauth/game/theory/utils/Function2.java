package com.github.dfauth.game.theory.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

import static com.github.dfauth.game.theory.utils.ExceptionalRunnable.tryCatch;

@Slf4j
public class Function2 {

    public static class Consumer {

        public static <T> java.util.function.Consumer<T> noOp() {
            return ignored -> {};
        }

        public static <T> java.util.function.Consumer<T> ignore(Runnable runnable) {
            return t -> {
                runnable.run();
            };
        }
    }

    public static class Function {

        public static <T> java.util.function.Function<T,T> peek(java.util.function.Consumer<T> consumer) {
            return t -> {
                consumer.accept(t);
                return t;
            };
        }

        public static <T,R> java.util.function.Function<T,R> ignore(Callable<R> callable) {
            return t -> tryCatch(callable);
        }
    }

}
