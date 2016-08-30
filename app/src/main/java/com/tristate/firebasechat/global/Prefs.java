package com.tristate.firebasechat.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.tristate.firebasechat.activity.LoginActivity;


public class Prefs {


    private static final String IS_PERSISTENCE = "is_persistance";
    private static final String USER_ID = "userId";
    private static final String USERNAME = "userName";
    private static final String EMAIL = "email";
    private static final String PHOTOURI = "photoUri";


    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(context.getPackageName(), 0);
    }

    //////////////////////////////////////////////////////////////////
    public static boolean isPersisrence(Context context) {
        return getPrefs(context).getBoolean(IS_PERSISTENCE, false);
    }

    public static void setPersistence(Context context, boolean value) {
        getPrefs(context).edit().putBoolean(IS_PERSISTENCE, value).commit();
    }

    @NonNull
    public static String getUserId(Context context) {
        return getPrefs(context).getString(USER_ID, "");
    }

    public static void setUserId(Context context, String value) {
        getPrefs(context).edit().putString(USER_ID, value).commit();
    }

    @NonNull
    public static String getUSERNAME(Context context) {
        return getPrefs(context).getString(USERNAME, "");
    }

    public static void setUSERNAME(Context context, String value) {
        getPrefs(context).edit().putString(USERNAME, value).commit();
    }

    @NonNull
    public static String getEMAIL(Context context) {
        return getPrefs(context).getString(EMAIL, "");
    }

    public static void setEMAIL(Context context, String value) {
        getPrefs(context).edit().putString(EMAIL, value).commit();
    }

    public static String getPhotoUri(Context context) {
        return getPrefs(context).getString(PHOTOURI, "");

    }

    public static void setPhotoUri(Context context, String value) {
        getPrefs(context).edit().putString(PHOTOURI, value).commit();
    }
}
