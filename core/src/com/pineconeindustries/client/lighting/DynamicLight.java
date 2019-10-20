package com.pineconeindustries.client.lighting;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.LightingManager;
import com.pineconeindustries.shared.components.gameobjects.GameObject;
import com.pineconeindustries.shared.components.gameobjects.Projectile;

import box2dLight.PointLight;

public class DynamicLight extends PointLight {
	Projectile parent;

	public DynamicLight(Projectile parent, Color color, int rays, float distance, float x, float y) {
		super(LightingManager.getInstance().getLighting(), rays, color, distance, x, y);
		this.setSoft(true);
		this.parent = parent;
	}

	public void updateLocation() {
		setPosition(parent.getCenterRenderLoc());
	}

}
