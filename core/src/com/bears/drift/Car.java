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
        setPosition(100, 100);
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

    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            angularVelocity += 0.002;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            angularVelocity -= 0.002;
        }
        angle += angularVelocity;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocityX -= Math.sin(angle) * .05;
            velocityY += Math.cos(angle) * .05;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocityX += Math.sin(angle) * .05;
            velocityY -= Math.cos(angle) * .05;
        }
        translate(velocityX, velocityY);

        velocityX *= 0.98;
        velocityY *= 0.98;
        angularVelocity *= 0.98;


        setRotation((float) Math.toDegrees(angle));
    }
}
