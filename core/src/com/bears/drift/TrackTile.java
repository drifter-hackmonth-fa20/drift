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
            float radius = (float)(Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)));
            radians = (x - centerX) / radius;
            return (float) (radians / (Math.PI/2)) * 280;
        } else if (s.equals("WS")) {
            centerX = 0;
            centerY = 0;
            float radius = (float)(Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)));
            radians = (x - centerX) / radius;
            return (float) (((Math.PI/2)-radians) / (Math.PI/2)) * 280;
        } else if (s.equals("SE")) {
            centerX = 280;
            centerY = 0;
            float radius = (float)(Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)));
            radians = (y - centerY) / radius;
            return (float) ((Math.PI/2 - (radians-(Math.PI/2))) / (Math.PI/2)) * 280;
        } else if (s.equals("ES")) {
            centerX = 280;
            centerY = 0;
            float radius = (float)(Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)));
            radians = (y - centerY) / radius;
            return (float) ((radians-(Math.PI/2)) / (Math.PI/2)) * 280;
        } else if (s.equals("NE")) {
            centerX = 280;
            centerY = 280;
            float radius = (float)(Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)));
            radians  = (x - centerX) / radius;
            return (float) ((radians + Math.PI) / (Math.PI/2)) * 280;
        } else if (s.equals("EN")) {
            centerX = 280;
            centerY = 280;
            float radius = (float)(Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)));
            radians  = (x - centerX) / radius;
            return (float) (((Math.PI/2)-(radians + Math.PI)) / (Math.PI/2)) * 280;
        } else if (s.equals("NW")) {
            centerX = 0;
            centerY = 280;
            float radius = (float)(Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)));
            radians  = (x - centerX) / radius;
            return (float) ((Math.PI/2) - (radians - ((3*Math.PI)/2)) / (Math.PI/2)) * 280;
        } else if (s.equals("WN")) {
            centerX = 0;
            centerY = 280;
            float radius = (float)(Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)));
            radians  = (x - centerX) / radius;
            return (float) (((radians - ((3*Math.PI)/2))) / (Math.PI/2)) * 280;
        }
    }
}
