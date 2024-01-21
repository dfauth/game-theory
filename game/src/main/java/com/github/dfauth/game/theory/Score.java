package com.github.dfauth.game.theory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

import static com.github.dfauth.game.theory.Draw.COOPERATE;
import static com.github.dfauth.game.theory.Draw.DEFECT;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Score {
    private final int wins;
    private final int ties;
    private final int losses;
    private final int points;

    public Score(int points) {
        this(points == 5 ? 1 : 0,
                points == 3 ? 1 : points == 1 ? 1 : 0,
                points == 0 ? 1 : 0,
                points);
    }

    public Score() {
        this(0,0,0,0);
    }

    public Score add(Score s) {
        return new Score(
                wins + s.wins,
                ties + s.ties,
                losses + s.losses,
                points + s.points
        );
    }

    public int compareTo(Score s) {
        return points - s.points;
    }

    public Optional<Draw> drew() {
        return Optional.of(rounds()).filter(r -> r==1).map(_ignored ->
                points == 5 ? DEFECT :
                        points == 3 ? COOPERATE :
                                points == 1 ? DEFECT : COOPERATE);
    }

    public int rounds() {
        return wins+ties+losses;
    }
}
