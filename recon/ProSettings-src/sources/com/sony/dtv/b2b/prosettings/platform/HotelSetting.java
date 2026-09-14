package com.sony.dtv.b2b.prosettings.platform;

import com.sony.dtv.b2b.hotelmode.HotelSettings;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import com.sony.dtv.tvinput.provider.SonyTvContract;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class HotelSetting extends PlatformBase {
    private static final String TAG = "HotelSetting";

    public static boolean setHotelSetting(ArrayList<String> arrayList) {
        try {
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                JSONObject jSONObject = new JSONObject(it.next());
                String string = jSONObject.getString(HotelSettings.NAME);
                String string2 = jSONObject.getString(SonyTvContract.Channels.TYPE);
                String string3 = jSONObject.getString(HotelSettings.VALUE);
                switch (string2) {
                    case "STRING":
                        if (!HotelSettings.putString(mContext.getContentResolver(), string, string3)) {
                            LogUtil.LogE(TAG, "Execute: Failed to call HotelSettings.putString: name=" + string + ", value=" + string3 + ", type=" + string2);
                            return false;
                        }
                        break;
                        break;
                    case "INTEGER":
                        if (!HotelSettings.putInt(mContext.getContentResolver(), string, Integer.parseInt(string3))) {
                            LogUtil.LogE(TAG, "Execute: Failed to call HotelMenuSettings.putInt: name=" + string + ", value=" + string3 + ", type=" + string2);
                            return false;
                        }
                        break;
                        break;
                    case "LONG":
                        if (!HotelSettings.putLong(mContext.getContentResolver(), string, Long.parseLong(string3))) {
                            LogUtil.LogE(TAG, "setHotelSetting:: Failed to call HotelMenuSettings.putLong: name=" + string + ", value=" + string3 + ", type=" + string2);
                            return false;
                        }
                        break;
                        break;
                    case "FLOAT":
                        if (!HotelSettings.putFloat(mContext.getContentResolver(), string, Float.parseFloat(string3))) {
                            LogUtil.LogE(TAG, "setHotelSetting: Failed to call HotelMenuSettings.putFloat: name=" + string + ", value=" + string3 + ", type=" + string2);
                            return false;
                        }
                        break;
                        break;
                    default:
                        LogUtil.LogE(TAG, "setHotelSetting: type is invalid: name=" + string + ", value=" + string3 + ", type=" + string2);
                        return false;
                }
            }
        } catch (JSONException e) {
            LogUtil.LogE(TAG, "setHotelSetting: JSONException", e);
        }
        return true;
    }

    public static boolean setHotelMenuSetting(ArrayList<String> arrayList) {
        boolean z = true;
        try {
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                JSONObject jSONObject = new JSONObject(it.next());
                String string = jSONObject.getString(HotelSettings.NAME);
                String string2 = jSONObject.getString(SonyTvContract.Channels.TYPE);
                String string3 = jSONObject.getString(HotelSettings.VALUE);
                byte b = -1;
                if (string2.hashCode() == -1838656495 && string2.equals("STRING")) {
                    b = 0;
                }
                if (b == 0) {
                    boolean zPutString = HotelSettings.putString(mContext.getContentResolver(), string, string3);
                    if (zPutString) {
                        z = zPutString;
                    } else {
                        try {
                            LogUtil.LogE(TAG, "Failed to call HotelMenuSettings.putString: name=" + string + ", value=" + string3 + ", type=" + string2);
                            return false;
                        } catch (JSONException e) {
                            e = e;
                            z = zPutString;
                        }
                    }
                } else {
                    LogUtil.LogE(TAG, "type is invalid: name=" + string + ", value=" + string3 + ", type=" + string2);
                    return false;
                }
                LogUtil.LogE(TAG, "setHotelMenuSetting: JSONException", e);
                return z;
            }
        } catch (JSONException e2) {
            e = e2;
        }
        return z;
    }
}
