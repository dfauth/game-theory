package com.github.dfauth.game.theory;

import com.github.dfauth.game.theory.strategies.WrapperStrategy;
import com.github.dfauth.game.theory.utils.Collectors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class Tournament {

    private final int start;
    private final int end;
    private final int games;
    private final Strategy[] strategies;

    public Tournament(int start, int end, int games, Strategy... strategies) {
        this.start = start;
        this.end = end;
        this.games = games;
        this.strategies = strategies;
    }

    public CompletableFuture<Optional<Result>> run() {
        List<CompletableFuture<Result>> tmp = new ArrayList<>(strategies.length * strategies.length);
        for(Strategy left : strategies) {
            for(Strategy right : strategies) {
                left = left == right ? new WrapperStrategy(left, String.format("%s-itself",left.getName())) : left;
                tmp.add(new Game(start,end,left,right).play());
            }
        }
        return tmp.stream().collect(Collectors.future(l -> l.stream().reduce(Result::add)));
    }
}
