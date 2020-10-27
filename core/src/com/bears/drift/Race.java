package com.bears.drift;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Race {
    GameScreen screen;
    Track track;
    ArrayList<Car> cars;
    float elapsedTime;
    boolean running;

    public Race(GameScreen screen, int numCars, boolean playerCar) {
        this.screen = screen;
        this.cars = new ArrayList<>();
        if (playerCar) {
            addPlayerCar();
            numCars -= 1;
        }
        addAutomatedCars(numCars);
        randomizeTrack();
        elapsedTime = 0;
        running = true;
    }

    public void begin() {
        randomizeTrack();
        elapsedTime = 0;
        running = true;
    }

    public void end() {
        running = false;
    }

    public void randomizeTrack() {
                track = new Track(screen.game);
        resetCars(track.startx, track.starty, track.startAngle);
    }

    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.TAB)) randomizeTrack();
        if (running) {
            elapsedTime += delta;
            doPhysicsStep(delta);
        }
        track.render();
        for (Car car: cars) {
            car.render();
        }
    }

    private float accumulator = 0;

    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= Constants.TIME_STEP) {
            for (int i = 0; i < Constants.SPEED; i++) {
                for (Car car: cars) {
                    car.update(Constants.TIME_STEP);
                }
            }
            accumulator -= Constants.TIME_STEP;
        }
    }

    private void addPlayerCar() {
        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(255/255f, 255/255f, 255/255f, 255/255f);
        pixmap.fillRectangle(2, 0, 6, 10);
        Car car = new Car(screen, new Texture(pixmap), true);
        cars.add(car);
    }

    private void addAutomatedCars(int numCars) {
        for (int i = 0; i < numCars; i++) {
            Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
            pixmap.setColor((float) Math.random(), (float) Math.random(), (float) Math.random(), .9f);
            pixmap.fillRectangle(2, 0, 6, 10);
            Car car = new Car(screen, new Texture(pixmap), false);
            cars.add(car);
        }
    }

    private void resetCars(int xpos, int ypos, int rotation) {
        for (Car car: cars) {
            car.setPosition(xpos, ypos);
            car.setRotation(rotation);
        }
    }
}
