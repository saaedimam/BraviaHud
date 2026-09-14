package com.sony.dtv.b2b.vendorprotocol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.sony.dtv.b2b.vendorprotocol.logging.Logger;
import com.sony.dtv.b2b.vendorprotocol.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public class SimpleIpControlStateReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        Logger.d("IN", new Object[0]);
        Preconditions.checkNotNull(context, "context == null");
        Preconditions.checkNotNull(intent, "intent == null");
        String action = (String) Preconditions.checkNotNull(intent.getAction(), "action == null");
        Bundle extra = (Bundle) Preconditions.checkNotNull(intent.getExtras(), "extra == null");
        Logger.i("action %s is received", action);
        if (VendorProtocolService.ACTION_CHANGE_SIMPLE_IP_CONTROL.equals(action)) {
            Intent setIntent = new Intent(context, (Class<?>) VendorProtocolService.class);
            setIntent.setAction(action);
            setIntent.putExtra(VendorProtocolService.SIMPLE_IP_CONTROL_BUNDLE_KEY, extra.getInt(VendorProtocolService.SIMPLE_IP_CONTROL_BUNDLE_KEY));
            context.startForegroundService(setIntent);
        }
    }
}
