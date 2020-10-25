package com.bears.drift;

import com.badlogic.gdx.graphics.Texture;

public abstract class Tile {
    Boolean visible;
    Texture texture;
    int size;

    abstract void fun();

    abstract int distanceToCurve1(int x, int y);
    abstract int distanceToCurve2(int x, int y);
}