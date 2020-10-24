package com.bears.drift;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Car extends Sprite {
    final Drift game;
    private float velocityX;
    private float velocityY;
    private float angle;
    private float angularVelocity;
    private boolean controllable;

    public Car(final Drift game, Texture texture, boolean controllable) {
        super(texture);
        setSize(20, 20);
        setPosition(game.screenSizeX/2, game.screenSizeY/2);
        setOrigin(10, 2);
        this.game = game;
        this.angle = 0;
        this.angularVelocity = 0;
        this.velocityX = 0;
        this.velocityY = 0;
        this.controllable = controllable;
    }

    public void render() {
        draw(game.batch);
    }

    public void update(float delta) {

        updateAngularVelocity();
        updateVelocity();

        double frictionFactor = Math.pow(Constants.VELOCITYFRICTION, delta);
        translate((float) (velocityX * (frictionFactor - 1)/Math.log(Constants.VELOCITYFRICTION)), (float) (velocityY * (frictionFactor - 1)/Math.log(Constants.VELOCITYFRICTION)));
        velocityX *= frictionFactor;
        velocityY *= frictionFactor;

        double speed = Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2));
        double friction = ((Math.pow(speed,3))/200000000+.00000001);
        frictionFactor = Math.pow(friction, delta);
        angle += angularVelocity * (frictionFactor - 1)/Math.log(friction);
        angularVelocity *= frictionFactor;
        setRotation((float) Math.toDegrees(angle));
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
                velocityX -= Math.sin(angle) * Constants.POWER;
                velocityY += Math.cos(angle) * Constants.POWER;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                velocityX += Math.sin(angle) * Constants.POWER/4;
                velocityY -= Math.cos(angle) * Constants.POWER/4;
            }
        } else {
            int input = getVelocityInput();
            if (input == 1) {
                velocityX -= Math.sin(angle) * Constants.POWER;
                velocityY += Math.cos(angle) * Constants.POWER;
            } else if (input == -1){
                velocityX += Math.sin(angle) * Constants.POWER/4;
                velocityY -= Math.cos(angle) * Constants.POWER/4;
            }
        }
    }

    private int getAngularVelocityInput() {
        return -1;
    }

    private int getVelocityInput() {
        return 1;
    }
}
