package com.pineconeindustries.shared.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.pineconeindustries.shared.actions.Action;
import com.pineconeindustries.shared.data.Global;

public class Files {

	public static ArrayList<Action> loadActions() {

		ArrayList<Action> actions = new ArrayList<Action>();

		File f;

		f = Gdx.files.internal("actions/actionlist.txt").file();

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {

				String data[] = sCurrentLine.split("-");
				Action a = new Action(Integer.parseInt(data[0].trim()), data[1].trim());
				actions.add(a);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return actions;

	}
	
	public static String loadShipLayout(String path) {

		StringBuilder dataBuilder = new StringBuilder();

		File f;

		if (Global.isServer()) {
			f = Gdx.files.internal("server/shiplayouts/" + path + ".txt").file();
		} else {
			f = Gdx.files.internal("shiplayouts/" + path + ".txt").file();
		}

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

	public static boolean cachedShipLayout(String path, long checksum) {

		boolean exists = Gdx.files.internal("shiplayouts/" + path + ".txt").file().exists();
		boolean cs = false;
		long fileCS = 0;
		if (exists) {

			char[] array = loadShipLayout(path).toCharArray();

			for (char c : array) {
				fileCS += c;
			}

		}

		return (checksum == fileCS && exists);

	}

	public static String loadShipPathmap(String path) {

		StringBuilder dataBuilder = new StringBuilder();
		File f;

		if (Global.isServer()) {
			f = Gdx.files.internal("server/shiplayouts/" + path + "-pm.txt").file();
		} else {
			f = Gdx.files.internal("shiplayouts/" + path + "-pm.txt").file();
		}

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
