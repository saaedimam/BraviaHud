package com.sony.dtv.update;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.sony.dtv.provider.modelvariation.util.ModelVariationUtil;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class UpdateSessionManager {
    public static final int ATTEMPTING_ROLLBACK = 8;
    public static final int CHECKING_FOR_UPDATE = 1;
    public static final int DISABLED = 9;
    public static final int DOWNLOADING = 3;
    public static final int DOWNLOAD_PAYLOAD_VERIFICATION_ERROR = 12;
    public static final int DOWNLOAD_TRANSFER_ERROR = 9;
    public static final int ERROR = 1;
    public static final int FILESYSTEM_COPIER_ERROR = 4;
    public static final int FINALIZING = 5;
    public static final int IDLE = 0;
    public static final int INSTALL_DEVICE_OPEN_ERROR = 7;
    public static final int KERNEL_DEVICE_OPEN_ERROR = 8;
    public static final int PAYLOAD_HASH_MISMATCH_ERROR = 10;
    public static final int PAYLOAD_MISMATCHED_TYPE_ERROR = 6;
    public static final int PAYLOAD_SIZE_MISMATCH_ERROR = 11;
    public static final int POST_INSTALL_RUNNER_ERROR = 5;
    public static final int REPORTING_ERROR_EVENT = 7;
    public static final int SUCCESS = 0;
    private static final String TAG = "UpdateSessionManager";
    public static final int UPDATED_NEED_REBOOT = 6;
    public static final int UPDATE_AVAILABLE = 2;
    private static final String UPDATE_MANAGER_PACKAGE = "com.sony.dtv.update";
    private static final String UPDATE_MANAGER_SERVICE = "com.sony.dtv.update.UpdateSessionManagerService";
    public static final int VERIFYING = 4;
    private static IUpdateSessionManagerService mService = null;
    static String packageName = "";
    private Context context_;
    private UpdateProgressListener listener_;
    private ServiceConnectedListener serviceConnectedListener;
    private final Object mLock = new Object();
    private IUpdateProgressListener mUpdateProgressListener = new IUpdateProgressListener.Stub() { // from class: com.sony.dtv.update.UpdateSessionManager.1
        @Override // com.sony.dtv.update.IUpdateProgressListener
        public void onStatusUpdate(int i, float f) {
            if (UpdateSessionManager.this.listener_ != null) {
                UpdateSessionManager.this.listener_.onStatusUpdate(i, f);
            }
        }

        @Override // com.sony.dtv.update.IUpdateProgressListener
        public void onPayloadApplicationComplete(int i) {
            if (UpdateSessionManager.this.listener_ != null) {
                UpdateSessionManager.this.listener_.onPayloadApplicationComplete(i);
            }
        }
    };
    private String packageVersion_ = "";
    private ServiceConnection mConnection = new ServiceConnection() { // from class: com.sony.dtv.update.UpdateSessionManager.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(UpdateSessionManager.TAG, "ServiceConnection#onServiceConnected(): " + componentName);
            IUpdateSessionManagerService unused = UpdateSessionManager.mService = IUpdateSessionManagerService.Stub.asInterface(iBinder);
            if (UpdateSessionManager.this.serviceConnectedListener != null) {
                UpdateSessionManager.this.serviceConnectedListener.onServiceConnected();
            }
            try {
                UpdateSessionManager.mService.setListener(UpdateSessionManager.packageName, UpdateSessionManager.this.mUpdateProgressListener);
            } catch (RemoteException e) {
                Log.e(UpdateSessionManager.TAG, "failed to set the progresslistner to the IUpdateSessionManagerService", e);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            try {
                UpdateSessionManager.this.mUpdateProgressListener = null;
                IUpdateSessionManagerService unused = UpdateSessionManager.mService = null;
                UpdateSessionManager.this.mConnection = null;
            } catch (Exception e) {
                Log.e(UpdateSessionManager.TAG, "failed on unbindservice", e);
            }
        }
    };

    public interface ServiceConnectedListener {
        void onServiceConnected();
    }

    public interface UpdateProgressListener {
        void onPayloadApplicationComplete(int i);

        void onStatusUpdate(int i, float f);
    }

    public UpdateSessionManager(UpdateProgressListener updateProgressListener) {
        this.listener_ = updateProgressListener;
    }

    public UpdateSessionManager(String str, UpdateProgressListener updateProgressListener) {
        this.listener_ = updateProgressListener;
    }

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

    public static IUpdateSessionManagerService GetSessionManagerService() {
        return mService;
    }

    public boolean bindService(Context context) {
        this.context_ = context;
        packageName = context.getApplicationContext().getPackageName();
        Log.i(TAG, "bindService: packageName=" + packageName);
        synchronized (this.mLock) {
            try {
                try {
                    Intent intent = new Intent(IUpdateSessionManagerService.class.getName());
                    intent.setPackage("com.sony.dtv.update");
                    intent.setClassName("com.sony.dtv.update", UPDATE_MANAGER_SERVICE);
                    context.bindService(intent, this.mConnection, 1);
                } catch (Exception e) {
                    Log.e(TAG, "bindService: update service not found. Did the service fail to start?", e);
                    return false;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return true;
    }

    public boolean bindService(Context context, ServiceConnectedListener serviceConnectedListener) {
        this.serviceConnectedListener = serviceConnectedListener;
        return bindService(context);
    }

    public boolean waitConnectService(long j) {
        while (j > 0) {
            if (isConnected()) {
                return true;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(5L);
            } catch (InterruptedException unused) {
            }
            j -= 5;
        }
        return false;
    }

    public void unbindService() {
        this.listener_ = null;
        this.serviceConnectedListener = null;
        try {
            this.mUpdateProgressListener = null;
            mService.setListener(packageName, this.mUpdateProgressListener);
            this.context_.unbindService(this.mConnection);
            mService = null;
            this.mConnection = null;
        } catch (Exception e) {
            Log.e(TAG, "failed on unbindservice", e);
        }
    }

    private void printversion() {
        logprint("versionName=1.0.2");
    }

    public boolean isConnected() {
        return mService != null;
    }

    public boolean offerPayload(PayloadInformation payloadInformation) {
        printversion();
        try {
            logprint("is called. dump PayloadInformation");
            payloadInformation.dump();
            return mService.offerPayload(packageName, payloadInformation);
        } catch (RemoteException unused) {
            return false;
        }
    }

    public String getRunningVersion() {
        if (this.packageVersion_.isEmpty()) {
            String packageVersion = getPackageVersion();
            if (!packageVersion.isEmpty() && !packageVersion.startsWith("sony_dtv")) {
                packageVersion = "sony_dtv" + packageVersion;
            }
            if (!packageVersion.isEmpty()) {
                if (new PackageVersionParser(packageVersion).complete) {
                    this.packageVersion_ = packageVersion;
                } else {
                    Log.e(TAG, "failed to read/parse the system version string");
                    this.packageVersion_ = "";
                }
            }
        }
        return this.packageVersion_;
    }

    private String getPackageVersion() {
        ContentResolver contentResolver = this.context_.getContentResolver();
        String str = contentResolver != null ? ModelVariationUtil.get(contentResolver, 105) : "";
        Log.i(TAG, "System packageVersion=" + str);
        return str;
    }

    public boolean cancelUpdate() {
        printversion();
        try {
            return mService.cancelUpdate(packageName);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean suspendUpdate() {
        try {
            return mService.suspendUpdate(packageName);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean resumeUpdate() {
        try {
            return mService.resumeUpdate(packageName);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidTarget(PayloadInformation payloadInformation) {
        if (payloadInformation == null) {
            Log.e(TAG, "isValidTarget: invalid argument.");
            return false;
        }
        return payloadInformation.isValidTarget(getRunningVersion());
    }

    public boolean isNewerThanRunningVersion(PayloadInformation payloadInformation) {
        if (payloadInformation == null) {
            Log.e(TAG, "isNewerThanRunningVersion: invalid argument.");
            return false;
        }
        return payloadInformation.isNewerThanRunningVersion(getRunningVersion());
    }
}
