package com.pineconeindustries.client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.pineconeindustries.client.chat.Chatbox;
import com.pineconeindustries.client.manager.InputState;
import com.pineconeindustries.shared.objects.Player;

public class UserInterface {

	Stage stage;
	Table rootTable;
	Chatbox chatbox;
	Player player;
	Skin skin;

	LSelectBox selectBox;

	static UserInterface instance = null;

	public static UserInterface getInstance() {

		if (instance == null) {
			instance = new UserInterface();
		}
		return instance;

	}

	private UserInterface() {

		this.stage = new Stage();

		buildInterface();
		this.selectBox = new LSelectBox(getSkin());

		stage.getRoot().addCaptureListener(new InputListener() {

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

				if (button == 0)
					InputState.LEFT_MOUSE_DOWN = true;
				if (button == 1)
					InputState.RIGHT_MOUSE_DOWN = true;
				if (!(event.getTarget() instanceof TextField)) {
					stage.setKeyboardFocus(null);
				}
				if (!(event.getTarget() instanceof LSelectBox)) {
					selectBox.clearItems();
				}
				return false;

			}
		});

	}

	public void buildInterface() {

		skin = new Skin(Gdx.files.internal("ui/quantum-horizon-ui.json"));

		rootTable = new Table(skin);
		rootTable.setFillParent(true);

		chatbox = new Chatbox(.36f, .28f, skin, stage);

		rootTable.add(chatbox).expand().bottom().left();
		rootTable.add(selectBox).expand().center();
		stage.addActor(rootTable);

	}

	public void adddSelectBox() {
		selectBox.test();
	}

	public Skin getSkin() {
		return skin;
	}

	public void update() {

	}

	public void render() {
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		chatbox.update();

	}

	public void dispose() {
		stage.dispose();
	}

	public Stage getStage() {
		return stage;
	}

	public Chatbox getChat() {
		return chatbox;
	}

	public Player getPlayer() {
		return player;
	}

}
