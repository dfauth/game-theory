package com.github.dfauth.game.theory;

import com.github.dfauth.game.theory.strategies.AlwaysCooperate;
import com.github.dfauth.game.theory.strategies.AlwaysDefect;
import com.github.dfauth.game.theory.strategies.WeightedRandom;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class GameTest {

    @Test
    public void testIt() {
        Strategy strategy1 = new AlwaysCooperate("alwaysCooperate1");
        Strategy strategy2 = new AlwaysCooperate("alwaysCooperate2");

        Game game = new Game(1, strategy1, strategy2);
        int rounds = game.getRounds();
        CompletableFuture<Result> result = game.play();
        assertEquals(new Result(Map.of(strategy1.getName(),new Score(3*rounds), strategy2.getName(),new Score(3*rounds))), TestUtils.waitOn(result));
        assertTrue(TestUtils.waitOn(result).getWinner().map(game).isEmpty());
    }

    @Test
    public void testRandomGame() {
        Strategy strategy1 = new AlwaysCooperate("alwaysCooperate1");
        Strategy strategy2 = new AlwaysCooperate("alwaysCooperate2");

        Game game = new Game(150,250, strategy1, strategy2);
        int rounds = game.getRounds();
        assertTrue(rounds>=150);
        assertTrue(rounds<=250);
        CompletableFuture<Result> result = game.play();
        assertEquals(new Result(Map.of(strategy1.getName(),new Score(0,rounds, 0,3*rounds), strategy2.getName(),new Score(0,rounds,0,3*rounds))), TestUtils.waitOn(result));
        assertTrue(TestUtils.waitOn(result).getWinner().map(game).isEmpty());
    }

    @Test
    public void testRandomVsAlwaysDefectGame() {
        Strategy strategy1 = new AlwaysDefect();
        Strategy strategy2 = new WeightedRandom(0.10d);

        Game game = new Game(100, strategy1, strategy2);
        CompletableFuture<Result> result = game.play();
        result.thenAccept(r -> log.info("result: "+r));
        assertTrue(TestUtils.waitOn(result,1000).getWinner().map(game).isPresent());
        assertEquals(strategy1, TestUtils.waitOn(result).getWinner().map(game).get());
    }

}
