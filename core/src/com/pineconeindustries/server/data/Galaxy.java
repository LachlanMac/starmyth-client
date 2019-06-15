package com.pineconeindustries.server.data;

import java.util.concurrent.ArrayBlockingQueue;

public class Galaxy {

	
	
	
	
	private static Galaxy instance = null;

	private Galaxy() {
		
	}

	public Galaxy getInstance() {

		if (instance == null) {
			instance = new Galaxy();
		}
		return instance;

	}

}
