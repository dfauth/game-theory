package com.github.dfauth.game.theory;

public interface Strategy {
    void play(Round round);

    default String getName() {
        return this.getClass().getSimpleName();
    }
}
