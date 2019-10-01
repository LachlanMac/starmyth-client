package com.pineconeindustries.client.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class AnimationSet {

	public static final int DOWNED = 1;

	public static final float CHARACTER_ANIMATION_SPEED = 0.15f;

	Texture spriteSheet;
	Animation<TextureRegion> idle_up, idle_down, idle_left, idle_right, mov_up, mov_down, mov_left, mov_right, dead;

	public AnimationSet(Texture spriteSheet) {

		this.spriteSheet = spriteSheet;

	}

	public void loadAnimations() {

		TextureRegion[][] regions = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 8,
				spriteSheet.getHeight() / 4);

		mov_down = new Animation<TextureRegion>(CHARACTER_ANIMATION_SPEED,
				new TextureRegion[] { regions[1][1], regions[1][2], regions[1][3], regions[1][4] });
		mov_up = new Animation<TextureRegion>(CHARACTER_ANIMATION_SPEED,
				new TextureRegion[] { regions[0][1], regions[0][2], regions[0][3], regions[0][4] });
		mov_left = new Animation<TextureRegion>(CHARACTER_ANIMATION_SPEED,
				new TextureRegion[] { regions[3][1], regions[3][2], regions[3][3], regions[3][4] });
		mov_right = new Animation<TextureRegion>(CHARACTER_ANIMATION_SPEED,
				new TextureRegion[] { regions[2][1], regions[2][2], regions[2][3], regions[2][4] });

		idle_up = new Animation<TextureRegion>(CHARACTER_ANIMATION_SPEED, new TextureRegion[] { regions[0][0] });
		idle_down = new Animation<TextureRegion>(CHARACTER_ANIMATION_SPEED, new TextureRegion[] { regions[1][0] });
		idle_left = new Animation<TextureRegion>(CHARACTER_ANIMATION_SPEED, new TextureRegion[] { regions[3][0] });
		idle_right = new Animation<TextureRegion>(CHARACTER_ANIMATION_SPEED, new TextureRegion[] { regions[2][0] });

		dead = new Animation<TextureRegion>(CHARACTER_ANIMATION_SPEED, new TextureRegion[] { regions[2][5] });

	}

	public Animation<TextureRegion> getAnimation(Vector2 direction, float velocity, int special) {

		if (special == DOWNED) {
			return dead;
		}

		// character is not moving
		if (velocity == 0) {
			if (direction.x > 0)
				return idle_left;
			if (direction.x < 0)
				return idle_right;
			if (direction.y > 0)
				return idle_up;
			if (direction.y < 0)
				return idle_down;
		} else {

			if (direction.x > 0)
				return mov_left;
			if (direction.x < 0)
				return mov_right;
			if (direction.y > 0)
				return mov_up;
			if (direction.y < 0)
				return mov_down;

		}
		return idle_down;

	}

}
