package com.github.dfauth.game.theory.strategies;

import com.github.dfauth.game.theory.Draw;
import com.github.dfauth.game.theory.Round;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

import static com.github.dfauth.game.theory.Draw.COOPERATE;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Slf4j
public class TitForTat extends NamedStrategy {

    private CompletableFuture<Draw> drew = completedFuture(COOPERATE);

    @Override
    public void play(Round round) {
        this.drew = drew.thenCompose(round::submit); // submit what the opponent drew last time
    }
}
