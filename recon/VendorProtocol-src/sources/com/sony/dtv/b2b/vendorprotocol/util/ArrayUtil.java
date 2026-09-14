package com.sony.dtv.b2b.vendorprotocol.util;

import android.support.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public final class ArrayUtil {
    private ArrayUtil() {
        throw new AssertionError();
    }

    public static boolean isEmpty(@Nullable byte... array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(@Nullable short... array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(@Nullable int... array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(@Nullable long... array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(@Nullable float... array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(@Nullable double... array) {
        return array == null || array.length == 0;
    }

    @SafeVarargs
    public static <E> boolean isEmpty(@Nullable E... array) {
        return array == null || array.length == 0;
    }

    @SafeVarargs
    public static <E> int size(@Nullable E... array) {
        if (array == null) {
            return 0;
        }
        return array.length;
    }
}
