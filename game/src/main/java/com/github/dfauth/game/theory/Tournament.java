package com.github.dfauth.game.theory;

import com.github.dfauth.game.theory.strategies.WrapperStrategy;
import com.github.dfauth.game.theory.utils.CompletableFutures;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.completedFuture;

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

    public CompletableFuture<Map<String,Score>> run() {
        Map<String,CompletableFuture<Score>> tmp = new HashMap<>();
        for(Supplier<Strategy> leftSupplier : strategies) {
            for(Supplier<Strategy> rightSupplier : strategies) {
                Strategy left = leftSupplier.get();
                Strategy right = rightSupplier.get();
                left = left == right ? new WrapperStrategy(left, String.format("%s-itself",left.getName())) : left;
                new Match(start,end,left,right)
                        .play()
                        .thenAccept(m -> m.entrySet().stream()
                                .forEach(e -> tmp.merge(e.getKey(),
                                        completedFuture(e.getValue()),
                                        (v1,v2) -> v1.thenCompose(_v1 -> v2.thenApply(_v1::add)))));
            }
        }
        return tmp.entrySet().stream().map(e -> e.getValue().thenApply(s -> Map.entry(e.getKey(), s))).collect(CompletableFutures.future(l -> l.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
    }
}
