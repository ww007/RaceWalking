package com.example.fpl_racewaiking.vo;

public class LoginReturns {
	private String Flag;
	private String GMID;
	private String CHNName;
	private String ENGName;
	private String StandNo;
	private String EndUpTime;
	private String Note;

	public LoginReturns() {
		// TODO Auto-generated constructor stub
	}

	public LoginReturns(String flag, String gMID, String cHNName, String eNGName, String standNo, String endUpTime,
			String note) {
		super();
		Flag = flag;
		GMID = gMID;
		CHNName = cHNName;
		ENGName = eNGName;
		StandNo = standNo;
		EndUpTime = endUpTime;
		Note = note;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}

	public String getGMID() {
		return GMID;
	}

	public void setGMID(String gMID) {
		GMID = gMID;
	}

	public String getCHNName() {
		return CHNName;
	}

	public void setCHNName(String cHNName) {
		CHNName = cHNName;
	}

	public String getENGName() {
		return ENGName;
	}

	public void setENGName(String eNGName) {
		ENGName = eNGName;
	}

	public String getStandNo() {
		return StandNo;
	}

	public void setStandNo(String standNo) {
		StandNo = standNo;
	}

	public String getEndUpTime() {
		return EndUpTime;
	}

	public void setEndUpTime(String endUpTime) {
		EndUpTime = endUpTime;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	@Override
	public String toString() {
		return "LoginReturns [Flag=" + Flag + ", GMID=" + GMID + ", CHNName=" + CHNName + ", ENGName=" + ENGName
				+ ", StandNo=" + StandNo + ", EndUpTime=" + EndUpTime + ", Note=" + Note + "]";
	}

}
