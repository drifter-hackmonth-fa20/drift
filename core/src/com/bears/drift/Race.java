package com.bears.drift;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.apache.commons.math3.distribution.ExponentialDistribution;

import java.util.ArrayList;

public class Race {
    GameScreen screen;
    Track track;
    ArrayList<Car> cars;
    float elapsedTime;
    boolean running;
    boolean over;

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
        over = false;
    }

    public void pause() {
        running = !running;
    }

    public void end() {
        running = false;
        over = true;
    }

    public void reset() {
        elapsedTime = 0;
        over = false;
        resetCars(track.startx, track.starty, track.startAngle);
    }

    private void evolve() {

    }

    private void randomizeTrack() {
        track = new Track(screen.game);
        resetCars(track.startx, track.starty, track.startAngle);
    }

    public void render(float delta) {
        if (running) {
            elapsedTime += delta*Constants.SPEED;
            doPhysicsStep(delta);
        }
        track.render();
        for (Car car: cars) {
            car.render();
        }
    }

    private static int sampleExponential() {
        ExponentialDistribution e = new ExponentialDistribution(Constants.NUMCARS/20f);
        int s = (int) e.sample();
        while (s > Constants.NUMCARS) {
            s = (int) e.sample();
        }
        return s;
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
            car.reset(xpos, ypos, rotation);
        }
    }
}
