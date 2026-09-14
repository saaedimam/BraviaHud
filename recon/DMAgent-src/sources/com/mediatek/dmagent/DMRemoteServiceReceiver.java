package com.mediatek.dmagent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class DMRemoteServiceReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Log.v("[J]DMRemoteServiceReceiver", "onReceive");
        context.startService(new Intent(context, (Class<?>) DMRemoteServiceAgent.class));
    }
}
