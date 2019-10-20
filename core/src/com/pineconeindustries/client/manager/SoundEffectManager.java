package com.pineconeindustries.client.manager;

import com.badlogic.gdx.audio.Sound;

public class SoundEffectManager {

	Sound explosion, shipStart, shipStop, shipLoop, pew1, pew2, pew3;
	
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

	public void loadSounds() {

	}

}
