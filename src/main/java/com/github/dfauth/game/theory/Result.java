package com.github.dfauth.game.theory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Slf4j
@Data
@ToString
@EqualsAndHashCode
public class Result {

    private final Map<String, Integer> map = new HashMap<>();

    public Result(Map<String, Integer> map) {
        this.map.putAll(map);
    }

    public Result add(Result r) {
        r.forEach((k,v) -> this.map.computeIfPresent(k, (_k, _v) -> _v + v));
        return this;
    }

    private void forEach(BiConsumer<String, Integer> c2) {
        map.entrySet().stream().forEach(e -> c2.accept(e.getKey(),e.getValue()));
    }
}
