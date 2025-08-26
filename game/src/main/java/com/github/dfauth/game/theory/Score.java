package com.github.dfauth.game.theory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.function.UnaryOperator;

import static com.github.dfauth.game.theory.Result.*;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Score {
    private final int wins;
    private final int draw_cooperate;
    private final int draw_defect;
    private final int losses;

    public Score(Result result) {
        this(
                result.isWin() ? 1: 0,
                result.isDraw() ? result.playerDraw().isCooperate() ? 1 : 0 : 0,
                result.isDraw() ? result.playerDraw().isDefect() ? 1 : 0 : 0,
                result.isLose() ? 1: 0
        );
    }

    public Score() {
        this(0,0,0,0);
    }

    public Score map(UnaryOperator<Score> f) {
        return f.apply(this);
    }

    public Score add(Result r) {
        return add(new Score(r));
    }

    public Score add(Score s) {
        return new Score(
                wins + s.wins,
                draw_cooperate + s.draw_cooperate,
                draw_defect + s.draw_defect,
                losses + s.losses
        );
    }

    public int points() {
        return wins*WIN.points() +
                draw_cooperate * DRAW_COOPERATE.points() +
                draw_defect * DRAW_DEFECT.points() +
                losses * LOSE.points();
    }

    public int compareTo(Score s) {
        return points() - s.points();
    }

    public int rounds() {
        return wins+draw_cooperate+draw_defect+losses;
    }
}
