package com.pineconeindustries.shared.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Files {

	public static String loadShipLayout(String path) {

		StringBuilder dataBuilder = new StringBuilder();

		File f = Gdx.files.internal("shiplayouts/" + path + ".txt").file();

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				dataBuilder.append(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataBuilder.toString().trim();

	}

	public static String[] loadAIScript(String path) {

		String[] schedule = new String[24];
		File f = Gdx.files.internal(path).file();

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {

			String sCurrentLine;
			int i = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				schedule[i] = sCurrentLine.trim().toUpperCase();
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return schedule;
	}

}
