package com.ncrb.samapre.myapplication;

public class PropertySearchInfo {

	//private String FIR_NO;
	//private String GD_No;
	//private String Reg_Owner;
	private String PROP_REG_NUM;
	private String MV_Type;
	private String REGISTRATION_NO;
	private String CHASSIS_NO;
	private String ENGINE_NO;
	private String Full_FIR_NUMBER;
	//private String State;
	//private String district;
	//private String ps;
	//private String propertyNature;
	private String mvModel;
	//private String mvMake;
	//private String mvColor;
	//private String state_cd;
	//private String district_cd;
	//private String ps_cd;


	public PropertySearchInfo(String PROP_REG_NUM, String MV_Type, String REGISTRATION_NO, String CHASSIS_NO, String ENGINE_NO, String full_FIR_NUMBER, String mvModel) {
		this.PROP_REG_NUM=PROP_REG_NUM;
		this.MV_Type = MV_Type;
		this.REGISTRATION_NO = REGISTRATION_NO;
		this.CHASSIS_NO = CHASSIS_NO;
		this.ENGINE_NO = ENGINE_NO;
		Full_FIR_NUMBER = full_FIR_NUMBER;
		this.mvModel = mvModel;
	}

	public String getPROP_REG_NUM() {
		return PROP_REG_NUM;
	}

	public void setPROP_REG_NUM(String PROP_REG_NUM) {
		this.PROP_REG_NUM = PROP_REG_NUM;
	}

	public String getMV_Type() {
		return MV_Type;
	}

	public void setMV_Type(String MV_Type) {
		this.MV_Type = MV_Type;
	}

	public String getREGISTRATION_NO() {
		return REGISTRATION_NO;
	}

	public void setREGISTRATION_NO(String REGISTRATION_NO) {
		this.REGISTRATION_NO = REGISTRATION_NO;
	}

	public String getCHASSIS_NO() {
		return CHASSIS_NO;
	}

	public void setCHASSIS_NO(String CHASSIS_NO) {
		this.CHASSIS_NO = CHASSIS_NO;
	}

	public String getENGINE_NO() {
		return ENGINE_NO;
	}

	public void setENGINE_NO(String ENGINE_NO) {
		this.ENGINE_NO = ENGINE_NO;
	}

	public String getFull_FIR_NUMBER() {
		return Full_FIR_NUMBER;
	}

	public void setFull_FIR_NUMBER(String full_FIR_NUMBER) {
		Full_FIR_NUMBER = full_FIR_NUMBER;
	}

	public String getMvModel() {
		return mvModel;
	}

	public void setMvModel(String mvModel) {
		this.mvModel = mvModel;
	}
}