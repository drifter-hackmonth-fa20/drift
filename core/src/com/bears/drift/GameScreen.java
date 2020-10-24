package com.bears.drift;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {
    final Drift game;
    Stage stage;
    OrthographicCamera camera;
    Car car;

    public GameScreen(final Drift game) {
        this.game = game;
        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(153/255f, 255/255f, 153/255f, 255/255f);
        pixmap.fillRectangle(2, 0, 6, 10);
        car = new Car(game, new Texture(pixmap), true);

        camera = new OrthographicCamera(game.screenSizeX, game.screenSizeY);
        stage = new Stage(new FitViewport(game.screenSizeX, game.screenSizeY, camera));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        doPhysicsStep(delta);

        game.batch.begin();
        car.render();
        game.batch.end();
    }

    private float accumulator = 0;

    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= Constants.TIME_STEP) {
            for (int i = 0; i < Constants.SPEED; i++) {
                car.update(Constants.TIME_STEP);
            }
            accumulator -= Constants.TIME_STEP;
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
