package com.sony.dtv.b2b.prosettings.platform;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.UserManager;
import com.mediatek.twoworlds.tv.MtkTvConfig;
import com.mediatek.twoworlds.tv.MtkTvHotel;
import com.sony.dtv.b2b.hotelmode.HotelSettings;
import com.sony.dtv.b2b.prosettings.util.LogUtil;

/* JADX INFO: loaded from: classes.dex */
public abstract class PlatformBase {
    private static final Uri SETTINGS_PROVIDER_URI = Uri.parse("content://com.sony.dtv.settingsprovider/global");
    private static final String TAG = "PlatformBase";
    protected static ActivityManager mActivityManager;
    protected static Context mContext;
    protected static MtkTvConfig mMtkTvConfig;
    protected static MtkTvHotel mMtkTvHotel;
    protected static PackageManager mPackageManager;
    protected static UserManager mUserManager;

    private static class SettingsProviderColumn {
        private static final String KEY = "key";
        private static final String VALUE = "value";

        private SettingsProviderColumn() {
        }
    }

    public static void init(Context context, MtkTvConfig mtkTvConfig, MtkTvHotel mtkTvHotel) {
        mContext = context;
        mMtkTvConfig = mtkTvConfig;
        mMtkTvHotel = mtkTvHotel;
        mPackageManager = context.getPackageManager();
        mUserManager = (UserManager) context.getSystemService("user");
        mActivityManager = (ActivityManager) context.getSystemService("activity");
    }

    public static boolean setSettingsprovider(String str, ContentValues contentValues) {
        return setSettingsprovider(new String[]{str}, contentValues);
    }

    public static boolean setSettingsprovider(String[] strArr, ContentValues contentValues) {
        if (mContext.getContentResolver().update(SETTINGS_PROVIDER_URI, contentValues, "key=?", strArr) >= 0) {
            return true;
        }
        LogUtil.LogE(TAG, "setSettingsprovider(" + strArr + ", " + contentValues.toString() + ") fail.");
        return false;
    }

    public static String getSystemGrobalSettings(String str) throws Throwable {
        LogUtil.LogD(TAG, "getSystemGrobalSettings:");
        Cursor cursor = null;
        string = null;
        String string = null;
        if (str == null) {
            LogUtil.LogE(TAG, "getSystemGrobalSettings: key is null.");
            return null;
        }
        try {
            Cursor cursorQuery = mContext.getContentResolver().query(SETTINGS_PROVIDER_URI, new String[]{HotelSettings.VALUE}, "key=?", new String[]{str}, null);
            if (cursorQuery != null) {
                try {
                    if (cursorQuery.moveToFirst()) {
                        string = cursorQuery.getString(0);
                    }
                } catch (Throwable th) {
                    cursor = cursorQuery;
                    th = th;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            return string;
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
