package com.github.dfauth.game.theory;

import java.util.function.Function;

public enum Draw {
    COOPERATE(d -> d.isCooperate() ? new Score(3) : new Score(0)),
    DEFECT(d -> d.isDefect() ? new Score(1) : new Score(5));

    private boolean isDefect() {
        return !isCooperate();
    }

    private boolean isCooperate() {
        return this == COOPERATE;
    }

    private Function<Draw, Score> f;

    Draw(Function<Draw, Score> f) {
        this.f = f;
    }

    public Score play(Draw draw) {
        return f.apply(draw);
    }
}
