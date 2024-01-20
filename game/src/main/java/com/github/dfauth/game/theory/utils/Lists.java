package com.github.dfauth.game.theory.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

public interface Lists {

    static <T> Optional<T> head(List<T> l) {
        return l.stream().findFirst();
    }

}
