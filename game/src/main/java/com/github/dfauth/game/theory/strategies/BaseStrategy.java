package com.github.dfauth.game.theory.strategies;

import com.github.dfauth.game.theory.Match;
import com.github.dfauth.game.theory.Side;
import com.github.dfauth.game.theory.Strategy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseStrategy implements Strategy {

    private Side side;

    @Override
    public void initialise(Match match) {
        this.side = match.register(this);
    }

}
