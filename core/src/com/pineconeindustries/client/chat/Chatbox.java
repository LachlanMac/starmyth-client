package com.pineconeindustries.client.chat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Queue;
import com.pineconeindustries.client.manager.InputManager;
import com.pineconeindustries.client.manager.InputState;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.client.networking.packets.PacketFactory;
import com.pineconeindustries.shared.log.Log;

public class Chatbox extends Table {

	TextField chatEntry;
	TextArea chatHistory;

	TextFieldStyle textStyle;

	// SKIN TEST

	private float chatBoxWidth, chatBoxHeight, widthPercent, heightPercent;

	Table chatArea;
	ScrollPane scroll;
	Skin skin;

	Stage stage;
	Queue<Label> chatQueue;

	public Chatbox(float widthPercent, float heightPercent, Skin skin, Stage stage) {
		super(skin);

		this.stage = stage;
		chatQueue = new Queue<Label>();
		this.skin = skin;
		this.widthPercent = widthPercent;
		this.heightPercent = heightPercent;
		float windowWidth = (float) Gdx.graphics.getWidth();
		float windowHeight = (float) Gdx.graphics.getHeight();

		chatBoxWidth = windowWidth * widthPercent;
		chatBoxHeight = windowHeight * heightPercent;
		this.setWidth(chatBoxWidth);
		this.setHeight(chatBoxHeight);

		chatArea = new Table(skin);

		scroll = new ScrollPane(chatArea, skin);

		scroll.setScrollingDisabled(true, false);
		scroll.setForceScroll(false, true);

		chatArea.setWidth(chatBoxWidth);

		chatEntry = new TextField("", skin);

		stage.setKeyboardFocus(null);

		this.add(scroll).width(chatBoxWidth).height(chatBoxHeight * 0.8f).expandX().padBottom(10);
		this.row();
		this.add(chatEntry).width(chatBoxWidth).height(chatBoxHeight * 0.2f);

	}

	public void update() {

		if (InputState.isPressed(InputState.ENTER)) {

			if (stage.getKeyboardFocus() == chatEntry) {

				String msg = chatEntry.getText();

				if (msg.length() >= 1) {

					if (msg.charAt(0) == '!') {
						sendAdminCmd(msg);

					} else if (msg.charAt(0) == '#') {
						sendLocalCmd(msg);
					} else {

						sendMessage(msg);

					}

					chatEntry.setText("");

				}
			}
		}

	}

	public void sendLocalCmd(String msg) {

		if (msg.equalsIgnoreCase("#loc")) {
			Vector2 loc = LogicController.getInstance().getPlayer().getLoc();
			String s = new String(loc.x + ", " + loc.y);
			addMsg(s);

		}

	}

	public void sendAdminCmd(String msg) {
		Log.print("Sending Admin Command");
		LogicController.getInstance().getConnection().sendTCP(PacketFactory.makeChatCommandPacket(msg));

	}

	public void sendMessage(String msg) {

		LogicController.getInstance().getConnection().sendTCP(PacketFactory.makeChatSayPacket(msg));

	}

	public void addMsg(String msg) {

		chatArea.row();

		Label label = new Label(msg, skin);
		label.setAlignment(Align.left);
		label.setWrap(true);
		chatArea.add(label).width(getChatboxWidth());

		chatQueue.addLast(label);
		while (chatQueue.size > 20) {

			Label l = chatQueue.removeFirst();
			l.remove();
			l.clear();

		}

		scroll.layout();
		scroll.scrollTo(0, 0, 0, 0);

	}

	public float getChatboxWidth() {
		return chatBoxWidth;
	}

	public boolean isTyping() {

		return stage.getKeyboardFocus() == chatEntry;

	}

}
