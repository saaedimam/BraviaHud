package com.sony.dtv.tvinput.provider;

import android.util.Base64;
import com.mediatek.twoworlds.tv.model.MtkTvChannelInfo;
import com.sony.dtv.tvinput.provider.internal.utils.LogUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class SonyChannel implements Serializable {
    private static final int DEFAULT_VERSION = 1;
    private static final int INVALID_VALUE = -1;
    private static final String TAG = "SonyChannel";
    private static final int VERSION_ADD_CHANNEL_ID = 3;
    private static final int VERSION_ADD_CHANNEL_ID_RULE = 4;
    private String mChannelId;
    private int mChannelIdRule;
    private MtkTvChannelInfo mMtkinfo;
    private int mVersionNumber;

    public SonyChannel() {
        this(new MtkTvChannelInfo());
    }

    public SonyChannel(MtkTvChannelInfo mtkTvChannelInfo) {
        this(mtkTvChannelInfo, null, 1, -1);
    }

    public SonyChannel(MtkTvChannelInfo mtkTvChannelInfo, String str, int i) {
        this(mtkTvChannelInfo, str, i, -1);
    }

    public SonyChannel(MtkTvChannelInfo mtkTvChannelInfo, String str, int i, int i2) {
        this.mMtkinfo = null;
        this.mChannelId = null;
        this.mMtkinfo = mtkTvChannelInfo;
        this.mChannelId = str;
        this.mVersionNumber = i;
        this.mChannelIdRule = i2;
    }

    public SonyChannel(byte[] bArr, int i) {
        this.mMtkinfo = null;
        this.mChannelId = null;
        this.mVersionNumber = i;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            readObject(objectInputStream);
            objectInputStream.close();
            byteArrayInputStream.close();
        } catch (IOException unused) {
            LogUtil.w(TAG, "SonyChannel: IOException");
        } catch (ClassNotFoundException unused2) {
            LogUtil.w(TAG, "SonyChannel: ClassNotFoundException");
        }
    }

    public SonyChannel(byte[] bArr) {
        this(bArr, 1);
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
            LogUtil.w(TAG, "getAsByteArray: IOException");
            return new byte[0];
        }
    }

    public String toBase64String() {
        try {
            return Base64.encodeToString(getAsByteArray(), 0);
        } catch (IllegalArgumentException unused) {
            LogUtil.w(TAG, "toBase64String: IllegalArgumentException");
            return "";
        }
    }

    public MtkTvChannelInfo getAsMtkTvChannelInfo() {
        return this.mMtkinfo;
    }

    public String getChannelId() {
        return this.mChannelId;
    }

    public int getChannelIdRule() {
        return this.mChannelIdRule;
    }

    public void setChannelId(String str) {
        this.mChannelId = str;
    }

    public void setChannelIdRule(int i) {
        this.mChannelIdRule = i;
    }

    public void setVersionNumber(int i) {
        this.mVersionNumber = i;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (objectInputStream == null) {
            this.mMtkinfo = new MtkTvChannelInfo();
            return;
        }
        this.mMtkinfo = new MtkTvChannelInfo(objectInputStream.readInt(), objectInputStream.readInt());
        this.mMtkinfo.setChannelId(objectInputStream.readInt());
        this.mMtkinfo.setBroadcastType(objectInputStream.readInt());
        this.mMtkinfo.setNetworkMask(objectInputStream.readInt());
        this.mMtkinfo.setOptionMask(objectInputStream.readInt());
        this.mMtkinfo.setServiceType(objectInputStream.readInt());
        this.mMtkinfo.setBroadcastMedium(objectInputStream.readInt());
        this.mMtkinfo.setChannelNumber(objectInputStream.readInt());
        this.mMtkinfo.setFrequency(objectInputStream.readInt());
        this.mMtkinfo.setServiceName(objectInputStream.readUTF());
        if (this.mVersionNumber >= 3) {
            this.mChannelId = objectInputStream.readUTF();
        }
        if (this.mVersionNumber >= 4) {
            this.mChannelIdRule = objectInputStream.readInt();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.mMtkinfo == null || objectOutputStream == null) {
            return;
        }
        objectOutputStream.writeInt(this.mMtkinfo.getSvlId());
        objectOutputStream.writeInt(this.mMtkinfo.getSvlRecId());
        objectOutputStream.writeInt(this.mMtkinfo.getChannelId());
        objectOutputStream.writeInt(this.mMtkinfo.getBroadcastType());
        objectOutputStream.writeInt(this.mMtkinfo.getNetworkMask());
        objectOutputStream.writeInt(this.mMtkinfo.getOptionMask());
        objectOutputStream.writeInt(this.mMtkinfo.getServiceType());
        objectOutputStream.writeInt(this.mMtkinfo.getBroadcastMedium());
        objectOutputStream.writeInt(this.mMtkinfo.getChannelNumber());
        objectOutputStream.writeInt(this.mMtkinfo.getFrequency());
        objectOutputStream.writeUTF(this.mMtkinfo.getServiceName());
        if (this.mVersionNumber >= 3) {
            objectOutputStream.writeUTF(this.mChannelId);
        }
        if (this.mVersionNumber >= 4) {
            objectOutputStream.writeInt(this.mChannelIdRule);
        }
    }

    public static SonyChannel fromBase64String(String str) {
        byte[] bArrDecode;
        try {
            bArrDecode = Base64.decode(str, 0);
        } catch (IllegalArgumentException unused) {
            LogUtil.w(TAG, "fromBase64String: IllegalArgumentException");
            bArrDecode = new byte[0];
        }
        return new SonyChannel(bArrDecode);
    }
}
