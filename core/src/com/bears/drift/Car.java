package com.bears.drift;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;

public class Car extends Sprite {
    final GameScreen screen;
    private float velocityX;
    private float velocityY;
    private float angularVelocity;
    private boolean controllable;
    private NeuralNet net;
    private RealMatrix prediction;

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
        dim.add(8); dim.add(64); dim.add(64); dim.add(4);
        this.net = new NeuralNet(dim, true, "sigmoid");
    }

    public void render() {
        draw(screen.game.batch);
    }

    public void update(float delta) {
        makePrediction();
        updateAngularVelocity();
        updateVelocity();

        double frictionFactor = Math.pow(Constants.VELOCITYFRICTION, delta);
        float deltaX = (float) (velocityX * (frictionFactor - 1)/Math.log(Constants.VELOCITYFRICTION));
        float deltaY =  (float) (velocityY * (frictionFactor - 1)/Math.log(Constants.VELOCITYFRICTION));

        translate(deltaX, deltaY);
        if (distanceToCurve1()<=Math.max(getOriginX(), getOriginY())
                || distanceToCurve2()<=Math.max(getOriginX(), getOriginY())) {
            translate(-deltaX-.01f, -deltaY-.01f);
        }
        velocityX *= frictionFactor;
        velocityY *= frictionFactor;

        double speed = Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2));
        double friction = ((Math.pow(speed,3))/200000000+.00000001);
        frictionFactor = Math.pow(friction, delta);
        rotate((float) Math.toDegrees(angularVelocity * (frictionFactor - 1)/Math.log(friction)));
        angularVelocity *= frictionFactor;
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
        data[0][0] = getCenterX()%280;
        data[0][1] = getCenterY()%280;
        data[0][2] = velocityX;
        data[0][3] = velocityY;
        data[0][4] = getRotation();
        data[0][5] = angularVelocity;
        data[0][6] = distanceToCurve1();
        data[0][7] = distanceToCurve2();
        RealMatrix inputs = MatrixUtils.createRealMatrix(data);
        prediction = net.predict(inputs);
    }

    public float getCenterX() {
        return getX()+getOriginX();
    }

    public float getCenterY() {
        return getY()+getOriginY();
    }

    public Tile getTile() {
        return screen.race.track.getTile(getCenterX(), getCenterY());
    }

    public float distanceToCurve1() {
        Tile tile = getTile();
        float x = getCenterX()%tile.size;
        float y = getCenterY()%tile.size;
        return tile.distanceToCurve1(x, y);
    }

    public float distanceToCurve2() {
        Tile tile = getTile();
        float x = getCenterX()%tile.size;
        float y = getCenterY()%tile.size;
        return tile.distanceToCurve2(x, y);
    }

    public void reset(int xpos, int ypos, int rotation) {
        setPosition(xpos, ypos);
        setRotation(rotation);
        velocityY = 0;
        velocityX = 0;
        angularVelocity = 0;
    }
}
