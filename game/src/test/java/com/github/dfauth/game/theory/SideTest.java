package com.github.dfauth.game.theory;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static com.github.dfauth.game.theory.Side.AWAY;
import static com.github.dfauth.game.theory.Side.HOME;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class SideTest {

    @Test
    public void testIt() {
        assertTrue(HOME.isHome());
        assertTrue(AWAY.isAway());
        assertFalse(HOME.isAway());
        assertFalse(AWAY.isHome());
        assertEquals(AWAY, HOME.flip());
        assertEquals(HOME, AWAY.flip());
    }

}
