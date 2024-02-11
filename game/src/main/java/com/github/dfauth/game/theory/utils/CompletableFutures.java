package com.github.dfauth.game.theory.utils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.function.Function.identity;

public class CompletableFutures {

    public static <T> BinaryOperator<CompletableFuture<T>> compose(BinaryOperator<T> f2) {
        return (fut1,fut2) -> fut1.thenCompose(t1 -> fut2.thenApply(t2 -> f2.apply(t1,t2)));
    }

    public static <T> Collector<CompletableFuture<T>, CompletableFuture<List<T>>,CompletableFuture<List<T>>> future() {
        return future(identity());
    }

    public static <T,R> Collector<CompletableFuture<T>, CompletableFuture<List<T>>,CompletableFuture<R>> future(Function<List<T>,R> finisher) {

        return new Collector<>() {

            private CompletableFuture<List<T>> futListOfT = new CompletableFuture<>();
            private CompletableFuture<Void> streamComplete = new CompletableFuture<>();
            private List<CompletableFuture<T>> listOfFutureT = Collections.synchronizedList(new ArrayList<>());
            private List<T> listOfT = Collections.synchronizedList(new ArrayList<>());

            @Override
            public Supplier<CompletableFuture<List<T>>> supplier() {
                return () -> futListOfT;
            }

            @Override
            public BiConsumer<CompletableFuture<List<T>>, CompletableFuture<T>> accumulator() {
                return (fListOfT,fT) -> {
                    listOfFutureT.add(fT);
                    fT.thenAccept(t -> {
                        listOfT.add(t);
                        resolveFutures();
                    });
                };
            }

            private void resolveFutures() {
                Optional.of(listOfT)
                        .filter(ignored -> listOfFutureT.size() == listOfT.size())
                        .filter(ignored -> streamComplete.isDone())
                        .ifPresent(futListOfT::complete);
            }

            @Override
            public BinaryOperator<CompletableFuture<List<T>>> combiner() {
                return (f1, f2) -> {
                    throw new UnsupportedOperationException();
                };
            }

            @Override
            public Function<CompletableFuture<List<T>>, CompletableFuture<R>> finisher() {
                streamComplete.thenAccept(ignored -> resolveFutures());
                streamComplete.complete(null);
                return fList -> fList.thenApply(finisher);
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Set.of();
            }
        };
    }

}
