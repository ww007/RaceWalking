package com.example.fpl_racewaiking.vo;

public class FoulListInfo {

	private String athlete;
	private String content;
	private String time;
	private String upload;

	public FoulListInfo() {
		// TODO Auto-generated constructor stub
	}

	public FoulListInfo(String athlete, String content, String time, String upload) {
		super();
		this.athlete = athlete;
		this.content = content;
		this.time = time;
		this.upload = upload;
	}

	public String getAthlete() {
		return athlete;
	}

	public void setAthlete(String athlete) {
		this.athlete = athlete;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUpload() {
		return upload;
	}

	public void setUpload(String upload) {
		this.upload = upload;
	}

	@Override
	public String toString() {
		return "FoulListInfo [athlete=" + athlete + ", content=" + content + ", time=" + time + ", upload=" + upload
				+ "]";
	}

}
