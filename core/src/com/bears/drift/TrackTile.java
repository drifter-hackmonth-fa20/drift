package com.bears.drift;

import com.badlogic.gdx.graphics.Texture;

public class TrackTile extends Tile{
    int entrance; // Side the car enters the track
    int exit; // Side the car exits
    String[] dir = {"S", "W", "N", "E"};
    int order;


    public TrackTile(int entrance, int exit, int order) {
        this.size = 280;
        this.entrance = entrance;
        this.exit = exit;
        this.visible = true;
        this.order = order;
        this.isTrack = true;
        loadTexture();
    }

    /** Generate a texture based on the entrance and exit side. IE if the entrance is the bottom and the exit is the right, the texture is set to a 45 degree curve from the bottom to the right. */
    private void loadTexture() {
        String s = getID();
        if (s.equals("SN") || s.equals("NS")) {
            this.texture = new Texture("vertical-tile.png");
        } else if (s.equals("EW") || s.equals("WE")) {
            this.texture = new Texture("horizontal-tile.png");
        } else if (s.equals("SW") || s.equals("WS")) {
            this.texture = new Texture("bottomleft-tile.png");
        } else if (s.equals("WN") || s.equals("NW")) {
            this.texture = new Texture("topleft-tile.png");
        } else if (s.equals("NE") || s.equals("EN")) {
            this.texture = new Texture("topright-tile.png");
        } else
            this.texture = new Texture("bottomright-tile.png");
    }

    /** Generate a unique id based on the entrance and exit side. */
    public String getID() {
        return dir[entrance]+dir[exit];
    }

    public int getNum() {
        return entrance*10+exit*3;
    }

    @Override
    void fun() {}

    @Override
    /** Return the distance from the car to the inner side of the track. */
    public float distanceToCurve1(float x, float y) {
        String s = getID();
        if (s.equals("SN") || s.equals("NS")) {
            return x - (float)(size/8);
        } else if (s.equals("EW") || s.equals("WE")) {
            return y - (float)(size/8);
        } else if (s.equals("SW") || s.equals("WS")) {
            return (float)(Math.sqrt(x*x+y*y) - (float)(size/8));
        } else if (s.equals("WN") || s.equals("NW")) {
            return (float)(Math.sqrt(x*x+(size-y)*(size-y)) - (float)(size/8));
        } else if (s.equals("NE") || s.equals("EN")) {
            return (float)(Math.sqrt((size-x)*(size-x)+(size-y)*(size-y)) - (float)(size/8));
        } else
            return (float)(Math.sqrt((size-x)*(size-x)+y*y) - (float)(size/8));
    }

    @Override
    /** Return the distance from the car to the outer side of the track. */
    public float distanceToCurve2(float x, float y) {
        String s = getID();
        if (s.equals("SN") || s.equals("NS")) {
            return (float)(7*size/8) - x;
        } else if (s.equals("EW") || s.equals("WE")) {
            return (float)(7*size/8) - y;
        } else if (s.equals("SW") || s.equals("WS")) {
            return (float)(7*size/8) - (float)(Math.sqrt(x*x+y*y));
        } else if (s.equals("WN") || s.equals("NW")) {
            return (float)(7*size/8) - (float)(Math.sqrt(x*x+(size-y)*(size-y)));
        } else if (s.equals("NE") || s.equals("EN")) {
            return (float)(7*size/8) - (float)(Math.sqrt((size-x)*(size-x)+(size-y)*(size-y)));
        } else
            return (float)(7*size/8) - (float)(Math.sqrt((size-x)*(size-x)+y*y));
    }
}
