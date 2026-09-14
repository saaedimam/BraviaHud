package com.sony.dtv.b2b.vendorprotocol.common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;
import com.sony.dtv.b2b.vendorprotocol.R;
import com.sony.dtv.b2b.vendorprotocol.logging.Logger;
import com.sony.dtv.b2b.vendorprotocol.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public class NotificationManagerWrapper {
    private static final String CHANNEL_ID = "com.sony.dtv.b2b.vendorprotocol.channel_1";
    public static final int NOTIFICATION_ID_FOREGROUND_SERVICE = 101;
    private final Context mContext;
    private final NotificationManager mNotificationManager;

    public NotificationManagerWrapper(@NonNull Context context) {
        this.mContext = (Context) Preconditions.checkNotNull(context, "context == null");
        this.mNotificationManager = (NotificationManager) this.mContext.getSystemService("notification");
    }

    public void createNotificationChannel() {
        Logger.d("IN", new Object[0]);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, this.mContext.getString(R.string.app_name), 3);
        this.mNotificationManager.createNotificationChannel(notificationChannel);
    }

    @NonNull
    public Notification createNotification() {
        Logger.d("IN", new Object[0]);
        Preconditions.checkNotNull(this.mContext, "mContext == null");
        return new Notification.Builder(this.mContext, CHANNEL_ID).setContentTitle(this.mContext.getString(R.string.app_name)).setSmallIcon(R.drawable.settings_system).setOngoing(false).build();
    }
}
