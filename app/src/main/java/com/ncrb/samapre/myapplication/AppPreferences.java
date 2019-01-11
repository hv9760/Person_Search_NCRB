package com.ncrb.samapre.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {

	private static final String APP_SHARED_PREFS = "ncrb.nic.in.prefs";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	public AppPreferences(Context context) {

		this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
	}

	/**
	 * get and set the user preference value
	 * */

	// Set App Preference for Appointment date to show calendar
	public void setUserDefaults(String key,String value){
		Utils.printv("shared preference "+key+"value: "+value);
		prefsEditor.putString(key, value);
		prefsEditor.commit();
	}

    // Set App Preference for Appointment date to show calendar
    public void setUserDefaults(String key,Long value){
        prefsEditor.putLong(key, value);
        prefsEditor.commit();
    }

    public void saveUserLogin(String userName, String password){

		this.prefsEditor = appSharedPrefs.edit();
		this.prefsEditor.putString("userName", userName);
		this.prefsEditor.putString("password", password);
		this.prefsEditor.apply();

	}


	public String getUserDefaults(String key){
		Utils.printv("shared preference "+key);
		return appSharedPrefs.getString(key, "0");
	}

    public long getUserDefaultsLong(String key){
        return appSharedPrefs.getLong(key, 0);
    }
	

}// end main
