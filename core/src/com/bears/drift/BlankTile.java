package com.bears.drift;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class BlankTile extends Tile{
    public BlankTile(Drift game) {
        this.texture = game.getTexture("badlogic.jpg");
        this.visible = true;
        this.isTrack = false;
    }

    @Override
    float distanceToCurve1(float x, float y) {return 280;}

    @Override
    float distanceToCurve2(float x, float y) {return 280;}

    @Override
    float distanceTraveled(float x, float y) {
        return 0;
    }

    @Override
    int getNum() {
        return 0;
    }
}
