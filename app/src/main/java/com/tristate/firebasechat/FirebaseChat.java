package com.tristate.firebasechat;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;

/**
 * Created by tristate-android1 on 15/6/16.
 */
public class FirebaseChat extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
    }
}
