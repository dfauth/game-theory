package com.github.dfauth.game.theory.strategies;

import com.github.dfauth.game.theory.Round;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

import static com.github.dfauth.game.theory.Draw.COOPERATE;
import static com.github.dfauth.game.theory.Draw.DEFECT;

@Slf4j
public class WeightedRandom extends NamedStrategy {

    private final double weighting;
    private final SecureRandom random = new SecureRandom();

    public WeightedRandom() {
        this( 0.50);
    }

    public WeightedRandom(double weighting) {
        this(null, weighting);
    }

    public WeightedRandom(String name, double weighting) {
        super(name);
        this.weighting = weighting;
    }

    @Override
    public void play(Round round) {
        round.submit(random.nextDouble() <= weighting ?
                DEFECT :
                COOPERATE);
    }
}
