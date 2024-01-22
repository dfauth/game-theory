package com.github.dfauth.game.theory;

import com.github.dfauth.game.theory.utils.CompletableFutures;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.github.dfauth.game.theory.utils.Function2.Function.peek;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Slf4j
public class Game implements Function<String,Strategy> {

    private final CompletableFuture<Result>[] rounds;
    private final Strategy s1;
    private final Strategy s2;
    private final Executor executor;

    public Game(int start, int end, Strategy s1, Strategy s2) {
        this(ForkJoinPool.commonPool(), (int)(Math.random()*(end-start))+start,s1,s2);
    }

    public Game(Executor executor, int start, int end, Strategy s1, Strategy s2) {
        this(executor, (int)(Math.random()*(end-start))+start,s1,s2);
    }

    public Game(int cnt, Strategy s1, Strategy s2) {
        this(ForkJoinPool.commonPool(),cnt,s1,s2);
    }

    public Game(Executor executor, int cnt, Strategy s1, Strategy s2) {
        this.executor = executor;
        this.s1 = s1;
        this.s2 = s2;

        this.rounds = new CompletableFuture[cnt];
    }

    private CompletableFuture<Result> playRound(Strategy s1, Strategy s2) {

        CompletableFuture<Draw> f1 = new CompletableFuture<>();
        CompletableFuture<Draw> f2 = new CompletableFuture<>();

        executor.execute(() -> s1.play(draw -> {
            f1.complete(draw);
            return f2;
        }));
        executor.execute(() -> s2.play(draw -> {
            f2.complete(draw);
            return f1;
        }));
        return f1.thenCompose(d1 -> f2.thenApply(d2 ->
                new Result(Map.of(s1.getName(), d1.play(d2), s2.getName(), d2.play(d1)))));
    }

    public CompletableFuture<Result> play() {
        return play(r -> {});
    }

    public CompletableFuture<Result> play(Consumer<Result> consumer) {
        return IntStream.range(0,rounds.length)
                .boxed()
                .reduce(completedFuture(new Result()),
                        (prevResult,ignored) -> prevResult.thenCompose(r -> playRound(s1,s2).thenApply(_r -> _r.add(r))),
                        CompletableFutures.<Result,Result>compose(Result::add)
                ).thenApply(peek(consumer));
    }

    public int getRounds() {
        return rounds.length;
    }

    @Override
    public Strategy apply(String n) {
        return Optional.of(n).filter(s1.getName()::equals).map(_ignored -> s1).orElse(s2);
    }
}
