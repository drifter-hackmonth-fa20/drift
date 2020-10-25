package com.bears.drift;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Drift extends Game {
	SpriteBatch batch;
	Texture img;
	int screenSizeX;
	int screenSizeY;
	int centerX;
	int centerY;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		screenSizeX = 1680; screenSizeY = 1680;
		centerX = screenSizeX/2; centerY = screenSizeY/2;
		Gdx.graphics.setWindowedMode(screenSizeX, screenSizeY);
		this.setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
