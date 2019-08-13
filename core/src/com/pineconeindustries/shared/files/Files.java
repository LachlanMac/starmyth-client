package com.pineconeindustries.shared.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Files {

	public static String loadFile(String path, long checksum) {

		StringBuilder dataBuilder = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				dataBuilder.append(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String data = dataBuilder.toString().trim();

		long fileCS = 0;

		for (int i = 0; i < data.length(); i++) {

			fileCS += data.charAt(i);
		}

		if (checksum != fileCS) {
			System.out.println("CHECKSUM FAILED  " + fileCS + "  VS  " + checksum);
		}

		return dataBuilder.toString().trim();

	}

	public static String[] loadAIScript(String path) {
		
		
		String[] schedule = new String[24];

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {

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
