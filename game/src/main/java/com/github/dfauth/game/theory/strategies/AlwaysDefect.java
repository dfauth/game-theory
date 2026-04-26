package com.github.dfauth.game.theory.strategies;

import com.github.dfauth.game.theory.Action;
import com.github.dfauth.game.theory.Round;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlwaysDefect extends BaseStrategy {

    @Override
    public void play(Round round) {
        round.submit(Action.DEFECT);
    }
}
