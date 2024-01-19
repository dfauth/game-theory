package com.github.dfauth.game.theory.strategies;

import com.github.dfauth.game.theory.Strategy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class NamedStrategy implements Strategy {

    private final String name;

    public NamedStrategy() {
        this(null);
    }

    public NamedStrategy(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name != null ? name : Strategy.super.getName();
    }
}
