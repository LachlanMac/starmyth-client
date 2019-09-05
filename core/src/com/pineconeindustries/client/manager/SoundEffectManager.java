package com.pineconeindustries.client.manager;

import com.badlogic.gdx.audio.Sound;

public class SoundEffectManager {

	private static SoundEffectManager instance = null;

	public static SoundEffectManager getInstance() {
		if (instance == null) {
			instance = new SoundEffectManager();
		}
		return instance;
	}

	public void playSoundEffect(Sound sound, float volume) {

		sound.play(volume);

	}

}
