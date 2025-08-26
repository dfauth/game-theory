package com.github.dfauth.game.theory;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Slf4j
public class Match implements Function<String,Strategy> {

    private final CompletableFuture<MatchResult>[] rounds;
    private final Strategy s1;
    private final Strategy s2;
    private final Executor executor;

    public Match(int start, int end, Strategy s1, Strategy s2) {
        this(ForkJoinPool.commonPool(), (int)(Math.random()*(end-start))+start,s1,s2);
    }

    public Match(Executor executor, int start, int end, Strategy s1, Strategy s2) {
        this(executor, (int)(Math.random()*(end-start))+start,s1,s2);
    }

    public Match(int cnt, Strategy s1, Strategy s2) {
        this(ForkJoinPool.commonPool(),cnt,s1,s2);
    }

    public Match(Executor executor, int cnt, Strategy s1, Strategy s2) {
        this.executor = executor;
        this.s1 = s1;
        this.s2 = s2;

        this.rounds = new CompletableFuture[cnt];
    }

    private CompletableFuture<Map<String, Score>> playRound(Strategy s1, Strategy s2) {

        CompletableFuture<Draw> fDraw1 = new CompletableFuture<>();
        CompletableFuture<Draw> fDraw2 = new CompletableFuture<>();
        CompletableFuture<Result> f1 = fDraw1.thenCompose(d1 -> fDraw2.thenApply(d1::play));
        CompletableFuture<Result> f2 = fDraw2.thenCompose(d2 -> fDraw1.thenApply(d2::play));

        executor.execute(() -> s1.play(draw -> {
            fDraw1.complete(draw);
            return f2;
        }));
        executor.execute(() -> s2.play(draw -> {
            fDraw2.complete(draw);
            return f1;
        }));
        return f1.thenCompose(r1 -> f2.thenApply(r2 -> Map.of(s1.getName(), new Score(r1), s2.getName(), new Score(r2))));
    }

    public CompletableFuture<Map<String, Score>> play() {
        return play(r -> {});
    }

    public CompletableFuture<Map<String, Score>> play(Consumer<Map<String,Score>> consumer) {
        return IntStream.range(0,rounds.length)
                .boxed()
                .map(ignore -> playRound(s1,s2))
                .reduce((fAcc,fMap) -> fAcc.thenCompose(acc -> fMap.thenApply(map -> {
                            map.entrySet().forEach(e -> acc.merge(e.getKey(), e.getValue(), Score::add));
                            return acc;
                        })
                )).orElse(completedFuture(Collections.emptyMap()));
    }

    public int getRounds() {
        return rounds.length;
    }

    @Override
    public Strategy apply(String n) {
        return Optional.of(n).filter(s1.getName()::equals).map(_ignored -> s1).orElse(s2);
    }
}
