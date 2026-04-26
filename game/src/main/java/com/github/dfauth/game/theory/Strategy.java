package com.github.dfauth.game.theory;

public interface Strategy {

    void initialise(Match match);

    void play(Round round);

    default String name() {
        return getClass().getSimpleName();
    }
}
