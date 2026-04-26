package com.github.dfauth.game.theory.strategies;

import com.github.dfauth.game.theory.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;

import static io.github.dfauth.trycatch.TryCatch.tryCatch;

@Slf4j
public class Random implements Strategy {

    private Side side;

    @Override
    public void initialise(Match match) {
        this.side = match.register(this);
    }

    @Override
    public void play(Round round) {
        ForkJoinPool.commonPool().submit(() -> {
            tryCatch(() -> Thread.sleep((long) (Math.random()* 10.0)));
            round.submit(Math.random() > 0.5 ? Action.COOPERATE : Action.DEFECT)
//                    .thenAccept(r -> log.info("{}: action {}  score {}, {} action {} score {}",
//                            name(),
//                            r.getAction(side),
//                            r.getPoints(this.side),
//                            r.getStrategy(this.side.flip()).name(),
//                            r.getAction(this.side.flip()),
//                            r.getPoints(this.side.flip())))
            ;
        });
    }
}
