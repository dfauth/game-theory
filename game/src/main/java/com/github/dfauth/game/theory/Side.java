package com.github.dfauth.game.theory;

public enum Side {
    HOME, AWAY;

    public Side flip() {
        return isHome() ? AWAY: HOME;
    }

    public boolean isHome() {
        return this == HOME;
    }

    public boolean isAway() {
        return !isHome();
    }
}
