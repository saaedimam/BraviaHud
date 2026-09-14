package com.sony.dtv.b2b.prosettings.platform;

import android.content.Context;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import com.mediatek.twoworlds.tv.MtkTvInputSource;
import com.mediatek.twoworlds.tv.MtkTvInputSourceBase;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class CustomLabels extends PlatformBase {
    private static final String HW_DEVICE_ID_PATTERN = "HW(.)$";
    private static final int MTK_INVALID_DEVICE_ID = -1;
    private static final String PREFIX_HARDWARE_DEVICE = "HW";
    private static final String TAG = "CustomLabels";
    private static MtkTvInputSource mInputSource;
    private static TvInputManager mTvInputManager;

    private static int getDeviceId(String str) {
        if (str == null) {
            return -1;
        }
        Matcher matcher = Pattern.compile(HW_DEVICE_ID_PATTERN).matcher(str);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        LogUtil.LogE(TAG, "getDeviceId:: does not match" + str);
        return -1;
    }

    private static int getSharedPinDeviceId(String str) {
        if (str == null) {
            return -1;
        }
        int deviceId = getDeviceId(str);
        List<MtkTvInputSourceBase.ShareInputPinRecord> sharePinInputs = mInputSource.getSharePinInputs();
        if (sharePinInputs == null) {
            return -1;
        }
        for (MtkTvInputSourceBase.ShareInputPinRecord shareInputPinRecord : sharePinInputs) {
            if (deviceId == shareInputPinRecord.getInputId()) {
                int sharedPinInputId = shareInputPinRecord.getSharedPinInputId();
                if (sharedPinInputId == -1) {
                    return -1;
                }
                return sharedPinInputId;
            }
        }
        return -1;
    }

    private static void setCustomLabelNative(String str, String str2) {
        int inputLabelIdx;
        int inputLabelIdx2;
        if (mTvInputManager.getTvInputInfo(str) == null) {
            return;
        }
        int deviceId = getDeviceId(str);
        if (deviceId == -1) {
            LogUtil.LogD(TAG, "setCustomLabelNative: can not get actual device id");
            return;
        }
        LogUtil.LogD(TAG, "setCutomLabelNative: inputId=" + str + ", label=" + str2);
        if (str2 != null) {
            inputLabelIdx = mInputSource.setInputLabelUserDefName(deviceId, str2);
        } else {
            inputLabelIdx = mInputSource.setInputLabelIdx(deviceId, 0);
        }
        LogUtil.LogD(TAG, "setCutomLabelNative: set label to native [" + inputLabelIdx + "]");
        int sharedPinDeviceId = getSharedPinDeviceId(str);
        if (sharedPinDeviceId != -1) {
            if (str2 != null) {
                inputLabelIdx2 = mInputSource.setInputLabelUserDefName(sharedPinDeviceId, str2);
            } else {
                inputLabelIdx2 = mInputSource.setInputLabelIdx(sharedPinDeviceId, 0);
            }
            LogUtil.LogD(TAG, "setCutomLabelNative: set shared pin label to native [" + inputLabelIdx2 + "]");
        }
    }

    public static boolean setCustomLabels(HashMap<String, String> map) {
        if (mInputSource == null) {
            mInputSource = MtkTvInputSource.getInstance();
            mTvInputManager = (TvInputManager) mContext.getSystemService("tv_input");
        }
        try {
            Class<?> cls = Class.forName("android.media.tv.TvInputInfo$TvInputSettings");
            try {
                Method method = cls.getMethod("putCustomLabels", Context.class, Map.class, Integer.TYPE);
                if (method == null) {
                    LogUtil.LogE(TAG, "setCustomLabel: TvInputSettings.putCustomLabels is null.");
                    return false;
                }
                try {
                    Iterator<String> it = map.keySet().iterator();
                    while (true) {
                        String str = null;
                        if (!it.hasNext()) {
                            break;
                        }
                        String next = it.next();
                        String str2 = map.get(next);
                        if (!str2.isEmpty()) {
                            str = str2;
                        }
                        int deviceId = getDeviceId(next);
                        LogUtil.LogD(TAG, "SetCustomLabel: input_id=" + next + ", device_id=" + deviceId + ", label = " + str);
                        mInputSource.setInputLabelUserDefName(deviceId, str);
                    }
                    LogUtil.LogD(TAG, "SetCustomLabel: pass #1");
                    HashMap map2 = new HashMap();
                    for (String str3 : map.keySet()) {
                        String str4 = map.get(str3);
                        if (!str4.isEmpty()) {
                            map2.put(str3, str4);
                        }
                    }
                    method.invoke(cls, mContext, map2, 0);
                    for (TvInputInfo tvInputInfo : mTvInputManager.getTvInputList()) {
                        if (tvInputInfo.getType() != 0) {
                            String id = tvInputInfo.getId();
                            if (map.containsKey(id)) {
                                String str5 = map.get(id);
                                if (str5.isEmpty()) {
                                    str5 = null;
                                }
                                setCustomLabelNative(id, str5);
                                LogUtil.LogD(TAG, "setCustomLabel: input_id=" + id + ", label=" + str5);
                            }
                        }
                    }
                    return true;
                } catch (IllegalAccessException e) {
                    LogUtil.LogE(TAG, "setCustomLabel: IllegalAccessException.", e);
                    return false;
                } catch (IllegalArgumentException e2) {
                    LogUtil.LogE(TAG, "setCustomLabel: IllegalArgumentException.", e2);
                    return false;
                } catch (InvocationTargetException e3) {
                    LogUtil.LogE(TAG, "setCustomLabel: InvocationTargetException.", e3);
                    Throwable cause = e3.getCause();
                    if (cause == null) {
                        LogUtil.LogE(TAG, "setCustomLabel: cause is null.");
                        return false;
                    }
                    LogUtil.LogE(TAG, "setCustomLabel: cause = " + cause.getMessage());
                    return false;
                }
            } catch (NoSuchMethodException e4) {
                LogUtil.LogE(TAG, "setCustomLabel: NoSuchMethodException.", e4);
                return false;
            }
        } catch (ClassNotFoundException e5) {
            LogUtil.LogE(TAG, "setCustomLabel: ClassNotFoundException.", e5);
            return false;
        }
    }
}
