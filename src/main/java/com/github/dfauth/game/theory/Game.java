package com.github.dfauth.game.theory;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Slf4j
public class Game {

    private final CompletableFuture<Result>[] rounds;
    private final Strategy s1;
    private final Strategy s2;
    private CompletableFuture<Draw> f1 = new CompletableFuture<>();
    private CompletableFuture<Draw> f2 = new CompletableFuture<>();

    public Game(int start, int end, Strategy s1, Strategy s2) {
        this((int)(Math.random()*(end-start))+start,s1,s2);
    }

    public Game(int cnt, Strategy s1, Strategy s2) {
        this.s1 = s1;
        this.s2 = s2;

        this.rounds = new CompletableFuture[cnt];
    }

    private CompletableFuture<Result> playRound(Strategy s1, Strategy s2) {
        CompletableFuture<Result> f = f1.thenCompose(d1 -> f2.thenApply(d2 ->
                new Result(Map.of(s1.getName(), d1.play(d2), s2.getName(), d2.play(d1)))));
        s1.play(draw -> {
            f1.complete(draw);
            return f;
        });
        s2.play(draw -> {
            f2.complete(draw);
            return f;
        });
        return f;
    }

    public CompletableFuture<Result> play() {
        int i = 0;
        for(CompletableFuture<Result> r : rounds) {
            rounds[i++] = playRound(s1,s2);
        }
        return result();
    }

    private CompletableFuture<Result> result() {
        Result r = new Result(Map.of(s1.getName(), 0, s2.getName(), 0));
        return completedFuture(Arrays.stream(rounds).reduce(r,(_m, _r) -> {
            _r.thenAccept(_m::add);
            return _m;
        },(m1,m2) -> m1));
    }

    public int getRounds() {
        return rounds.length;
    }

    public Optional<Strategy> getWinner() {
//        CompletableFuture<Optional<Map.Entry<String, Integer>>> result = result().thenApply(m -> m.getMap().entrySet().stream().reduce((e1, e2) -> e1.getValue().intValue() > e2.getValue().intValue() ? e1.getKey() : e2.getKey()));
        return Optional.empty();
    }
}
