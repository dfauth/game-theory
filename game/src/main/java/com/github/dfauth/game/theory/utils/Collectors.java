package com.github.dfauth.game.theory.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static com.github.dfauth.game.theory.utils.Function2.noOp;
import static java.util.function.Function.identity;
import static java.util.function.Predicate.not;

@Slf4j
public class Collectors {

    public static <T> Collector<CompletableFuture<T>, CompletableFuture<List<T>>,CompletableFuture<List<T>>> future() {
        return future(identity());
    }

    public static <T,R> Collector<CompletableFuture<T>, CompletableFuture<List<T>>,CompletableFuture<R>> future(Function<List<T>,R> finisher) {

        return new Collector<>() {

            private CompletableFuture<List<T>> futListOfT = new CompletableFuture<>();
            private CompletableFuture<Void> streamComplete = new CompletableFuture<>();
            private List<CompletableFuture<T>> listOfFutureT = new ArrayList<>();
            private List<T> listOfT = new ArrayList<>();

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
                        listOfFutureT.stream()
                                .filter(not(CompletableFuture::isDone))
                                .findFirst()
                                .ifPresentOrElse(
                                    noOp(),
                                    () -> {
                                        Optional.of(listOfT)
                                        .filter(ignored -> streamComplete.isDone())
                                        .filter(ignored -> listOfFutureT.size() == listOfT.size())
                                        .ifPresent(futListOfT::complete);
                                    }
                        );
                    });
                };
            }

            @Override
            public BinaryOperator<CompletableFuture<List<T>>> combiner() {
                return (f1, f2) -> {
                    throw new UnsupportedOperationException();
                };
            }

            @Override
            public Function<CompletableFuture<List<T>>, CompletableFuture<R>> finisher() {
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
