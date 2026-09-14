package com.sony.dtv.b2b.prosettings.platform;

import com.sony.dtv.b2b.hotelmode.HotelSettings;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SoundPictureSetting extends PlatformBase {
    private static final int MTK_SET_CONFIG_RETRY_COUNT = 3;
    static final String TAG = "SoundPictureSetting";

    public static boolean setSoundPictureSetting(ArrayList<String> arrayList) {
        int configValueWrapper;
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            try {
                JSONObject jSONObject = new JSONObject(it.next());
                String strOptString = jSONObject.optString("input");
                String strOptString2 = jSONObject.optString("key");
                String strOptString3 = jSONObject.optString(HotelSettings.VALUE);
                LogUtil.LogD(TAG, "setSoundPictureSetting: input=" + strOptString + ", key=" + strOptString2 + ", value=" + strOptString3);
                if (strOptString.isEmpty() || strOptString2.isEmpty()) {
                    LogUtil.LogE(TAG, "setSoundPictureSetting: input or key is empty");
                    return false;
                }
                try {
                    int i = Integer.parseInt(strOptString3);
                    if (strOptString.equals("direct")) {
                        configValueWrapper = setConfigValueDirectWrapper(strOptString2, i);
                    } else {
                        configValueWrapper = setConfigValueWrapper(strOptString2, i, strOptString);
                    }
                    if (configValueWrapper != 0) {
                        if (configValueWrapper == -2 || configValueWrapper == -1) {
                            LogUtil.LogE(TAG, "setSoundPictureSetting: Fail to set: Ignore. result=" + configValueWrapper);
                            return false;
                        }
                        LogUtil.LogE(TAG, "setSoundPictureSetting: Fail to set: result=" + configValueWrapper);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    LogUtil.LogE(TAG, "setSoundPictureSetting: 'value' is not number", e);
                    return false;
                }
            } catch (JSONException e2) {
                LogUtil.LogE(TAG, "setSoundPictureSetting: JSONException", e2);
                return false;
            }
        }
        return true;
    }

    private static int setConfigValueWrapper(String str, int i, String str2) {
        int configValue = 1;
        for (int i2 = 1; i2 <= 3; i2++) {
            configValue = mMtkTvConfig.setConfigValue(str, i);
            if (configValue == 0) {
                int configValue2 = mMtkTvConfig.getConfigValue(str);
                if (configValue2 == i) {
                    break;
                }
                LogUtil.LogE(TAG, "setConfigValueWrapper: count=" + i2 + " cfgId=" + str + " input=" + str2 + " setValue=" + i + " :Not set value. getValue=" + configValue2);
            } else {
                LogUtil.LogE(TAG, "setConfigValueWrapper: count=" + i2 + " cfgId=" + str + " input=" + str2 + " setValue=" + i + " :setConfigValue is failed. return=" + configValue);
            }
        }
        return configValue;
    }

    private static int setConfigValueDirectWrapper(String str, int i) {
        int configValueDirect = 1;
        for (int i2 = 1; i2 <= 3; i2++) {
            configValueDirect = mMtkTvConfig.setConfigValueDirect(str, i);
            if (configValueDirect == 0) {
                int configValue = mMtkTvConfig.getConfigValue(str);
                if (configValue == i) {
                    break;
                }
                LogUtil.LogE(TAG, "setConfigValueDirectWrapper: count=" + i2 + " cfgId=" + str + " setValue=" + i + " :Not set value. getValue=" + configValue);
            } else {
                LogUtil.LogE(TAG, "setConfigValueDirectWrapper: count=" + i2 + " cfgId=" + str + " setValue=" + i + " :setConfigValueDirect is failed. return=" + configValueDirect);
            }
        }
        return configValueDirect;
    }
}
