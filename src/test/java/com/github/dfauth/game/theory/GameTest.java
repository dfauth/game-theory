package com.github.dfauth.game.theory;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.github.dfauth.game.theory.Draw.COOPERATE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class GameTest {

    @Test
    public void testIt() throws ExecutionException, InterruptedException, TimeoutException {
        Strategy strategy1 = new Strategy() {

            @Override
            public String getName() {
                return "Strategy1";
            }

            @Override
            public void play(Round round) {
                CompletableFuture<Result> f = round.submit(COOPERATE);
            }
        };
        Strategy strategy2 = new Strategy() {
            @Override
            public String getName() {
                return "Strategy2";
            }

            @Override
            public void play(Round round) {
                CompletableFuture<Result> f = round.submit(COOPERATE);
            }
        };

        Game game = new Game(1, strategy1, strategy2);
        CompletableFuture<Result> result = game.play();
        assertEquals(new Result(Map.of(strategy1.getName(),3, strategy2.getName(),3)), result.get(1000, TimeUnit.MILLISECONDS));
    }

}
