package com.sony.dtv.update;

import android.os.RemoteException;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class UpdateLock {
    private static final String TAG = "UpdateSessionManager";
    private IUpdateSessionManagerService mService;
    private String packageName = "";

    private static void logprint(Object... objArr) {
        StringBuffer stringBuffer = new StringBuffer();
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        stringBuffer.append("[");
        stringBuffer.append(stackTraceElement.getClassName());
        stringBuffer.append("::");
        stringBuffer.append(stackTraceElement.getMethodName());
        stringBuffer.append("] ");
        for (int i = 0; i < objArr.length; i++) {
            if (i > 0) {
                stringBuffer.append(" ");
            }
            stringBuffer.append(objArr[i]);
        }
        Log.e(TAG, stringBuffer.toString());
    }

    private boolean checkServiceConnecting() {
        this.mService = UpdateSessionManager.GetSessionManagerService();
        this.packageName = UpdateSessionManager.packageName;
        return this.mService != null;
    }

    public boolean acquire(boolean z) {
        if (checkServiceConnecting()) {
            try {
                return this.mService.acquireLock(this.packageName, z);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        Log.e(TAG, "acquire(): binder is not connected yet.");
        return false;
    }

    public boolean acquire() {
        return acquire(false);
    }

    public boolean release() {
        if (checkServiceConnecting()) {
            try {
                return this.mService.releaseLock(this.packageName);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        Log.e(TAG, "release(): binder is not connected yet.");
        return false;
    }

    public boolean isHeld() {
        if (checkServiceConnecting()) {
            try {
                return this.mService.isLockHeld();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        Log.e(TAG, "isHeld(): binder is not connected yet.");
        return false;
    }

    public boolean forceRelease() {
        if (checkServiceConnecting()) {
            try {
                return this.mService.forceReleaseLock();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        Log.e(TAG, "forceRelease(): binder is not connected yet.");
        return false;
    }
}
