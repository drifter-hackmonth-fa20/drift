package com.bears.drift;

public class Track {
    public int startx;
    public int starty;
    public int startAngle;
    private Tile[][] tiles;
    Drift game;

    public Track(Drift game) {
        this.game = game;
        startx = 150;
        starty = 150;
        startAngle = -45;
        tiles = new Tile[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                tiles[i][j] = new BlankTile();
            }
        }
    }

    public void render() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                game.batch.draw(tiles[i][j].texture, i*280, j*280);
            }
        }
    }

    public Tile getTile(int tileNum) {
        return tiles[tileNum%6][tileNum/6];
    }

    public Tile getTile(float x, float y) {
        return getTile(getTileNum(x, y));
    }

    public int getTileNum(float x, float y) {
        return ((int)x)/280 + ((int)y)/280*6;
    }
}
