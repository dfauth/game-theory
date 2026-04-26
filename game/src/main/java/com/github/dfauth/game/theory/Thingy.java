package com.github.dfauth.game.theory;

import java.util.concurrent.CompletableFuture;

public record Thingy(Strategy home, Strategy away) {

    public CompletableFuture<Result> play() {
        CompletableFuture<Action> homeFut = new CompletableFuture<>();
        CompletableFuture<Action> awayFut = new CompletableFuture<>();
        CompletableFuture<Result> resultFut = homeFut
                .thenCompose(h -> awayFut
                        .thenApply(a -> new Result(home, h, away, a)));
        home.play(draw -> {
            homeFut.complete(draw);
            return resultFut;
        });
        away.play(draw -> {
            awayFut.complete(draw);
            return resultFut;
        });
        return resultFut;
    }
}
