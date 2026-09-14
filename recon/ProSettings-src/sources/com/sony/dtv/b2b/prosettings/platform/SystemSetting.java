package com.sony.dtv.b2b.prosettings.platform;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.provider.Settings;
import com.sony.dtv.b2b.hotelmode.HotelSettings;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import com.sony.dtv.provider.modelvariation.util.ModelVariationUtil;
import com.sony.dtv.provider.modelvariation.util.ModelVariationValue;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class SystemSetting extends PlatformBase {
    private static final String SETTINGS_SECURE_SCREENSAVER_COMPONENTS = "screensaver_components";
    private static final String SETTINGS_SECURE_SCREENSAVER_ENABLED = "screensaver_enabled";
    private static final String SETTINGS_SECURE_SLEEP_TIMEOUT = "sleep_timeout";
    private static final String SETTINGS_SYSTEM_SCREEN_OFF_TIMEOUT = "screen_off_timeout";
    private static final String TAG = "SystemSetting";
    private static final String sharedPrefKey = "TimeDuration";
    private static final String sharedPrefKeyAutoShutOff = "AutoShutOff";

    private static int cnvPowerSaveProSetting2Mtk(int i) {
        if (i == 3) {
            return 3;
        }
        switch (i) {
            case 0:
            default:
                return 0;
            case 1:
                return 1;
        }
    }

    public static boolean setStartupUrl(HashMap<String, String> map) {
        return true;
    }

    public static boolean setSystemSetting(HashMap<String, String> map) {
        if (map.containsKey("power_saving")) {
            updateMTKConfigurationValue("g_custom_base__power_saving", cnvPowerSaveProSetting2Mtk(Integer.parseInt(map.get("power_saving"))));
        }
        if (map.containsKey("idle_standby")) {
            setIdleStandbyTime(Integer.parseInt(map.get("idle_standby")));
        }
        if (map.containsKey("auto_shutoff")) {
            setAutoShutOff(Integer.parseInt(map.get("auto_shutoff")));
        }
        if (map.containsKey("daydream")) {
            setDaydream(map.get("daydream"));
        }
        if (map.containsKey("when_to_daydream")) {
            setWhenToDaydream(Integer.parseInt(map.get("when_to_daydream")));
        }
        if (map.containsKey("when_to_sleep")) {
            setWhenToSleep(Integer.parseInt(map.get("when_to_sleep")));
        }
        if (map.containsKey(B2bcmdManager.B2BCMD_TUNER_DISABLE)) {
            setTunerDisable(map.get(B2bcmdManager.B2BCMD_TUNER_DISABLE));
        }
        if (!map.containsKey(HotelSettings.WEB_APPS_INSTALLATION)) {
            return true;
        }
        setWebAppsInstallation(map.get(HotelSettings.WEB_APPS_INSTALLATION));
        return true;
    }

    private static int getConfigurationValue(String str) {
        return mMtkTvConfig.getConfigValue(str);
    }

    private static int updateMTKConfigurationValue(String str, int i) {
        getConfigurationValue(str);
        int configValue = mMtkTvConfig.setConfigValue(str, i);
        if (configValue != 0) {
            LogUtil.LogE(TAG, "updateMTKConfigurationValue: mMtkTvConfig.setConfigValue error. configId = " + str + " configValue = " + i);
        }
        getConfigurationValue(str);
        return configValue;
    }

    private static boolean setIdleStandbyTime(int i) {
        Intent intent = new Intent();
        intent.setClassName("com.sony.dtv.timers", "com.sony.dtv.timers.idletvstandby.IdleTvStandByService");
        intent.putExtra("Time Duration", i);
        mContext.startService(intent);
        Settings.System.putInt(mContext.getContentResolver(), sharedPrefKey, i);
        return true;
    }

    private static boolean setAutoShutOff(int i) {
        updateMTKConfigurationValue("g_misc__auto_shutoff", 0);
        Settings.System.putInt(mContext.getContentResolver(), sharedPrefKeyAutoShutOff, i);
        return true;
    }

    private static boolean setDaydream(String str) {
        if (str.equals("Sleep")) {
            Settings.Secure.putInt(mContext.getContentResolver(), SETTINGS_SECURE_SCREENSAVER_ENABLED, 0);
        } else {
            Settings.Secure.putInt(mContext.getContentResolver(), SETTINGS_SECURE_SCREENSAVER_ENABLED, 1);
            Settings.Secure.putString(mContext.getContentResolver(), SETTINGS_SECURE_SCREENSAVER_COMPONENTS, str);
        }
        return true;
    }

    private static boolean setWhenToDaydream(int i) {
        return Settings.System.putInt(mContext.getContentResolver(), SETTINGS_SYSTEM_SCREEN_OFF_TIMEOUT, i);
    }

    private static boolean setWhenToSleep(int i) {
        return Settings.Secure.putInt(mContext.getContentResolver(), SETTINGS_SECURE_SLEEP_TIMEOUT, i);
    }

    private static boolean setTunerDisable(String str) {
        String strGetDestination = GetDestination(mContext.getContentResolver());
        if (!strGetDestination.equals(ModelVariationValue.DESTINATION_DVB_AEP_S2) && !strGetDestination.equals(ModelVariationValue.DESTINATION_DVB_AEP_STD) && !strGetDestination.equals(ModelVariationValue.DESTINATION_DVB_AEP_T2) && !strGetDestination.equals(ModelVariationValue.DESTINATION_DVB_AEP_T2S2)) {
            LogUtil.LogE(TAG, "setTunerDisable: DESTINATION is not DVB_AEP_*, destination = " + strGetDestination);
            return false;
        }
        if (str.equals("on")) {
            StringBuilder sb = new StringBuilder();
            int iExecCmd = B2bcmdManager.execCmd(B2bcmdManager.B2BCMD_TUNER_DISABLE, null, null, sb);
            if (iExecCmd == 0) {
                LogUtil.LogI(TAG, "setTunerDisable: got tuner_disable_finished");
            } else {
                if (sb.toString().contentEquals("tuner_disable_error")) {
                    LogUtil.LogI(TAG, "setTunerDisable: got tuner_disable_error");
                    return false;
                }
                if (iExecCmd == -1003) {
                    LogUtil.LogE(TAG, "setTunerDisable: timeout wait for tuner_disable_finished; over 10 sec");
                    return false;
                }
                LogUtil.LogE(TAG, "setTunerDisable: fail. rc: false");
                return false;
            }
        } else if (str.equals("off")) {
            StringBuilder sb2 = new StringBuilder();
            int iExecCmd2 = B2bcmdManager.execCmd(B2bcmdManager.B2BCMD_TUNER_ENABLE, null, null, sb2);
            if (iExecCmd2 == 0) {
                LogUtil.LogI(TAG, "setTunerEnable: got tuner_enable_finished");
            } else {
                if (sb2.toString().contentEquals("tuner_disable_error")) {
                    LogUtil.LogI(TAG, "setTunerEnable: got tuner_enable_error");
                    return false;
                }
                if (iExecCmd2 == -1003) {
                    LogUtil.LogE(TAG, "setTunerEnable: timeout wait for tuner_enable_finished; over 10 sec");
                    return false;
                }
                LogUtil.LogE(TAG, "setTunerEnable: fail. rc: false");
                return false;
            }
        } else {
            LogUtil.LogE(TAG, "Invalid request '" + str + "' is specified.");
            return false;
        }
        return true;
    }

    private static boolean setWebAppsInstallation(String str) {
        return HotelSettings.putString(mContext.getContentResolver(), HotelSettings.WEB_APPS_INSTALLATION, str);
    }

    public static String GetDestination(ContentResolver contentResolver) {
        try {
            return ModelVariationUtil.get(contentResolver, 39);
        } catch (Exception e) {
            LogUtil.LogE(TAG, "getDestination: Exception: " + e);
            return "";
        }
    }

    public static void clearAllAccounts() {
        AccountManager accountManager = AccountManager.get(mContext);
        for (Account account : accountManager.getAccounts()) {
            accountManager.removeAccount(account, null, null);
            LogUtil.LogD(TAG, "RemoveAccount: " + account.name + "(" + account.type + ")");
        }
    }
}
