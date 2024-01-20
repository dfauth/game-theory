package com.github.dfauth.game.theory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

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
        int w = wins - s.wins;
        int t = ties - s.ties;
        int l = losses - s.losses;
        int p = points - s.points;
        return w > 0 ? 1 :
                w < 0 ? -1 :
                        t > 0 ? 1 :
                                t < 0 ? -1 :
                                        l > 0 ? -1 :
                                            l > 0 ? 1 :
                                                    p > 0 ? 1 : -1;

    }
}
