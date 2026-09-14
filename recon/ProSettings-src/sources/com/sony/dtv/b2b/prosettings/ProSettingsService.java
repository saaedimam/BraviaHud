package com.sony.dtv.b2b.prosettings;

import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.UserHandle;
import android.os.UserManager;
import com.mediatek.twoworlds.tv.MtkTvConfig;
import com.mediatek.twoworlds.tv.MtkTvHotel;
import com.sony.dtv.b2b.prosettings.platform.AbUpdate;
import com.sony.dtv.b2b.prosettings.platform.PlatformBase;
import com.sony.dtv.b2b.prosettings.service.ServiceManager;
import com.sony.dtv.b2b.prosettings.util.FileUtil;
import com.sony.dtv.b2b.prosettings.util.IntentListenerEmitter;
import com.sony.dtv.b2b.prosettings.util.J8Compat.Consumer;
import com.sony.dtv.b2b.prosettings.util.J8Compat.Function;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class ProSettingsService extends AndroidServiceBase {
    private static final String TMPDIRPATH_UO = "/vendor/tmp/odm/b2b/";
    private static final long USER_SERIAL_NUMBER_OWNER = 0;
    private boolean mBeforeToreble;
    private final String TAG = getClass().getSimpleName();
    private boolean isBootCompletedAlreadyReceived = false;
    private RemoteCallbackList<IProSettingsCallback> mCallbacks = null;
    private ServiceManager mServiceManager = null;
    private ProSettingsServiceBinder mServiceBinder = null;
    private IntentListenerEmitter mIntentListenerEmitter = new IntentListenerEmitter();
    private AbUpdate mAbupdate = null;

    private boolean isParentUser() {
        UserHandle userHandleMyUserHandle = Process.myUserHandle();
        UserManager userManager = (UserManager) getSystemService("user");
        return userManager != null && userManager.getSerialNumberForUser(userHandleMyUserHandle) == 0;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        LogUtil.LogD(this.TAG, "onBind()");
        if (this.mServiceBinder == null) {
            this.mServiceBinder = new ProSettingsServiceBinder(getApplicationContext(), this.mServiceManager, this.mAbupdate);
        }
        return this.mServiceBinder;
    }

    @Override // com.sony.dtv.b2b.prosettings.AndroidServiceBase, android.app.Service
    public void onCreate() throws Throwable {
        super.onCreate();
        LogUtil.LogD(this.TAG, "onCreate:");
        if (!isParentUser()) {
            LogUtil.LogD(this.TAG, "onCreate: stopService because not owner profile.");
            stopService(new Intent(this, (Class<?>) ProSettingsService.class));
            return;
        }
        if (new File(TMPDIRPATH_UO).exists()) {
            this.mBeforeToreble = false;
            this.mAbupdate = new AbUpdate();
        } else {
            this.mBeforeToreble = true;
            this.mAbupdate = null;
        }
        PlatformBase.init(getApplicationContext(), new MtkTvConfig(), new MtkTvHotel(getApplicationContext()));
        this.mCallbacks = new RemoteCallbackList<>();
        this.mServiceManager = new ServiceManager(getApplicationContext());
        FileUtil.deploymentAsset(getApplicationContext().getAssets(), "", getApplicationContext().getApplicationInfo().dataDir, "master", true);
        this.mIntentListenerEmitter.addListener("android.intent.action.BOOT_COMPLETED", new Function<Intent, Boolean>() { // from class: com.sony.dtv.b2b.prosettings.ProSettingsService.1
            @Override // com.sony.dtv.b2b.prosettings.util.J8Compat.Function
            public Boolean apply(Intent intent) {
                if (!ProSettingsService.this.isBootCompletedAlreadyReceived) {
                    ProSettingsService.this.isBootCompletedAlreadyReceived = true;
                    final AndroidServiceBase.ForegroundServiceHandler foregroundServiceHandlerStartForeground = ProSettingsService.this.StartForeground();
                    if (ProSettingsService.this.mAbupdate != null) {
                        ProSettingsService.this.mAbupdate.onBootComplete(new Consumer<Boolean>() { // from class: com.sony.dtv.b2b.prosettings.ProSettingsService.1.1
                            @Override // com.sony.dtv.b2b.prosettings.util.J8Compat.Consumer
                            public void accept(Boolean bool) {
                                foregroundServiceHandlerStartForeground.close();
                            }
                        });
                    } else {
                        foregroundServiceHandlerStartForeground.close();
                    }
                }
                return false;
            }
        });
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if (!isParentUser()) {
            return 2;
        }
        this.mIntentListenerEmitter.emitListener(intent);
        return 2;
    }

    @Override // android.app.Service
    public void onDestroy() {
        LogUtil.LogD(this.TAG, "onDestroy");
        if (isParentUser()) {
        }
    }
}
