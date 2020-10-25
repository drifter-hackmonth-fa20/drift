package com.bears.drift;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

public class GameScreen implements Screen {
    final Drift game;
    Track track;
    Stage stage;
    OrthographicCamera camera;
    ArrayList<Car> cars;
    Car car;

    public GameScreen(final Drift game) {
        this.game = game;

        track = new Track(game);

        cars = new ArrayList<>();
        addCars(1, true);
        resetCars(track.startx, track.starty, track.startAngle);

        camera = new OrthographicCamera(game.screenSizeX, game.screenSizeY);
        stage = new Stage(new FitViewport(game.screenSizeX, game.screenSizeY, camera));
    }

    int tileNum;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        doPhysicsStep(delta);



        game.batch.begin();
        track.render();
        for (Car car: cars) {
            int num = track.getTileNum(car.getX()+car.getOriginX(), car.getY()+car.getOriginY());
            if (num != tileNum) {
                track.getTile(num).fun();
                tileNum = num;
            }
            car.render();
        }
        game.batch.end();
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

    private void addCars(int numCars, boolean controllable) {
        for (int i = 0; i < numCars; i++) {
            Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
            pixmap.setColor(153/255f, 255/255f, 153/255f, 255/255f);
            pixmap.fillRectangle(2, 0, 6, 10);
            car = new Car(game, new Texture(pixmap), controllable);
            cars.add(car);
        }
    }

    private void resetCars(int xpos, int ypos, int rotation) {
        for (Car car: cars) {
            car.setPosition(xpos, ypos);
            car.setRotation(rotation);
        }
    }

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
