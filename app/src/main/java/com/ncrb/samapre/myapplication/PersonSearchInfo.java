package com.ncrb.samapre.myapplication;

public class PersonSearchInfo {


	private String FULL_NAME;

	private String ALIAS1;

	private String RELATION_TYPE;
	private String RELATIVE_NAME;

	private String REGISTRATION_NUM;

	private String accused_srno;

	public PersonSearchInfo(String FULL_NAME, String ALIAS1, String RELATION_TYPE, String RELATIVE_NAME, String REGISTRATION_NUM, String accused_srno) {
		this.FULL_NAME = FULL_NAME;
		this.ALIAS1 = ALIAS1;
		this.RELATION_TYPE = RELATION_TYPE;
		this.RELATIVE_NAME = RELATIVE_NAME;
		this.REGISTRATION_NUM = REGISTRATION_NUM;
		this.accused_srno = accused_srno;
	}

	public String getFULL_NAME() {
		return FULL_NAME;
	}

	public void setFULL_NAME(String FULL_NAME) {
		this.FULL_NAME = FULL_NAME;
	}

	public String getALIAS1() {
		return ALIAS1;
	}

	public void setALIAS1(String ALIAS1) {
		this.ALIAS1 = ALIAS1;
	}

	public String getRELATION_TYPE() {
		return RELATION_TYPE;
	}

	public void setRELATION_TYPE(String RELATION_TYPE) {
		this.RELATION_TYPE = RELATION_TYPE;
	}

	public String getRELATIVE_NAME() {
		return RELATIVE_NAME;
	}

	public void setRELATIVE_NAME(String RELATIVE_NAME) {
		this.RELATIVE_NAME = RELATIVE_NAME;
	}

	public String getREGISTRATION_NUM() {
		return REGISTRATION_NUM;
	}

	public void setREGISTRATION_NUM(String REGISTRATION_NUM) {
		this.REGISTRATION_NUM = REGISTRATION_NUM;
	}

	public String getAccused_srno() {
		return accused_srno;
	}

	public void setAccused_srno(String accused_srno) {
		this.accused_srno = accused_srno;
	}
}