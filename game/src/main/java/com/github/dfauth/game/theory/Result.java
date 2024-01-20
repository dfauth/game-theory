package com.github.dfauth.game.theory;

import com.github.dfauth.game.theory.utils.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Slf4j
@Data
@ToString
@EqualsAndHashCode
public class Result {

    private final Map<String, Score> map = new HashMap<>();

    public Result(Map<String, Score> map) {
        this.map.putAll(map);
    }

    public Result add(Result r) {
        r.forEach((k,v) -> this.map.computeIfPresent(k, (_k, _v) -> _v.add(v)));
        return this;
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
            return winners.size() == 1 ? Lists.head(winners) : Optional.empty();
        });
    }
}
