package com.bears.drift;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    Stage stage;
    OrthographicCamera camera;
    Race race;

    public GameScreen(final Drift game) {
        this.game = game;

        camera = new OrthographicCamera(game.screenSizeX, game.screenSizeY);
        stage = new Stage(new FitViewport(game.screenSizeX, game.screenSizeY, camera));
        race = new Race(this, Constants.NUMCARS, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        if ((race.elapsedTime>Constants.RACESECONDS && !race.over) || race.dead) {
            race.end();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            race.end();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            race.reset();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            race.pause();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            Constants.RANDOMIZE = !Constants.RANDOMIZE;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) Constants.SPEED = 1;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) Constants.SPEED = 2;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) Constants.SPEED = 3;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) Constants.SPEED = 4;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) Constants.SPEED = 5;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) Constants.SPEED = 6;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) Constants.SPEED = 7;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) Constants.SPEED = 8;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) Constants.SPEED = 9;

        game.batch.begin();
        race.render(delta);
        game.batch.end();
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
