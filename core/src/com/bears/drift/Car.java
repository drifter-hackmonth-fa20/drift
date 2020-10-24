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
    private double power;
    private double turnSpeed;

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
        this.power = 4;
        this.turnSpeed = .2;
    }

    public void render() {
        draw(game.batch);
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            angularVelocity += turnSpeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            angularVelocity -= turnSpeed * delta;
        }
        angle += angularVelocity;
        setRotation((float) Math.toDegrees(angle));

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocityX -= Math.sin(angle) * power * delta;
            velocityY += Math.cos(angle) * power * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocityX += Math.sin(angle) * power/4 * delta;
            velocityY -= Math.cos(angle) * power/4 * delta;
        }
        translate(velocityX, velocityY);

        double speed = Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2));
        angularVelocity *= Math.pow(Math.E, delta * (-30/(3*speed+2)));
        velocityX *= Math.pow(Math.E, delta * -1.2);
        velocityY *= Math.pow(Math.E, delta * -1.2);
    }
}
