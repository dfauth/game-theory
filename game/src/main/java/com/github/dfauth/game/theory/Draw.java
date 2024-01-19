package com.github.dfauth.game.theory;

import java.util.function.Function;

public enum Draw {
    COOPERATE(d -> d.isCooperate() ? 3 : 0),
    DEFECT(d -> d.isDefect() ? 1 : 5);

    private boolean isDefect() {
        return !isCooperate();
    }

    private boolean isCooperate() {
        return this == COOPERATE;
    }

    private Function<Draw, Integer> f;

    Draw(Function<Draw, Integer> f) {
        this.f = f;
    }

    public int play(Draw draw) {
        return f.apply(draw);
    }
}
