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

    public Car(final Drift game, Texture texture) {
        super(texture);
        setSize(20, 20);
        setPosition(game.screenSizeX/2, game.screenSizeY/2);
        setOriginCenter();
        this.game = game;
        this.angle = 0;
        this.angularVelocity = 0;
        this.velocityX = 0;
        this.velocityY = 0;
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

        frictionFactor = Math.pow(Constants.ANGULARFRICTION, delta);
        angle += angularVelocity * (frictionFactor - 1)/Math.log(Constants.ANGULARFRICTION);
        angularVelocity *= frictionFactor;
        setRotation((float) Math.toDegrees(angle));
    }

    private void updateAngularVelocity() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            angularVelocity += Constants.TURNSPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            angularVelocity -= Constants.TURNSPEED;
        }
    }

    private void updateVelocity() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocityX -= Math.sin(angle) * Constants.POWER;
            velocityY += Math.cos(angle) * Constants.POWER;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocityX += Math.sin(angle) * Constants.POWER/4;
            velocityY -= Math.cos(angle) * Constants.POWER/4;
        }
    }
}
