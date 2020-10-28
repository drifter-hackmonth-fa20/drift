package com.bears.drift;

public class Track {
    public int startx;
    public int starty;
    public boolean firstTile;
    public int firstTileX;
    public int firstTileY;
    public int firstTileEntrance;
    public int firstTileExit;
    public int startAngle;
    public int length;
    private Tile[][] tiles;
    Drift game;

    private static int[] dirX = {0, -1, 0, 1};
    private static int[] dirY = {-1, 0, 1, 0};

    private boolean works;

    public Track(Drift game) {
        this.game = game;
        randomizeTiles();
        /* Hardcoded track generation for testing purposes
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
        }*/

        /* Random track generation */

//        if (firstTileEntrance == 0) {
//            if (firstTileExit == 1) {
//                startAngle = 180;
//            } else if (firstTileExit == 2) {
//                startAngle = 0;
//            } else if (firstTileExit == 3) {
//                startAngle = -180;
//            }
//        } else if (firstTileEntrance == 1) {
//            if (firstTileExit == 0) {
//                startAngle = -45;
//            } else if (firstTileExit == 2) {
//                startAngle = 45;
//            } else if (firstTileExit == 3) {
//                startAngle = -0;
//            }
//        } else if (firstTileEntrance == 2) {
//            if (firstTileExit == 0) {
//                startAngle = -90;
//            } else if (firstTileExit == 1) {
//                startAngle = -135;
//            } else if (firstTileExit == 3) {
//                startAngle = -45;
//            }
//        } else if (firstTileEntrance == 3) {
//            if (firstTileExit == 0) {
//                startAngle = -135;
//            } else if (firstTileExit == 1) {
//                startAngle = 180;
//            } else if (firstTileExit == 2) {
//                startAngle = 135;
//            }
//        }
    }

    public void randomizeTiles() {
        tiles = new Tile[6][6];
        firstTile = true;
        failed = true;
        int starti = 0;
        int startj = 0;
        while (failed) {
            works = false; failed = false; depth = 0; first = true; length = 0;
            starti = 1;
            startj = 1;
            dfs(starti, startj, 0, 0);
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (tiles[i][j] == null) {
                    tiles[i][j] = new BlankTile();
                } else if (firstTile) {
                    firstTileX = startj;
                    firstTileY = starti;
                    firstTileEntrance = ((TrackTile) tiles[starti][startj]).entrance;
                    firstTileExit = ((TrackTile) tiles[starti][startj]).exit;
                    firstTile = false;
                }
            }
        }


        startx = 130 + 1*280;
        starty = 130 + 1*280;
        startAngle = 0;
    }

    private int depth = 0;
    private boolean failed;
    private boolean first;

    private void dfs(int i, int j, int entry, int order) {
        depth++;
        if (depth > 75) {
            failed = true;
            return;
        }
        if (i < 0 || i > 5 || j < 0 || j > 5) return;
        if (tiles[i][j] != null) {
            if (((TrackTile) tiles[i][j]).entrance == entry) works = true;
            return;
        }
        int randomDir;
        if (first) {
            randomDir = 2;
            first = false;
        }
        else randomDir = (int)(Math.random()*4);
        for (int k = randomDir, counter = 0; counter < 4; k = (k+1)%4, counter++) {
            if (k == entry) continue;
            tiles[i][j] = new TrackTile(game, entry, k, order);
            dfs(i+dirY[k], j + dirX[k], (k + 2)%4, order + 1);
            if (works || failed) {
                if (order>length) length = order;
                break;
            }
        }
        if (!works) tiles[i][j] = null;
    }

    public void render() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                game.batch.draw(tiles[i][j].texture, j*280, i*280);
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
        return ((int)x)/280*6 + ((int)y)/280;
    }

    public int getTileOrder(float x, float y) {
        if (!tiles[(int)(y/280)][(int)(x/280)].isTrack) return -1;
        else return ((TrackTile)(tiles[(int)(y/280)][(int)(x/280)])).order;
    }
}
