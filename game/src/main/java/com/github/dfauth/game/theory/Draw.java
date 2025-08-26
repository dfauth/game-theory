package com.github.dfauth.game.theory;

import java.util.function.Function;

import static com.github.dfauth.game.theory.Result.*;

public enum Draw {
    COOPERATE(d -> d.isCooperate() ? DRAW_COOPERATE : LOSE),
    DEFECT(d -> d.isDefect() ? DRAW_DEFECT : WIN);

    public boolean isDefect() {
        return !isCooperate();
    }

    public boolean isCooperate() {
        return this == COOPERATE;
    }

    private Function<Draw, Result> f;

    Draw(Function<Draw, Result> f) {
        this.f = f;
    }

    public Result play(Draw draw) {
        return f.apply(draw);
    }
}
