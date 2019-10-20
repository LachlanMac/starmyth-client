package com.pineconeindustries.server.ai.roles;

import java.util.ArrayList;

import com.pineconeindustries.shared.actions.ActionManager;
import com.pineconeindustries.shared.actions.types.ActionBase;
import com.pineconeindustries.shared.files.Files;

public class RoleManager {

	public static ArrayList<Role> professions;
	private static RoleManager instance;
	private Role defaultProfession;

	private RoleManager() {
		loadProfessions();
	}

	public void loadProfessions() {
		professions = Files.loadProfessions();
		loadDefaults();

	}

	public static RoleManager getInstance() {
		if (instance == null)
			instance = new RoleManager();

		return instance;

	}

	public static Role loadProfession(String id, String name, String type) {

		Role p = new Role(Integer.parseInt(id), name);

		return p;

	}

	public static Role getProfessionByID(int id) {

		Role profession = null;

		for (Role p : professions) {
			if (id == p.getID())
				profession = p;
		}

		return profession;

	}

	public void loadDefaults() {

		defaultProfession = new Role(0, "default");

	}

}
