package com.github.dfauth.game.theory;

import java.util.concurrent.CompletableFuture;

public interface Round {
    CompletableFuture<Result> submit(Action action);
}
