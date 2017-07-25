package com.example.fpl_racewaiking.vo;

public class LoginInfo {
	private String strUserName;
	private String strPassWord;
	private String strStandNo;
	private String strNumber;
	private String strMacInfo;

	public LoginInfo() {
		// TODO Auto-generated constructor stub
	}

	public LoginInfo(String strUserName, String strPassWord, String strStandNo, String strNumber, String strMacInfo) {
		super();
		this.strUserName = strUserName;
		this.strPassWord = strPassWord;
		this.strStandNo = strStandNo;
		this.strNumber = strNumber;
		this.strMacInfo = strMacInfo;
	}

	public String getStrUserName() {
		return strUserName;
	}

	public void setStrUserName(String strUserName) {
		this.strUserName = strUserName;
	}

	public String getStrPassWord() {
		return strPassWord;
	}

	public void setStrPassWord(String strPassWord) {
		this.strPassWord = strPassWord;
	}

	public String getStrStandNo() {
		return strStandNo;
	}

	public void setStrStandNo(String strStandNo) {
		this.strStandNo = strStandNo;
	}

	public String getStrNumber() {
		return strNumber;
	}

	public void setStrNumber(String strNumber) {
		this.strNumber = strNumber;
	}

	public String getStrMacInfo() {
		return strMacInfo;
	}

	public void setStrMacInfo(String strMacInfo) {
		this.strMacInfo = strMacInfo;
	}

	@Override
	public String toString() {
		return "LoginInfo [strUserName=" + strUserName + ", strPassWord=" + strPassWord + ", strStandNo=" + strStandNo
				+ ", strNumber=" + strNumber + ", strMacInfo=" + strMacInfo + "]";
	}

}
