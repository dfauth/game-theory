package com.github.dfauth.game.theory.utils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public interface Lists<T> {

    static <T> Lists<T> lists(List<T> l) {
        return () -> l;
    }

    static <T> Optional<T> head(List<T> l) {
        return l.stream().findFirst();
    }

    static <T,R> List<R> map(List<T> l, Function<T,R> f) {
        return map(l,f,Collectors.toList());
    }

    static <T,R> List<R> map(List<T> l, Function<T,R> f, Collector<R,?,List<R>> collector) {
        return l.stream().map(f).collect(collector);
    }

    static <T,K,V> Map<K,V> toMap(List<T> l, Function<T,K> keyMapper,Function<T,V> valueMapper, Collector<Map.Entry<K,V>,?,Map<K,V>> collector) {
        return toMap(l,t -> Map.entry(keyMapper.apply(t),valueMapper.apply(t)),collector);
    }

    static <T,K,V> Map<K,V> toMap(List<T> l, Function<T,Map.Entry<K,V>> f, Collector<Map.Entry<K,V>,?,Map<K,V>> collector) {
        return l.stream().map(f).collect(collector);
    }

    default <R> Lists<R> map(Function<T,R> f) {
        return lists(map(toList(), f));
    }

    List<T> toList();

}
