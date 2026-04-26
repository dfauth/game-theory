package com.github.dfauth.game.theory;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Points {

    private final int home;
    private final int away;

    public Points(Action homeAction, Action awayAction) {
        this.home = homeAction.play(awayAction);
        this.away = awayAction.play(homeAction);
    }
}
