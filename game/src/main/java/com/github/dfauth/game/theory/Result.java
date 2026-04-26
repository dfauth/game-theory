package com.github.dfauth.game.theory;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static com.github.dfauth.game.theory.Side.AWAY;
import static com.github.dfauth.game.theory.Side.HOME;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Result {

    private final Strategy home;
    private final Action homeAction;
    private final Strategy away;
    private final Action awayAction;

    public int getHomePoints() {
        return getPoints(HOME);
    }

    public int getAwayPoints() {
        return getPoints(AWAY);
    }

    public int getPoints(Side side) {
        return side.isHome() ? homeAction.play(awayAction) : awayAction.play(homeAction);
    }

    public Action getAction(Side side) {
        return side.isHome() ? homeAction : awayAction;
    }

    public Strategy getStrategy(Side side) {
        return side.isHome() ? home : away;
    }
}
