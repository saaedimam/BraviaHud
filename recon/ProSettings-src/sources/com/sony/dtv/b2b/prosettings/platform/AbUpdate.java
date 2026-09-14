package com.sony.dtv.b2b.prosettings.platform;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import com.sony.dtv.b2b.prosettings.util.J8Compat.BiConsumer;
import com.sony.dtv.b2b.prosettings.util.J8Compat.Consumer;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import com.sony.dtv.update.PayloadInformation;
import com.sony.dtv.update.UpdateLock;
import com.sony.dtv.update.UpdateSessionManager;
import java.io.File;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class AbUpdate extends PlatformBase implements UpdateSessionManager.UpdateProgressListener {
    private static final String PACKAGE_NAME = "/upgrade_pkg_b2b.zip";
    private static final String TAG = "AbUpdate";
    private int mDryRunMode = DRYRUN_MODE_NORMAL;
    private UpdateManager mManager = new UpdateManager(this);
    private BiConsumer<Integer, Float> mProgressCb;
    private int mUpdateResult;
    private boolean mWaitComplete;
    private static int DRYRUN_MODE_NORMAL = 0;
    private static int DRYRUN_MODE_OFFERPAYLOAD = DRYRUN_MODE_NORMAL + 1;
    private static int DRYRUN_MODE_FULL = DRYRUN_MODE_OFFERPAYLOAD + 1;

    private String getPackagePath() {
        return "/data/upgrade/upgrade_pkg_b2b.zip";
    }

    public void setDryRunMode(int i) {
        LogUtil.LogE(TAG, "setDryRunMode(" + i + ")");
        if (DRYRUN_MODE_NORMAL > i || i > DRYRUN_MODE_FULL) {
            return;
        }
        this.mDryRunMode = i;
    }

    public String prepareFwWrite() {
        Log.e(TAG, "prepareFwWrite()");
        deleteAbupdatePackage();
        File file = new File(getPackagePath());
        try {
            file.createNewFile();
            file.setWritable(true, false);
            file.setReadable(true, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getPackagePath();
    }

    public void cleanupFwWrite() {
        Log.e(TAG, "cleanupFwWrite()");
        deleteAbupdatePackage();
    }

    public static String makeResponse(boolean z) {
        return makeResponse(!z ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String makeResponse(int i) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("result", i == 0);
            jSONObject.put("code", i);
        } catch (JSONException unused) {
        }
        return jSONObject.toString();
    }

    public void startFwUpdate(final Consumer<String> consumer, BiConsumer<Integer, Float> biConsumer) {
        Log.e(TAG, "startSoftwareUpdate()");
        final File file = new File(getPackagePath());
        if (!file.exists()) {
            Log.e(TAG, "Package file not found.");
            consumer.accept("{ result: false }");
        } else {
            this.mProgressCb = biConsumer;
            new Thread(new Runnable() { // from class: com.sony.dtv.b2b.prosettings.platform.AbUpdate.1
                /* JADX WARN: Code duplicated, block: B:38:0x00df A[DONT_INVERT] */
                /* JADX WARN: Code duplicated, block: B:43:0x00ea A[Catch: Exception -> 0x00ee, TryCatch #0 {Exception -> 0x00ee, blocks: (B:7:0x0023, B:14:0x0071, B:20:0x0095, B:29:0x00d0, B:39:0x00e1, B:43:0x00ea, B:42:0x00e6, B:44:0x00ed), top: B:69:0x0023, inners: #4 }] */
                /* JADX WARN: Code duplicated, block: B:70:0x0138 A[EXC_TOP_SPLITTER, SYNTHETIC] */
                /* JADX WARN: Code duplicated, block: B:74:0x00e1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
                /* JADX WARN: Code duplicated, block: B:78:0x011d A[EXC_TOP_SPLITTER, SYNTHETIC] */
                /* JADX WARN: Code duplicated, block: B:81:0x0125 A[SYNTHETIC] */
                /* JADX WARN: Code duplicated, block: B:85:? A[Catch: Exception -> 0x00ee, SYNTHETIC, TRY_LEAVE, TryCatch #0 {Exception -> 0x00ee, blocks: (B:7:0x0023, B:14:0x0071, B:20:0x0095, B:29:0x00d0, B:39:0x00e1, B:43:0x00ea, B:42:0x00e6, B:44:0x00ed), top: B:69:0x0023, inners: #4 }] */
                @Override // java.lang.Runnable
                public void run() throws Throwable {
                    Throwable th;
                    Throwable th2;
                    PayloadInformation payloadInfoFromFile;
                    if (!AbUpdate.this.mManager.syncBind(PlatformBase.mContext)) {
                        LogUtil.LogE(AbUpdate.TAG, "UpdateSessionManager bind fail.");
                        consumer.accept(AbUpdate.makeResponse(1));
                        return;
                    }
                    try {
                        UpdateManagerUnbinder updateManagerUnbinder = new UpdateManagerUnbinder(AbUpdate.this.mManager);
                        try {
                            if (AbUpdate.this.mDryRunMode <= AbUpdate.DRYRUN_MODE_OFFERPAYLOAD) {
                                payloadInfoFromFile = PayloadInformation.getPayloadInfoFromFile("file://" + file);
                                if (!AbUpdate.this.mManager.isValidTarget(payloadInfoFromFile)) {
                                    LogUtil.LogE(AbUpdate.TAG, "Package is not valid.");
                                    consumer.accept(AbUpdate.makeResponse(1));
                                    if (updateManagerUnbinder != null) {
                                        updateManagerUnbinder.close();
                                        return;
                                    }
                                    return;
                                }
                                if (!AbUpdate.this.mManager.isNewerThanRunningVersion(payloadInfoFromFile)) {
                                    LogUtil.LogI(AbUpdate.TAG, "Current version is newer then package.");
                                    consumer.accept(AbUpdate.makeResponse(1));
                                    if (updateManagerUnbinder != null) {
                                        updateManagerUnbinder.close();
                                        return;
                                    }
                                    return;
                                }
                            } else {
                                payloadInfoFromFile = null;
                            }
                            AbUpdate.this.mWaitComplete = true;
                            if (AbUpdate.this.mDryRunMode <= AbUpdate.DRYRUN_MODE_NORMAL) {
                                AbUpdate.this.mManager.offerPayload(payloadInfoFromFile);
                            } else {
                                LogUtil.LogE(AbUpdate.TAG, "!!!!!!!!!!!!!!!!!!!! DUMMY OFFERPAYLOAD !!!!!!!!!!!!!!!!!!!");
                                new Thread(new Runnable() { // from class: com.sony.dtv.b2b.prosettings.platform.AbUpdate.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        int[][] iArr = {new int[]{2, 1}, new int[]{3, 10}, new int[]{5, 1}, new int[]{6, 1}, new int[]{0, 0}};
                                        int i = 0;
                                        for (int i2 = 0; iArr[i2][0] != 0; i2++) {
                                            for (int i3 = iArr[i2][1]; i3 > 0; i3--) {
                                                try {
                                                    Thread.sleep(1000L);
                                                } catch (InterruptedException unused) {
                                                }
                                                AbUpdate.this.onStatusUpdate(iArr[i2][0], i / 100.0f);
                                                if (iArr[i2][0] == 3) {
                                                    i += 10;
                                                }
                                            }
                                        }
                                        AbUpdate.this.onPayloadApplicationComplete(0);
                                    }
                                }).start();
                            }
                            updateManagerUnbinder.detach();
                            if (updateManagerUnbinder != null) {
                                updateManagerUnbinder.close();
                            }
                            while (true) {
                                try {
                                    Thread.sleep(1000L);
                                } catch (InterruptedException unused) {
                                }
                                Log.e(AbUpdate.TAG, "wait : " + AbUpdate.this.mWaitComplete);
                                synchronized (AbUpdate.this) {
                                    if (!AbUpdate.this.mWaitComplete) {
                                        consumer.accept(AbUpdate.makeResponse(AbUpdate.this.mUpdateResult));
                                        synchronized (AbUpdate.this) {
                                            AbUpdate.this.mProgressCb = null;
                                            AbUpdate.this.mManager.unbindService();
                                        }
                                        return;
                                    }
                                }
                            }
                        } catch (Throwable th3) {
                            try {
                                throw th3;
                            } catch (Throwable th4) {
                                th = th3;
                                th2 = th4;
                                if (updateManagerUnbinder != null) {
                                    throw th2;
                                }
                                if (th == null) {
                                    updateManagerUnbinder.close();
                                    throw th2;
                                }
                                updateManagerUnbinder.close();
                                throw th2;
                                while (true) {
                                    Thread.sleep(1000L);
                                    Log.e(AbUpdate.TAG, "wait : " + AbUpdate.this.mWaitComplete);
                                    synchronized (AbUpdate.this) {
                                        if (!AbUpdate.this.mWaitComplete) {
                                            consumer.accept(AbUpdate.makeResponse(AbUpdate.this.mUpdateResult));
                                            synchronized (AbUpdate.this) {
                                                AbUpdate.this.mProgressCb = null;
                                                AbUpdate.this.mManager.unbindService();
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        AbUpdate.this.onPayloadApplicationComplete(1);
                    }
                }
            }).start();
        }
    }

    public synchronized void calcelFwUpdate() {
        LogUtil.LogE(TAG, "calcelFwUpdate()   waiting ; " + this.mWaitComplete);
        if (this.mManager.isConnected() && this.mWaitComplete) {
            this.mManager.cancelUpdate();
        }
    }

    private void deleteAbupdatePackage() {
        File file = new File(getPackagePath());
        if (file.exists() && file.isFile()) {
            Log.d(TAG, "Delete abupdate package file.");
            file.delete();
        }
    }

    public void onBootComplete(final Consumer<Boolean> consumer) {
        LogUtil.LogE(TAG, "onBootComplete()");
        new Thread(new Runnable() { // from class: com.sony.dtv.b2b.prosettings.platform.AbUpdate.2
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                boolean z = true;
                if (AbUpdate.this.mManager.syncBind(PlatformBase.mContext)) {
                    int hotelMode = HotelMode.getHotelMode();
                    LogUtil.LogE(AbUpdate.TAG, "onBootComplete() mode : " + hotelMode);
                    UpdateLock updateLock = new UpdateLock();
                    if (hotelMode != HotelMode.MODE_HOTEL) {
                        if (!updateLock.isHeld()) {
                            LogUtil.LogE(AbUpdate.TAG, "onBootComplete() already unlocked");
                        } else {
                            boolean zRelease = updateLock.release();
                            LogUtil.LogE(AbUpdate.TAG, "onBootComplete() unlocked : " + zRelease);
                        }
                    } else if (updateLock.isHeld()) {
                        LogUtil.LogE(AbUpdate.TAG, "onBootComplete() already locked");
                    } else {
                        boolean zAcquire = updateLock.acquire(true);
                        LogUtil.LogE(AbUpdate.TAG, "onBootComplete() locked : " + zAcquire);
                    }
                    AbUpdate.this.mManager.unbindService();
                } else {
                    z = false;
                }
                LogUtil.LogD(AbUpdate.TAG, "onBootComplete() done : " + z);
                consumer.accept(Boolean.valueOf(z));
            }
        }).start();
    }

    @Override // com.sony.dtv.update.UpdateSessionManager.UpdateProgressListener
    public void onStatusUpdate(int i, float f) {
        Log.e(TAG, "onStatusUpdate: " + i + ", " + f);
        synchronized (this) {
            if (this.mProgressCb != null) {
                this.mProgressCb.accept(Integer.valueOf(i), Float.valueOf(f));
            }
        }
    }

    @Override // com.sony.dtv.update.UpdateSessionManager.UpdateProgressListener
    public void onPayloadApplicationComplete(int i) {
        Log.e(TAG, "onPayloadApplicationComplete: " + i);
        synchronized (this) {
            this.mUpdateResult = i;
            this.mWaitComplete = false;
        }
    }

    static class UpdateManager {
        private static String TAG = "AbUpdate$UpdateManager";
        private final UpdateSessionManager.UpdateProgressListener mListener;
        private UpdateSessionManager mManager = null;

        public UpdateManager(UpdateSessionManager.UpdateProgressListener updateProgressListener) {
            this.mListener = updateProgressListener;
        }

        public synchronized boolean isConnected() {
            return this.mManager != null ? this.mManager.isConnected() : false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean syncBind(Context context) {
            if (Looper.getMainLooper().equals(Looper.myLooper())) {
                throw new IllegalThreadStateException("Don't call this from UI Thread.");
            }
            if (isConnected()) {
                LogUtil.LogD(TAG, "UpdateSessionManager already bind.");
                return true;
            }
            LogUtil.LogD(TAG, "Start connect to UpdateSessionManager.");
            if (this.mManager == null) {
                this.mManager = new UpdateSessionManager(this.mListener);
            }
            if (!this.mManager.bindService(context, new UpdateSessionManager.ServiceConnectedListener() { // from class: com.sony.dtv.b2b.prosettings.platform.AbUpdate.UpdateManager.1
                @Override // com.sony.dtv.update.UpdateSessionManager.ServiceConnectedListener
                public void onServiceConnected() {
                    LogUtil.LogD(UpdateManager.TAG, "onServiceConnected()");
                    synchronized (UpdateManager.this) {
                        UpdateManager.this.notifyAll();
                    }
                }
            })) {
                LogUtil.LogE(TAG, "Request to bind fail.");
                return false;
            }
            try {
                wait();
                LogUtil.LogD(TAG, "Connect done.");
                return true;
            } catch (InterruptedException e) {
                LogUtil.LogE(TAG, e.getMessage());
                return false;
            }
        }

        public synchronized void unbindService() {
            LogUtil.LogD(TAG, "unbindService()");
            if (isConnected()) {
                this.mManager.unbindService();
                this.mManager = null;
            }
        }

        public boolean isValidTarget(PayloadInformation payloadInformation) {
            if (this.mManager != null) {
                return this.mManager.isValidTarget(payloadInformation);
            }
            return false;
        }

        public boolean isNewerThanRunningVersion(PayloadInformation payloadInformation) {
            if (this.mManager != null) {
                return this.mManager.isNewerThanRunningVersion(payloadInformation);
            }
            return false;
        }

        public boolean offerPayload(PayloadInformation payloadInformation) {
            if (this.mManager != null) {
                return this.mManager.offerPayload(payloadInformation);
            }
            return false;
        }

        public boolean cancelUpdate() {
            if (this.mManager != null) {
                return this.mManager.cancelUpdate();
            }
            return false;
        }
    }

    private static class UpdateManagerUnbinder implements AutoCloseable {
        private UpdateManager mManager;

        UpdateManagerUnbinder(UpdateManager updateManager) {
            this.mManager = updateManager;
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            if (this.mManager == null || !this.mManager.isConnected()) {
                return;
            }
            this.mManager.unbindService();
            this.mManager = null;
        }

        public void detach() {
            this.mManager = null;
        }
    }
}
