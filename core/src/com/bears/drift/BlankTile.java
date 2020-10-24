package com.bears.drift;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class BlankTile extends Tile{
    public BlankTile() {
        this.size = 128;
        Pixmap pixmap = new Pixmap(this.size, this.size, Pixmap.Format.RGBA8888);
        pixmap.setColor((float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random());
        pixmap.fillRectangle(0, 0, this.size, this.size);
        this.texture = new Texture(pixmap);
        this.visible = true;
    }
}
