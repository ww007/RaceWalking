package com.example.fpl_racewaiking.vo;

public class GameInfo {

	private String Id;
	private String Type;
	private String Info;

	public GameInfo() {
		// TODO Auto-generated constructor stub
	}

	public GameInfo(String id, String type, String info) {
		super();
		Id = id;
		Type = type;
		Info = info;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getInfo() {
		return Info;
	}

	public void setInfo(String info) {
		Info = info;
	}

	@Override
	public String toString() {
		return "GameInfo [Id=" + Id + ", Type=" + Type + ", Info=" + Info + "]";
	}

}
