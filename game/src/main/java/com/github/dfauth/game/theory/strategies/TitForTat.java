package com.github.dfauth.game.theory.strategies;

import com.github.dfauth.game.theory.Draw;
import com.github.dfauth.game.theory.Result;
import com.github.dfauth.game.theory.Round;
import com.github.dfauth.game.theory.Score;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.github.dfauth.game.theory.Draw.COOPERATE;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Slf4j
public class TitForTat extends NamedStrategy {

    private CompletableFuture<Draw> drew = completedFuture(COOPERATE);

    @Override
    public void play(Round round) {
        CompletableFuture<Result> result = drew.thenCompose(round::submit); // submit what the opponent drew last time
        Function<Result,Optional<Draw>> xy = r -> r.getOpponent(getName()).flatMap(r::getScore).flatMap(Score::drew); // determine what the opponent drew from the score
        this.drew = result.thenApply(xy).thenApply(Optional::orElseThrow);
    }
}
