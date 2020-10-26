package com.bears.drift;

import com.badlogic.gdx.graphics.Texture;

public class TrackTile extends Tile{
    int entrance; // Side the car enters the track
    int exit; // Side the car exits
    String[] dir = {"S", "W", "N", "E"};

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

    /** Generate a unique id based on the entrance and exit side. */
    public String getID() {
        return dir[entrance]+dir[exit];
    }

    @Override
    void fun() {}

    @Override
    /** Return the distance from the car to the inner side of the track. */
    public float distanceToCurve1(float x, float y) {
        String s = getID();
        if (s.equals("SN") || s.equals("NS")) {
            return x - (float)(size/4);
        } else if (s.equals("EW") || s.equals("WE")) {
            return y - (float)(size/4);
        } else if (s.equals("SW") || s.equals("WS")) {
            return (float)(Math.sqrt(x*x+y*y) - (float)(size/4));
        } else if (s.equals("WN") || s.equals("NW")) {
            return (float)(Math.sqrt(x*x+(size-y)*(size-y)) - (float)(size/4));
        } else if (s.equals("NE") || s.equals("EN")) {
            return (float)(Math.sqrt((size-x)*(size-x)+(size-y)*(size-y)) - (float)(size/4));
        } else
            return (float)(Math.sqrt((size-x)*(size-x)+y*y) - (float)(size/4));
    }

    @Override
    /** Return the distance from the car to the outer side of the track. */
    public float distanceToCurve2(float x, float y) {
        String s = getID();
        if (s.equals("SN") || s.equals("NS")) {
            return (float)(3*size/4) - x;
        } else if (s.equals("EW") || s.equals("WE")) {
            return (float)(3*size/4) - y;
        } else if (s.equals("SW") || s.equals("WS")) {
            return (float)(3*size/4) - (float)(Math.sqrt(x*x+y*y));
        } else if (s.equals("WN") || s.equals("NW")) {
            return (float)(3*size/4) - (float)(Math.sqrt(x*x+(size-y)*(size-y)));
        } else if (s.equals("NE") || s.equals("EN")) {
            return (float)(3*size/4) - (float)(Math.sqrt((size-x)*(size-x)+(size-y)*(size-y)) - (float)(size/4));
        } else
            return (float)(3*size/4) - (float)(Math.sqrt((size-x)*(size-x)+y*y));
    }
}
