package com.github.dfauth.game.theory.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.function.Function.identity;

@Slf4j
public class Collectors {

    public static <T> Collector<CompletableFuture<T>, List<CompletableFuture<T>>,CompletableFuture<List<T>>> future() {
        return future(identity());
    }

    public static <T,R> Collector<CompletableFuture<T>, List<CompletableFuture<T>>,CompletableFuture<R>> future(Function<List<T>,R> f) {

        return new Collector<>() {

            @Override
            public Supplier<List<CompletableFuture<T>>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<List<CompletableFuture<T>>, CompletableFuture<T>> accumulator() {
                return List::add;
            }

            @Override
            public BinaryOperator<List<CompletableFuture<T>>> combiner() {
                return (f1, f2) -> {
                    throw new UnsupportedOperationException();
                };
            }

            @Override
            public Function<List<CompletableFuture<T>>, CompletableFuture<R>> finisher() {
                List<T> tmp = new ArrayList<>();
                CompletableFuture<R> fR = new CompletableFuture<>();
                return l -> {
                    l.stream().forEach(_f -> _f.thenAccept(t -> {
                        tmp.add(t);
                        if (tmp.size() >= l.size()) {
                            fR.complete(f.apply(tmp));
                        }
                        ;
                    }));
                    return fR;
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Set.of();
            }
        };
    }

}
