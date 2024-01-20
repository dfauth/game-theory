package com.github.dfauth.game.theory;

import com.github.dfauth.game.theory.strategies.AlwaysCooperate;
import com.github.dfauth.game.theory.strategies.AlwaysDefect;
import com.github.dfauth.game.theory.strategies.WeightedRandom;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.github.dfauth.game.theory.GameTest.waitOn;
import static java.util.Collections.reverse;
import static java.util.Collections.sort;

@Slf4j
public class TournamentTest {

    private int start = 150;
    private int end = 250;
    private int games = 1;

    @Test
    public void testIt() {

        Tournament tournament = new Tournament(start, end, games,
                new AlwaysCooperate(),
                new AlwaysDefect(),
                new WeightedRandom(0.10d)
        );
        CompletableFuture<Optional<Result>> result = tournament.run();
        result.thenAccept(o -> o.map(Result::getMap).ifPresent(m -> {
            List<Map.Entry<String, Score>> l = new ArrayList<>(m.entrySet());
            sort(l,(e1,e2) -> e1.getValue().compareTo(e2.getValue()));
            reverse(l);
            l.stream().forEach(e -> log.info("{} = {}",e.getKey(), e.getValue()));
        }));
        waitOn(result).map(Result::getWinner).ifPresentOrElse(w -> log.info("winner is "+w.get()), () -> log.info("no winner"));
    }

    @Test
    public void testItNoWinner() {

        Tournament tournament = new Tournament(start, end, games,
                new AlwaysCooperate()
        );
        CompletableFuture<Optional<Result>> result = tournament.run();
        result.thenAccept(o -> o.map(Result::getMap).ifPresent(m -> {
            List<Map.Entry<String, Score>> l = new ArrayList<>(m.entrySet());
            sort(l,(e1,e2) -> e1.getValue().compareTo(e2.getValue()));
            reverse(l);
            l.stream().forEach(e -> log.info("{} = {}",e.getKey(), e.getValue()));
        }));
        waitOn(result).map(Result::getWinner).ifPresentOrElse(w -> log.info("winner is "+w), () -> log.info("no winner"));
    }

}
