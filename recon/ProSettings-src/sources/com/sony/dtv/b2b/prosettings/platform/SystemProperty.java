package com.sony.dtv.b2b.prosettings.platform;

import com.sony.dtv.b2b.prosettings.util.LogUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class SystemProperty extends PlatformBase {
    private static final String TAG = "SystemProperty";

    public static String invokeSystemPropertiesGet(String str, String str2) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            try {
                Method method = cls.getMethod("get", String.class, String.class);
                if (method == null) {
                    LogUtil.LogE(TAG, "invokeSystemPropertiesGet: SystemProperties.get == null");
                    return null;
                }
                try {
                    return (String) method.invoke(cls, str, str2);
                } catch (IllegalAccessException e) {
                    LogUtil.LogE(TAG, "invokeSystemPropertiesGet: IllegalAccessException", e);
                    return "";
                } catch (IllegalArgumentException e2) {
                    LogUtil.LogE(TAG, "invokeSystemPropertiesGet: IllegalArgumentException", e2);
                    return "";
                } catch (InvocationTargetException e3) {
                    LogUtil.LogE(TAG, "invokeSystemPropertiesGet: InvocationTargetException; " + e3.getCause().getMessage());
                    return "";
                }
            } catch (NoSuchMethodException e4) {
                LogUtil.LogE(TAG, "invokeSystemPropertiesGet: SystemProperries.get() method is not found", e4);
                return "";
            }
        } catch (ClassNotFoundException unused) {
            LogUtil.LogE(TAG, "invokeSystemPropertiesGet: SystemProperties class is not found");
            return "";
        }
    }

    public static boolean invokeSystemPropertiesSet(String str, String str2) {
        boolean z;
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            try {
                Method method = cls.getMethod("set", String.class, String.class);
                if (method == null) {
                    LogUtil.LogE(TAG, "invokeSystemPropertiesSet: SystemProperties.set == null");
                    z = false;
                } else {
                    z = true;
                }
                try {
                    method.invoke(cls, str, str2);
                    return z;
                } catch (IllegalAccessException e) {
                    LogUtil.LogE(TAG, "invokeSystemPropertiesSet: IllegalAccessException", e);
                    return false;
                } catch (IllegalArgumentException e2) {
                    LogUtil.LogE(TAG, "invokeSystemPropertiesSet: IllegalArgumentException", e2);
                    return false;
                } catch (InvocationTargetException e3) {
                    LogUtil.LogE(TAG, "invokeSystemPropertiesSet: InvocationTargetException; " + e3.getCause().getMessage());
                    return false;
                }
            } catch (NoSuchMethodException unused) {
                LogUtil.LogE(TAG, "invokeSystemPropertiesSet: SystemProperries.set() method is not found");
                return false;
            }
        } catch (ClassNotFoundException unused2) {
            LogUtil.LogE(TAG, "invokeSystemPropertiesSet: SystemProperties class is not found");
            return false;
        }
    }
}
