package com.github.dfauth.game.theory;

import com.github.dfauth.game.theory.strategies.AlwaysCooperate;
import com.github.dfauth.game.theory.strategies.AlwaysDefect;
import com.github.dfauth.game.theory.strategies.TitForTat;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.github.dfauth.game.theory.TestUtils.waitOn;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class TitForTatTest {

    @Test
    public void testIt() {
        Strategy strategy1 = new AlwaysCooperate("alwaysCooperate1");
        Strategy strategy2 = new TitForTat();

        Game game = new Game(1, strategy1, strategy2);
        int rounds = game.getRounds();
        CompletableFuture<Result> result = game.play();
        assertEquals(new Result(Map.of(strategy1.getName(),new Score(3*rounds), strategy2.getName(),new Score(3*rounds))), waitOn(result, 10000));
        assertTrue(waitOn(result, 10000).getWinner().map(game).isEmpty());
    }

    @Test
    public void testAgainstAlwaysDefect() {
/**
        {
            Strategy strategy1 = new AlwaysDefect();
            Strategy strategy2 = new TitForTat();
            Game game = new Game(1, strategy1, strategy2);
            int rounds = game.getRounds();
            CompletableFuture<Result> result = game.play();
            assertEquals(new Result(Map.of(strategy1.getName(),new Score(5*rounds), strategy2.getName(),new Score(0*rounds))), waitOn(result));
            assertTrue(waitOn(result).getWinner().map(game).isPresent());
            assertEquals(strategy1.getName(), waitOn(result).getWinner().get());
            result.thenApply(Objects::toString).thenAccept(log::info);
        }
**/
        {
            Strategy strategy1 = new AlwaysDefect();
            Strategy strategy2 = new TitForTat();
            Game game = new Game(2, strategy1, strategy2);
            int rounds = game.getRounds();
            CompletableFuture<Result> result = game.play(r -> log.info(r.toString()));
            assertEquals(new Result(Map.of(strategy1.getName(),new Score(1,1,0,6), strategy2.getName(),new Score(0,1,1,1))), waitOn(result, 10000));
            assertTrue(waitOn(result,10000).getWinner().map(game).isPresent());
            assertEquals(strategy1.getName(), waitOn(result).getWinner().get());
            result.thenApply(Objects::toString).thenAccept(log::info);
        }
    }
}
