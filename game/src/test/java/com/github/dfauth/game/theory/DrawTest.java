package com.github.dfauth.game.theory;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static com.github.dfauth.game.theory.Draw.COOPERATE;
import static com.github.dfauth.game.theory.Draw.DEFECT;
import static com.github.dfauth.game.theory.Result.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class DrawTest {

    @Test
    public void testIt() {
        assertEquals(DRAW_COOPERATE,COOPERATE.play(COOPERATE));
        assertEquals(LOSE,COOPERATE.play(DEFECT));
        assertEquals(WIN,DEFECT.play(COOPERATE));
        assertEquals(DRAW_DEFECT,DEFECT.play(DEFECT));
    }

}
