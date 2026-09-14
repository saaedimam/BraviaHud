package com.sony.dtv.b2b.prosettings.service;

import android.content.Context;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import com.sony.dtv.b2b.prosettings.IProSettingsCallback;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class ServiceBase {
    protected String SERVICE_NAME;
    protected String TAG;
    protected Context mContext;

    public void processRequest(String str, JSONObject jSONObject, IProSettingsCallback iProSettingsCallback) throws RemoteException {
    }

    public void processSubscribe(String str, IProSettingsCallback iProSettingsCallback) throws RemoteException {
    }

    public void processUnsubscribe(String str, IProSettingsCallback iProSettingsCallback) throws RemoteException {
    }

    protected ServiceBase() {
        this.TAG = getClass().getSimpleName();
        this.SERVICE_NAME = NotificationCompat.CATEGORY_SERVICE;
        this.mContext = null;
    }

    public ServiceBase(Context context) {
        this.TAG = getClass().getSimpleName();
        this.SERVICE_NAME = NotificationCompat.CATEGORY_SERVICE;
        this.mContext = null;
        this.mContext = context;
    }

    public String getServiceName() {
        return this.SERVICE_NAME;
    }

    public void invokeCallback(IProSettingsCallback iProSettingsCallback, JSONObject jSONObject) throws RemoteException {
        if (iProSettingsCallback != null) {
            LogUtil.LogD(this.TAG, "invokeCallback: calling onResult");
            iProSettingsCallback.onResult(jSONObject.toString());
        } else {
            LogUtil.LogE(this.TAG, "invokeCallback: callback is null");
        }
    }

    public JSONObject processRequestSync(String str, JSONObject jSONObject) throws RemoteException {
        return new JSONObject();
    }
}
