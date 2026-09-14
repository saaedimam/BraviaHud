package com.sony.dtv.b2b.prosettings.util;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ArrayUtil {
    public static byte[] toByteArray(List<Byte> list) {
        int size = list.size();
        byte[] bArr = new byte[size];
        for (int i = 0; i < size; i++) {
            bArr[i] = list.get(i).byteValue();
        }
        return bArr;
    }

    public static List<Byte> toByteList(byte[] bArr) {
        ArrayList arrayList = new ArrayList();
        for (byte b : bArr) {
            arrayList.add(Byte.valueOf(b));
        }
        return arrayList;
    }
}
