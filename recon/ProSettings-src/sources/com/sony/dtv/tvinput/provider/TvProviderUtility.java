package com.sony.dtv.tvinput.provider;

import android.content.Context;
import android.database.Cursor;
import com.mediatek.twoworlds.tv.common.MtkTvChCommon;
import com.sony.dtv.tvinput.provider.internal.utils.LogUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class TvProviderUtility {
    public static final String CHANNEL_NOTIFY_ACTION = "com.sony.dtv.tvinput.datamgr.channels";
    public static final String COLUMN_IS_PAY_PROGRAM = "internal_provider_flag1";
    public static final String COLUMN_IS_SHARED_EVENT = "internal_provider_flag2";
    private static final Map<Integer, String> DB_NAME_CONVERION_MAP = new HashMap();
    private static final int EVENT_VERSION = 1;
    public static final int F_EVENT = 2;
    public static final int INVALID_ID = -1;
    public static final String PROGRAM_NOTIFY_ACTION = "com.sony.dtv.tvinput.datamgr.programs";
    private static final Map<String, Integer> PROG_LIST_TYPE_CONVERION_MAP;
    public static final String PROVIDER_API_GET_VERSION_INFO = "PROVIDER_API_GET_VERSION_INFO";
    public static final String PROVIDER_API_RESULT_KEY_VERSION_INFO = "PROVIDER_API_RESULT_KEY_VERSION_INFO";
    public static final int P_EVENT = 1;
    public static final String SCAN_COMPLETE_NOTIFY_ACTION = "com.sony.dtv.tvinput.datamgr.scan_complete";
    public static final int SCH_EVENT = 3;
    public static final String SELECTION_CHANNEL_PACKAGE = "package_name=\"com.sony.dtv.tvinput.tuner\"";
    public static final String SELECTION_EVENT_PACKAGE = "package_name=\"com.sony.dtv.tvinput.tuner\"";
    public static final String SELECTION_PACKAGE_NAME = "com.sony.dtv.tvinput.tuner";
    private static final String TAG = "EventData";
    public static final String TARGET_REGION_CHANGED_NOTIFY_ACTION = "com.sony.dtv.tvinput.datamgr.target_region_changed";
    private static final String TVINPUT = "com.sony.dtv.tvinput";
    private static final String TVINPUT_PROVIDER = "com.sony.dtv.tvinput.provider";
    public static final int TYPE_ARIB = 2;
    public static final int TYPE_GENERAL = 1;

    @Deprecated
    public static final String UPDATE_SONY_CHANNEL_ACTION = "com.sony.dtv.tvinput.provider.UPDATE_SONY_CHANNEL_ACTION";

    @Deprecated
    public static final String UPDATE_SONY_FAVORITE_ACTION = "com.sony.dtv.tvinput.provider.UPDATE_SONY_FAVORITE_ACTION";

    @Deprecated
    public static final String UPDATE_SONY_FAVORITE_TAG_ACTION = "com.sony.dtv.tvinput.provider.UPDATE_SONY_FAVORITE_TAG_ACTION";

    @Deprecated
    public static final String UPDATE_SONY_LAST_WATCHED_CHANNEL_ACTION = "com.sony.dtv.tvinput.provider.UPDATE_SONY_LAST_WATCHED_CHANNEL_ACTION";

    @Deprecated
    public static final String UPDATE_SONY_LAST_WATCHED_INPUT_ACTION = "com.sony.dtv.tvinput.provider.UPDATE_SONY_LAST_WATCHED_INPUT_ACTION";

    @Deprecated
    public static final String UPDATE_SONY_LAST_WATCHED_SUB_INPUT_ACTION = "com.sony.dtv.tvinput.provider.UPDATE_SONY_LAST_WATCHED_SUB_INPUT_ACTION";

    @Deprecated
    public static final String UPDATE_SONY_NETWORK_ACTION = "com.sony.dtv.tvinput.provider.UPDATE_SONY_NETWORK_ACTION";

    @Deprecated
    public static final String UPDATE_SONY_PRESET_CHANNELS_ACTION = "com.sony.dtv.tvinput.provider.UPDATE_SONY_PRESET_CHANNELS_ACTION";

    static {
        DB_NAME_CONVERION_MAP.put(Integer.valueOf(SonyTvContract.Channels.PROG_LIST_TYPE_ANALOG), MtkTvChCommon.DB_ATV);
        DB_NAME_CONVERION_MAP.put(16908288, MtkTvChCommon.DB_DTV);
        DB_NAME_CONVERION_MAP.put(16908544, MtkTvChCommon.DB_SAT);
        DB_NAME_CONVERION_MAP.put(16908545, MtkTvChCommon.DB_SAT_PRF);
        DB_NAME_CONVERION_MAP.put(16908801, MtkTvChCommon.DB_CI_PLUS);
        DB_NAME_CONVERION_MAP.put(17039360, MtkTvChCommon.DB_AIR);
        DB_NAME_CONVERION_MAP.put(17039616, MtkTvChCommon.DB_CAB);
        DB_NAME_CONVERION_MAP.put(16909057, MtkTvChCommon.DB_CI_PLUS);
        PROG_LIST_TYPE_CONVERION_MAP = new HashMap();
        PROG_LIST_TYPE_CONVERION_MAP.put(MtkTvChCommon.DB_ATV, Integer.valueOf(SonyTvContract.Channels.PROG_LIST_TYPE_ANALOG));
        PROG_LIST_TYPE_CONVERION_MAP.put(MtkTvChCommon.DB_DTV, 16908288);
        PROG_LIST_TYPE_CONVERION_MAP.put(MtkTvChCommon.DB_SAT, 16908544);
        PROG_LIST_TYPE_CONVERION_MAP.put(MtkTvChCommon.DB_SAT_PRF, 16908545);
        PROG_LIST_TYPE_CONVERION_MAP.put(MtkTvChCommon.DB_CI_PLUS, 16908801);
        PROG_LIST_TYPE_CONVERION_MAP.put(MtkTvChCommon.DB_AIR, 17039360);
        PROG_LIST_TYPE_CONVERION_MAP.put(MtkTvChCommon.DB_CAB, 17039616);
    }

    private TvProviderUtility() {
    }

    public static String getDbNameByProgListType(int i) {
        if (DB_NAME_CONVERION_MAP.containsKey(Integer.valueOf(i))) {
            return DB_NAME_CONVERION_MAP.get(Integer.valueOf(i));
        }
        return null;
    }

    public static int getProgListTypeByDbName(String str) {
        if (PROG_LIST_TYPE_CONVERION_MAP.containsKey(str)) {
            return PROG_LIST_TYPE_CONVERION_MAP.get(str).intValue();
        }
        return -1;
    }

    public static int getNextUpdateCount(Context context) {
        Cursor cursorQuery = context.getContentResolver().query(SonyTvContract.Channels.CONTENT_URI, new String[]{SonyTvContract.Channels.UPDATE_COUNT}, null, null, SonyTvContract.Channels.UPDATE_COUNT);
        int i = 0;
        if (cursorQuery != null && cursorQuery.getCount() > 0 && cursorQuery.moveToLast()) {
            i = cursorQuery.getInt(0) + 1;
        }
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        return i;
    }

    public static class InternalProviderEventData implements Serializable {
        private int broadcastType;
        private int eventId;
        private long mEndTime;
        private int mMtkChannelId;
        private int mMtkEventId;
        private long mStartTime;
        private int pf;
        private int serviceId;
        private int sharedEventId;
        private int sharedNetworkId;
        private int sharedServiceId;
        private int sharedTsId;
        private int type;

        public InternalProviderEventData() {
            this.type = 0;
            this.pf = 0;
            this.mStartTime = 0L;
            this.mEndTime = 0L;
            this.mMtkChannelId = -1;
            this.mMtkEventId = -1;
            this.broadcastType = 0;
            this.serviceId = -1;
            this.eventId = -1;
            this.sharedNetworkId = -1;
            this.sharedTsId = -1;
            this.sharedServiceId = -1;
            this.sharedEventId = -1;
            this.type = 0;
            this.pf = 0;
            this.mMtkChannelId = -1;
            this.mMtkEventId = -1;
            this.broadcastType = 0;
            this.serviceId = -1;
            this.eventId = -1;
            this.sharedNetworkId = -1;
            this.sharedTsId = -1;
            this.sharedServiceId = -1;
            this.sharedEventId = -1;
            this.mStartTime = 0L;
            this.mEndTime = 0L;
        }

        public InternalProviderEventData(int i, int i2, int i3) {
            this(i, i2, i3, 0L, 0L);
        }

        public InternalProviderEventData(int i, int i2, int i3, long j, long j2) {
            this.type = 0;
            this.pf = 0;
            this.mStartTime = 0L;
            this.mEndTime = 0L;
            this.mMtkChannelId = -1;
            this.mMtkEventId = -1;
            this.broadcastType = 0;
            this.serviceId = -1;
            this.eventId = -1;
            this.sharedNetworkId = -1;
            this.sharedTsId = -1;
            this.sharedServiceId = -1;
            this.sharedEventId = -1;
            this.type = 1;
            this.pf = i;
            this.mMtkChannelId = i2;
            this.mMtkEventId = i3;
            this.mStartTime = j;
            this.mEndTime = j2;
        }

        public InternalProviderEventData(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
            this(i, i2, i3, i4, i5, i6, i7, 0, 0L, 0L);
        }

        public InternalProviderEventData(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, long j, long j2) {
            this.type = 0;
            this.pf = 0;
            this.mStartTime = 0L;
            this.mEndTime = 0L;
            this.mMtkChannelId = -1;
            this.mMtkEventId = -1;
            this.broadcastType = 0;
            this.serviceId = -1;
            this.eventId = -1;
            this.sharedNetworkId = -1;
            this.sharedTsId = -1;
            this.sharedServiceId = -1;
            this.sharedEventId = -1;
            this.type = 2;
            this.broadcastType = i;
            this.serviceId = i2;
            this.eventId = i3;
            this.sharedNetworkId = i4;
            this.sharedTsId = i5;
            this.sharedServiceId = i6;
            this.sharedEventId = i7;
            this.pf = i8;
            this.mStartTime = j;
            this.mEndTime = j2;
        }

        public InternalProviderEventData(byte[] bArr) {
            this(bArr, 0);
        }

        public InternalProviderEventData(byte[] bArr, int i) {
            this.type = 0;
            this.pf = 0;
            this.mStartTime = 0L;
            this.mEndTime = 0L;
            this.mMtkChannelId = -1;
            this.mMtkEventId = -1;
            this.broadcastType = 0;
            this.serviceId = -1;
            this.eventId = -1;
            this.sharedNetworkId = -1;
            this.sharedTsId = -1;
            this.sharedServiceId = -1;
            this.sharedEventId = -1;
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                readObject(objectInputStream, i);
                objectInputStream.close();
                byteArrayInputStream.close();
            } catch (IOException unused) {
                LogUtil.w(TvProviderUtility.TAG, "InternalProviderEventData: IOException");
            } catch (ClassNotFoundException unused2) {
                LogUtil.w(TvProviderUtility.TAG, "InternalProviderEventData: ClassNotFoundException");
            }
        }

        public byte[] getAsByteArray() {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                writeObject(objectOutputStream);
                objectOutputStream.close();
                byteArrayOutputStream.close();
                return byteArrayOutputStream.toByteArray();
            } catch (IOException unused) {
                LogUtil.w(TvProviderUtility.TAG, "getAsByteArray: IOException");
                return new byte[0];
            }
        }

        public int getType() {
            return this.type;
        }

        public int getpf() {
            return this.pf;
        }

        public int getMtkChannelId() {
            return this.mMtkChannelId;
        }

        public int getMtkEventId() {
            return this.mMtkEventId;
        }

        public int getBroadcastType() {
            return this.broadcastType;
        }

        public int getServiceId() {
            return this.serviceId;
        }

        public int getEventId() {
            return this.eventId;
        }

        public int getSharedNetworkId() {
            return this.sharedNetworkId;
        }

        public int getSharedTsId() {
            return this.sharedTsId;
        }

        public int getSharedServiceId() {
            return this.sharedServiceId;
        }

        public int getSharedEventId() {
            return this.sharedEventId;
        }

        public boolean isValid() {
            if (this.type == 1) {
                return (this.mMtkChannelId == -1 || this.mMtkEventId == -1) ? false : true;
            }
            return (this.type != 2 || this.serviceId == -1 || this.eventId == -1) ? false : true;
        }

        public boolean isSharedValid() {
            return (this.type != 2 || this.sharedNetworkId == -1 || this.sharedTsId == -1 || this.sharedServiceId == -1 || this.sharedEventId == -1) ? false : true;
        }

        public long getStartTime() {
            return this.mStartTime;
        }

        public long getEndTime() {
            return this.mEndTime;
        }

        private void readObject(ObjectInputStream objectInputStream, int i) throws IOException, ClassNotFoundException {
            if (objectInputStream == null) {
                this.pf = 0;
                this.mMtkChannelId = -1;
                this.mMtkEventId = -1;
                this.broadcastType = 0;
                this.serviceId = -1;
                this.eventId = -1;
                this.sharedNetworkId = -1;
                this.sharedTsId = -1;
                this.sharedServiceId = -1;
                this.sharedEventId = -1;
                this.mStartTime = 0L;
                this.mEndTime = 0L;
                return;
            }
            this.type = objectInputStream.readByte();
            if (this.type == 1) {
                this.pf = objectInputStream.readByte();
                this.mMtkChannelId = objectInputStream.readInt();
                this.mMtkEventId = objectInputStream.readInt();
                if (1 <= i) {
                    this.mStartTime = objectInputStream.readLong();
                    this.mEndTime = objectInputStream.readLong();
                    return;
                } else {
                    this.mStartTime = 0L;
                    this.mEndTime = 0L;
                    return;
                }
            }
            if (this.type == 2) {
                this.broadcastType = objectInputStream.readInt();
                this.serviceId = objectInputStream.readInt();
                this.eventId = objectInputStream.readInt();
                this.sharedNetworkId = objectInputStream.readInt();
                this.sharedTsId = objectInputStream.readInt();
                this.sharedServiceId = objectInputStream.readInt();
                this.sharedEventId = objectInputStream.readInt();
                if (1 <= i) {
                    this.pf = objectInputStream.readInt();
                    this.mStartTime = objectInputStream.readLong();
                    this.mEndTime = objectInputStream.readLong();
                    return;
                } else {
                    this.pf = 0;
                    this.mStartTime = 0L;
                    this.mEndTime = 0L;
                    return;
                }
            }
            this.mMtkChannelId = -1;
            this.mMtkEventId = -1;
            this.broadcastType = 0;
            this.serviceId = -1;
            this.eventId = -1;
            this.sharedNetworkId = -1;
            this.sharedTsId = -1;
            this.sharedServiceId = -1;
            this.sharedEventId = -1;
            this.mStartTime = 0L;
            this.mEndTime = 0L;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            if (objectOutputStream == null) {
                return;
            }
            objectOutputStream.writeByte((byte) this.type);
            if (this.type == 1) {
                objectOutputStream.writeByte((byte) this.pf);
                objectOutputStream.writeInt(this.mMtkChannelId);
                objectOutputStream.writeInt(this.mMtkEventId);
                objectOutputStream.writeLong(this.mStartTime);
                objectOutputStream.writeLong(this.mEndTime);
                return;
            }
            if (this.type == 2) {
                objectOutputStream.writeInt(this.broadcastType);
                objectOutputStream.writeInt(this.serviceId);
                objectOutputStream.writeInt(this.eventId);
                objectOutputStream.writeInt(this.sharedNetworkId);
                objectOutputStream.writeInt(this.sharedTsId);
                objectOutputStream.writeInt(this.sharedServiceId);
                objectOutputStream.writeInt(this.sharedEventId);
                objectOutputStream.writeInt(this.pf);
                objectOutputStream.writeLong(this.mStartTime);
                objectOutputStream.writeLong(this.mEndTime);
            }
        }
    }
}
