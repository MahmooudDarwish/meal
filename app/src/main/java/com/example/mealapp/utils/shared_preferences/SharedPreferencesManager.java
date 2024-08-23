package com.example.mealapp.utils.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mealapp.utils.constants.ConstantKeys;

public class SharedPreferencesManager {

    private static SharedPreferencesManager instance;
    private final SharedPreferences userDataSharedPref;
    private final SharedPreferences userSettingsSharedPref;

    private SharedPreferencesManager(Context context) {
        userDataSharedPref = context.getSharedPreferences(ConstantKeys.USER_DATA, Context.MODE_PRIVATE);
        userSettingsSharedPref = context.getSharedPreferences(ConstantKeys.USER_SETTINGS, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context.getApplicationContext());
        }
        return instance;
    }

    public void savePreference(String key, String value, boolean fromUserData) {
        SharedPreferences.Editor editor = (fromUserData ? userDataSharedPref : userSettingsSharedPref).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getPreference(String key, boolean fromUserData) {
        return (fromUserData ? userDataSharedPref : userSettingsSharedPref).getString(key, "");
    }

    public void savePreference(String key, boolean value, boolean fromUserData) {
        SharedPreferences.Editor editor = (fromUserData ? userDataSharedPref : userSettingsSharedPref).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getPreference(String key, boolean defaultValue, boolean fromUserData) {
        return (fromUserData ? userDataSharedPref : userSettingsSharedPref).getBoolean(key, defaultValue);
    }

    public void clearPreferences(boolean fromUserData) {
        SharedPreferences.Editor editor = (fromUserData ? userDataSharedPref : userSettingsSharedPref).edit();
        editor.clear();
        editor.apply();
    }

    public void clearAllPreferences() {
        clearPreferences(true);
        clearPreferences(false);
    }
}
