package com.bears.drift;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Drift extends Game {
	SpriteBatch batch;
	Texture img;
	Car car;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
		pixmap.setColor(153/255f, 255/255f, 153/255f, 255/255f);
		pixmap.fillRectangle(2, 0, 6, 10);
		car = new Car(this, new Texture(pixmap));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		car.update();
		car.render();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
