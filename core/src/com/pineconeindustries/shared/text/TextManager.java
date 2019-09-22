package com.pineconeindustries.shared.text;

import java.util.concurrent.ArrayBlockingQueue;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextManager {

	private static final int HEIGHT_OFFSET = 64;
	private ArrayBlockingQueue<Text> textItems;
	private BitmapFont font;
	private static TextManager instance = null;
	private GlyphLayout layout;

	private TextManager() {
		layout = new GlyphLayout();
		textItems = new ArrayBlockingQueue<Text>(1024);
		font = new BitmapFont();
	}

	public static TextManager getInstance() {
		if (instance == null) {
			instance = new TextManager();
		}

		return instance;
	}

	public void render(SpriteBatch b) {
		for (Text t : textItems) {

			layout.setText(font, t.getValue());
			float width = layout.width;// contains the width of the current set text
			//
			font.draw(b, t.getValue(), getXOffset(t.getX(), width, t.getSize()), t.getCenter().y + HEIGHT_OFFSET);
		}
	}

	public float getXOffset(float x, float width, float size) {

		float widthDiff = (size - width) / 2;
		return x + widthDiff;

	}

	public void dispose() {
		font.dispose();
	}

	public void addText(Text t) {
		textItems.add(t);

	}

	public void removeText(Text t) {
		textItems.remove(t);
	}

}
