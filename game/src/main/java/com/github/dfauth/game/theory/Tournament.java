package com.github.dfauth.game.theory;

import com.github.dfauth.game.theory.strategies.WrapperStrategy;
import com.github.dfauth.game.theory.utils.Collectors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
public class Tournament {

    private final int start;
    private final int end;
    private final int games;
    private final Supplier<Strategy>[] strategies;

    public Tournament(int start, int end, int games, Supplier<Strategy>... strategies) {
        this.start = start;
        this.end = end;
        this.games = games;
        this.strategies = strategies;
    }

    public CompletableFuture<Result> run() {
        List<CompletableFuture<Result>> tmp = new ArrayList<>(strategies.length * strategies.length);
        for(Supplier<Strategy> leftSupplier : strategies) {
            for(Supplier<Strategy> rightSupplier : strategies) {
                Strategy left = leftSupplier.get();
                Strategy right = rightSupplier.get();
                left = left == right ? new WrapperStrategy(left, String.format("%s-itself",left.getName())) : left;
                tmp.add(new Game(start,end,left,right).play());
            }
        }
        return tmp.stream().collect(Collectors.future(Result.reduce));
    }
}
