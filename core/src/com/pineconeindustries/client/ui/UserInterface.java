package com.pineconeindustries.client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.pineconeindustries.client.chat.Chatbox;
import com.pineconeindustries.client.objects.Player;

public class UserInterface {

	Stage stage;

	Table rootTable;

	Chatbox chatbox;
	Player player;
	
	public UserInterface(Stage stage) {

		this.player = player;
		this.stage = stage;

		stage.getRoot().addCaptureListener(new InputListener() {

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (!(event.getTarget() instanceof TextField))
					stage.setKeyboardFocus(null);
				return false;
			}

		});

		buildInterface();

	}

	public void buildInterface() {

		Skin skin = new Skin(Gdx.files.internal("ui/quantum-horizon-ui.json"));

		rootTable = new Table(skin);
		rootTable.setFillParent(true);

		chatbox = new Chatbox(.36f, .28f, skin, stage);

		rootTable.add(chatbox).expand().bottom().left();
		stage.addActor(rootTable);

	}

	public Chatbox getChat() {
		return chatbox;
	}

	public Player getPlayer() {
		return player;
	}

}
