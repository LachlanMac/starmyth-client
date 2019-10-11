package com.pineconeindustries.client.ui;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.shared.components.structures.Structure;


public class SelectorFactory {

	static boolean selectBoxShown = false;

	public SelectorFactory() {

	}

	public static SelectBox<String> getSelectBox() {

		final SelectBox<String> selectBox = new SelectBox<String>(UserInterface.getInstance().getSkin());

		Structure s = LogicController.getInstance().getSector()
				.getStructureByID(LogicController.getInstance().getPlayer().getStructureID());

		String[] layerDesc = new String[s.getLayers()];

		String selected = "Select a Deck";
		for (int i = 1; i < s.getLayers() + 1; i++) {

			if (i == LogicController.getInstance().getPlayer().getLayer()) {
				layerDesc[i - 1] = "Current Deck " + (i);

			} else {
				layerDesc[i - 1] = "Deck " + (i);
			}

		}
		selectBox.setItems(layerDesc);
		selectBox.setSelected(selected);

		selectBox.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {

				System.out.println("SELECTED: " + (selectBox.getSelected()));
				return true;
			}
		});

		selectBoxShown = true;
		return selectBox;

	}

}
