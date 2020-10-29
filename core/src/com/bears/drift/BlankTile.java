package com.bears.drift;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class BlankTile extends Tile{
    public BlankTile(Drift game) {
        this.texture = game.getTexture("badlogic.jpg");
        this.visible = true;
        this.isTrack = false;
    }

    public void fun() {
        Pixmap pixmap = new Pixmap(this.size, this.size, Pixmap.Format.RGBA8888);
        pixmap.setColor((float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random());
        pixmap.fillRectangle(0, 0, this.size, this.size);
        this.texture = new Texture(pixmap);
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
