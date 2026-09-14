package com.sony.dtv.b2b.prosettings.platform;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: loaded from: classes.dex */
public class Package extends PlatformBase {
    private static final int ID_OWNER = 0;
    private static final String TAG = "Package";

    public static boolean getAppPackageEnabled(String str) {
        try {
            return mPackageManager.getApplicationInfo(str, 0).enabled;
        } catch (PackageManager.NameNotFoundException unused) {
            LogUtil.LogE(TAG, "getAppPackageEnabled: Package Not Found: " + str);
            return false;
        }
    }

    public static void disableAppPackageIfEnabled(String str) {
        try {
            if (mPackageManager.getApplicationInfo(str, 0).enabled) {
                disableAppPackage(str);
            } else {
                LogUtil.LogD(TAG, "Package Disabled: " + str);
            }
        } catch (PackageManager.NameNotFoundException unused) {
            LogUtil.LogE(TAG, "Package Not Found: " + str);
        }
    }

    public static void enableAppPackageIfChanged(String str) {
        try {
            if (!mPackageManager.getApplicationInfo(str, 0).enabled) {
                enableAppPackage(str);
            } else {
                LogUtil.LogD(TAG, "Package Enabled: " + str);
            }
        } catch (PackageManager.NameNotFoundException unused) {
            LogUtil.LogE(TAG, "Package Not Found: " + str);
        }
    }

    public static void disableAppPackage(String str) {
        LogUtil.LogD(TAG, "DisableAppPackage: " + str);
        setAppPackageEnableMode(str, 2);
    }

    public static void enableAppPackage(String str) {
        LogUtil.LogD(TAG, "EnableAppPackage: " + str);
        setAppPackageEnableMode(str, 1);
    }

    public static boolean setAppPackageEnableMode(String str, int i) {
        return setAppPackageEnableMode(str, i, 1);
    }

    public static boolean setAppPackageEnableMode(String str, int i, int i2) {
        LogUtil.LogD(TAG, "setAppPackageEnableMode(" + str + ", " + i + ")");
        try {
            mPackageManager.setApplicationEnabledSetting(str, i, i2);
            flushPackageRestrictionsAsUser();
            return true;
        } catch (IllegalArgumentException unused) {
            LogUtil.LogE(TAG, "Not Found: " + str);
            return false;
        }
    }

    public static int getAppPackageEnableMode(String str) throws IllegalArgumentException {
        return mPackageManager.getApplicationEnabledSetting(str);
    }

    public static boolean setApplicationHiddenSettingAsUser(String str, boolean z) {
        LogUtil.LogD(TAG, "setApplicationHiddenSettingAsUser(" + str + ", " + z + ")");
        try {
            return ((Boolean) mPackageManager.getClass().getMethod("setApplicationHiddenSettingAsUser", String.class, Boolean.TYPE, UserHandle.class).invoke(mPackageManager, str, Boolean.valueOf(z), mUserManager.getUserForSerialNumber(0L))).booleanValue();
        } catch (IllegalAccessException unused) {
            LogUtil.LogE(TAG, "Illegal Access: setApplicationHiddenSettingAsUser");
            return false;
        } catch (NoSuchMethodException unused2) {
            LogUtil.LogE(TAG, "No Such Method: setApplicationHiddenSettingAsUser");
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean getApplicationHiddenSettingAsUser(String str) {
        LogUtil.LogD(TAG, "getApplicationHiddenSettingAsUser(" + str + ")");
        try {
            return ((Boolean) mPackageManager.getClass().getMethod("getApplicationHiddenSettingAsUser", String.class, UserHandle.class).invoke(mPackageManager, str, mUserManager.getUserForSerialNumber(0L))).booleanValue();
        } catch (IllegalAccessException unused) {
            LogUtil.LogE(TAG, "Illegal Access: getApplicationHiddenSettingAsUser");
            return false;
        } catch (NoSuchMethodException unused2) {
            LogUtil.LogE(TAG, "No Such Method: getApplicationHiddenSettingAsUser");
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean setComponentEnabledSetting(String str, String str2, int i, int i2) {
        LogUtil.LogD(TAG, "setComponentEnabledSetting(" + str2 + ", " + i + ", " + i2 + ")");
        StringBuilder sb = new StringBuilder();
        sb.append("SetAppComponent: ");
        sb.append(str2);
        LogUtil.LogD(TAG, sb.toString());
        ComponentName componentName = new ComponentName(str, str2);
        try {
            mPackageManager.setComponentEnabledSetting(componentName, i, i2);
            return true;
        } catch (IllegalArgumentException unused) {
            LogUtil.LogI(TAG, "Not Found: " + componentName);
            return false;
        }
    }

    public static int getComponentEnabledSetting(String str, String str2) {
        LogUtil.LogD(TAG, "getComponentEnabledSetting(" + str + ", " + str2 + ")");
        StringBuilder sb = new StringBuilder();
        sb.append("GetAppComponent: ");
        sb.append(str2);
        LogUtil.LogD(TAG, sb.toString());
        ComponentName componentName = new ComponentName(str, str2);
        try {
            return mPackageManager.getComponentEnabledSetting(componentName);
        } catch (IllegalArgumentException unused) {
            LogUtil.LogI(TAG, "Not Found: " + componentName);
            return -1;
        }
    }

    private static boolean flushPackageRestrictionsAsUser() {
        try {
            mPackageManager.getClass().getMethod("flushPackageRestrictionsAsUser", Integer.TYPE).invoke(mPackageManager, 0);
            return true;
        } catch (IllegalAccessException unused) {
            LogUtil.LogE(TAG, "Illegal Access: flushPackageRestrictionsAsUser");
            return false;
        } catch (NoSuchMethodException unused2) {
            LogUtil.LogE(TAG, "No Such Method: flushPackageRestrictionsAsUser");
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void clearUserData(String str) {
        LogUtil.LogD(TAG, "ClearUserData: " + str);
        try {
            mContext.getPackageManager().getPackageInfo(str, 128);
            forceStopPackage(str);
            clearApplicationUserData(str);
        } catch (PackageManager.NameNotFoundException unused) {
            LogUtil.LogD(TAG, "clearUserData(" + str + ") : package not found.");
        }
    }

    private static boolean forceStopPackage(String str) {
        try {
            mActivityManager.getClass().getMethod("forceStopPackage", String.class).invoke(mActivityManager, str);
            return true;
        } catch (IllegalAccessException unused) {
            LogUtil.LogE(TAG, "Illegal Access: forceStopPackage");
            return false;
        } catch (NoSuchMethodException unused2) {
            LogUtil.LogE(TAG, "No Such Method: forceStopPackage");
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean clearApplicationUserData(String str) {
        try {
            mPackageManager.getClass().getMethod("clearApplicationUserData", String.class, Class.forName("android.content.pm.IPackageDataObserver")).invoke(mPackageManager, str, null);
            return true;
        } catch (ClassNotFoundException unused) {
            LogUtil.LogE(TAG, "Class Not Found: android.content.pm.IPackageDataObserver");
            return false;
        } catch (IllegalAccessException unused2) {
            LogUtil.LogE(TAG, "Illegal Access: clearApplicationUserData");
            return false;
        } catch (NoSuchMethodException unused3) {
            LogUtil.LogE(TAG, "No Such Method: clearApplicationUserData");
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }
}
