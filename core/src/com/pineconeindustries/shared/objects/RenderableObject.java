package com.pineconeindustries.shared.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface RenderableObject {

	public void update();

	public void render(SpriteBatch b);

	public void debugRender(ShapeRenderer debugRenderer);

}
