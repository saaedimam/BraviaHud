package com.sony.dtv.b2b.prosettings.service;

import android.content.Context;
import android.os.RemoteException;
import com.sony.dtv.b2b.prosettings.GV;
import com.sony.dtv.b2b.prosettings.IProSettingsCallback;
import com.sony.dtv.b2b.prosettings.Response;
import com.sony.dtv.b2b.prosettings.platform.B2bcmdManager;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ServiceSystem extends ServiceBase {
    private final String SERVICE_NAME;
    private final String TAG;
    private boolean mIsBusy;

    @Override // com.sony.dtv.b2b.prosettings.service.ServiceBase
    public String getServiceName() {
        return "system";
    }

    public ServiceSystem(Context context) {
        super(context);
        this.TAG = getClass().getSimpleName();
        this.SERVICE_NAME = "system";
        this.mIsBusy = false;
    }

    @Override // com.sony.dtv.b2b.prosettings.service.ServiceBase
    public void processRequest(String str, JSONObject jSONObject, IProSettingsCallback iProSettingsCallback) throws RemoteException {
        if (str.contentEquals("prepareFwWrite")) {
            prepareFwWrite(iProSettingsCallback, jSONObject);
        } else {
            Response.invokeErrorCallback(iProSettingsCallback, Response.ERROR_METHOD_NOT_FOUND, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setBusy(boolean z) {
        this.mIsBusy = z;
    }

    private synchronized void prepareFwWrite(final IProSettingsCallback iProSettingsCallback, JSONObject jSONObject) {
        if (this.mIsBusy) {
            Response.invokeErrorCallback(iProSettingsCallback, -1000, "fwwrite process busy");
            return;
        }
        if (GV.BEFORE_TREBLE) {
            setBusy(true);
            B2bcmdManager.execCmdAsync(B2bcmdManager.B2BCMD_PREPARE_FWWRITE, null, new B2bcmdManager.execCmdAsyncCB() { // from class: com.sony.dtv.b2b.prosettings.service.ServiceSystem.1
                /* JADX WARN: Multi-variable type inference failed */
                /* JADX WARN: Type inference failed for: r3v3, types: [com.sony.dtv.b2b.prosettings.service.ServiceSystem] */
                @Override // com.sony.dtv.b2b.prosettings.platform.B2bcmdManager.execCmdAsyncCB
                protected void apply(int i, String str, String str2) {
                    try {
                        try {
                            JSONObject jSONObject2 = new JSONObject();
                            if (i != 0) {
                                Response.invokeErrorCallback(iProSettingsCallback, -1000, "B2bcmdManagerResult : " + i);
                            } else {
                                jSONObject2.put("socket_name", str);
                                Response.invokeCallback(iProSettingsCallback, jSONObject2);
                            }
                        } catch (JSONException e) {
                            Response.invokeErrorCallback(iProSettingsCallback, -1000, Response.getErrorDetail(e));
                        }
                    } finally {
                        ServiceSystem.this.setBusy(false);
                    }
                }
            });
        } else {
            Response.invokeErrorCallback(iProSettingsCallback, -1002, "prepareFwWrite does not available.");
        }
    }
}
