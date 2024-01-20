package com.github.dfauth.game.theory;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static com.github.dfauth.game.theory.Draw.COOPERATE;
import static com.github.dfauth.game.theory.Draw.DEFECT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class DrawTest {

    @Test
    public void testIt() {
        assertEquals(new Score(3),COOPERATE.play(COOPERATE));
        assertEquals(new Score(0),COOPERATE.play(DEFECT));
        assertEquals(new Score(5),DEFECT.play(COOPERATE));
        assertEquals(new Score(1),DEFECT.play(DEFECT));
    }

}
