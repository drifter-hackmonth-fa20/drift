package com.bears.drift;

import com.badlogic.gdx.graphics.Texture;

public abstract class Tile {
    Boolean visible;
    Texture texture;
    int size;
    boolean isTrack;
    int order;

    abstract float distanceToCurve1(float x, float y);
    abstract float distanceToCurve2(float x, float y);
    abstract float distanceTraveled(float x, float y);
    abstract int getNum();
}