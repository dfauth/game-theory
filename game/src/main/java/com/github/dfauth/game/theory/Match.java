package com.github.dfauth.game.theory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static io.github.dfauth.trycatch.Utils.oops;

@Slf4j
@Getter
@RequiredArgsConstructor
public class Match {

    private final int trials;
    private Score score = new Score();

    public Side register(Strategy strategy) {
        return score.assign(strategy);
    }

    public CompletableFuture<Score> match(Strategy home, Strategy away) {
        home.initialise(this);
        away.initialise(this);
        Thingy thingy = new Thingy(score.getHome(), score.getAway());
        return IntStream.range(0, trials)
                .mapToObj(i -> thingy.play())
                .reduce(CompletableFuture.completedFuture(score),
                        (l, r) -> {
                    return l.thenCompose(l1 -> r.thenApply(l1::add));
                }, oops());
    }

}
