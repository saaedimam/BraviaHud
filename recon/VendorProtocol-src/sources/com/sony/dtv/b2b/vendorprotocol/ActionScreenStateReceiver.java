package com.sony.dtv.b2b.vendorprotocol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.sony.dtv.b2b.vendorprotocol.logging.Logger;
import com.sony.dtv.b2b.vendorprotocol.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public class ActionScreenStateReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        Logger.d("IN", new Object[0]);
        Preconditions.checkNotNull(context, "context == null");
        Preconditions.checkNotNull(intent, "intent == null");
        String action = (String) Preconditions.checkNotNull(intent.getAction(), "action == null");
        Logger.i("action %s is received", action);
        if ("android.intent.action.SCREEN_OFF".equals(action) || "android.intent.action.SCREEN_ON".equals(action)) {
            Intent setIntent = new Intent(context, (Class<?>) VendorProtocolService.class);
            setIntent.setAction(action);
            context.startForegroundService(setIntent);
        }
    }
}
