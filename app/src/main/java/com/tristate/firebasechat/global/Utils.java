package com.tristate.firebasechat.global;


import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class Utils {

    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static void hidekeybord(Context homeContext, EditText edittext_Search) {
        InputMethodManager imm = (InputMethodManager) homeContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edittext_Search.getWindowToken(), 0);
    }
    public static long getCurrentTimeStamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        return tsLong;
    }
    public  static String getLastFromTimestamp(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);

            /*calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
            Date currenTimeZone = calendar.getTime();
            SimpleDateFormat sdfTmp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
            sdfTmp.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date lastSeen = sdf.parse(sdfTmp.format(currenTimeZone));
            calendar.setTimeInMillis(lastSeen.getTime());*/

            Calendar cal = Calendar.getInstance();
            Date date1 = new Date(cal.getTimeInMillis());
            Date date2 = new Date(calendar.getTimeInMillis());
            long diff = date1.getTime() - date2.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            long months = 0;
            if (days > 30) {
                months = days / 30;
            }
            long years = 0;
            if (months > 11) {
                years = months / 12;
            }

            if (years == 1) {
                return "1 Year ago";
            } else if (years > 1) {
                return years + " Years ago";
            } else if (months == 1) {
                return "1 Month ago";
            } else if (months > 1) {
                return months + " Months ago";
            } else if (days == 1) {
                return "1 Day ago";
            } else if (days > 1) {
                return days + " Days ago";
            } else if (hours == 1) {
                return "1 Hour ago";
            } else if (hours > 1) {
                return hours + " Hours ago";
            } else if (minutes == 1) {
                return "1 Minute ago";
            } else if (minutes > 1) {
                return minutes + " Minutes ago";
            } else if (seconds == 1) {
                return "1 Second ago";
            } else if (seconds <= 0) {
                return "0 Second ago";
            } else {
                return seconds + " Seconds ago";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



}
