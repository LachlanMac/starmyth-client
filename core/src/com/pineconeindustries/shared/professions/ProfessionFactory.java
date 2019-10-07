package com.pineconeindustries.shared.professions;

import java.util.ArrayList;

import com.pineconeindustries.shared.actions.ActionManager;
import com.pineconeindustries.shared.actions.types.ActionBase;
import com.pineconeindustries.shared.files.Files;

public class ProfessionFactory {

	public static ArrayList<Profession> professions;
	private static ProfessionFactory instance;

	private ProfessionFactory() {

	}

	public void loadProfessions() {
		professions = Files.loadProfessions();
	}

	public static ProfessionFactory getInstance() {
		if (instance == null)
			instance = new ProfessionFactory();

		return instance;

	}

	public static Profession loadProfession(String id, String name, String type) {

		Profession p = new Profession(Integer.parseInt(id), name);

		return p;

	}

	public static Profession getProfessionByID(int id) {

		Profession profession = null;

		for (Profession p : professions) {
			if (id == p.getID())
				profession = p;
		}

		return profession;

	}

}
