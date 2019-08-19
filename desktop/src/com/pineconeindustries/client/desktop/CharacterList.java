package com.pineconeindustries.client.desktop;

import java.util.ArrayList;

import com.pineconeindustries.client.data.LocalPlayerData;

public class CharacterList {

	String status1, status2, status3, status4;
	String name1, name2, name3, name4;
	int charID1, charID2, charID3, charID4;
	int sector1, sector2, sector3, sector4;
	int layerID1, layerID2, layerID3, layerID4;
	float x1, x2, x3, x4;
	float y1, y2, y3, y4;
	String model1, model2, model3, model4;
	String user1, user2, user3, user4;
	int id1, id2, id3, id4;
	int account_id = 0;
	String status;

	public String getStatus() {
		return status;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public int getId1() {
		return id1;
	}

	public void setId1(int id1) {
		this.id1 = id1;
	}

	public int getId2() {
		return id2;
	}

	public void setId2(int id2) {
		this.id2 = id2;
	}

	public CharacterList(String status1, String status2, String status3, String status4, String name1, String name2,
			String name3, String name4, int charID1, int charID2, int charID3, int charID4, int sector1, int sector2,
			int sector3, int sector4, float x1, float x2, float x3, float x4, float y1, float y2, float y3, float y4,
			String model1, String model2, String model3, String model4, String user1, String user2, String user3,
			String user4, int id1, int id2, int id3, int id4, int account_id, int layerID1, int layerID2, int layerID3,
			int layerID4, String status) {
		super();
		this.status1 = status1;
		this.status2 = status2;
		this.status3 = status3;
		this.status4 = status4;
		this.name1 = name1;
		this.name2 = name2;
		this.name3 = name3;
		this.name4 = name4;
		this.charID1 = charID1;
		this.charID2 = charID2;
		this.charID3 = charID3;
		this.charID4 = charID4;
		this.sector1 = sector1;
		this.sector2 = sector2;
		this.sector3 = sector3;
		this.sector4 = sector4;
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
		this.x4 = x4;
		this.y1 = y1;
		this.y2 = y2;
		this.y3 = y3;
		this.y4 = y4;
		this.model1 = model1;
		this.model2 = model2;
		this.model3 = model3;
		this.model4 = model4;
		this.user1 = user1;
		this.user2 = user2;
		this.user3 = user3;
		this.user4 = user4;
		this.id1 = id1;
		this.id2 = id2;
		this.id3 = id3;
		this.id4 = id4;
		this.layerID1 = layerID1;
		this.layerID2 = layerID2;
		this.layerID3 = layerID3;
		this.layerID4 = layerID4;
		this.account_id = account_id;
		this.status = status;
	}

	public String getStatus1() {
		return status1;
	}

	public void setStatus1(String status1) {
		this.status1 = status1;
	}

	public String getStatus2() {
		return status2;
	}

	public void setStatus2(String status2) {
		this.status2 = status2;
	}

	public String getStatus3() {
		return status3;
	}

	public void setStatus3(String status3) {
		this.status3 = status3;
	}

	public String getStatus4() {
		return status4;
	}

	public void setStatus4(String status4) {
		this.status4 = status4;
	}

	public int getId3() {
		return id3;
	}

	public void setId3(int id3) {
		this.id3 = id3;
	}

	public int getId4() {
		return id4;
	}

	public void setId4(int id4) {
		this.id4 = id4;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public void setName3(String name3) {
		this.name3 = name3;
	}

	public String getName1() {
		return name1;
	}

	public String getName2() {
		return name2;
	}

	public String getName3() {
		return name3;
	}

	public String getName4() {
		return name4;
	}

	public int getCharID1() {
		return charID1;
	}

	public int getCharID2() {
		return charID2;
	}

	public int getCharID3() {
		return charID3;
	}

	public int getCharID4() {
		return charID4;
	}

	public int getSector1() {
		return sector1;
	}

	public int getSector2() {
		return sector2;
	}

	public int getSector3() {
		return sector3;
	}

	public int getSector4() {
		return sector4;
	}

	public float getX1() {
		return x1;
	}

	public float getX2() {
		return x2;
	}

	public float getX3() {
		return x3;
	}

	public float getX4() {
		return x4;
	}

	public float getY1() {
		return y1;
	}

	public float getY2() {
		return y2;
	}

	public float getY3() {
		return y3;
	}

	public float getY4() {
		return y4;
	}

	public String getModel1() {
		return model1;
	}

	public String getModel2() {
		return model2;
	}

	public String getModel3() {
		return model3;
	}

	public String getModel4() {
		return model4;
	}

	public String getUser1() {
		return user1;
	}

	public String getUser2() {
		return user2;
	}

	public String getUser3() {
		return user3;
	}

	public String getUser4() {
		return user4;
	}

	public void setName4(String name4) {
		this.name4 = name4;
	}

	public void setCharID1(int charID1) {
		this.charID1 = charID1;
	}

	public void setCharID2(int charID2) {
		this.charID2 = charID2;
	}

	public void setCharID3(int charID3) {
		this.charID3 = charID3;
	}

	public void setCharID4(int charID4) {
		this.charID4 = charID4;
	}

	public void setSector1(int sector1) {
		this.sector1 = sector1;
	}

	public void setSector2(int sector2) {
		this.sector2 = sector2;
	}

	public void setSector3(int sector3) {
		this.sector3 = sector3;
	}

	public void setSector4(int sector4) {
		this.sector4 = sector4;
	}

	public void setX1(float x1) {
		this.x1 = x1;
	}

	public void setX2(float x2) {
		this.x2 = x2;
	}

	public void setX3(float x3) {
		this.x3 = x3;
	}

	public void setX4(float x4) {
		this.x4 = x4;
	}

	public void setY1(float y1) {
		this.y1 = y1;
	}

	public void setY2(float y2) {
		this.y2 = y2;
	}

	public void setY3(float y3) {
		this.y3 = y3;
	}

	public void setY4(float y4) {
		this.y4 = y4;
	}

	public void setModel1(String model1) {
		this.model1 = model1;
	}

	public void setModel2(String model2) {
		this.model2 = model2;
	}

	public void setModel3(String model3) {
		this.model3 = model3;
	}

	public void setModel4(String model4) {
		this.model4 = model4;
	}

	public void setUser1(String user1) {
		this.user1 = user1;
	}

	public void setUser2(String user2) {
		this.user2 = user2;
	}

	public void setUser3(String user3) {
		this.user3 = user3;
	}

	public void setUser4(String user4) {
		this.user4 = user4;
	}

	public int getAccountID() {
		return account_id;
	}

	public ArrayList<LocalPlayerData> getLocalPlayerData() {

		ArrayList<LocalPlayerData> list = new ArrayList<LocalPlayerData>();

		if (name1 == null) {
			return list;
		} else if (name2 == null) {

			list.add(new LocalPlayerData(charID1, id1, model1, name1, sector1, status1, user1, x1, y1, layerID1));
			return list;
		} else if (name3 == null) {
			list.add(new LocalPlayerData(charID1, id1, model1, name1, sector1, status1, user1, x1, y1, layerID1));
			list.add(new LocalPlayerData(charID2, id2, model2, name2, sector2, status2, user2, x2, y2, layerID2));
			return list;
		} else if (name4 == null) {
			list.add(new LocalPlayerData(charID1, id1, model1, name1, sector1, status1, user1, x1, y1, layerID1));
			list.add(new LocalPlayerData(charID2, id2, model2, name2, sector2, status2, user2, x2, y2, layerID2));
			list.add(new LocalPlayerData(charID3, id3, model3, name3, sector3, status3, user3, x3, y3, layerID3));
			return list;
		} else {
			list.add(new LocalPlayerData(charID1, id1, model1, name1, sector1, status1, user1, x1, y1, layerID1));
			list.add(new LocalPlayerData(charID2, id2, model2, name2, sector2, status2, user2, x2, y2, layerID2));
			list.add(new LocalPlayerData(charID3, id3, model3, name3, sector3, status3, user3, x3, y3, layerID3));
			list.add(new LocalPlayerData(charID4, id4, model4, name4, sector4, status4, user4, x4, y4, layerID4));
			return list;
		}

	}

}
