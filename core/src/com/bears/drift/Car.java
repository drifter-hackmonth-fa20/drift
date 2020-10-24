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
        this.power = 0.05;
        this.turnSpeed = 0.002;
    }

    public void render() {
        draw(game.batch);
    }

    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            angularVelocity += turnSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            angularVelocity -= turnSpeed;
        }
        angle += angularVelocity;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocityX -= Math.sin(angle) * power;
            velocityY += Math.cos(angle) * power;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocityX += Math.sin(angle) * power/4;
            velocityY -= Math.cos(angle) * power/4;
        }
        translate(velocityX, velocityY);

//        velocityX *= 0.985;
//        velocityY *= 0.985;
//        angularVelocity *= 0.98;
        velocityX *= 0.985;
        velocityY *= 0.985;
        double speed = Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2));
        System.out.println(.5+(speed/(speed+.25))/2);
        angularVelocity *= Math.min(.5+(speed/(speed+.25))/2, 1);


        setRotation((float) Math.toDegrees(angle));
    }
}
