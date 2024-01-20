package com.github.dfauth.game.theory.strategies;

import com.github.dfauth.game.theory.Round;
import com.github.dfauth.game.theory.Strategy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WrapperStrategy extends NamedStrategy {

    private final Strategy nested;

    public WrapperStrategy(Strategy strategy, String name) {
        super(name);
        this.nested = strategy;
    }

    @Override
    public void play(Round round) {
        this.nested.play(round);
    }
}
