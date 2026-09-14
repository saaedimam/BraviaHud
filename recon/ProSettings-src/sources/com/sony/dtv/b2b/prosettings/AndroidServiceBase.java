package com.sony.dtv.b2b.prosettings;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.os.Build;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public abstract class AndroidServiceBase extends Service {
    private static final int ID_FOREGROUND = 1;
    private static final String TAG = "AndroidServiceBase";
    protected Notification mNotification = null;
    private Set<ForegroundServiceHandler> mForegroundHandlers = new HashSet();

    @Override // android.app.Service
    public void onCreate() {
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 26) {
            notificationManager.createNotificationChannel(new NotificationChannel(TAG, TAG + " Channel", 1));
            this.mNotification = new Notification.Builder(getApplicationContext(), TAG).setContentTitle(TAG).setContentText("NodeRuntime Service").build();
        }
    }

    protected synchronized ForegroundServiceHandler StartForeground() {
        ForegroundServiceHandler foregroundServiceHandler;
        foregroundServiceHandler = new ForegroundServiceHandler();
        if (this.mForegroundHandlers.isEmpty() && Build.VERSION.SDK_INT >= 26) {
            LogUtil.LogD(TAG, "Become foreground service.");
            startForeground(1, this.mNotification);
        }
        this.mForegroundHandlers.add(foregroundServiceHandler);
        return foregroundServiceHandler;
    }

    public synchronized void EndForeground(ForegroundServiceHandler foregroundServiceHandler) {
        if (this.mForegroundHandlers.contains(foregroundServiceHandler)) {
            this.mForegroundHandlers.remove(foregroundServiceHandler);
            if (this.mForegroundHandlers.isEmpty()) {
                LogUtil.LogD(TAG, "Become background service.");
                stopForeground(true);
            }
        }
    }

    protected class ForegroundServiceHandler implements AutoCloseable {
        protected ForegroundServiceHandler() {
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            AndroidServiceBase.this.EndForeground(this);
        }
    }
}
