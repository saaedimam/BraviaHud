package com.sony.dtv.b2b.prosettings;

import android.os.RemoteException;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class Response {
    public static final int ERROR_EVENT_NOT_FOUND = -1005;
    public static final int ERROR_INTERNAL_ERROR = -1000;
    public static final int ERROR_INVALID_PARAMS = -1006;
    public static final int ERROR_INVALID_REQUEST = -1002;
    public static final int ERROR_METHOD_NOT_FOUND = -1004;
    public static final int ERROR_PARSE_ERROR = -1001;
    public static final int ERROR_SERVICE_NOT_FOUND = -1003;
    private static final String TAG = "Response";

    public static JSONObject createErrorResponse(int i, String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("error", i);
            if (str != null) {
                jSONObject.put("error_detail", str);
            }
        } catch (JSONException e) {
            LogUtil.LogE(TAG, "JSONException", e);
        }
        return jSONObject;
    }

    public static String getErrorDetail(Exception exc) {
        StackTraceElement[] stackTrace = exc.getStackTrace();
        return stackTrace[stackTrace.length - 1].toString();
    }

    public static void invokeCallback(IProSettingsCallback iProSettingsCallback, JSONObject jSONObject) {
        if (iProSettingsCallback != null) {
            try {
                iProSettingsCallback.onResult(jSONObject.toString());
            } catch (RemoteException e) {
                LogUtil.LogE(TAG, "invokeCallback: failed to invoke callback", e);
            }
        }
    }

    public static void invokeErrorCallback(IProSettingsCallback iProSettingsCallback, int i, String str) {
        if (iProSettingsCallback != null) {
            try {
                iProSettingsCallback.onResult(createErrorResponse(i, str).toString());
            } catch (RemoteException e) {
                LogUtil.LogE(TAG, "invokeErrorCallback: failed to invoke callback", e);
            }
        }
    }
}
