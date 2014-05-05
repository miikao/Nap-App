package com.momi.napapp;

public class Reading {

	int _id;
	String _date;
	String _location;
	String _sound;
	String _temp;
	String _ctemp;
	String _intensity;

	public Reading() {
	};

	public Reading(int id, String date, String location, String sound, String temp, String ctemp, String intensity){
		this._id = id;
		this._date = date;
		this._location = location;
		this._sound = sound;
		this._temp = temp;
		this._ctemp = ctemp;
		this._intensity = intensity;
	};
	
	public Reading(String date, String location, String sound, String temp, String ctemp, String intensity){
		this._date = date;
		this._location = location;
		this._sound = sound;
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
	public String get_date() {
		return _date;
	}

	public void set_date(String _date) {
		this._date = _date;
	}
	public String get_location() {
		return _location;
	}

	public void set_location(String _location) {
		this._location = _location;
	}

	public String get_sound() {
		return _sound;
	}

	public void set_sound(String _sound) {
		this._sound = _sound;
	}

	@Override
	public String toString() {
		return "Reading [_id=" + _id + ", _date=" + _date + ", _location="
				+ _location + ", _sound=" + _sound + ", _temp=" + _temp
				+ ", _ctemp=" + _ctemp + ", _intensity=" + _intensity + "]";
	}



}
