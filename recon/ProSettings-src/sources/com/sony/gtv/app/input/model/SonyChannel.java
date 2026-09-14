package com.sony.gtv.app.input.model;

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
@Deprecated
public class SonyChannel implements Serializable {
    private static final String TAG = "SonyChannel_gtv";
    private MtkTvChannelInfo mMtkinfo;

    @Deprecated
    public SonyChannel() {
        this.mMtkinfo = null;
        this.mMtkinfo = new MtkTvChannelInfo();
    }

    @Deprecated
    public SonyChannel(MtkTvChannelInfo mtkTvChannelInfo) {
        this.mMtkinfo = null;
        this.mMtkinfo = mtkTvChannelInfo;
    }

    @Deprecated
    public SonyChannel(byte[] bArr) {
        this.mMtkinfo = null;
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

    @Deprecated
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

    @Deprecated
    public String toBase64String() {
        try {
            return Base64.encodeToString(getAsByteArray(), 0);
        } catch (IllegalArgumentException unused) {
            LogUtil.w(TAG, "toBase64String: IllegalArgumentException");
            return "";
        }
    }

    @Deprecated
    public MtkTvChannelInfo getAsMtkTvChannelInfo() {
        return this.mMtkinfo;
    }

    @Deprecated
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
    }

    @Deprecated
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
    }

    @Deprecated
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
