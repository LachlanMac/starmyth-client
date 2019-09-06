package com.pineconeindustries.client.lighting;

import com.pineconeindustries.client.manager.LightingManager;

public class DarkenEffect extends Thread {

	private float targetAlpha, speed;

	private float timeLeft = 0;
	private float runTime = 0;
	private float iterations = 0;
	private float deltaAlpha = 0;

	public DarkenEffect(float targetAlpha, float speed) {
		this.targetAlpha = targetAlpha;
		this.speed = speed;
		this.deltaAlpha = targetAlpha / speed;
		this.iterations = deltaAlpha;
		this.start();
	}

	@Override
	public void run() {

		while (LightingManager.getInstance().getAlpha() >= targetAlpha) {
			try {
				Thread.sleep(10);
				LightingManager.getInstance().lowerAlpha(-deltaAlpha);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		while (LightingManager.getInstance().getAlpha() <= LightingManager.getInstance().DEFAULT_ALPHA) {
			try {
				Thread.sleep(10);
				LightingManager.getInstance().raiseAlpha(deltaAlpha);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
