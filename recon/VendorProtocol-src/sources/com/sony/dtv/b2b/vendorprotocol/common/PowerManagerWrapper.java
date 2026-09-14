package com.sony.dtv.b2b.vendorprotocol.common;

import android.content.Context;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import com.sony.dtv.b2b.vendorprotocol.logging.Logger;
import com.sony.dtv.b2b.vendorprotocol.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public class PowerManagerWrapper {
    private static final String TAG = "VendorProtocolService";
    private final PowerManager mPowerManager;
    private final PowerManager.WakeLock mWakeLock;

    public PowerManagerWrapper(@NonNull Context context) {
        Logger.d("IN", new Object[0]);
        Preconditions.checkNotNull(context, "context == null");
        this.mPowerManager = (PowerManager) context.getSystemService("power");
        Preconditions.checkNotNull(this.mPowerManager, "mPowerManager == null");
        this.mWakeLock = this.mPowerManager.newWakeLock(1, TAG);
    }

    public Boolean isInteractive() {
        Boolean result = Boolean.valueOf(this.mPowerManager.isInteractive());
        Logger.d("isInteractive: %b", result);
        return result;
    }

    public PowerManager.WakeLock getWakeLock() {
        return this.mWakeLock;
    }
}
