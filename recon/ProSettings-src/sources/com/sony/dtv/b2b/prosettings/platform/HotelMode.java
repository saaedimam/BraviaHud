package com.sony.dtv.b2b.prosettings.platform;

import android.database.Cursor;
import com.sony.dtv.b2b.hotelmode.HotelSettings;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import com.sony.dtv.provider.modelvariation.util.ModelVariationValue;
import com.sony.dtv.tvinput.provider.SonyTvContract;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class HotelMode extends PlatformBase {
    private static final String CHANNELS_COLUMN_ID = "_id";
    private static final long INVALID_ID = -1;
    public static int MODE_HOTEL = 2;
    public static int MODE_NORMAL = 0;
    public static int MODE_SETTINGS = 1;
    private static final String TAG = "HotelMode";

    public static class DmChsHotelMeetingDisplaySettings {
        public String m_autoAdjust;
        public DmChsHotelMeetingDisplayWakeup m_wakeup = new DmChsHotelMeetingDisplayWakeup();
    }

    public static class DmChsHotelMeetingDisplayWakeup {
        public int m_begin;
        public int m_end;
        public String m_wakeupTarget;
    }

    private static class TvChannelInfo {
        public long channelContentId;
        public String channelId;
        public long id;
        public long progListType;

        private TvChannelInfo() {
        }
    }

    private static class TvChannelInfoForJp {
        public long channelContentId;
        public long id;
        public long originalNetworkId;
        public long serviceId;
        public long transportStreamId;

        private TvChannelInfoForJp() {
        }
    }

    public static int getMtkHotelMode() {
        return mMtkTvHotel.getHotelMode();
    }

    public static int getMtkCurrentHotelMode() {
        return mMtkTvHotel.getCurrentMode();
    }

    public static boolean setHotelMode(HashMap<String, String> map) {
        if (map.containsKey("hotel_menu")) {
            String str = map.get("hotel_menu");
            if (!DmChassis_SetUserHomeMenuMode(str)) {
                LogUtil.LogE("HotelMode", "setHotelMode: Error, hotel_menu=" + str);
                return false;
            }
        }
        if (map.containsKey("hotel_menu_url")) {
            String str2 = map.get("hotel_menu_url");
            if (!putSettingsString(HotelSettings.CUSTOM_HOME_URI, str2)) {
                LogUtil.LogE("HotelMode", "setHotelMode: Error, hotel_menu_url=" + str2);
                return false;
            }
        }
        if (map.containsKey("androidapp_params") && !map.get("androidapp_params").isEmpty()) {
            String str3 = map.get("androidapp_params");
            try {
                JSONObject jSONObject = new JSONObject(str3);
                if (!putSettingsString(HotelSettings.STARTUP_ANDROID_APPLICATION_PACKAGE, jSONObject.optString("package"))) {
                    LogUtil.LogE("HotelMode", "setHotelMode: Error, androidapp_params::package fail. " + str3);
                    return false;
                }
                if (!putSettingsString(HotelSettings.STARTUP_ANDROID_APPLICATION_ACTIVITY, jSONObject.optString("activity"))) {
                    LogUtil.LogE("HotelMode", "setHotelMode: Error, androidapp_params::activity fail. " + str3);
                    return false;
                }
                if (!putSettingsString(HotelSettings.STARTUP_ANDROID_APPLICATION_DATA, jSONObject.optString("data"))) {
                    LogUtil.LogE("HotelMode", "setHotelMode: Error, androidapp_params::data fail. " + str3);
                    return false;
                }
                JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("extra");
                if (jSONArrayOptJSONArray != null) {
                    JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(0);
                    if (!putSettingsString(HotelSettings.STARTUP_ANDROID_APPLICATION_EXTRA_NAME, jSONObjectOptJSONObject.optString(HotelSettings.NAME))) {
                        LogUtil.LogE("HotelMode", "setHotelMode: Error, androidapp_params::extra:name fail. " + str3);
                        return false;
                    }
                    if (!putSettingsString(HotelSettings.STARTUP_ANDROID_APPLICATION_EXTRA_VALUE, jSONObjectOptJSONObject.optString(HotelSettings.VALUE))) {
                        LogUtil.LogE("HotelMode", "setHotelMode: Error, androidapp_params::extra:value fail. " + str3);
                        return false;
                    }
                }
                if (!putSettingsString(HotelSettings.STARTUP_ANDROID_APPLICATION, HotelSettings.VALUE_ON)) {
                    LogUtil.LogE("HotelMode", "setHotelMode: Error, STARTUP_ANDROID_APPLICATION fail.");
                    return false;
                }
                map.put("hotel_initial_input", "");
            } catch (JSONException unused) {
                LogUtil.LogE("HotelMode", "setHotelMode: Error, androidapp_params JSON error. " + str3);
                return false;
            }
        } else {
            LogUtil.LogD("HotelMode", "Clear androidapp_params.");
            putSettingsString(HotelSettings.STARTUP_ANDROID_APPLICATION, "Off");
            putSettingsString(HotelSettings.STARTUP_ANDROID_APPLICATION_PACKAGE, "");
            putSettingsString(HotelSettings.STARTUP_ANDROID_APPLICATION_ACTIVITY, "");
            putSettingsString(HotelSettings.STARTUP_ANDROID_APPLICATION_DATA, "");
            putSettingsString(HotelSettings.STARTUP_ANDROID_APPLICATION_EXTRA_NAME, "");
            putSettingsString(HotelSettings.STARTUP_ANDROID_APPLICATION_EXTRA_VALUE, "");
        }
        if (map.containsKey("hotel_initial_input")) {
            String str4 = map.get("hotel_initial_input");
            int i = Integer.parseInt(map.get("hotel_initial_channel"));
            if (!DmChassis_SetInitialInput_Hotel(str4, i)) {
                LogUtil.LogE("HotelMode", "setHotelMode: Error, hotel_initial_input=" + str4 + ", hotel_initial_channel" + i);
                return false;
            }
        }
        if (map.containsKey("power_on_mode")) {
            String str5 = map.get("power_on_mode");
            if (!DmChassisIF_SetAcPowerOnMode_Hotel(str5)) {
                LogUtil.LogE("HotelMode", "setHotelMode: Error, power_on_mode=" + str5);
                return false;
            }
        }
        if (map.containsKey("vol_max")) {
            int i2 = Integer.parseInt(map.get("vol_max"));
            if (!DmSoundIF_SetMaxVolume_Hotel(i2)) {
                LogUtil.LogE("HotelMode", "setHotelMode: Error, vol_max=" + i2);
                return false;
            }
        }
        if (map.containsKey("vol_init")) {
            int i3 = Integer.parseInt(map.get("vol_init"));
            if (!DmSoundIF_SetInitialVolume_Hotel(i3)) {
                LogUtil.LogE("HotelMode", "setHotelMode: Error, vol_init=" + i3);
                return false;
            }
        }
        if (map.containsKey("wakeup_on_signal_feature")) {
            DmChsHotelMeetingDisplaySettings dmChsHotelMeetingDisplaySettingsDmChassisIF_GetMeetingDisplaySettings_Hotel = DmChassisIF_GetMeetingDisplaySettings_Hotel();
            dmChsHotelMeetingDisplaySettingsDmChassisIF_GetMeetingDisplaySettings_Hotel.m_wakeup.m_wakeupTarget = map.get("wakeup_on_signal_feature");
            if (map.containsKey("wakeup_on_signal_detect_begin")) {
                dmChsHotelMeetingDisplaySettingsDmChassisIF_GetMeetingDisplaySettings_Hotel.m_wakeup.m_begin = Integer.parseInt(map.get("wakeup_on_signal_detect_begin"));
            }
            if (map.containsKey("wakeup_on_signal_detect_end")) {
                dmChsHotelMeetingDisplaySettingsDmChassisIF_GetMeetingDisplaySettings_Hotel.m_wakeup.m_end = Integer.parseInt(map.get("wakeup_on_signal_detect_end"));
            }
            if (map.containsKey("wakeup_on_signal_auto_resolution")) {
                dmChsHotelMeetingDisplaySettingsDmChassisIF_GetMeetingDisplaySettings_Hotel.m_autoAdjust = map.get("wakeup_on_signal_auto_resolution");
            }
            if (!DmChassisIF_SetMeetingDisplaySettings_Hotel(dmChsHotelMeetingDisplaySettingsDmChassisIF_GetMeetingDisplaySettings_Hotel)) {
                return false;
            }
            if (map.containsKey(HotelSettings.POWER_OFF_ON_SIGNAL_LOSS)) {
                String str6 = map.get(HotelSettings.POWER_OFF_ON_SIGNAL_LOSS);
                if (!putSettingsString(HotelSettings.POWER_OFF_ON_SIGNAL_LOSS, str6)) {
                    LogUtil.LogE("HotelMode", "setHotelMode: Error, power_off_on_signal_loss=" + str6);
                    return false;
                }
            }
        }
        if (map.containsKey("ntp_address")) {
            String str7 = map.get("ntp_address");
            if (!DmChassisIF_SetNtpServerAddress_Hotel(str7)) {
                LogUtil.LogE("HotelMode", "setHotelMode: Error, ntp_address=" + str7);
                return false;
            }
        }
        if (map.containsKey("user_control")) {
            String str8 = map.get("user_control");
            if (!putSettingsString(HotelSettings.KEY_CONTROL_SETTING, str8)) {
                LogUtil.LogE("HotelMode", "setHotelMode: Error, user_control=" + str8);
                return false;
            }
        }
        if (map.containsKey(HotelSettings.PC_INPUT_OPTIMISATION)) {
            String str9 = map.get(HotelSettings.PC_INPUT_OPTIMISATION);
            if (!putSettingsString(HotelSettings.PC_INPUT_OPTIMISATION, str9)) {
                LogUtil.LogE("HotelMode", "setHotelMode: Error, pc_input_optimisation=" + str9);
                return false;
            }
        }
        if (map.containsKey("account_expiration")) {
            String str10 = map.get("account_expiration");
            if (!putSettingsString(HotelSettings.CLEAR_ACCOUNT, str10)) {
                LogUtil.LogE("HotelMode", "setHotelMode: Error, account_expiration=" + str10);
                return false;
            }
        }
        if (map.containsKey("menu_type_inputs")) {
            String str11 = map.get("menu_type_inputs");
            if (!putSettingsString(HotelSettings.QUICK_LAUNCHER_MODE, str11)) {
                LogUtil.LogE("HotelMode", "setHotelMode: Error, quick_launcher_mode=" + str11);
                return false;
            }
        }
        if (!map.containsKey("menu_type_quick_settings")) {
            return true;
        }
        String str12 = map.get("menu_type_quick_settings");
        if (putSettingsString(HotelSettings.QUICK_SETTINGS_MODE, str12)) {
            return true;
        }
        LogUtil.LogE("HotelMode", "setHotelMode: Error, quick_settings_mode=" + str12);
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:18:0x003b  */
    public static int getHotelMode() throws Throwable {
        switch (getSettingsString(HotelSettings.HOTEL_MODE, "")) {
            case "NormalMode":
                return MODE_NORMAL;
            case "HotelSettingsMode":
                return MODE_SETTINGS;
            case "HotelMode":
                return MODE_HOTEL;
            default:
                return -1;
        }
    }

    public static String[] getLimitedEnableAppList() throws Throwable {
        String settingsString = getSettingsString(HotelSettings.CLEAR_DATA_APPLICATION_LIST, "");
        if (settingsString.isEmpty()) {
            return new String[0];
        }
        return settingsString.split(",");
    }

    private static boolean DmChassis_SetUserHomeMenuMode(String str) {
        return !str.isEmpty() && putSettingsString(HotelSettings.HOME_KEY_BEHAVIOR, str);
    }

    private static boolean DmChassisIF_SetAcPowerOnMode_Hotel(String str) {
        return putSettingsString(HotelSettings.AC_POWER_ON, str);
    }

    private static boolean isJpRegion() {
        return ModelVariationValue.DESTINATION_ISDB_JPN.contentEquals(SystemSetting.GetDestination(mContext.getContentResolver()));
    }

    private static boolean DmChassis_SetInitialInput_Hotel(String str, int i) throws Throwable {
        LogUtil.LogD("HotelMode", "DmChassis_SetInitialInput_Hotel: initialInput=" + str + "  initialChannel=" + i);
        if (str.equals("TUNER")) {
            if (!putSettingsString(HotelSettings.INITIAL_INPUT, "")) {
                LogUtil.LogD("HotelMode", "DmChassis_SetInitialInput_Hotel: HotelSettings.INITIAL_INPUT putSettingsString fail.");
                return false;
            }
            if (isJpRegion()) {
                TvChannelInfoForJp tvChannelInfoByIdForJp = getTvChannelInfoByIdForJp(Long.valueOf(i));
                if (tvChannelInfoByIdForJp == null) {
                    LogUtil.LogD("HotelMode", "DmChassis_SetInitialInput_Hotel: getTvChannelInfoById fail.");
                    return false;
                }
                putSettingsLong(HotelSettings.INITIAL_TV_ORIGINAL_NETWORK_ID, tvChannelInfoByIdForJp.originalNetworkId);
                putSettingsLong(HotelSettings.INITIAL_TV_TRANSPORT_STREAM_ID, tvChannelInfoByIdForJp.transportStreamId);
                putSettingsLong(HotelSettings.INITIAL_TV_SERVICE_ID, tvChannelInfoByIdForJp.serviceId);
                putSettingsLong(HotelSettings.INITIAL_TV_CHANNEL, tvChannelInfoByIdForJp.id);
                putSettingsLong(HotelSettings.INITIAL_TV_PROG_LIST_TYPE, -1L);
                putSettingsString(HotelSettings.INITIAL_TV_CHANNEL_ID, "");
                return true;
            }
            TvChannelInfo tvChannelInfoById = getTvChannelInfoById(Long.valueOf(i));
            if (tvChannelInfoById == null) {
                LogUtil.LogD("HotelMode", "DmChassis_SetInitialInput_Hotel: getTvChannelInfoById fail.");
                return false;
            }
            putSettingsLong(HotelSettings.INITIAL_TV_PROG_LIST_TYPE, tvChannelInfoById.progListType);
            putSettingsString(HotelSettings.INITIAL_TV_CHANNEL_ID, tvChannelInfoById.channelId);
            putSettingsLong(HotelSettings.INITIAL_TV_CHANNEL, tvChannelInfoById.id);
            putSettingsLong(HotelSettings.INITIAL_TV_ORIGINAL_NETWORK_ID, -1L);
            putSettingsLong(HotelSettings.INITIAL_TV_TRANSPORT_STREAM_ID, -1L);
            putSettingsLong(HotelSettings.INITIAL_TV_SERVICE_ID, -1L);
            return true;
        }
        if (!putSettingsString(HotelSettings.INITIAL_INPUT, str)) {
            LogUtil.LogD("HotelMode", "DmChassis_SetInitialInput_Hotel: HotelSettings.INITIAL_INPUT putSettingsString fail.");
            return false;
        }
        putSettingsLong(HotelSettings.INITIAL_TV_ORIGINAL_NETWORK_ID, -1L);
        putSettingsLong(HotelSettings.INITIAL_TV_TRANSPORT_STREAM_ID, -1L);
        putSettingsLong(HotelSettings.INITIAL_TV_SERVICE_ID, -1L);
        putSettingsLong(HotelSettings.INITIAL_TV_PROG_LIST_TYPE, -1L);
        putSettingsString(HotelSettings.INITIAL_TV_CHANNEL_ID, "");
        putSettingsLong(HotelSettings.INITIAL_TV_CHANNEL, -1L);
        return true;
    }

    private static boolean DmChassisIF_SetMeetingDisplaySettings_Hotel(DmChsHotelMeetingDisplaySettings dmChsHotelMeetingDisplaySettings) {
        int i = dmChsHotelMeetingDisplaySettings.m_wakeup.m_begin / 60;
        int i2 = dmChsHotelMeetingDisplaySettings.m_wakeup.m_begin % 60;
        int i3 = dmChsHotelMeetingDisplaySettings.m_wakeup.m_end / 60;
        int i4 = dmChsHotelMeetingDisplaySettings.m_wakeup.m_end % 60;
        LogUtil.LogD("HotelMode", "DmChassisIF_SetMeetingDisplaySettings_Hotel: signalAutoDetectBeginHourSettings   = " + i);
        LogUtil.LogD("HotelMode", "DmChassisIF_SetMeetingDisplaySettings_Hotel: signalAutoDetectBeginMinuteSettings = " + i2);
        LogUtil.LogD("HotelMode", "DmChassisIF_SetMeetingDisplaySettings_Hotel: signalAutoDetectEndHourSettings     = " + i3);
        LogUtil.LogD("HotelMode", "DmChassisIF_SetMeetingDisplaySettings_Hotel: signalAutoDetectEndMinuteSettings = " + i4);
        return putSettingsString(HotelSettings.WAKEUP_ON_SIGNAL, dmChsHotelMeetingDisplaySettings.m_wakeup.m_wakeupTarget) && putSettingsInt(HotelSettings.SIGNAL_DETECT_BEGIN_HOUR, i) && putSettingsInt(HotelSettings.SIGNAL_DETECT_BEGIN_MINUTE, i2) && putSettingsInt(HotelSettings.SIGNAL_DETECT_END_HOUR, i3) && putSettingsInt(HotelSettings.SIGNAL_DETECT_END_MINUTE, i4) && putSettingsString(HotelSettings.PC_INPUT_OPTIMISATION, dmChsHotelMeetingDisplaySettings.m_autoAdjust);
    }

    private static boolean DmChassisIF_SetNtpServerAddress_Hotel(String str) {
        return putSettingsString(HotelSettings.NTP_SERVER, str);
    }

    private static String getSettingsString(String str, String str2) throws Throwable {
        String string = HotelSettings.getString(mContext.getContentResolver(), str);
        if (string != null) {
            return string;
        }
        LogUtil.LogE("HotelMode", "getSettingsString: Can not get value. name=" + str);
        return str2;
    }

    private static int getSettingsInt(String str, int i) throws Throwable {
        int i2 = HotelSettings.getInt(mContext.getContentResolver(), str, Integer.MIN_VALUE);
        if (i2 != Integer.MIN_VALUE) {
            return i2;
        }
        LogUtil.LogE("HotelMode", "getSettingsInt: Can not get value. name=" + str);
        return i;
    }

    private static boolean putSettingsString(String str, String str2) {
        boolean zPutString = HotelSettings.putString(mContext.getContentResolver(), str, str2);
        if (!zPutString) {
            LogUtil.LogE("HotelMode", "putSettingsString: Can not put value. name=" + str + " value=" + str2);
        }
        return zPutString;
    }

    private static boolean putSettingsLong(String str, long j) {
        boolean zPutLong = HotelSettings.putLong(mContext.getContentResolver(), str, j);
        if (!zPutLong) {
            LogUtil.LogE("HotelMode", "putSettingsLong: Can not put value. name=" + str + " value=" + j);
        }
        return zPutLong;
    }

    private static boolean putSettingsInt(String str, int i) {
        boolean zPutInt = HotelSettings.putInt(mContext.getContentResolver(), str, i);
        if (!zPutInt) {
            LogUtil.LogE("HotelMode", "putSettingsInt: Can not put value. name=" + str + " value=" + i);
        }
        return zPutInt;
    }

    private static boolean DmSoundIF_SetMaxVolume_Hotel(int i) {
        return putSettingsInt(HotelSettings.MAXIMUM_VOLUME, i);
    }

    private static boolean DmSoundIF_SetInitialVolume_Hotel(int i) {
        return putSettingsInt(HotelSettings.INITIAL_VOLUME, i);
    }

    private static DmChsHotelMeetingDisplaySettings DmChassisIF_GetMeetingDisplaySettings_Hotel() {
        DmChsHotelMeetingDisplaySettings dmChsHotelMeetingDisplaySettings = new DmChsHotelMeetingDisplaySettings();
        dmChsHotelMeetingDisplaySettings.m_wakeup.m_wakeupTarget = getSettingsString(HotelSettings.WAKEUP_ON_SIGNAL, "Off");
        dmChsHotelMeetingDisplaySettings.m_wakeup.m_begin = (getSettingsInt(HotelSettings.SIGNAL_DETECT_BEGIN_HOUR, 0) * 60) + getSettingsInt(HotelSettings.SIGNAL_DETECT_BEGIN_MINUTE, 0);
        dmChsHotelMeetingDisplaySettings.m_wakeup.m_end = (getSettingsInt(HotelSettings.SIGNAL_DETECT_END_HOUR, 0) * 60) + getSettingsInt(HotelSettings.SIGNAL_DETECT_END_MINUTE, 0);
        dmChsHotelMeetingDisplaySettings.m_autoAdjust = getSettingsString(HotelSettings.PC_INPUT_OPTIMISATION, "Off");
        LogUtil.LogD("HotelMode", "DmChassisIF_GetMeetingDisplaySettings_Hotel: wakeupTarget = " + dmChsHotelMeetingDisplaySettings.m_wakeup.m_wakeupTarget);
        LogUtil.LogD("HotelMode", "DmChassisIF_GetMeetingDisplaySettings_Hotel: begin        = " + dmChsHotelMeetingDisplaySettings.m_wakeup.m_begin);
        LogUtil.LogD("HotelMode", "DmChassisIF_GetMeetingDisplaySettings_Hotel: end          = " + dmChsHotelMeetingDisplaySettings.m_wakeup.m_end);
        LogUtil.LogD("HotelMode", "DmChassisIF_GetMeetingDisplaySettings_Hotel: m_autoAdjust = " + dmChsHotelMeetingDisplaySettings.m_autoAdjust);
        return dmChsHotelMeetingDisplaySettings;
    }

    /* JADX WARN: Code duplicated, block: B:15:0x0098 A[Catch: all -> 0x0093, Throwable -> 0x0096, TRY_LEAVE, TryCatch #5 {all -> 0x0093, Throwable -> 0x0096, blocks: (B:8:0x0037, B:10:0x003d, B:15:0x0098), top: B:39:0x0037 }] */
    /* JADX WARN: Code duplicated, block: B:24:0x00bc A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:29:0x00c7 A[Catch: Exception -> 0x00cb, TryCatch #1 {Exception -> 0x00cb, blocks: (B:6:0x0028, B:18:0x00b1, B:25:0x00be, B:29:0x00c7, B:28:0x00c3, B:30:0x00ca), top: B:34:0x0028, inners: #3 }] */
    /* JADX WARN: Code duplicated, block: B:37:0x00be A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:41:? A[Catch: Exception -> 0x00cb, SYNTHETIC, TRY_LEAVE, TryCatch #1 {Exception -> 0x00cb, blocks: (B:6:0x0028, B:18:0x00b1, B:25:0x00be, B:29:0x00c7, B:28:0x00c3, B:30:0x00ca), top: B:34:0x0028, inners: #3 }] */
    /* JADX WARN: Instruction removed from duplicated block: B:15:0x0098, please report this as an issue */
    private static TvChannelInfo getTvChannelInfoById(Long l) throws Throwable {
        TvChannelInfo tvChannelInfo;
        Throwable th;
        Throwable th2;
        String[] strArr = {"content_id", "prog_list_type", "channel_id", CHANNELS_COLUMN_ID};
        if (l.longValue() == -1) {
            return null;
        }
        try {
            Cursor cursorQuery = mContext.getContentResolver().query(SonyTvContract.Channels.CONTENT_URI, strArr, "_id=?", new String[]{Long.toString(l.longValue())}, null);
            if (cursorQuery != null) {
                try {
                    if (cursorQuery.moveToFirst()) {
                        tvChannelInfo = new TvChannelInfo();
                        tvChannelInfo.channelContentId = cursorQuery.getLong(0);
                        tvChannelInfo.progListType = cursorQuery.getLong(1);
                        tvChannelInfo.channelId = cursorQuery.getString(2);
                        tvChannelInfo.id = cursorQuery.getLong(3);
                        LogUtil.LogD("HotelMode", "getTvChannelInfo: " + tvChannelInfo.channelContentId + "/" + tvChannelInfo.progListType + "/" + tvChannelInfo.channelId + "/" + tvChannelInfo.id);
                    } else {
                        LogUtil.LogW("HotelMode", "Fail to getTvChannelInfo for _ID: " + l);
                        tvChannelInfo = null;
                    }
                } catch (Throwable th3) {
                    try {
                        throw th3;
                    } catch (Throwable th4) {
                        th = th3;
                        th2 = th4;
                        if (cursorQuery != null) {
                            throw th2;
                        }
                        if (th == null) {
                            cursorQuery.close();
                            throw th2;
                        }
                        cursorQuery.close();
                        throw th2;
                    }
                }
            } else {
                LogUtil.LogW("HotelMode", "Fail to getTvChannelInfo for _ID: " + l);
                tvChannelInfo = null;
            }
            if (cursorQuery == null) {
                return tvChannelInfo;
            }
            cursorQuery.close();
            return tvChannelInfo;
        } catch (Exception e) {
            LogUtil.LogE("HotelMode", e.getMessage());
            return null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:15:0x00ab A[Catch: all -> 0x00a6, Throwable -> 0x00a9, TRY_LEAVE, TryCatch #5 {all -> 0x00a6, Throwable -> 0x00a9, blocks: (B:8:0x0039, B:10:0x003f, B:15:0x00ab), top: B:38:0x0039 }] */
    /* JADX WARN: Code duplicated, block: B:24:0x00cf A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:29:0x00da A[Catch: Exception -> 0x00de, TryCatch #0 {Exception -> 0x00de, blocks: (B:6:0x002a, B:18:0x00c4, B:25:0x00d1, B:29:0x00da, B:28:0x00d6, B:30:0x00dd), top: B:33:0x002a, inners: #3 }] */
    /* JADX WARN: Code duplicated, block: B:36:0x00d1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:40:? A[Catch: Exception -> 0x00de, SYNTHETIC, TRY_LEAVE, TryCatch #0 {Exception -> 0x00de, blocks: (B:6:0x002a, B:18:0x00c4, B:25:0x00d1, B:29:0x00da, B:28:0x00d6, B:30:0x00dd), top: B:33:0x002a, inners: #3 }] */
    /* JADX WARN: Instruction removed from duplicated block: B:15:0x00ab, please report this as an issue */
    private static TvChannelInfoForJp getTvChannelInfoByIdForJp(Long l) throws Throwable {
        TvChannelInfoForJp tvChannelInfoForJp;
        Throwable th;
        Throwable th2;
        String[] strArr = {"content_id", SonyTvContract.Channels.ORIGINAL_NETWORK_ID, SonyTvContract.Channels.TRANSPORT_STREAM_ID, SonyTvContract.Channels.SERVICE_ID, CHANNELS_COLUMN_ID};
        if (l.longValue() == -1) {
            return null;
        }
        try {
            Cursor cursorQuery = mContext.getContentResolver().query(SonyTvContract.Channels.CONTENT_URI, strArr, "_id=?", new String[]{Long.toString(l.longValue())}, null);
            if (cursorQuery != null) {
                try {
                    if (cursorQuery.moveToFirst()) {
                        tvChannelInfoForJp = new TvChannelInfoForJp();
                        tvChannelInfoForJp.channelContentId = cursorQuery.getLong(0);
                        tvChannelInfoForJp.originalNetworkId = cursorQuery.getLong(1);
                        tvChannelInfoForJp.transportStreamId = cursorQuery.getLong(2);
                        tvChannelInfoForJp.serviceId = cursorQuery.getLong(3);
                        tvChannelInfoForJp.id = cursorQuery.getLong(4);
                        LogUtil.LogD("HotelMode", "getTvChannelInfoForJp: " + tvChannelInfoForJp.channelContentId + "/" + tvChannelInfoForJp.originalNetworkId + "/" + tvChannelInfoForJp.transportStreamId + "/" + tvChannelInfoForJp.serviceId + "/" + tvChannelInfoForJp.id);
                    } else {
                        LogUtil.LogW("HotelMode", "Fail to getTvChannelInfo for _ID: " + l);
                        tvChannelInfoForJp = null;
                    }
                } catch (Throwable th3) {
                    try {
                        throw th3;
                    } catch (Throwable th4) {
                        th = th3;
                        th2 = th4;
                        if (cursorQuery != null) {
                            throw th2;
                        }
                        if (th == null) {
                            cursorQuery.close();
                            throw th2;
                        }
                        cursorQuery.close();
                        throw th2;
                    }
                }
            } else {
                LogUtil.LogW("HotelMode", "Fail to getTvChannelInfo for _ID: " + l);
                tvChannelInfoForJp = null;
            }
            if (cursorQuery == null) {
                return tvChannelInfoForJp;
            }
            cursorQuery.close();
            return tvChannelInfoForJp;
        } catch (Exception unused) {
            return null;
        }
    }
}
