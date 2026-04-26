package com.github.dfauth.game.theory.strategies;

import com.github.dfauth.game.theory.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlwaysCooperate extends BaseStrategy {

    @Override
    public void play(Round round) {
        round.submit(Action.COOPERATE);
    }
}
