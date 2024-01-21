package com.github.dfauth.game.theory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.dfauth.game.theory.utils.Lists.head;

@Slf4j
@Data
@ToString
@EqualsAndHashCode
public class Result {

    public static Function<List<Result>, Result> reduce = l -> l.stream().reduce(Result::add).orElseThrow(() -> new IllegalArgumentException("No results provided"));

    private final Map<String, Score> map = new HashMap<>();

    public Result() {
    }

    public Result(Map<String, Score> map) {
        this.map.putAll(map);
    }

    public Result add(Result r) {
        r.forEach((k,v) -> {
            this.map.computeIfPresent(k, (_k, _v) -> _v.add(v));
            this.map.computeIfAbsent(k, _k -> v);
        });
        return this;
    }

    public Optional<String> getOpponent(String name) {
        List<Map.Entry<String, Score>> result = map.entrySet().stream().filter(e -> !e.getKey().equals(name)).collect(Collectors.toList());
        return head(result).filter(n -> result.size() == 1).map(Map.Entry::getKey);
    }

    public Optional<Score> getScore(String name) {
        return Optional.of(map.get(name)).map(Score.class::cast);
    }

    private void forEach(BiConsumer<String, Score> c2) {
        map.entrySet().stream().forEach(e -> c2.accept(e.getKey(),e.getValue()));
    }

    public Optional<String> getWinner() {
        // find the max
        return map.values().stream().max(Score::compareTo).flatMap(max -> {
            // now find all entries equals to the max
            List<String> winners = map.entrySet().stream().filter(e -> e.getValue().equals(max)).map(Map.Entry::getKey).collect(Collectors.toList());
            // there can only be one winner
            return winners.size() == 1 ? head(winners) : Optional.empty();
        });
    }
}
