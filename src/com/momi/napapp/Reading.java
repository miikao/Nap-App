package com.momi.napapp;

public class Reading {

	int _id;
	String _date;
	String _temp;
	String _ctemp;
	String _intensity;

	public Reading() {
	};

	public Reading(int id, String date, String temp, String ctemp, String intensity){
		this._id = id;
		this._date = date;
		this._temp = temp;
		this._ctemp = ctemp;
		this._intensity = intensity;
	};
	
	public Reading(String date, String temp, String ctemp, String intensity){
		this._date = date;
		this._temp = temp;
		this._ctemp = ctemp;
		this._intensity = intensity;
	};
	
	public int getID(){
		return this._id;
	};
	
	public String getTemp(){
		return this._temp;
	};
	
	public String getCTemp(){
		return this._ctemp;
	};
	
	public String getIntensity(){
		return this._intensity;
	};
	
	public void setTemp(String temp){
		this._temp = temp;
	};
	
	public void setCTemp(String ctemp){
		this._ctemp = ctemp;
	};
	
	public void setIntensity(String intensity){
		this._intensity = intensity;
	}

	@Override
	public String toString() {
		return "Reading [_id=" + _id + ", _date=" + _date + ", _temp=" + _temp
				+ ", _ctemp=" + _ctemp + ", _intensity=" + _intensity + "]";
	};
}
