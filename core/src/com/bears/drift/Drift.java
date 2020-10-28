package com.bears.drift;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

public class Drift extends Game {
	SpriteBatch batch;
	int screenSizeX;
	int screenSizeY;
	int centerX;
	int centerY;
	private HashMap<String, Texture> loaded;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		loaded = new HashMap<>();
		screenSizeX = 1680; screenSizeY = 1680;
		centerX = screenSizeX/2; centerY = screenSizeY/2;
		Gdx.graphics.setWindowedMode(screenSizeX, screenSizeY);
		this.setScreen(new GameScreen(this));
	}

	public Texture getTexture(String path) {
		if (loaded.containsKey(path)) {
			return loaded.get(path);
		} else {
			Texture texture = new Texture(path);
			loaded.put(path, texture);
			return texture;
		}
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
