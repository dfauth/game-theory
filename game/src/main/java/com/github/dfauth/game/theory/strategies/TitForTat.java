package com.github.dfauth.game.theory.strategies;

import com.github.dfauth.game.theory.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TitForTat implements Strategy {

    private Side side;
    private Action nextAction =Action.COOPERATE;


    @Override
    public void initialise(Match match) {
        side = match.register(this);
    }

    @Override
    public void play(Round round) {
        round.submit(nextAction).thenAccept(r -> {
            Action theirAction = r.getAction(side.flip());
            nextAction = theirAction.isCooperate() ? Action.COOPERATE : Action.DEFECT;
        });
    }
}
