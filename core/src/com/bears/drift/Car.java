package com.bears.drift;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Car extends Sprite {
    final GameScreen screen;
    private float velocityX;
    private float velocityY;
    private float angularVelocity;
    private boolean controllable;

    public Car(final GameScreen screen, Texture texture, boolean controllable) {
        super(texture);
        setSize(40, 40);
        setOrigin(20, 5);
        this.screen = screen;
        this.angularVelocity = 0;
        this.velocityX = 0;
        this.velocityY = 0;
        this.controllable = controllable;
    }

    public void render() {
        draw(screen.game.batch);
    }

    public void update(float delta) {
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
            int input = getAngularVelocityInput();
            if (input == -1) {
                angularVelocity += Constants.TURNSPEED;
            } else if (input == 1) {
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
            int input = getVelocityInput();
            if (input == 1) {
                velocityX -= Math.sin(Math.toRadians(getRotation())) * Constants.POWER;
                velocityY += Math.cos(Math.toRadians(getRotation())) * Constants.POWER;
            } else if (input == -1){
                velocityX += Math.sin(Math.toRadians(getRotation())) * Constants.POWER/4;
                velocityY -= Math.cos(Math.toRadians(getRotation())) * Constants.POWER/4;
            }
        }
    }

    private int getAngularVelocityInput() {
        return -1;
    }

    private int getVelocityInput() {
        return 1;
    }

    public float getCenterX() {
        return getX()+getOriginX();
    }

    public float getCenterY() {
        return getY()+getOriginY();
    }

    public Tile getTile() {
        return screen.track.getTile(getCenterX(), getCenterY());
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
}
