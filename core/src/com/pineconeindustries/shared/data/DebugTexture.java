package com.pineconeindustries.shared.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DebugTexture {

	Texture t;
	private float x, y;
	private boolean on;

	public DebugTexture(Texture t, float x, float y) {
		this.x = x;
		this.y = y;
		this.t = t;
		on = false;
	}

	public void render(SpriteBatch b) {
		if (on) {
			b.draw(t, x, y);
		}
	}

	public void setOn() {
		this.on = true;
	}

	public void setOff() {
		this.on = false;
	}

	public void setLoc(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
