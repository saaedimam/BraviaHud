package com.mediatek.dmagent;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.ServiceManager;
import android.util.Log;
import com.mediatek.dm.DMRemoteServiceHandler;

/* JADX INFO: loaded from: classes.dex */
public class DMRemoteServiceAgent extends Service {
    @Override // android.app.Service
    public void onCreate() {
        Log.i("[J]DMRemoteServiceAgent", "Start DMRemoteService");
        IBinder b = ServiceManager.getService("DMRemoteService");
        if (b == null) {
            DMRemoteServiceHandler handler = new DMRemoteServiceHandler(this);
            ServiceManager.addService("DMRemoteService", handler);
        } else {
            Log.i("[J]DMRemoteServiceAgent", "Exist in ServiceManager, no need to create new DMRemoteService");
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        Log.i("[J]DMRemoteServiceAgent", "DMRemoteService onDestroy");
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        Log.d("[J]DMRemoteServiceAgent", "onbind " + intent);
        IBinder b = ServiceManager.getService("DMRemoteService");
        return b;
    }

    public DMRemoteServiceAgent() {
    }

    public DMRemoteServiceAgent(Context context) {
    }
}
