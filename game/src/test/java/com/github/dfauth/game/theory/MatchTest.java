package com.github.dfauth.game.theory;

import com.github.dfauth.game.theory.strategies.AlwaysCooperate;
import com.github.dfauth.game.theory.strategies.AlwaysDefect;
import com.github.dfauth.game.theory.strategies.Random;
import com.github.dfauth.game.theory.strategies.TitForTat;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

import static com.github.dfauth.game.theory.Side.AWAY;
import static com.github.dfauth.game.theory.Side.HOME;
import static io.github.dfauth.trycatch.TryCatch.tryCatch;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class MatchTest {

    @Test
    public void testAlways() throws InterruptedException, ExecutionException, TimeoutException {
        Strategy left = new AlwaysCooperate();
        Strategy right = new AlwaysCooperate();

        var match = new Match(1);
        match.match(left, right).get(10000, TimeUnit.MILLISECONDS);
        log.info("result: "+match.getScore());
        assertEquals(3, match.getScore().getPoints(HOME));
        assertEquals(3, match.getScore().getPoints(AWAY));
    }

    @Test
    public void testNever() throws InterruptedException, ExecutionException, TimeoutException {
        Strategy left = new AlwaysDefect();
        Strategy right = new AlwaysDefect();

        var match = new Match(1);
        match.match(left, right).get(10000, TimeUnit.MILLISECONDS);
        log.info("result: "+match.getScore());
        assertEquals(1, match.getScore().getPoints(HOME));
        assertEquals(1, match.getScore().getPoints(AWAY));
    }

    @Test
    public void testAlwaysAndTitForTat() throws InterruptedException, ExecutionException, TimeoutException {
        Strategy left = new AlwaysCooperate();
        Strategy right = new TitForTat();

        var match = new Match(2);
        match.match(left, right).get(10000, TimeUnit.MILLISECONDS);
        log.info("result: "+match.getScore());
        assertEquals(6, match.getScore().getPoints(HOME));
        assertEquals(6, match.getScore().getPoints(AWAY));
    }

    @Test
    public void testNeverAndTitForTat() throws InterruptedException, ExecutionException, TimeoutException {
        Strategy left = new AlwaysDefect();
        Strategy right = new TitForTat();

        var match = new Match(2);
        match.match(left, right).get(10000, TimeUnit.MILLISECONDS);
        log.info("result: "+match.getScore());
        assertEquals(6, match.getScore().getPoints(HOME));
        assertEquals(1, match.getScore().getPoints(AWAY));
    }


    @Test
    public void testOne() throws InterruptedException, ExecutionException, TimeoutException {
        Strategy left = new TitForTat();
        Strategy right = new Random();

        var match = new Match(2);
        match.match(left, right).get(10000, TimeUnit.MILLISECONDS);
        log.info("result: "+match.getScore());
    }

    @Test
    public void testIt() throws InterruptedException, ExecutionException, TimeoutException {
        Strategy left = new TitForTat();
        Strategy right = new Random();

        var match = new Match(200 + (int)(Math.random()-0.5)*40);
        match.match(left, right).get(10000, TimeUnit.MILLISECONDS);
        log.info("result: "+match.getScore());
    }

    @Test
    public void testTournament() throws InterruptedException, ExecutionException, TimeoutException {
        List<Strategy> strategies = List.of(new TitForTat(), new Random(), new AlwaysCooperate(), new AlwaysDefect());
        int[] scores = strategies.stream().mapToInt(_ -> 0).toArray();

        IntStream.range(0, strategies.size()).forEach(i -> {
            IntStream.range(i+1, strategies.size()).forEach(j -> {
                var match = new Match(200 + (int)(Math.random()-0.5)*40);
                tryCatch(() -> match.match(strategies.get(i), strategies.get(j)).get(10000, TimeUnit.MILLISECONDS));
                log.info("result: "+match.getScore());
                scores[i] += match.getScore().getPoints(HOME);
                scores[j] += match.getScore().getPoints(AWAY);
            });
        });

        int[] i = new int[] {0};
        strategies.stream().map(Strategy::name).forEach(s ->
                log.info("{}: {} ",s,scores[i[0]++])
        );
    }

}
