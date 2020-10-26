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

        /* Hardcoded track generation for testing purposes */
        tiles[0][0] = new TrackTile(2, 3);
        for (int i = 1; i < 5; i++) {
            tiles[0][i] = new TrackTile(1, 3);
        }
        tiles[0][5] = new TrackTile(1, 2);
        for (int i = 1; i < 5; i++) {
            tiles[i][0] = new TrackTile(0, 2);
            tiles[i][5] = new TrackTile(0, 2);
        }
        tiles[5][0] = new TrackTile(0, 3);
        for (int i = 1; i < 5; i++) {
            tiles[5][i] = new TrackTile(1, 3);
        }
        tiles[5][5] = new TrackTile(1, 0);
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                tiles[i][j] = new BlankTile();
            }
        }

        /* Random track generation */



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
