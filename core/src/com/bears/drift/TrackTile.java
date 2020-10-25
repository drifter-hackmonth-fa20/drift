package com.bears.drift;

import com.badlogic.gdx.graphics.Texture;

public class TrackTile extends Tile{
    int entrance; // Side the car enters the track
    int exit; // Side the car exits

    public TrackTile(int entrance, int exit) {
        this.size = 280;
        this.entrance = entrance;
        this.exit = exit;
        this.visible = true;
        loadTexture();
    }

    private void loadTexture() {
        //TODO generate a texture based on the entrance and exit side. IE if the entrance is the bottom and the exit is the right, the texture is set to a 45 degree curve from the bottom to the right.
    }

    public int getID() {
        //TODO generate a unique id based on the entrance and exit side.
        return 0;
    }

    @Override
    void fun() {}

    @Override
    public int distanceToCurve1(int x, int y) {
        return 0; //TODO
    }

    @Override
    public int distanceToCurve2(int x, int y) {
        return 0; //TODO
    }
}
