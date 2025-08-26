package com.github.dfauth.game.theory;

import static com.github.dfauth.game.theory.Draw.COOPERATE;
import static com.github.dfauth.game.theory.Draw.DEFECT;

public enum Result {
    WIN(DEFECT, COOPERATE, 5),
    DRAW_COOPERATE(COOPERATE, COOPERATE, 3),
    DRAW_DEFECT(DEFECT, DEFECT, 1),
    LOSE(COOPERATE, DEFECT, 0);

    private final int points;
    private Draw playerDraw;
    private Draw opponentDraw;

    Result(Draw playerDraw, Draw opponentDraw, int points) {
        this.playerDraw = playerDraw;
        this.opponentDraw = opponentDraw;
        this.points = points;
    }

    public int points() {
        return points;
    }

    public Draw playerDraw() {
        return playerDraw;
    }

    public Draw opponentDraw() {
        return opponentDraw;
    }

    public boolean isWin() {
        return this == WIN;
    }

    public boolean isDraw() {
        return this == DRAW_COOPERATE || this == DRAW_DEFECT;
    }

    public boolean isLose() {
        return this == LOSE;
    }

    public Score add(Result result) {
        return new Score(this).add(result);
    }
}
