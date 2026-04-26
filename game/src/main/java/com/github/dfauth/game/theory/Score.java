package com.github.dfauth.game.theory;

import lombok.*;

import java.util.Optional;

import static com.github.dfauth.game.theory.Side.AWAY;
import static com.github.dfauth.game.theory.Side.HOME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Score {

    private Strategy home;
    private Strategy away;
    private int homePoints;
    private int awayPoints;

    public Side assign(Strategy strategy) {
        return Optional.ofNullable(home).map(h -> {
            if(away == null) {
                away = strategy;
                return AWAY;
            } else {
                throw new IllegalStateException("Only 2 strategies per match");
            }
        }).orElseGet(() -> {
            home = strategy;
            return HOME;
        });
    }

    public Score add(Result r) {
        homePoints += r.getHomePoints();
        awayPoints += r.getAwayPoints();
        return this;
    }

    @Override
    public String toString() {
        return String.format("score: %s: %d %s: %d", home.name(), homePoints, away.name(), awayPoints);
    }

    public int getPoints(Side side) {
        return side.isHome() ? homePoints : awayPoints;
    }
}
