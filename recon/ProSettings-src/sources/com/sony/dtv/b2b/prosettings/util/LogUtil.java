package com.sony.dtv.b2b.prosettings.util;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class LogUtil {
    private static final String NODE_MODE_DEBUG = "Debug";
    private static final String NODE_MODE_RELEASE = "Release";
    private static String mAppTag = "";
    private static String sLogMode = "Debug";

    public static void LogSetAppTag(String str) {
        mAppTag = str;
    }

    public static void LogI(String str, String str2) {
        Log.i(mAppTag + ": " + str + ":", str2);
    }

    public static void LogD(String str, String str2) {
        if (sLogMode.equals(NODE_MODE_RELEASE)) {
            return;
        }
        Log.d(mAppTag + ": " + str + ":", str2);
    }

    public static void LogW(String str, String str2) {
        Log.w(mAppTag + ": " + str + ":", str2);
    }

    public static void LogE(String str, String str2) {
        Log.e(mAppTag + ": " + str + ":", str2);
    }

    public static void LogE(String str, String str2, Exception exc) {
        Log.e(mAppTag + ": " + str + ":", str2, exc);
    }
}
