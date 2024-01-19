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
        assertEquals(3,COOPERATE.play(COOPERATE));
        assertEquals(0,COOPERATE.play(DEFECT));
        assertEquals(5,DEFECT.play(COOPERATE));
        assertEquals(1,DEFECT.play(DEFECT));
    }

}
