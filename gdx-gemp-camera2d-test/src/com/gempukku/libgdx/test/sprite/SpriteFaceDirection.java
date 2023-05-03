package com.gempukku.libgdx.test.sprite;

public enum SpriteFaceDirection {
    Left(-1, 0), Right(1, 0);

    private int x;
    private int y;

    SpriteFaceDirection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
