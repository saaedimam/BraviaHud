package com.sony.dtv.b2b.prosettings;

import android.content.Context;
import android.os.Binder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import com.sony.dtv.b2b.prosettings.platform.AbUpdate;
import com.sony.dtv.b2b.prosettings.platform.B2bcmdManager;
import com.sony.dtv.b2b.prosettings.platform.BootLogo;
import com.sony.dtv.b2b.prosettings.platform.CustomLabels;
import com.sony.dtv.b2b.prosettings.platform.HotelMode;
import com.sony.dtv.b2b.prosettings.platform.HotelSetting;
import com.sony.dtv.b2b.prosettings.platform.IPControl;
import com.sony.dtv.b2b.prosettings.platform.NetworkSettings;
import com.sony.dtv.b2b.prosettings.platform.Package;
import com.sony.dtv.b2b.prosettings.platform.ScreenOverlay;
import com.sony.dtv.b2b.prosettings.platform.SoundPictureSetting;
import com.sony.dtv.b2b.prosettings.platform.SystemProperty;
import com.sony.dtv.b2b.prosettings.platform.SystemSetting;
import com.sony.dtv.b2b.prosettings.platform.TvChassis;
import com.sony.dtv.b2b.prosettings.platform.TvUpdateLegacy;
import com.sony.dtv.b2b.prosettings.service.ServiceManager;
import com.sony.dtv.b2b.prosettings.util.J8Compat.BiConsumer;
import com.sony.dtv.b2b.prosettings.util.J8Compat.Consumer;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ProSettingsServiceBinder extends IProSettingsService.Stub {
    private static final String JSON_KEY_BOOT_COUNT = "boot_count";
    private static final String JSON_KEY_PANEL_OP_TIME = "panel_op_time";
    private static final String JSON_KEY_RESULT = "result";
    private static final String JSON_KEY_TOTAL_OP_TIME = "total_op_time";
    private final String TAG = getClass().getSimpleName();
    private final AbUpdate mAbupdate;
    private final Context mContext;
    private final ServiceManager mServiceManager;

    ProSettingsServiceBinder(Context context, ServiceManager serviceManager, AbUpdate abUpdate) {
        this.mContext = context;
        this.mServiceManager = serviceManager;
        this.mAbupdate = abUpdate;
    }

    private boolean isPermittedCaller() {
        String nameForUid = this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
        if (nameForUid.equals(GV.PACKAGE_NAME_NODERUNTIME_NORMAL) || nameForUid.equals(GV.PACKAGE_NAME_NODERUNTIME_PRIVILEGE)) {
            return true;
        }
        LogUtil.LogE(this.TAG, "processRequest: cannot use from " + nameForUid);
        return false;
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized void processRequest(String str, IProSettingsCallback iProSettingsCallback, int i) throws RemoteException {
        if (!isPermittedCaller()) {
            LogUtil.LogE(this.TAG, "processRequest: caller MUST BE NodeRuntime.");
            Response.invokeErrorCallback(iProSettingsCallback, -1002, null);
            return;
        }
        LogUtil.LogD(this.TAG, "processRequest: request=" + str + ", callingAppUid=" + Binder.getCallingUid());
        this.mServiceManager.processRequest(str, iProSettingsCallback);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized void registerCallback(String str, IProSettingsCallback iProSettingsCallback, int i) throws RemoteException {
        if (!isPermittedCaller()) {
            LogUtil.LogE(this.TAG, "registerCallback: caller MUST BE NodeRuntime.");
            Response.invokeErrorCallback(iProSettingsCallback, -1002, null);
            return;
        }
        LogUtil.LogD(this.TAG, "registerCallback: callingAppUid=" + Binder.getCallingUid());
        this.mServiceManager.processSubscribe(str, iProSettingsCallback);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized void unregisterCallback(IProSettingsCallback iProSettingsCallback) throws RemoteException {
        if (!isPermittedCaller()) {
            LogUtil.LogE(this.TAG, "unregisterCallback: caller MUST BE NodeRuntime.");
            Response.invokeErrorCallback(iProSettingsCallback, -1002, null);
            return;
        }
        LogUtil.LogD(this.TAG, "unregisterCallback: callingAppUid=" + Binder.getCallingUid());
        this.mServiceManager.processUnsubscribe(iProSettingsCallback);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized String processRequestSync(String str) throws RemoteException {
        if (!isPermittedCaller()) {
            LogUtil.LogE(this.TAG, "processRequest: caller MUST BE NodeRuntime.");
            return "";
        }
        LogUtil.LogD(this.TAG, "processRequestSync: callingAppUid=" + Binder.getCallingUid());
        return this.mServiceManager.processRequestSync(str);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized void disableAppPackageIfEnabled(String str) throws RemoteException {
        LogUtil.LogD(this.TAG, "disableAppPackageIfEnabled: pkg_name=" + str);
        Package.disableAppPackageIfEnabled(str);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean getAppPackageEnabled(String str) throws RemoteException {
        LogUtil.LogD(this.TAG, "getAppPackageEnabled: pkg_name=" + str);
        return Package.getAppPackageEnabled(str);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized void enableAppPackageIfChanged(String str) throws RemoteException {
        LogUtil.LogD(this.TAG, "enableAppPackageIfChanged: pkg_name=" + str);
        Package.enableAppPackageIfChanged(str);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized void disableAppPackage(String str) throws RemoteException {
        LogUtil.LogD(this.TAG, "disableAppPackage: pkg_name=" + str);
        Package.disableAppPackage(str);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized void enableAppPackage(String str) throws RemoteException {
        LogUtil.LogD(this.TAG, "enableAppPackage: pkg_name=" + str);
        Package.enableAppPackage(str);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public boolean setAppPackageStateSetting(String str, int i, int i2) throws RemoteException {
        LogUtil.LogD(this.TAG, "enableAppPackage: pkg_name=" + str + "  mode=" + i + "  flags=" + i2);
        return Package.setAppPackageEnableMode(str, i, i2);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public int getAppPackageStateSetting(String str) throws RemoteException {
        try {
            return Package.getAppPackageEnableMode(str);
        } catch (IllegalArgumentException e) {
            LogUtil.LogD(this.TAG, e.getMessage());
            throw new RemoteException(e.getMessage());
        }
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public boolean setAppComponentStateSetting(String str, String str2, int i, int i2) throws RemoteException {
        return Package.setComponentEnabledSetting(str, str2, i, i2);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public int getAppComponentStateSetting(String str, String str2) throws RemoteException {
        return Package.getComponentEnabledSetting(str, str2);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public boolean setAppHiddenSettingAsUser(String str, boolean z) throws RemoteException {
        return Package.setApplicationHiddenSettingAsUser(str, z);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public boolean getAppHiddenSettingAsUser(String str) throws RemoteException {
        return Package.getApplicationHiddenSettingAsUser(str);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean setFeatures(Map map, String str) throws RemoteException {
        LogUtil.LogD(this.TAG, "setFeatures:");
        return DataProvider.setFeature(map, str);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean setSystemProperty(String str, String str2) throws RemoteException {
        LogUtil.LogD(this.TAG, "setSystemProperty:");
        return SystemProperty.invokeSystemPropertiesSet(str, str2);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean setNetworkSetting(Map map) throws RemoteException {
        LogUtil.LogD(this.TAG, "setNetworkSetting:");
        Looper.prepare();
        return NetworkSettings.setNetworkSetting((HashMap) map);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean setWirelessSetting(Map map) throws RemoteException {
        LogUtil.LogD(this.TAG, "setWirelessSetting:");
        return NetworkSettings.setWirelessSetting((HashMap) map);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean setCustomLabels(Map map) throws RemoteException {
        LogUtil.LogD(this.TAG, "setCustomLabels:");
        return CustomLabels.setCustomLabels((HashMap) map);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean setHotelMenuSetting(List list) throws RemoteException {
        LogUtil.LogD(this.TAG, "setHotelMenuSetting:");
        return HotelSetting.setHotelMenuSetting((ArrayList) list);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean setHotelMode(Map map) throws RemoteException {
        LogUtil.LogD(this.TAG, "setHotelMode:");
        return HotelMode.setHotelMode((HashMap) map);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean setHotelSetting(List list) throws RemoteException {
        LogUtil.LogD(this.TAG, "setHotelSetting:");
        return HotelSetting.setHotelSetting((ArrayList) list);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean setIPControl(Map map) throws RemoteException {
        LogUtil.LogD(this.TAG, "setIPControl:");
        return IPControl.setIPControl((HashMap) map);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean setSoundPictureSetting(List list) throws RemoteException {
        LogUtil.LogD(this.TAG, "setSoundPictureSetting:");
        return SoundPictureSetting.setSoundPictureSetting((ArrayList) list);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean setStartupUrl(Map map) throws RemoteException {
        LogUtil.LogD(this.TAG, "setStartupUrl:");
        return SystemSetting.setStartupUrl((HashMap) map);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean setSystemSetting(Map map) throws RemoteException {
        LogUtil.LogD(this.TAG, "setSystemSetting:");
        return SystemSetting.setSystemSetting((HashMap) map);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean installLogo(String str) throws RemoteException {
        LogUtil.LogD(this.TAG, "installLogo:");
        return new BootLogo().installLogo(str);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public synchronized boolean uninstallLogo() throws RemoteException {
        LogUtil.LogD(this.TAG, "uninstallLogo:");
        return new BootLogo().uninstallLogo();
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public String decryptPhrase(String str, String str2) throws RemoteException {
        LogUtil.LogD(this.TAG, "decryptPhrase:");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("key", str);
            jSONObject.put("data", str2);
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            JSONObject jSONObject2 = new JSONObject();
            try {
                if (B2bcmdManager.execCmd(B2bcmdManager.B2BCMD_DECRYPT_PHRASE, jSONObject.toString(), sb, sb2) == 0) {
                    jSONObject2.put("success", true);
                    jSONObject2.put("data", sb.toString());
                } else {
                    jSONObject2.put("success", false);
                }
                return jSONObject2.toString();
            } catch (JSONException unused) {
                return "{ success: false }";
            }
        } catch (JSONException unused2) {
            return "{ success: false }";
        }
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public String prepareFwWrite() throws RemoteException {
        if (this.mAbupdate == null) {
            return null;
        }
        return this.mAbupdate.prepareFwWrite();
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public void cleanupFwWrite() throws RemoteException {
        if (this.mAbupdate != null) {
            this.mAbupdate.cleanupFwWrite();
        } else {
            B2bcmdManager.execCmd(B2bcmdManager.B2BCMD_CLEANUP_FWWRITE, null, null, null);
        }
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public void startFwUpdate(final IProSettingsCallback iProSettingsCallback, final IProSettingsCallback iProSettingsCallback2) throws RemoteException {
        if (this.mAbupdate == null) {
            iProSettingsCallback.onResult(AbUpdate.makeResponse(false));
        } else {
            this.mAbupdate.startFwUpdate(new Consumer<String>() { // from class: com.sony.dtv.b2b.prosettings.ProSettingsServiceBinder.1
                @Override // com.sony.dtv.b2b.prosettings.util.J8Compat.Consumer
                public void accept(String str) {
                    try {
                        iProSettingsCallback.onResult(str);
                    } catch (RemoteException unused) {
                    }
                }
            }, new BiConsumer<Integer, Float>() { // from class: com.sony.dtv.b2b.prosettings.ProSettingsServiceBinder.2
                @Override // com.sony.dtv.b2b.prosettings.util.J8Compat.BiConsumer
                public void accept(Integer num, Float f) {
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put(NotificationCompat.CATEGORY_STATUS, num);
                        jSONObject.put(NotificationCompat.CATEGORY_PROGRESS, f);
                        iProSettingsCallback2.onResult(jSONObject.toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            });
        }
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public void cancelFwUpdate() throws RemoteException {
        if (this.mAbupdate != null) {
            this.mAbupdate.calcelFwUpdate();
        }
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public void setFwUpdateDryRunMode(int i) throws RemoteException {
        if (GV.BEFORE_TREBLE) {
            TvUpdateLegacy.setDryRunMode(i);
        } else if (this.mAbupdate != null) {
            this.mAbupdate.setDryRunMode(i);
        }
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public String getDeviceId() throws RemoteException {
        return TvChassis.getDeviceId();
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public String getPackageName() throws RemoteException {
        return TvUpdateLegacy.getPackageName();
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public String getSoftwareVersionDisplay() throws RemoteException {
        return TvUpdateLegacy.getSoftwareVersionDisplay();
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public String getIpControlPSK() throws RemoteException {
        return IPControl.getIpControlPSK();
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public int getSoftwareUpgradeStatus() throws RemoteException {
        return TvUpdateLegacy.getSoftwareUpgradeStatus();
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public int startSoftwareUpdateLegacy() throws RemoteException {
        return TvUpdateLegacy.startSoftwareUpdateLegacy();
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public int getIpControlAuthentication() throws RemoteException {
        return IPControl.getIpControlAuthentication();
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public int set4KBEGeneralControlCommand(List list) throws RemoteException {
        return TvChassis.set4KBEGeneralControlCommand(list);
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public List get4KBEGeneralControlCommand() throws RemoteException {
        return TvChassis.get4KBEGeneralControlCommand();
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public List getMtkHotelMode() throws RemoteException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(HotelMode.getMtkHotelMode()));
        arrayList.add(Integer.valueOf(HotelMode.getMtkCurrentHotelMode()));
        return arrayList;
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public void clearAllAccount(IProSettingsCallback iProSettingsCallback) throws RemoteException {
        SystemSetting.clearAllAccounts();
        iProSettingsCallback.onResult("{ result: true }");
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public void clearApplicationUserData(List list, IProSettingsCallback iProSettingsCallback) throws RemoteException {
        Iterator it = ((ArrayList) list).iterator();
        while (it.hasNext()) {
            Package.clearUserData((String) it.next());
        }
        iProSettingsCallback.onResult("{ result: true }");
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public void getDiagnosisInformation(IProSettingsCallback iProSettingsCallback) throws RemoteException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(JSON_KEY_TOTAL_OP_TIME, TvChassis.getTotalOperationTime());
            jSONObject.put(JSON_KEY_PANEL_OP_TIME, TvChassis.getPanelOperationTime());
            jSONObject.put(JSON_KEY_BOOT_COUNT, TvChassis.getBootCount());
            jSONObject.put(JSON_KEY_RESULT, true);
            iProSettingsCallback.onResult(jSONObject.toString());
        } catch (JSONException unused) {
            iProSettingsCallback.onResult("{ result: false }");
        }
    }

    @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
    public int setScreenOverlay(boolean z) throws RemoteException {
        return ScreenOverlay.showBlindfold(z);
    }
}
