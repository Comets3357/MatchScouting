package com.example.matchscouting.common;

public enum ScoringPosition {

    TOP_LEFT(0),
    MIDDLE_LEFT(1),
    BOTTOM_LEFT(2),
    BOTTOM_RIGHT(3),
    MIDDLE_RIGHT(4),
    TOP_RIGHT(5);
    final int startPos;

    ScoringPosition(int startPos) {
        this.startPos = startPos;
    }
    public int getArrayPos(boolean red, boolean scoringTable) {
        int toMove = 0;
        if (red) {
            toMove += 3;
        }
        if (!scoringTable) {
            toMove += 3;
        }
        return (this.startPos + toMove) % 6;
    }

}
