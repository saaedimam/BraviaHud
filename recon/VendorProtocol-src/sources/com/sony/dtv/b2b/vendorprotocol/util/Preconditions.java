package com.sony.dtv.b2b.vendorprotocol.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public final class Preconditions {
    private Preconditions() {
        throw new AssertionError();
    }

    @NonNull
    public static <T> T checkNotNull(@Nullable T t, String message) {
        if (t == null) {
            throw new IllegalArgumentException(message);
        }
        return t;
    }

    @NonNull
    public static <T> T checkNotNull(@Nullable T t, @NonNull String message, Object... messageArgs) {
        if (t == null) {
            throw new IllegalArgumentException(String.format(message, messageArgs));
        }
        return t;
    }
}
