package com.bears.drift;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Arrays;

public class Car extends Sprite {
    final GameScreen screen;
    private float velocityX;
    private float velocityY;
    private float angularVelocity;
    private boolean controllable;
    public NeuralNet net;
    public RealMatrix prediction;
    public int tilesTraversed;
    public Tile tile;
    public boolean dead;
    public float[] linesOfSight;
    RealMatrix inputs;

    public Car(final GameScreen screen, Texture texture, boolean controllable) {
        super(texture);
        setSize(30, 30);
        setOrigin(15, 4);
        this.screen = screen;
        this.angularVelocity = 0;
        this.velocityX = 0;
        this.velocityY = 0;
        this.controllable = controllable;
        ArrayList<Integer> dim = new ArrayList<>();
        dim.add(8); dim.add(64); dim.add(64); dim.add(64); dim.add(64); dim.add(64); dim.add(64); dim.add(4);
        this.net = new NeuralNet(dim, true, "sigmoid");
        linesOfSight = new float[5];
    }

    public void render() {
        draw(screen.game.batch);
    }

    public void update(float delta) {
        if (dead) return;
        getTile();
        if (distanceToCurve1()<=0
                || distanceToCurve2()<=0) {
            dead = true;
            return;
        }

        int startingSquare = tile.order;

        updateLinesOfSight();

        makePrediction();
        updateAngularVelocity();
        updateVelocity();

        double frictionFactor = Math.pow(Constants.VELOCITYFRICTION, delta);
        float deltaX = (float) (velocityX * (frictionFactor - 1)/Math.log(Constants.VELOCITYFRICTION));
        float deltaY =  (float) (velocityY * (frictionFactor - 1)/Math.log(Constants.VELOCITYFRICTION));

        translate(deltaX, deltaY);
        velocityX *= frictionFactor;
        velocityY *= frictionFactor;

        double speed = Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2));
        double friction = ((Math.pow(speed,3))/200000000+.00000001);
        frictionFactor = Math.pow(friction, delta);
        rotate((float) Math.toDegrees(angularVelocity * (frictionFactor - 1)/Math.log(friction)));
        angularVelocity *= frictionFactor;

        getTile();
        updateTilesTraversed(startingSquare, tile.order);

    }

    private void updateTilesTraversed(int startingSquare, int endingSquare) {
        if (startingSquare == 0 && endingSquare == screen.race.track.length) tilesTraversed--;
        else if (startingSquare == screen.race.track.length && endingSquare == 0) tilesTraversed++;
        else if (startingSquare>endingSquare) tilesTraversed--;
        else if (startingSquare<endingSquare) tilesTraversed++;
    }

    private void updateAngularVelocity() {
        if (controllable) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                angularVelocity += Constants.TURNSPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                angularVelocity -= Constants.TURNSPEED;
            }
        } else {
            if (prediction.getEntry(0, 2) > 0.5) {
                angularVelocity += Constants.TURNSPEED;
            } else if (prediction.getEntry(0, 3) > 0.5) {
                angularVelocity -= Constants.TURNSPEED;
            }
        }
    }

    private void updateVelocity() {
        if (controllable) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                velocityX -= Math.sin(Math.toRadians(getRotation())) * Constants.POWER;
                velocityY += Math.cos(Math.toRadians(getRotation())) * Constants.POWER;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                velocityX += Math.sin(Math.toRadians(getRotation())) * Constants.POWER/4;
                velocityY -= Math.cos(Math.toRadians(getRotation())) * Constants.POWER/4;
            }
        } else {
            if (prediction.getEntry(0, 0) > 0.5) {
                velocityX -= Math.sin(Math.toRadians(getRotation())) * Constants.POWER;
                velocityY += Math.cos(Math.toRadians(getRotation())) * Constants.POWER;
            } else if (prediction.getEntry(0, 1) > 0.5){
                velocityX += Math.sin(Math.toRadians(getRotation())) * Constants.POWER/4;
                velocityY -= Math.cos(Math.toRadians(getRotation())) * Constants.POWER/4;
            }
        }
    }

    private void makePrediction() {
        double[][] data = new double[1][8];
        double[] predictions = new double[]{
                velocityX, velocityY,
                angularVelocity,
                linesOfSight[0], linesOfSight[1], linesOfSight[2], linesOfSight[3], linesOfSight[4]
        };
//        predictions = normalize(predictions);
        data[0][0] = predictions[0];
        data[0][1] = predictions[1];
        data[0][2] = predictions[2];
        data[0][3] = predictions[3];
        data[0][4] = predictions[4];
        data[0][5] = predictions[5];
        data[0][6] = predictions[6];
        data[0][7] = predictions[7];
        inputs = MatrixUtils.createRealMatrix(data);
        prediction = net.predict(inputs);
    }

    public static double[] normalize(double[] data) {
        double[] norm = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            norm[i] = ((data[i] - Constants.INPUTMIN)/(Constants.INPUTMAX - Constants.INPUTMIN)-0.5)*50;
        }
        return norm;
    }

    public float getCenterX() {
        return getX()+getOriginX();
    }

    public float getCenterY() {
        return getY()+getOriginY();
    }

    public void getTile() {
        tile = screen.race.track.getTile(getCenterX(), getCenterY());
    }

    public float distanceToCurve1() {
        float x = getCenterX()%tile.size;
        float y = getCenterY()%tile.size;
        return tile.distanceToCurve1(x, y)-getOriginX();
    }

    public float distanceToCurve2() {
        float x = getCenterX()%tile.size;
        float y = getCenterY()%tile.size;
        return tile.distanceToCurve2(x, y)-getOriginX();
    }

    public void reset(int xpos, int ypos, int rotation) {
        setPosition(xpos, ypos);
        setRotation(rotation);
        velocityY = 0;
        velocityX = 0;
        angularVelocity = 0;
        tilesTraversed = 0;
        dead = false;
    }

    public int getScore() {
        return (int) (tilesTraversed*280+tile.distanceTraveled(getCenterX()%280, getCenterY()%280));
    }

    private float getLineOfSight(double centerRad, double offsetRad) {
        float x = getCenterX();
        float y = getCenterY();
        float distance1 = screen.race.track.getTile(x, y).distanceToCurve1(x%280, y%280);
        float distance2 = screen.race.track.getTile(x, y).distanceToCurve2(x%280, y%280);
        while (distance1 > 0 && distance2 > 0) {
            x -= Math.sin(centerRad+offsetRad)*10;
            y += Math.cos(centerRad+offsetRad)*10;
            distance1 = screen.race.track.getTile(x, y).distanceToCurve1(x%280, y%280);
            distance2 = screen.race.track.getTile(x, y).distanceToCurve2(x%280, y%280);
        }
        return (float) Math.sqrt(Math.pow(x-getCenterX(), 2) + Math.pow(y-getCenterY(), 2));
    }

    public void updateLinesOfSight() {
        double rad = Math.toRadians(getRotation());
        linesOfSight[0] = (int) getLineOfSight(rad, 0);
        linesOfSight[1] = (int) getLineOfSight(rad, Math.PI/2);
        linesOfSight[2] = (int) getLineOfSight(rad, -Math.PI/2);
        linesOfSight[3] = (int) getLineOfSight(rad, Math.PI/4);
        linesOfSight[4] = (int) getLineOfSight(rad, -Math.PI/4);
    }
}
