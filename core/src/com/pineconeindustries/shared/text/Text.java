package com.pineconeindustries.shared.text;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class Text {

	private String value;
	private float size;
	private Vector2 center;
	private BitmapFont font;
	float x, y;

	public Text(String value, Vector2 center, float size) {
		this.value = value;
		this.center = center;
		this.size = size;

	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setCenter(Vector2 center) {
		this.center = center;
	}

	public Vector2 getCenter() {
		return center;
	}

	public float getSize() {
		return size;
	}

	public float getX() {
		return center.x - (size / 2);
	}

	public float getY() {
		return center.y - (size / 2);
	}

	public BitmapFont getFont() {
		return font;
	}

	public void dispose() {
		font.dispose();
	}

	public void initFont() {
		this.font = new BitmapFont();
	}

}
