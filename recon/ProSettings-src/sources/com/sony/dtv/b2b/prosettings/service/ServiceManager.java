package com.sony.dtv.b2b.prosettings.service;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import com.sony.dtv.b2b.prosettings.IProSettingsCallback;
import com.sony.dtv.b2b.prosettings.Response;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ServiceManager {
    private static final String TAG = "ServiceManager";
    private HashMap<IBinder, JSONObject> mJsonMap = null;
    private ServiceBase[] mServices;

    public ServiceManager(Context context) {
        this.mServices = new ServiceBase[]{new ServiceSystem(context)};
    }

    private ServiceBase[] getServices() {
        return this.mServices;
    }

    public void processRequest(String str, IProSettingsCallback iProSettingsCallback) throws RemoteException {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString(NotificationCompat.CATEGORY_SERVICE);
            if (strOptString.isEmpty()) {
                Response.invokeErrorCallback(iProSettingsCallback, -1003, null);
                return;
            }
            String strOptString2 = jSONObject.optString("method");
            if (strOptString2.isEmpty()) {
                LogUtil.LogE(TAG, "processRequest: method is invalid.");
                Response.invokeErrorCallback(iProSettingsCallback, Response.ERROR_METHOD_NOT_FOUND, null);
                return;
            }
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("params");
            if (jSONObjectOptJSONObject == null) {
                LogUtil.LogE(TAG, "processRequest: params is invalid.");
                Response.invokeErrorCallback(iProSettingsCallback, Response.ERROR_INVALID_PARAMS, null);
                return;
            }
            for (ServiceBase serviceBase : this.mServices) {
                if (strOptString.equals(serviceBase.getServiceName())) {
                    LogUtil.LogD(TAG, "processRequest: matched service=" + serviceBase.getServiceName() + ", method=" + strOptString2);
                    serviceBase.processRequest(strOptString2, jSONObjectOptJSONObject, iProSettingsCallback);
                    return;
                }
            }
            Response.invokeErrorCallback(iProSettingsCallback, -1003, null);
        } catch (JSONException e) {
            LogUtil.LogE(TAG, "processRequest: parse error", e);
            Response.invokeErrorCallback(iProSettingsCallback, -1001, Response.getErrorDetail(e));
        }
    }

    public synchronized String processRequestSync(String str) throws RemoteException {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString(NotificationCompat.CATEGORY_SERVICE);
            if (strOptString.isEmpty()) {
                return jSONObject.toString();
            }
            String strOptString2 = jSONObject.optString("method");
            if (strOptString2.isEmpty()) {
                LogUtil.LogE(TAG, "processRequest: method is invalid.");
                return jSONObject.toString();
            }
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("params");
            if (jSONObjectOptJSONObject == null) {
                LogUtil.LogE(TAG, "processRequest: params is invalid.");
                return jSONObject.toString();
            }
            JSONObject jSONObject2 = new JSONObject();
            for (ServiceBase serviceBase : this.mServices) {
                if (strOptString.equals(serviceBase.getServiceName())) {
                    LogUtil.LogD(TAG, "processRequestSync: matched service=" + serviceBase.getServiceName() + ", method=" + strOptString2);
                    return serviceBase.processRequestSync(strOptString2, jSONObjectOptJSONObject).toString();
                }
            }
            return jSONObject2.toString();
        } catch (JSONException e) {
            LogUtil.LogE(TAG, "processRequest: parse error", e);
            return "{}";
        }
    }

    public synchronized void processSubscribe(String str, IProSettingsCallback iProSettingsCallback) throws RemoteException {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString(NotificationCompat.CATEGORY_SERVICE);
            if (strOptString.isEmpty()) {
                LogUtil.LogE(TAG, "registerCallback: service is invalid.");
                Response.invokeErrorCallback(iProSettingsCallback, -1003, null);
                return;
            }
            String strOptString2 = jSONObject.optString(NotificationCompat.CATEGORY_EVENT);
            if (strOptString2.isEmpty()) {
                LogUtil.LogE(TAG, "registerCallback: event is invalid.");
                Response.invokeErrorCallback(iProSettingsCallback, Response.ERROR_EVENT_NOT_FOUND, null);
                return;
            }
            for (ServiceBase serviceBase : this.mServices) {
                if (strOptString.equals(serviceBase.getServiceName())) {
                    LogUtil.LogD(TAG, "processSubscribe: matched service=" + serviceBase.getServiceName() + ", event=" + strOptString2);
                    serviceBase.processSubscribe(strOptString2, iProSettingsCallback);
                    this.mJsonMap.put(iProSettingsCallback.asBinder(), jSONObject);
                    return;
                }
            }
            Response.invokeErrorCallback(iProSettingsCallback, -1003, null);
        } catch (JSONException e) {
            LogUtil.LogE(TAG, "registerCallback: parse error,", e);
            Response.invokeErrorCallback(iProSettingsCallback, -1001, Response.getErrorDetail(e));
        }
    }

    public synchronized void processUnsubscribe(IProSettingsCallback iProSettingsCallback) throws RemoteException {
        if (!this.mJsonMap.containsKey(iProSettingsCallback.asBinder())) {
            LogUtil.LogE(TAG, "unregisterCallback: callback is not registered.");
            Response.invokeErrorCallback(iProSettingsCallback, -1000, null);
            return;
        }
        JSONObject jSONObject = this.mJsonMap.get(iProSettingsCallback.asBinder());
        String strOptString = jSONObject.optString(NotificationCompat.CATEGORY_SERVICE);
        if (strOptString.isEmpty()) {
            LogUtil.LogE(TAG, "unregisterCallback: service is invalid.");
            Response.invokeErrorCallback(iProSettingsCallback, -1003, null);
            return;
        }
        String strOptString2 = jSONObject.optString(NotificationCompat.CATEGORY_EVENT);
        if (strOptString2.isEmpty()) {
            LogUtil.LogE(TAG, "unregisterCallback: event is invalid.");
            Response.invokeErrorCallback(iProSettingsCallback, Response.ERROR_EVENT_NOT_FOUND, null);
            return;
        }
        for (ServiceBase serviceBase : this.mServices) {
            if (strOptString.equals(serviceBase.getServiceName())) {
                LogUtil.LogD(TAG, "processUnsubscribe: matched service=" + serviceBase.getServiceName() + ", event=" + strOptString2);
                serviceBase.processUnsubscribe(strOptString2, iProSettingsCallback);
                this.mJsonMap.remove(iProSettingsCallback.asBinder());
                return;
            }
        }
        Response.invokeErrorCallback(iProSettingsCallback, -1003, null);
    }
}
