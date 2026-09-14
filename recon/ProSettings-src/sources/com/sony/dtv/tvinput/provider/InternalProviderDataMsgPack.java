package com.sony.dtv.tvinput.provider;

import com.sony.dtv.tvinput.provider.internal.utils.LogUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import org.msgpack.MessagePack;

/* JADX INFO: loaded from: classes.dex */
public final class InternalProviderDataMsgPack implements Serializable {
    public static final int ADD_TIMETYPE_VERSION = 2;
    public static final int BLOB_INIT_VERSION = 1;
    public static final int BOTH_TIME_IS_UNKNOWN = 3;
    public static final int END_TIME_IS_UNKNOWN = 2;
    public static final int INVALID_ID = -1;
    public static final int NOT_SCRAMBLED = 0;
    public static final int SCRAMBLED = 1;
    public static final int SCRAMBLED_UNSUPPORTED = -1;
    public static final int START_TIME_IS_UNKNOWN = 1;
    private static final String TAG = "EventDataMsg";
    public static final int TIME_IS_NORMAL = 0;
    private static final long UNDEFINED_DURATION_GENERAL = 933555000;
    private static final long UNDEFINED_END_TIME_ARIB = 0;
    private static final long UNDEFINED_START_TIME_ARIB = 0;
    private static final long UNDEFINED_START_TIME_GENERAL = 2156111265000L;
    private int mEventId;
    private int mEventType;
    private int mIsScrambled;
    private List<Integer> mSharedServiceId;
    private int mTimeType;
    private int mVersion;

    /* JADX INFO: Access modifiers changed from: private */
    public static int convertTimeToType(long j, long j2) {
        int i = (UNDEFINED_START_TIME_GENERAL == j || 0 == j) ? 1 : 0;
        return (UNDEFINED_DURATION_GENERAL == j2 - j || 0 == j2) ? i + 2 : i;
    }

    private InternalProviderDataMsgPack() {
        this.mVersion = 2;
        this.mEventId = -1;
        this.mTimeType = 3;
        this.mEventType = -1;
        this.mIsScrambled = -1;
        this.mSharedServiceId = null;
    }

    private InternalProviderDataMsgPack(Builder builder) {
        this.mVersion = builder.mVersion;
        this.mEventId = builder.mEventId;
        this.mTimeType = builder.mTimeType;
        this.mEventType = builder.mEventType;
        this.mIsScrambled = builder.mIsScrambled;
        this.mSharedServiceId = builder.mSharedServiceId;
    }

    public byte[] getAsByteArray() {
        MessagePack messagePack = new MessagePack();
        messagePack.register(InternalProviderDataMsgPack.class, InternalProviderDataMsgPackTemplate.getInstance());
        try {
            return messagePack.write(this);
        } catch (IOException unused) {
            LogUtil.w(TAG, "getAsByteArray: IOException");
            return null;
        }
    }

    public int getVersion() {
        return this.mVersion;
    }

    public int getEventId() {
        return this.mEventId;
    }

    public int getTimeType() {
        return this.mTimeType;
    }

    public int getEventType() {
        return this.mEventType;
    }

    public int getIsScrambled() {
        return this.mIsScrambled;
    }

    public List<Integer> getSharedServiceId() {
        return this.mSharedServiceId;
    }

    public void setVersion(int i) {
        this.mVersion = i;
    }

    public void setEventId(int i) {
        this.mEventId = i;
    }

    public void setTimeType(long j, long j2) {
        this.mTimeType = convertTimeToType(j, j2);
    }

    public void setTimeType(int i) {
        this.mTimeType = i;
    }

    public void setEventType(int i) {
        this.mEventType = i;
    }

    public void setIsScrambled(int i) {
        this.mIsScrambled = i;
    }

    public void setSharedServiceId(List<Integer> list) {
        this.mSharedServiceId = list;
    }

    public static InternalProviderDataMsgPack createInstance(Builder builder) {
        return new InternalProviderDataMsgPack(builder);
    }

    public static class Builder {
        private int mEventId = -1;
        private long mStartTime = 0;
        private long mEndTime = 0;
        private int mTimeType = 3;
        private int mEventType = -1;
        private int mIsScrambled = -1;
        private List<Integer> mSharedServiceId = null;
        private int mVersion = 2;

        public Builder eventId(int i) {
            this.mEventId = i;
            return this;
        }

        public Builder startTime(long j) {
            this.mStartTime = j;
            return this;
        }

        public Builder endTime(long j) {
            this.mEndTime = j;
            return this;
        }

        public Builder timeType(long j, long j2) {
            this.mTimeType = InternalProviderDataMsgPack.convertTimeToType(j, j2);
            return this;
        }

        public Builder eventType(int i) {
            this.mEventType = i;
            return this;
        }

        public Builder isScrambled(boolean z) {
            if (z) {
                this.mIsScrambled = 1;
            } else {
                this.mIsScrambled = 0;
            }
            return this;
        }

        public Builder isScrambled(int i) {
            this.mIsScrambled = i;
            return this;
        }

        public Builder sharedServiceId(List<Integer> list) {
            this.mSharedServiceId = list;
            return this;
        }

        public Builder blob(byte[] bArr) {
            try {
                readObject(bArr);
            } catch (IOException unused) {
                LogUtil.w(InternalProviderDataMsgPack.TAG, "Builder blob: IOException");
            } catch (ClassNotFoundException unused2) {
                LogUtil.w(InternalProviderDataMsgPack.TAG, "Builder blob: ClassNotFoundException");
            }
            return this;
        }

        public InternalProviderDataMsgPack build() {
            if (3 == this.mTimeType) {
                this.mTimeType = InternalProviderDataMsgPack.convertTimeToType(this.mStartTime, this.mEndTime);
            }
            return InternalProviderDataMsgPack.createInstance(this);
        }

        private void readObject(byte[] bArr) throws IOException, ClassNotFoundException {
            if (bArr == null) {
                return;
            }
            MessagePack messagePack = new MessagePack();
            messagePack.register(InternalProviderDataMsgPack.class, InternalProviderDataMsgPackTemplate.getInstance());
            InternalProviderDataMsgPack internalProviderDataMsgPack = (InternalProviderDataMsgPack) messagePack.read(bArr, InternalProviderDataMsgPack.class);
            if (internalProviderDataMsgPack == null) {
                return;
            }
            this.mVersion = internalProviderDataMsgPack.mVersion;
            this.mEventId = internalProviderDataMsgPack.mEventId;
            this.mTimeType = internalProviderDataMsgPack.mTimeType;
            this.mEventType = internalProviderDataMsgPack.mEventType;
            this.mIsScrambled = internalProviderDataMsgPack.mIsScrambled;
            this.mSharedServiceId = internalProviderDataMsgPack.mSharedServiceId;
        }
    }
}
