package com.pineconeindustries.client.galaxy;

public class Galaxy {

	private static Galaxy instance = null;

	private Galaxy() {

	}

	public static Galaxy getInstance() {

		if (instance == null) {
			instance = new Galaxy();
		}
		return instance;

	}

}
