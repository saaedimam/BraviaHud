package com.sony.dtv.tvinput.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/* JADX INFO: loaded from: classes.dex */
public final class SonyTvContract {
    private static final String ASC = " ASC";
    public static final String AUTHORITY = "com.sony.dtv.tvinput.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://com.sony.dtv.tvinput.provider");
    public static final int DB_VER_ADD_CHANNEL_ID = 7;
    public static final int DB_VER_ADD_CHANNEL_ID_RULE = 10;
    public static final int DB_VER_ADD_IS_MULTI_BANK = 15;
    public static final int DB_VER_ADD_LAST_CHANNEL_INPUT_ID = 14;
    public static final int DB_VER_ADD_LOGICAL_CHANNEL_NUMBER = 4;
    public static final int DB_VER_ADD_NUMERIC_SELECTABLE = 16;
    public static final int DB_VER_ADD_OLD_CHANNEL_ID = 12;
    public static final int DB_VER_ADD_PACKAGE_NAME = 3;
    public static final int DB_VER_ADD_SVL_ID = 2;
    public static final int DB_VER_CREATE_FAVORITE_AND_FAVORITETAG = 8;
    public static final int DB_VER_CREATE_PRESET_CHANNEL = 11;
    public static final int DB_VER_IGNORE = 6;
    public static final int DB_VER_INITIAL = 1;
    public static final int DB_VER_NON_UPGRADE = 9;
    public static final int DB_VER_SET_OLD_CHANNEL_ID = 13;
    public static final int DB_VER_SYNC_LAST_WATCHED_CHANNEL = 5;
    private static final String INPUT_ID_BASE = "input_id";
    private static final String INTVALUE_1_BASE = "intval1";
    private static final String INTVALUE_2_BASE = "intval2";
    private static final String INTVALUE_3_BASE = "intval3";
    private static final String INTVALUE_4_BASE = "intval4";
    private static final String INTVALUE_5_BASE = "intval5";
    private static final String INTVALUE_6_BASE = "intval6";
    private static final float MAJOR_VERSION = 4.0f;
    private static final float MINOR_VERSION = 6.0f;
    public static final String PATH_CHANNEL = "sony_channel";
    public static final String PATH_FAVORITE = "favorite";
    public static final String PATH_FAVORITE_CHANNEL = "favorite_channel";
    public static final String PATH_FAVORITE_TAG = "favorite_tag";
    public static final String PATH_LAST_WATCHED_CHANNEL = "sony_last_channel";
    public static final String PATH_LAST_WATCHED_INPUT = "sony_last_input";
    public static final String PATH_LAST_WATCHED_SUB_INPUT = "sony_last_sub_input";
    public static final String PATH_NETWORK = "sony_network";
    public static final String PATH_PRESET_CHANNELS = "preset_channels";
    public static final String PATH_PRESET_CHANNELS_JOIN_CHANNEL = "preset_channels_join_channel";
    private static final String PROG_LIST_TYPE_BASE = "prog_list_type";
    private static final String TEXT_1_BASE = "text1";
    private static final String TEXT_2_BASE = "text2";
    private static final String TEXT_3_BASE = "text3";
    private static final String TEXT_4_BASE = "text4";
    private static final String URI_CONTENT = "content://";

    @Deprecated
    public static final int VERSION_MAJOR = 1;

    @Deprecated
    public static final int VERSION_MINOR = 6;

    public interface BaseSonyTvColumns extends BaseColumns {
    }

    public static float getVersionInfo() {
        return 4.6f;
    }

    private SonyTvContract() {
    }

    public static final class Channels implements BaseSonyTvColumns {
        public static final String BROADCASTER_ID = "broadcaster_id";
        public static final String BROADCAST_TYPE = "broadcast_type";
        public static final String BROWSABLE = "browsable";
        public static final String CHANNEL_CONTENT_ID = "content_id";
        public static final String CHANNEL_ID = "channel_id";
        public static final String CHANNEL_ID_RULE = "channel_id_rule";
        public static final String CHANNEL_UPDATE_TIME_UTC_MILLIS = "channel_update_time_utc_millis";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/sony_channel_v1";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/sony_channel_v1";
        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.dtv.tvinput.provider/sony_channel");
        public static final String CSX_CHANNEL_ID = "csx_channel_id";
        public static final String CSX_CHANNEL_IMAGE_URL = "csx_channel_image_url";
        public static final String CSX_CHANNEL_NAME = "csx_channel_name";
        public static final String CSX_CHANNEL_NUMBER = "csx_channel_number";
        public static final String CSX_CHANNEL_UPDATE_TIME_UTC_MILLIS = "csx_channel_update_time_utc_millis";
        public static final String DEFAULT_SORT_ORDER = "service_type, type, display_number ASC";
        public static final String DISPLAY_NAME = "display_name";
        public static final String DISPLAY_NUMBER = "display_number";
        public static final String EPG_SORTING_ORDER = "epg_sorting_order";
        public static final String EXTERNAL_INPUT_SELECTION = "prog_list_type>33554432";

        @Deprecated
        public static final String FAVORITE_CHANNEL_1 = "favorite_1";

        @Deprecated
        public static final String FAVORITE_CHANNEL_2 = "favorite_2";

        @Deprecated
        public static final String FAVORITE_CHANNEL_3 = "favorite_3";

        @Deprecated
        public static final String FAVORITE_CHANNEL_4 = "favorite_4";

        @Deprecated
        public static final String FAVORITE_ORDER_1 = "favorite_order_1";

        @Deprecated
        public static final String FAVORITE_ORDER_2 = "favorite_order_2";

        @Deprecated
        public static final String FAVORITE_ORDER_3 = "favorite_order_3";

        @Deprecated
        public static final String FAVORITE_ORDER_4 = "favorite_order_4";

        @Deprecated
        public static final String GRACE_NOTE_ID = "grace_note_id";
        public static final String INTERNAL_PROVIDER_DATA = "internal_provider_data";
        public static final String INTVALUE_1 = "intval1";
        public static final String INTVALUE_2 = "intval2";
        public static final String INTVALUE_3 = "intval3";
        public static final String INTVALUE_4 = "intval4";
        public static final String INTVALUE_5 = "intval5";
        public static final String INTVALUE_6 = "intval6";
        public static final String LOGICAL_CHANNEL_NUMBER = "logical_channel_number";
        public static final String LOGO_URI = "logo";
        public static final String MAJOR_CHANNEL_NUMBER = "major_channel_number";
        public static final String MINOR_CHANNEL_NUMBER = "minor_channel_number";
        public static final String MW_CHANNEL_ID = "mw_channel_id";
        public static final String NUMERIC_SELECTABLE = "numeric_selectable";
        public static final String OLD_CHANNEL_ID = "old_channel_id";
        public static final String ORIGINAL_NETWORK_ID = "original_network_id";
        public static final String PACKAGE_NAME = "package_name";
        public static final String PAY_CHANNEL_FLAG = "pay_channel_flag";
        public static final String PHYSICAL_CHANNEL = "physical_channel";

        @Deprecated
        public static final String PRESET_NUMBER = "preset_number";
        public static final String PROGRAM_LOCK = "program_lock";
        public static final String PROG_LIST_TYPE = "prog_list_type";
        public static final int PROG_LIST_TYPE_ADVBSD = 17039873;
        public static final int PROG_LIST_TYPE_ADVCSD = 17040130;
        public static final int PROG_LIST_TYPE_ANALOG = 16777217;
        public static final int PROG_LIST_TYPE_BS = 17039872;
        public static final int PROG_LIST_TYPE_CABLE = 17039616;

        @Deprecated
        public static final int PROG_LIST_TYPE_CAM = 16909057;
        public static final int PROG_LIST_TYPE_CI = 16908801;
        public static final int PROG_LIST_TYPE_COMPONENT = 33555200;
        public static final int PROG_LIST_TYPE_COMPOSITE = 33554944;
        public static final int PROG_LIST_TYPE_CS = 17040128;
        public static final int PROG_LIST_TYPE_DIGITAL = 16908288;
        public static final int PROG_LIST_TYPE_EXTERNAL = 33554432;
        public static final int PROG_LIST_TYPE_GENSAT = 16908544;
        public static final int PROG_LIST_TYPE_HDMI = 33554688;
        public static final int PROG_LIST_TYPE_OTHER = 0;
        public static final int PROG_LIST_TYPE_PC = 33555712;
        public static final int PROG_LIST_TYPE_PREFSAT = 16908545;
        public static final int PROG_LIST_TYPE_SCART = 33555456;
        public static final int PROG_LIST_TYPE_SKY = 17040129;
        public static final int PROG_LIST_TYPE_STB = 33554433;
        public static final int PROG_LIST_TYPE_TERR = 17039360;
        public static final int PROG_LIST_TYPE_TUNER = 16777216;
        public static final int PROG_LIST_TYPE_VIRTUAL_TUNER = 17301504;
        public static final String SEARCHABLE = "searchable";
        public static final String SERVICE_ID = "service_id";
        public static final String SERVICE_TYPE = "service_type";
        public static final int SERVICE_TYPE_AUDIO = 2;
        public static final int SERVICE_TYPE_AUDIO_VIDEO = 1;
        public static final int SERVICE_TYPE_OTHER = 0;
        public static final String SORTING_CHANNEL = "sorting_channel";
        public static final String SORTING_ORDER = "sorting_order";
        public static final String SVL_ID = "svl_id";
        public static final String TABLE_NAME = "channels";
        public static final String TEXT_1 = "text1";
        public static final String TEXT_2 = "text2";
        public static final String TEXT_3 = "text3";
        public static final String TEXT_4 = "text4";
        public static final String TRANSPORT_STREAM_ID = "transport_stream_id";
        public static final String TUNER_INPUT_SELECTION = "prog_list_type>16777216 AND prog_list_type<33554432";
        public static final String TYPE = "type";
        public static final int TYPE_1SEG = 263168;
        public static final int TYPE_ATSC_C = 197120;
        public static final int TYPE_ATSC_M_H = 197376;
        public static final int TYPE_ATSC_T = 196608;
        public static final int TYPE_CMMB = 327936;
        public static final int TYPE_DTMB = 327680;
        public static final int TYPE_DVB_C = 131584;
        public static final int TYPE_DVB_C2 = 131585;
        public static final int TYPE_DVB_H = 131840;
        public static final int TYPE_DVB_S = 131328;
        public static final int TYPE_DVB_S2 = 131329;
        public static final int TYPE_DVB_SH = 132096;
        public static final int TYPE_DVB_T = 131072;
        public static final int TYPE_DVB_T2 = 131073;
        public static final int TYPE_ISDB_C = 262912;
        public static final int TYPE_ISDB_S = 262656;
        public static final int TYPE_ISDB_T = 262144;
        public static final int TYPE_ISDB_TB = 262400;
        public static final int TYPE_OTHER = 0;
        public static final int TYPE_PASSTHROUGH = 65536;
        public static final int TYPE_S_DMB = 393472;
        public static final int TYPE_T_DMB = 393216;
        public static final String UPDATE_COUNT = "update_count";
        public static final String VERSION_NUMBER = "version_number";

        private Channels() {
        }

        public static String getDetailColumnInfo(String str) {
            return "channels." + str;
        }
    }

    public static final class LastWatchedChannel implements BaseSonyTvColumns {
        public static final String CHANNEL_ID = "channel_id";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/sony_last_channel_v1";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/sony_last_channel_v1";
        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.dtv.tvinput.provider/sony_last_channel");
        public static final String DEFAULT_SORT_ORDER = "last_watched_time_utc_millis DESC";
        public static final String INPUT_ID = "input_id";
        public static final String INTVALUE_1 = "intval1";
        public static final String INTVALUE_2 = "intval2";
        public static final String INTVALUE_3 = "intval3";
        public static final String INTVALUE_4 = "intval4";
        public static final String INTVALUE_5 = "intval5";
        public static final String INTVALUE_6 = "intval6";

        @Deprecated
        public static final String LAST_WATCHED_TIME_UTC_MILLIS = "last_watched_time_utc_millis";
        public static final String PROG_LIST_TYPE = "prog_list_type";
        public static final String SEQUENCE_NO = "sequence_no";
        public static final String TABLE_NAME = "last_watched_ch";
        public static final String TEXT_1 = "text1";
        public static final String TEXT_2 = "text2";
        public static final String TEXT_3 = "text3";
        public static final String TEXT_4 = "text4";

        private LastWatchedChannel() {
        }

        public static String getDetailColumnInfo(String str) {
            return "last_watched_ch." + str;
        }
    }

    public static final class LastWatchedInput implements BaseSonyTvColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/sony_last_input_v1";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/sony_last_input_v1";
        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.dtv.tvinput.provider/sony_last_input");
        public static final String DEFAULT_SORT_ORDER = "_id ASC";
        public static final String INPUT_ID = "input_id";
        public static final String INPUT_TYPE = "input_type";
        public static final int INPUT_TYPE_EXTERNAL = 16;
        public static final int INPUT_TYPE_OTHER = 256;
        public static final int INPUT_TYPE_TUNER = 1;
        public static final String INTVALUE_1 = "intval1";
        public static final String INTVALUE_2 = "intval2";
        public static final String INTVALUE_3 = "intval3";
        public static final String INTVALUE_4 = "intval4";
        public static final String INTVALUE_5 = "intval5";
        public static final String INTVALUE_6 = "intval6";
        public static final String PARENT_ID = "parent_id";
        public static final String TABLE_NAME = "last_watched_input";
        public static final String TEXT_1 = "text1";
        public static final String TEXT_2 = "text2";
        public static final String TEXT_3 = "text3";
        public static final String TEXT_4 = "text4";

        private LastWatchedInput() {
        }

        public static String getDetailColumnInfo(String str) {
            return "last_watched_input." + str;
        }
    }

    public static final class LastWatchedSubInput implements BaseSonyTvColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/sony_last_sub_input_v1";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/sony_last_sub_input_v1";
        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.dtv.tvinput.provider/sony_last_sub_input");
        public static final String DEFAULT_SORT_ORDER = "_id ASC";
        public static final String INPUT_ID = "input_id";
        public static final String INPUT_TYPE = "input_type";
        public static final String INTVALUE_1 = "intval1";
        public static final String INTVALUE_2 = "intval2";
        public static final String INTVALUE_3 = "intval3";
        public static final String INTVALUE_4 = "intval4";
        public static final String INTVALUE_5 = "intval5";
        public static final String INTVALUE_6 = "intval6";
        public static final String PARENT_ID = "parent_id";
        public static final String TABLE_NAME = "last_watched_sub_input";
        public static final String TEXT_1 = "text1";
        public static final String TEXT_2 = "text2";
        public static final String TEXT_3 = "text3";
        public static final String TEXT_4 = "text4";

        private LastWatchedSubInput() {
        }

        public static String getDetailColumnInfo(String str) {
            return "last_watched_sub_input." + str;
        }
    }

    public static final class Network implements BaseSonyTvColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/sony_network_v1";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/sony_network_v1";
        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.dtv.tvinput.provider/sony_network");
        public static final String CSX_CHANNEL_LIST_ID = "csx_channel_list_id";
        public static final String CSX_ID = "csx_channel_list_id";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";
        public static final String INTVALUE_1 = "intval1";
        public static final String INTVALUE_2 = "intval2";
        public static final String INTVALUE_3 = "intval3";
        public static final String INTVALUE_4 = "intval4";
        public static final String INTVALUE_5 = "intval5";
        public static final String INTVALUE_6 = "intval6";
        public static final int INVALID = 0;
        public static final String IS_MULTI_BANK = "is_multi_bank";
        public static final String IS_VALID = "is_valid";
        public static final String PROFILE_NAME = "profile_name";
        public static final String PROG_LIST_TYPE = "prog_list_type";
        public static final String TABLE_NAME = "network";
        public static final String TEXT_1 = "text1";
        public static final String TEXT_2 = "text2";
        public static final String TEXT_3 = "text3";
        public static final String TEXT_4 = "text4";
        public static final int VALID = 1;

        private Network() {
        }

        public static String getDetailColumnInfo(String str) {
            return "network." + str;
        }
    }

    public static final class Favorite implements BaseSonyTvColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/favorite_v1";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/favorite_v1";
        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.dtv.tvinput.provider/favorite");
        public static final String CUSTOM_LABEL = "custom_label";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";
        public static final int FAVORITE_1_ID = 0;
        public static final int FAVORITE_2_ID = 1;
        public static final int FAVORITE_3_ID = 2;
        public static final int FAVORITE_4_ID = 3;
        public static final String TABLE_NAME = "favorite";

        private Favorite() {
        }

        public static String getDetailColumnInfo(String str) {
            return "favorite." + str;
        }
    }

    public static final class FavoriteTag implements BaseSonyTvColumns {
        public static final String CHANNEL_CONTENT_ID = "content_id";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/favorite_tag_v1";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/favorite_tag_v1";
        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.dtv.tvinput.provider/favorite_tag");
        public static final Uri CONTENT_URI_JOIN_CHANNELS = Uri.parse("content://com.sony.dtv.tvinput.provider/favorite_channel");
        public static final String DEFAULT_JOIN_CHANNEL_SORT_ORDER = "favorite_tag._id ASC";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";
        public static final String FAVORITE_ID = "favorite_id";
        public static final String FAVORITE_ORDER = "favorite_order";
        public static final String INPUT_ID = "input_id";
        public static final String PACKAGE_NAME = "package_name";
        public static final String PROG_LIST_TYPE = "prog_list_type";
        public static final String TABLE_NAME = "favorite_tag";

        private FavoriteTag() {
        }

        public static String getDetailColumnInfo(String str) {
            return "favorite_tag." + str;
        }
    }

    public static final class PresetChannels implements BaseSonyTvColumns {
        public static final String CHANNEL_CONTENT_ID = "content_id";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/preset_channels_v1";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/preset_channels_v1";
        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.dtv.tvinput.provider/preset_channels");
        public static final Uri CONTENT_URI_JOIN_CHANNELS = Uri.parse("content://com.sony.dtv.tvinput.provider/preset_channels_join_channel");
        public static final String DEFAULT_JOIN_CHANNEL_SORT_ORDER = "preset_channels._id ASC";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";
        public static final String PRESET_NUMBER = "preset_number";
        public static final String PROG_LIST_TYPE = "prog_list_type";
        public static final String TABLE_NAME = "preset_channels";

        private PresetChannels() {
        }

        public static String getDetailColumnInfo(String str) {
            return "preset_channels." + str;
        }
    }
}
