package com.bears.drift;

import com.badlogic.gdx.graphics.Texture;

public class TrackTile extends Tile{
    int entrance; // Side the car enters the track
    int exit; // Side the car exits
    String[] dir = {"S", "W", "N", "E"};
    Drift game;

    public TrackTile(Drift game, int entrance, int exit, int order) {
        this.game = game;
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
            this.texture = game.getTexture("vertical-tile.png");
        } else if (s.equals("EW") || s.equals("WE")) {
            this.texture = game.getTexture("horizontal-tile.png");
        } else if (s.equals("SW") || s.equals("WS")) {
            this.texture = game.getTexture("bottomleft-tile.png");
        } else if (s.equals("WN") || s.equals("NW")) {
            this.texture = game.getTexture("topleft-tile.png");
        } else if (s.equals("NE") || s.equals("EN")) {
            this.texture = game.getTexture("topright-tile.png");
        } else
            this.texture = game.getTexture("bottomright-tile.png");
    }

    /** Generate a unique id based on the entrance and exit side. */
    public String getID() {
        return dir[entrance]+dir[exit];
    }

    public int getNum() {
        String s = getID();
        if (s.equals("SN") || s.equals("NS") || s.equals("EW") || s.equals("WE")) {
            return 0;
        } else if (s.equals("SW") || s.equals("WN") || s.equals("NE") || s.equals("ES")) {
            return -1;
        } else if (s.equals("SE") || s.equals("EN") || s.equals("NW") || s.equals("WS"))
            return 1;
        return 0;
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

    public float distanceTraveled(float x, float y) {
        String s = getID();
        int centerX;
        int centerY;
        float radians;
        if (s.equals("SN")) {
            return y;
        } else if (s.equals("NS")) {
            return 280-y;
        } else if (s.equals("WE")) {
            return x;
        } else if (s.equals("EW")) {
            return 280-x;
        } else if (s.equals("SW")) {
            centerX = 0;
            centerY = 0;
            return (float) Math.atan((y-centerY)/(x-centerX))*170;
        } else if (s.equals("WS")) {
            centerX = 0;
            centerY = 0;
            return (float) Math.atan((x-centerX)/(y-centerY))*170;
        } else if (s.equals("SE")) {
            centerX = 280;
            centerY = 0;
            return (float) Math.atan((y-centerY)/(centerX-x))*170;
        } else if (s.equals("ES")) {
            centerX = 280;
            centerY = 0;
            return (float) Math.atan((centerX-x)/(y-centerY))*170;
        } else if (s.equals("NE")) {
            centerX = 280;
            centerY = 280;
            return (float) Math.atan((centerY-y)/(centerX-x))*170;
        } else if (s.equals("EN")) {
            centerX = 280;
            centerY = 280;
            return (float) Math.atan((centerX-x)/(centerY-y))*170;
        } else if (s.equals("NW")) {
            centerX = 0;
            centerY = 280;
            return (float) Math.atan((centerY-y)/(x-centerX))*170;
        } else if (s.equals("WN")) {
            centerX = 0;
            centerY = 280;
            return (float) Math.atan((x-centerX)/(centerY-y))*170;
        }
        return 0;
    }
}
