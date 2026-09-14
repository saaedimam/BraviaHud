package com.sony.dtv.b2b.prosettings.platform;

import android.content.ContentValues;
import android.content.Intent;
import android.os.RemoteException;
import android.os.UserHandle;
import com.mediatek.twoworlds.tv.MtkTvHotel;
import com.sony.dtv.b2b.hotelmode.HotelSettings;
import com.sony.dtv.b2b.prosettings.GV;
import com.sony.dtv.b2b.prosettings.util.BindUtil;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import com.sony.dtv.magicpacketnotifyservice.IMagicPacketNotifyService;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class IPControl extends PlatformBase {
    private static final String COMMON_DISABLE = "false";
    private static final String COMMON_ENABLE = "true";
    private static final String COMMON_OFF = "0";
    private static final String COMMON_ON = "1";
    private static final String IPCONTROL_CONTROL4_SDDP = "vendorprotocol.sddp";
    private static final String IPCONTROL_SIMPLE_IP_CONTROL = "vendorprotocol.ssip";
    private static final String MAGICPACKET_NOTIFY_SERVICE_CLASS_NAME = "com.sony.dtv.magicpacketnotifyservice.MagicPacketNotifyService";
    private static final String MAGICPACKET_NOTIFY_SERVICE_PROCESS_NAME = "com.sony.dtv.magicpacketnotifyservice";
    private static final String REMOTE_DEVICE_CONTROL_REMOTELY = "seconddisp.remotecontrol";
    private static final String TAG = "IPControl";
    private static final String VENDOR_PROTOCOL_PKG = "com.sony.dtv.b2b.vendorprotocol";
    private static IMagicPacketNotifyService m_magicPacketNotifyService;
    private static BindUtil m_magicPacketNotifyServiceBinder;

    private static int cnvAuthModeProSetting2Mtk(int i) {
        switch (i) {
            case 0:
            default:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
        }
    }

    public static boolean setIPControl(HashMap<String, String> map) {
        String systemGrobalSettings = getSystemGrobalSettings(IPCONTROL_SIMPLE_IP_CONTROL);
        String systemGrobalSettings2 = getSystemGrobalSettings(IPCONTROL_CONTROL4_SDDP);
        String systemGrobalSettings3 = getSystemGrobalSettings(REMOTE_DEVICE_CONTROL_REMOTELY);
        if (mMtkTvHotel == null) {
            mMtkTvHotel = new MtkTvHotel(mContext);
        }
        if (map.containsKey("authentication_mode")) {
            if (mMtkTvHotel.setIpControlAuthentication(cnvAuthModeProSetting2Mtk(Integer.parseInt(map.get("authentication_mode")))) != 0) {
                LogUtil.LogE(TAG, "setIPControl: Fail SetIpControlAuthentication.");
                return false;
            }
        }
        if (map.containsKey("preshared_key")) {
            if (mMtkTvHotel.setIpControlPSK(map.get("preshared_key")) != 0) {
                LogUtil.LogE(TAG, "Execute: Fail SetIpControlPSK.");
                return false;
            }
        }
        if (map.containsKey("enable_ssip")) {
            int i = Integer.parseInt(map.get("enable_ssip"));
            if (GV.BEFORE_TREBLE) {
                if (mMtkTvHotel.setStartupApp(i) != 0) {
                    LogUtil.LogE(TAG, "Execute: Fail SetStartupApp.");
                    return false;
                }
            } else {
                ContentValues contentValues = new ContentValues();
                String str = i == 0 ? COMMON_OFF : COMMON_ON;
                if (!systemGrobalSettings.contentEquals(str)) {
                    contentValues.put(HotelSettings.VALUE, str);
                    if (!setSettingsprovider(IPCONTROL_SIMPLE_IP_CONTROL, contentValues)) {
                        LogUtil.LogE(TAG, "setSettingsprovider(ssip) fail.");
                        return false;
                    }
                }
                systemGrobalSettings = str;
            }
        }
        if (map.containsKey("enable_sddp") && !GV.BEFORE_TREBLE) {
            String str2 = Integer.parseInt(map.get("enable_sddp")) == 0 ? COMMON_OFF : COMMON_ON;
            if (!systemGrobalSettings2.contentEquals(str2)) {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put(HotelSettings.VALUE, str2);
                if (!setSettingsprovider(IPCONTROL_CONTROL4_SDDP, contentValues2)) {
                    LogUtil.LogE(TAG, "setSettingsprovider(sddp) fail.");
                    return false;
                }
                systemGrobalSettings2 = str2;
            }
        }
        if (map.containsKey("enable_wol")) {
            boolean z = Boolean.parseBoolean(map.get("enable_wol"));
            if (ModelVariationInfo.GENERATION_SAKURA < ModelVariationInfo.getGeneration()) {
                LogUtil.LogD(TAG, "set WOL without MagicPacketNotifyService");
                ContentValues contentValues3 = new ContentValues();
                contentValues3.put(HotelSettings.VALUE, String.valueOf(z));
                if (!setSettingsprovider("network.remotestart", contentValues3)) {
                    LogUtil.LogE(TAG, "Execute: Fail enable_wol.");
                    return false;
                }
            } else {
                LogUtil.LogD(TAG, "set WOL with MagicPacketNotifyService");
                bindMagicPacketNotifyService();
                try {
                    try {
                        m_magicPacketNotifyService.setRemoteStartSetting(z);
                        unbindMagicPacketNotifyService();
                    } catch (RemoteException e) {
                        LogUtil.LogE(TAG, "Execute: Fail setRemoteStartSetting.", e);
                        unbindMagicPacketNotifyService();
                        return false;
                    }
                } catch (Throwable th) {
                    unbindMagicPacketNotifyService();
                    throw th;
                }
            }
        }
        if (GV.BEFORE_TREBLE) {
            return true;
        }
        if (!systemGrobalSettings.contentEquals(COMMON_ON) && !systemGrobalSettings2.contentEquals(COMMON_ON)) {
            return true;
        }
        if (systemGrobalSettings3.contentEquals(COMMON_DISABLE)) {
            ContentValues contentValues4 = new ContentValues();
            contentValues4.put(HotelSettings.VALUE, COMMON_ENABLE);
            if (!setSettingsprovider(REMOTE_DEVICE_CONTROL_REMOTELY, contentValues4)) {
                LogUtil.LogE(TAG, "setSettingsprovider(RDCR) fail.");
                return false;
            }
        }
        try {
            if (Package.getAppPackageEnableMode(VENDOR_PROTOCOL_PKG) != 0) {
                Package.setAppPackageEnableMode(VENDOR_PROTOCOL_PKG, 0);
                Intent intent = new Intent();
                intent.setPackage(VENDOR_PROTOCOL_PKG);
                intent.setAction("com.sony.dtv.b2b.vendorprotocol.action.START_SERVICE");
                try {
                    mContext.sendBroadcastAsUser(intent, (UserHandle) UserHandle.class.getField("ALL").get(null));
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                } catch (NoSuchFieldException e3) {
                    e3.printStackTrace();
                }
            }
            return true;
        } catch (IllegalArgumentException unused) {
            LogUtil.LogE(TAG, "com.sony.dtv.b2b.vendorprotocol is not found.");
            return true;
        }
    }

    public static String getIpControlPSK() {
        return mMtkTvHotel.getIpControlPSK();
    }

    public static int getIpControlAuthentication() {
        return mMtkTvHotel.getIpControlAuthentication();
    }

    private static void bindMagicPacketNotifyService() {
        LogUtil.LogD(TAG, "bindMagicPacketNotifyService:");
        m_magicPacketNotifyServiceBinder = new BindUtil(mContext, MAGICPACKET_NOTIFY_SERVICE_PROCESS_NAME, MAGICPACKET_NOTIFY_SERVICE_CLASS_NAME);
        if (m_magicPacketNotifyServiceBinder.syncBind()) {
            m_magicPacketNotifyService = IMagicPacketNotifyService.Stub.asInterface(m_magicPacketNotifyServiceBinder.getBinder());
        }
    }

    private static void unbindMagicPacketNotifyService() {
        LogUtil.LogD(TAG, "unbindMagicPacketNotifyService:");
        if (m_magicPacketNotifyService != null) {
            m_magicPacketNotifyServiceBinder.unbind();
            m_magicPacketNotifyService = null;
        }
    }
}
