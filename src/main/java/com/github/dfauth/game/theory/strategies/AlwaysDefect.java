package com.github.dfauth.game.theory.strategies;

import com.github.dfauth.game.theory.Draw;
import com.github.dfauth.game.theory.Round;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlwaysDefect extends NamedStrategy {

    public AlwaysDefect() {
    }

    public AlwaysDefect(String name) {
        super(name);
    }

    @Override
    public void play(Round round) {
        round.submit(Draw.DEFECT);
    }
}
