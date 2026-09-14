package com.sony.dtv.b2b.vendorprotocol.common;

import android.content.Context;
import android.content.pm.PackageManager;
import com.sony.dtv.b2b.vendorprotocol.logging.Logger;
import com.sony.dtv.b2b.vendorprotocol.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public class PackageManagerWrapper {
    private final PackageManager mPackageManager;
    private final String mPackageName;

    public PackageManagerWrapper(Context context) {
        Preconditions.checkNotNull(context, "context == null");
        this.mPackageManager = (PackageManager) Preconditions.checkNotNull(context.getPackageManager(), "PackageManager == null");
        this.mPackageName = (String) Preconditions.checkNotNull(context.getPackageName(), "packageName == null");
    }

    public void setApplicationEnabledSetting(int newState) {
        Logger.d("IN", new Object[0]);
        this.mPackageManager.setApplicationEnabledSetting(this.mPackageName, newState, 1);
    }
}
