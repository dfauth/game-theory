package com.github.dfauth.game.theory;

import java.util.function.Function;

public enum Action {
    COOPERATE(d -> d.isCooperate() ? 3 : 0),
    DEFECT(d -> d.isDefect() ? 1 : 5);

    public boolean isDefect() {
        return !isCooperate();
    }

    public boolean isCooperate() {
        return this == COOPERATE;
    }

    public Action flip() {
        return isCooperate() ? DEFECT : COOPERATE;
    }

    private Function<Action, Integer> f;

    Action(Function<Action, Integer> f) {
        this.f = f;
    }

    public int play(Action draw) {
        return f.apply(draw);
    }
}
