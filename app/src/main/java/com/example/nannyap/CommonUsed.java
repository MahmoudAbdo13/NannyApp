package com.example.nannyap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.example.nannyap.model.NannyModel;
import com.example.nannyap.model.ParentModel;

import java.util.Locale;


public class CommonUsed {

    public static ParentModel currentOnlineParent;
    public static NannyModel currentOnlineNanny;
    public static String NannyComments;
    public static final String NannyRequests = "Nanny Requests";
    public static final String Nannies = "Nannies";
    public static final String Parents = "Parents";
    public static final String Comment = "Comment";
    public static final String Rating = "Rating";
    public static final String BooknigRequests = "BooknigRequests";
    public static final String Booknigs = "Booknigs";
    public static final String Status = "status";
    public static final String Chat = "Chat";
    public static String ParentID;
    public static String NannyID;
    public static String NannyName;
    public static String NannyImageUrl;

    public static boolean isConnection(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo WiFiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo MobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (WiFiConn != null && WiFiConn.isConnected() || (MobileConn != null && MobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    public static void changeLang(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        persistLanguage(context, lang);
    }

    public static void getLanguage(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        persistLanguage(context, lang);
    }

    private static void persistLanguage(Context context, String language) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", language);
        editor.apply();
    }

}
