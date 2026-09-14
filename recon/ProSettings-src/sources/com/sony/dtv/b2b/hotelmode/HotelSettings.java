package com.sony.dtv.b2b.hotelmode;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public final class HotelSettings {
    public static final String AC_POWER_ON = "ac_power_on";
    public static final String ANYTIME_USB_CHARGE = "anytime_usb_charge";
    public static final String BATHSP_INITIAL_VOLUME = "bathsp_initial_volume";
    public static final String CLEAR_ACCOUNT = "clear_account";
    public static final String CLEAR_BML_NVRAM_DATA = "clear_bml_nvram_data";
    public static final String CLEAR_DATA_APPLICATION_LIST = "clear_data_application_list";
    public static final String CLEAR_WIFI_DIRECT_DEVICES = "clear_wifi_direct_devices";
    public static final String CUSTOM_HOME_URI = "custom_home_uri";
    private static final boolean DEBUG = false;
    public static final String DISABLED_APPLICATION_LIST = "disabled_application_list";
    public static final String ENABLED_APPLICATION_LIST = "enabled_application_list";
    public static final String ENABLE_GOOGLE_CAST = "enable_google_cast";
    public static final String HOME_KEY_BEHAVIOR = "home_key_behavior";
    public static final String HOTEL_MODE = "hotel_mode";
    public static final String IGNORED_APPLICATION_LIST = "ignored_application_list";
    public static final String INITIAL_INPUT = "initial_input";
    public static final String INITIAL_TV_CHANNEL = "initial_tv_channel";
    public static final String INITIAL_TV_CHANNEL_ID = "initial_tv_channel_id";
    public static final String INITIAL_TV_ORIGINAL_NETWORK_ID = "initial_tv_original_network_id";
    public static final String INITIAL_TV_PROG_LIST_TYPE = "initial_tv_prog_list_type";
    public static final String INITIAL_TV_SERVICE_ID = "initial_tv_service_id";
    public static final String INITIAL_TV_TRANSPORT_STREAM_ID = "initial_tv_transport_stream_id";
    public static final String INITIAL_VOLUME = "initial_volume";
    public static final String INPUT_DETECTION_PREFIX = "InputDetection://";
    public static final String INTERNAL_MODE = "internal_mode";
    public static final String IR_PASS_THROUGH = "ir_pass_through";
    public static final String KEY_CONTROL = "key_control";
    public static final String KEY_CONTROL_SETTING = "key_control_setting";
    public static final String LINEOUT_OFFSET = "lineout_offset";
    public static final String MAXIMUM_VOLUME = "maximum_volume";
    public static final String NAME = "name";
    public static final String NTP_SERVER = "ntp_server";
    public static final String PC_INPUT_OPTIMISATION = "pc_input_optimisation";
    public static final String PERIODIC_RESTART = "periodic_restart";
    public static final String PERIODIC_RESTART_FORCIBLY = "periodic_restart_forcibly";
    public static final String PERIODIC_RESTART_HOUR = "periodic_restart_hour";
    public static final String PERIODIC_RESTART_MINUTE = "periodic_restart_minute";
    public static final String PERIODIC_RESTART_WEEKLY_FRIDAY = "periodic_restart_weekly_friday";
    public static final String PERIODIC_RESTART_WEEKLY_MONDAY = "periodic_restart_weekly_monday";
    public static final String PERIODIC_RESTART_WEEKLY_SATURDAY = "periodic_restart_weekly_saturday";
    public static final String PERIODIC_RESTART_WEEKLY_SUNDAY = "periodic_restart_weekly_sunday";
    public static final String PERIODIC_RESTART_WEEKLY_THURSDAY = "periodic_restart_weekly_thursday";
    public static final String PERIODIC_RESTART_WEEKLY_TUESDAY = "periodic_restart_weekly_tuesday";
    public static final String PERIODIC_RESTART_WEEKLY_WEDNESDAY = "periodic_restart_weekly_wednesday";
    public static final String PIN_PROTECTION = "pin_protection";
    public static final String POWER_OFF_ON_SIGNAL_LOSS = "power_off_on_signal_loss";
    public static final String POWER_SCHEDULING = "power_scheduling";
    public static final String POWER_SCHEDULING_OFF_HOUR = "power_scheduling_off_hour";
    public static final String POWER_SCHEDULING_OFF_MINUTE = "power_scheduling_off_minute";
    public static final String POWER_SCHEDULING_ON_HOUR = "power_scheduling_on_hour";
    public static final String POWER_SCHEDULING_ON_MINUTE = "power_scheduling_on_minute";
    public static final String POWER_SCHEDULING_WEEKLY_FRIDAY = "power_scheduling_weekly_friday";
    public static final String POWER_SCHEDULING_WEEKLY_MONDAY = "power_scheduling_weekly_monday";
    public static final String POWER_SCHEDULING_WEEKLY_SATURDAY = "power_scheduling_weekly_saturday";
    public static final String POWER_SCHEDULING_WEEKLY_SUNDAY = "power_scheduling_weekly_sunday";
    public static final String POWER_SCHEDULING_WEEKLY_THURSDAY = "power_scheduling_weekly_thursday";
    public static final String POWER_SCHEDULING_WEEKLY_TUESDAY = "power_scheduling_weekly_tuesday";
    public static final String POWER_SCHEDULING_WEEKLY_WEDNESDAY = "power_scheduling_weekly_wednesday";
    public static final String POWER_STATE_CONTROL = "power_state_control";
    public static final String QUICK_LAUNCHER_MODE = "quick_launcher_mode";
    public static final String QUICK_SETTINGS_MODE = "quick_settings_mode";
    public static final String RESTORE_PICTURE_SOUND_SETTINGS = "restore_picture_sound_settings";
    public static final String SCREEN_MIRRORING = "screen_mirroring";
    private static final String SELECTION_NAME = "name=?";
    public static final String SIGNAL_DETECT_BEGIN_HOUR = "signal_detect_begin_hour";
    public static final String SIGNAL_DETECT_BEGIN_MINUTE = "signal_detect_begin_minute";
    public static final String SIGNAL_DETECT_END_HOUR = "signal_detect_end_hour";
    public static final String SIGNAL_DETECT_END_MINUTE = "signal_detect_end_minute";
    public static final String SIGNAL_SOURCE_PREFIX = "SignalSource://";
    public static final String SILENT_APK_INSTALL = "silent_apk_install";
    public static final String SI_MODE_SELECTION = "si_mode_selection";
    public static final String SI_MODE_SELECTION_TARGET = "si_mode_selection_target";
    public static final String STARTUP_ANDROID_APPLICATION = "startup_android_application";
    public static final String STARTUP_ANDROID_APPLICATION_ACTIVITY = "startup_android_application_activity";
    public static final String STARTUP_ANDROID_APPLICATION_DATA = "startup_android_application_data";
    public static final String STARTUP_ANDROID_APPLICATION_EXTRA_NAME = "startup_android_application_extra_name";
    public static final String STARTUP_ANDROID_APPLICATION_EXTRA_VALUE = "startup_android_application_extra_value";
    public static final String STARTUP_ANDROID_APPLICATION_PACKAGE = "startup_android_application_package";
    public static final String STARTUP_APPLICATION = "startup_application";
    public static final String STARTUP_APPLICATION_URI = "startup_application_uri";
    private static final String TAG = "HotelSettings";
    public static final String VALUE_AC_ALWAYS_ON = "AlwaysOn";
    public static final String VALUE_AC_ALWAYS_STANDBY = "AlwaysStandby";
    public static final String VALUE_AC_STANDARD = "Standard";
    public static final String VALUE_HOME_CUSTOM = "Custom";
    public static final String VALUE_HOME_DISABLED = "Disabled";
    public static final String VALUE_HOME_STANDARD = "Standard";
    public static final String VALUE_HOME_TOGGLE = "Toggle";
    public static final String VALUE_MODE_HOTEL = "HotelMode";
    public static final String VALUE_MODE_HOTEL_SETTINGS = "HotelSettingsMode";
    public static final String VALUE_MODE_IMPORT_ON_HOTEL = "ImportOnHotelMode";
    public static final String VALUE_MODE_NORMAL = "NormalMode";
    public static final String VALUE_OFF = "Off";
    public static final String VALUE_ON = "On";
    public static final String VALUE_ON_REMOTEKEY_ONLY = "RemoteKeyOnly";
    public static final String VALUE_ON_TVKEY_ONLY = "TvKeyOnly";
    public static final String VALUE_PERIODIC_RESTART_DAILY = "PeriodicRestartDaily";
    public static final String VALUE_PERIODIC_RESTART_OFF = "PeriodicRestartOff";
    public static final String VALUE_PERIODIC_RESTART_WEEKLY = "PeriodicRestartWeekly";
    public static final String VALUE_POWER_SCHEDULING_DAILY = "PowerSchedulingDaily";
    public static final String VALUE_POWER_SCHEDULING_OFF = "PowerSchedulingOff";
    public static final String VALUE_POWER_SCHEDULING_WEEKLY = "PowerSchedulingWeekly";
    public static final String VALUE_QUICK_LAUNCHER_MODE_DEFAULT = "Default";
    public static final String VALUE_QUICK_LAUNCHER_MODE_SIMPLE = "Simple";
    public static final String VALUE_QUICK_SETTINGS_MODE_DEFAULT = "Default";
    public static final String VALUE_QUICK_SETTINGS_MODE_OFF = "Off";
    public static final String VALUE_QUICK_SETTINGS_MODE_SIMPLE = "Simple";
    public static final String VALUE_SIGNAL_NOT_USE = "NotUse";
    public static final String VALUE_SIGNAL_USE = "Use";
    public static final String VALUE_SIGNAL_USE_WO_LABEL = "UseWithoutLable";
    public static final String VALUE_SI_LOCATEL = "Locatel";
    public static final String VALUE_SI_OFF = "Off";
    public static final String VALUE_SI_STANDARD = "Standard";
    public static final String VALUE_SI_VDA = "VDA";
    public static final String WAKEUP_ON_SIGNAL = "wakeup_on_signal";
    public static final String WEB_APPS_INSTALLATION = "web_apps_installation";
    public static final String WIDE_ZOOM_KEY = "wide_zoom_key";
    public static final Uri CONTENT_URI = Uri.parse("content://com.sony.dtv.b2b.hotelsettings/hotel");
    public static final String VALUE = "value";
    private static final String[] PROJECTION_VALUE = {VALUE};

    public static boolean putString(ContentResolver contentResolver, String str, String str2) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NAME, str);
            contentValues.put(VALUE, str2);
            contentResolver.insert(CONTENT_URI, contentValues);
            return true;
        } catch (SQLException unused) {
            Log.w(TAG, "Can't set key: " + str);
            return false;
        } catch (IllegalArgumentException unused2) {
            Log.w(TAG, "Can't set key: " + str);
            return false;
        }
    }

    public static String getString(ContentResolver contentResolver, String str) throws Throwable {
        Cursor cursorQuery;
        try {
            cursorQuery = contentResolver.query(CONTENT_URI, PROJECTION_VALUE, SELECTION_NAME, new String[]{str}, null);
            try {
                if (cursorQuery == null) {
                    Log.w(TAG, "Can't get key: " + str);
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    return null;
                }
                String string = cursorQuery.moveToFirst() ? cursorQuery.getString(0) : null;
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                return string;
            } catch (Throwable th) {
                th = th;
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            cursorQuery = null;
        }
    }

    public static boolean putInt(ContentResolver contentResolver, String str, int i) {
        return putString(contentResolver, str, Integer.toString(i));
    }

    public static int getInt(ContentResolver contentResolver, String str, int i) throws Throwable {
        String string = getString(contentResolver, str);
        if (string == null) {
            return i;
        }
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    public static boolean putLong(ContentResolver contentResolver, String str, long j) {
        return putString(contentResolver, str, Long.toString(j));
    }

    public static long getLong(ContentResolver contentResolver, String str, long j) throws Throwable {
        String string = getString(contentResolver, str);
        if (string == null) {
            return j;
        }
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException unused) {
            return j;
        }
    }

    public static boolean putFloat(ContentResolver contentResolver, String str, float f) {
        return putString(contentResolver, str, Float.toString(f));
    }

    public static float getFloat(ContentResolver contentResolver, String str, float f) throws Throwable {
        String string = getString(contentResolver, str);
        if (string == null) {
            return f;
        }
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException unused) {
            return f;
        }
    }

    public static boolean isHotelMode(ContentResolver contentResolver) throws Throwable {
        String string = getString(contentResolver, HOTEL_MODE);
        return string != null && string.equals(VALUE_MODE_HOTEL);
    }
}
