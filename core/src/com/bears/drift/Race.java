package com.bears.drift;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.apache.commons.math3.distribution.ExponentialDistribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

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
        track = new Track(screen.game);
        begin();
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
        evolve();
        begin();
    }

    public void reset() {
        elapsedTime = 0;
        if (over) running = true;
        over = false;
        resetCars(track.startx, track.starty, track.startAngle);
    }

    private void evolve() {
        cars.sort(new Comparator<Car>() {
            @Override
            public int compare(Car car, Car t1) {
                return t1.getScore() - car.getScore();
            }
        });
        ArrayList<Car> newCars = new ArrayList<>();
        newCars.add(cars.get(0));
        int num = 0;
        for (int i = 1; i < Constants.NUMCARS; i++) {
            Car parent1 = cars.get(num);
            Car parent2 = cars.get(sampleExponential());
            Car newCar = new Car(screen, parent1.getTexture(), false);
            newCar.setColor(parent1.getColor());
            newCar.net = parent1.net.mate(parent2.net);
            newCars.add(newCar);
            num = (num+1)%(Constants.NUMCARS/10);
        }
        cars = newCars;
    }

    private void randomizeTrack() {
        if (Constants.RANDOMIZE) track.randomizeTiles();
        resetCars(track.startx, track.starty, track.startAngle);
    }

    public boolean dead;

    public void render(float delta) {
        if (running) {
            elapsedTime += delta*Constants.SPEED;
            doPhysicsStep(delta);
        }
        track.render();
        dead = true;
//        if (!cars.get(0).dead) System.out.println(cars.get(0).inputs);
        for (Car car: cars) {
            car.render();
            if (!car.dead) {
                dead = false;
            }
        }
    }

    private static int sampleExponential() {
        ExponentialDistribution e = new ExponentialDistribution(Constants.NUMCARS/5f);
        int s = (int) e.sample();
        while (s >= Constants.NUMCARS) {
            s = (int) e.sample();
        }
        return s;
    }

    private float accumulator = 0;

    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= Constants.TIME_STEP) {
            for (Car car: cars) {
                for (int i = 0; i < Constants.SPEED; i++) car.update(Constants.TIME_STEP);
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
        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(1,1,1,.9f);
        pixmap.fillRectangle(2, 0, 6, 10);
        Texture texture = new Texture(pixmap);
        for (int i = 0; i < numCars; i++) {
            Car car = new Car(screen, texture, false);
            car.setColor((float) Math.random(), (float) Math.random(), (float) Math.random(), .9f);
            cars.add(car);
        }
    }

    private void resetCars(int xpos, int ypos, int rotation) {
        for (Car car: cars) {
            car.reset(xpos, ypos, rotation);
        }
    }
}
