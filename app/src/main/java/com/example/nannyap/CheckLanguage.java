package com.example.nannyap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class CheckLanguage {

    private final Context context;

    public CheckLanguage(Context context) {
        this.context = context;

    }
    public void UpdateLanguage() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String language = prefs.getString("language","ar");
        CommonUsed.changeLang(context, language);
    }
    public void ChangeSavedLanguage(String lang) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language",lang);
        editor.apply();
    }

}
