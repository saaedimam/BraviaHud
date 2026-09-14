package com.sony.dtv.b2b.vendorprotocol;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.mediatek.twoworlds.tv.MtkTvHotel;
import com.sony.dtv.b2b.vendorprotocol.common.Constants;
import com.sony.dtv.b2b.vendorprotocol.common.NotificationManagerWrapper;
import com.sony.dtv.b2b.vendorprotocol.common.PowerManagerWrapper;
import com.sony.dtv.b2b.vendorprotocol.logging.Logger;
import com.sony.dtv.b2b.vendorprotocol.util.Preconditions;
import com.sony.dtv.provider.modelvariation.util.ModelVariationUtil;
import com.sony.dtv.webapi.aidl.IWebApiCore;
import com.sony.dtv.webapi.aidl.IWebApiCoreConfig;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class VendorProtocolService extends Service {
    public static final String ACTION_CHANGE_SIMPLE_IP_CONTROL = "com.sony.dtv.homenetwork.broadcast.change_simple_ip_control";
    private static final String ASSETS_ROOT_DIRECTORY_NAME = "node";
    private static final String MODEL_NAME_DUMMY = "KDL-GN4";
    private static final String NETWORK_TYPE_NAME_ETHERNET = "eth0";
    private static final String NETWORK_TYPE_NAME_WIFI = "wlan0";
    private static final int NETWORK_TYPE_NONE = -1;
    public static final String NOTIFY_WAKE_ON_LAN = "com.sony.dtv.magicpacketnotifyservice.ACTION_RECEIVE_MAGICPACKET";
    public static final String SIMPLE_IP_CONTROL_BUNDLE_KEY = "simple_ip_control_mode";
    private static final int SIMPLE_IP_CONTROL_OFF = 0;
    private static final int SIMPLE_IP_CONTROL_ON = 1;
    private static final long START_SDDP_DELAY_MILLIS = 3000;
    private ActionScreenStateReceiver mActionScreenStateReceiver;
    private ConnectivityManager mConnectivityManager;
    private ContentResolver mContentResolver;
    private ContentObserver mControlRemotelyObserver;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    private NotificationManagerWrapper mNotificationManagerWrapper;
    private NotifyWakeOnLanReceiver mNotifyWakeOnLanReceiver;
    private PowerManagerWrapper mPowerManagerWrapper;
    private SimpleIpControlStateReceiver mSimpleIpControlStateReceiver;
    private SonySettings mSonySettings;
    private IWebApiCore mWebApiCore;
    private AssetManager mAssetManager = null;
    private SsipProcess mSsipProcess = null;
    private SddpProcess mSddpProcess = null;
    private ScheduledExecutorService mScheduler = null;
    private ScheduledFuture<?> mScheduledFuture = null;
    private Handler mDelayHandler = null;
    private Runnable mDelayRunnable = null;
    private boolean mIsBindWebApiCore = false;
    private String mModelName = com.sony.dtv.webapi.aidl.BuildConfig.FLAVOR;
    private String mMacAddress = null;
    private String mIpAddress = null;
    private int mNetworkType = NETWORK_TYPE_NONE;
    private boolean mIsSsipControlOn = false;
    private boolean mIsControlRemotelyOn = false;
    private final ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.sony.dtv.b2b.vendorprotocol.VendorProtocolService.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.d(name.toShortString(), new Object[0]);
            VendorProtocolService.this.mWebApiCore = IWebApiCore.Stub.asInterface(service);
            VendorProtocolService.this.mIsBindWebApiCore = true;
            String applicationToken = com.sony.dtv.webapi.aidl.BuildConfig.FLAVOR;
            if (VendorProtocolService.this.mWebApiCore != null) {
                try {
                    applicationToken = VendorProtocolService.this.mWebApiCore.getToken();
                } catch (RemoteException e) {
                    Logger.e("Error has occurred with the IWebApiCore.getToken() " + e, new Object[0]);
                }
            }
            VendorProtocolService.this.unbindService(VendorProtocolService.this.mServiceConnection);
            VendorProtocolService.this.mWebApiCore = null;
            VendorProtocolService.this.mIsBindWebApiCore = false;
            if (applicationToken != null && !applicationToken.equals(com.sony.dtv.webapi.aidl.BuildConfig.FLAVOR)) {
                Logger.d("application token : '" + applicationToken + "'", new Object[0]);
                VendorProtocolService.this.mSsipProcess.setApplicationToken(applicationToken);
                VendorProtocolService.this.mSsipProcess.start();
                return;
            }
            Logger.e("application token : null or \"\"", new Object[0]);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            Logger.d(name.toShortString(), new Object[0]);
            VendorProtocolService.this.mWebApiCore = null;
            VendorProtocolService.this.mIsBindWebApiCore = false;
        }
    };

    private class DelayRunnable implements Runnable {
        private DelayRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            VendorProtocolService.this.notifyStartSddp();
        }
    }

    private class MonitoringTempSddpTimerTask extends TimerTask {
        private final PowerManagerWrapper mPowerManagerWrapper;

        MonitoringTempSddpTimerTask(PowerManagerWrapper powerManagerWrapper) {
            this.mPowerManagerWrapper = (PowerManagerWrapper) Preconditions.checkNotNull(powerManagerWrapper, "powerManagerWrapper == null");
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            Logger.i("Temporarily start SDDP of the delay timer is firing.", new Object[0]);
            if (!this.mPowerManagerWrapper.isInteractive().booleanValue()) {
                VendorProtocolService.this.notifyStopSddp();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyStartSddp() {
        notifyStartSddpInternal(false);
    }

    private void notifyForceStartSddp() {
        notifyStartSddpInternal(true);
    }

    private void notifyStartSddpInternal(boolean forceStart) {
        if (!this.mPowerManagerWrapper.isInteractive().booleanValue()) {
            if (forceStart) {
                Logger.d("Force start SDDP in ScreenOff.", new Object[0]);
                Logger.d("Start monitoring of temporarily start SDDP.", new Object[0]);
                Timer timer = new Timer();
                timer.schedule(new MonitoringTempSddpTimerTask(this.mPowerManagerWrapper), 60000L);
            } else {
                Logger.d("Not start SDDP in ScreenOff.", new Object[0]);
                return;
            }
        }
        Logger.i("mIsControlRemotelyOn: %1$b", Boolean.valueOf(this.mIsControlRemotelyOn));
        if (!this.mIsControlRemotelyOn) {
            Logger.d("ControlRemotely is Off. Not start SDDP.", new Object[0]);
            return;
        }
        if (this.mConnectivityManager == null) {
            Logger.d("mConnectivityManager is Null. Not start SDDP.", new Object[0]);
            return;
        }
        Logger.i("Start SDDP", new Object[0]);
        NetworkInfo info = this.mConnectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            if (this.mDelayRunnable == null) {
                Logger.i("SDDP is started after 3 seconds...", new Object[0]);
                if (this.mDelayHandler == null) {
                    this.mDelayHandler = new Handler();
                }
                this.mDelayRunnable = new DelayRunnable();
                this.mDelayHandler.postDelayed(this.mDelayRunnable, START_SDDP_DELAY_MILLIS);
                return;
            }
            Logger.w("start of the SDDP was given up", new Object[0]);
            this.mDelayHandler.removeCallbacks(this.mDelayRunnable);
            this.mDelayRunnable = null;
            return;
        }
        if (this.mDelayHandler != null && this.mDelayRunnable != null) {
            this.mDelayHandler.removeCallbacks(this.mDelayRunnable);
            this.mDelayRunnable = null;
        }
        if (this.mSddpProcess == null) {
            if (this.mModelName == null || this.mMacAddress == null || this.mIpAddress == null) {
                Logger.e("Required parameter is null, start SDDP is skipped", new Object[0]);
                return;
            }
            this.mSddpProcess = new SddpProcess(Constants.DIR_PATH_NODE, getWorkDirectory() + ASSETS_ROOT_DIRECTORY_NAME + "/", this.mModelName, this.mMacAddress, this.mIpAddress);
        }
        NetworkInterface networkInterface = null;
        try {
            if (this.mNetworkType == 9) {
                networkInterface = NetworkInterface.getByName(NETWORK_TYPE_NAME_ETHERNET);
            } else if (this.mNetworkType == 1) {
                networkInterface = NetworkInterface.getByName(NETWORK_TYPE_NAME_WIFI);
            }
            if (networkInterface != null && networkInterface.supportsMulticast() && networkInterface.isUp() && !networkInterface.isLoopback() && !networkInterface.isPointToPoint()) {
                this.mSddpProcess.start();
            }
        } catch (SocketException e) {
            Logger.e("Error has occurred with the notifyStartSddp() " + e, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyStartSsip() {
        Logger.i("mIsControlRemotelyOn: %1$b, mIsSsipControlOn: %2$b", Boolean.valueOf(this.mIsControlRemotelyOn), Boolean.valueOf(this.mIsSsipControlOn));
        if (!this.mIsControlRemotelyOn || !this.mIsSsipControlOn) {
            Logger.d("ControlRemotely or Control UI is Off. Not start SSIP.", new Object[0]);
            return;
        }
        Logger.i("Start SSIP", new Object[0]);
        if (this.mSsipProcess == null) {
            this.mSsipProcess = new SsipProcess(Constants.DIR_PATH_NODE, getWorkDirectory() + ASSETS_ROOT_DIRECTORY_NAME + "/", com.sony.dtv.webapi.aidl.BuildConfig.FLAVOR, this.mPowerManagerWrapper.getWakeLock(), this);
        }
        if (this.mSsipProcess.getIsThreadStart()) {
            Logger.d("SSIP has already started, skip", new Object[0]);
        } else if (!bindWebApiCoreService()) {
            retryBindWebApiCoreService();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyStopSddp() {
        Logger.i("Stop SDDP", new Object[0]);
        if (this.mDelayHandler != null && this.mDelayRunnable != null) {
            this.mDelayHandler.removeCallbacks(this.mDelayRunnable);
            this.mDelayRunnable = null;
        }
        if (this.mSddpProcess != null) {
            this.mSddpProcess.stop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyStopSsip() {
        Logger.i("Stop SSIP", new Object[0]);
        if (this.mSsipProcess != null) {
            this.mSsipProcess.stop();
        }
    }

    public void notifyTerminatedByUnexpectedErrorSsip() {
        Logger.i("Terminated by unexpected error SSIP", new Object[0]);
        notifyStartSsip();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent arg0) {
        Logger.d("IN", new Object[0]);
        return null;
    }

    @Override // android.app.Service
    public void onCreate() throws Throwable {
        super.onCreate();
        Logger.i("IN", new Object[0]);
        Context context = getApplicationContext();
        this.mConnectivityManager = (ConnectivityManager) getSystemService("connectivity");
        this.mContentResolver = getContentResolver();
        this.mPowerManagerWrapper = new PowerManagerWrapper(context);
        this.mSonySettings = new SonySettings(context);
        this.mNotificationManagerWrapper = new NotificationManagerWrapper(context);
        this.mNotificationManagerWrapper.createNotificationChannel();
        this.mAssetManager = getResources().getAssets();
        deploymentAsset(com.sony.dtv.webapi.aidl.BuildConfig.FLAVOR, true);
        this.mModelName = getModelName(this.mContentResolver);
        this.mIsControlRemotelyOn = this.mSonySettings.queryRemoteControl(SonySettings.CONTROL_REMOTELY_SELECTION_ARGS);
        this.mIsSsipControlOn = getSsipControlSettings();
        registerReceivers();
        registerControlRemotelyObserver(this.mContentResolver);
        initializeNetworkInfo(this.mConnectivityManager);
    }

    private String getModelName(@NonNull ContentResolver contentResolver) {
        Preconditions.checkNotNull(contentResolver, "contentResolver == null");
        String modelName = com.sony.dtv.webapi.aidl.BuildConfig.FLAVOR;
        try {
            modelName = ModelVariationUtil.get(contentResolver, 51);
        } catch (Exception e) {
            Logger.e("Error has occurred with the get ModelName " + e, new Object[0]);
        }
        if (TextUtils.isEmpty(modelName)) {
            Logger.w("Model name is null, set dummy model name", new Object[0]);
            modelName = MODEL_NAME_DUMMY;
        }
        Logger.i("OUT ModelName: %s", modelName);
        return modelName;
    }

    private void registerReceivers() {
        this.mActionScreenStateReceiver = new ActionScreenStateReceiver();
        IntentFilter filterScreenState = new IntentFilter();
        filterScreenState.addAction("android.intent.action.SCREEN_OFF");
        filterScreenState.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.mActionScreenStateReceiver, filterScreenState);
        this.mSimpleIpControlStateReceiver = new SimpleIpControlStateReceiver();
        IntentFilter filterSimpleIpControlState = new IntentFilter();
        filterSimpleIpControlState.addAction(ACTION_CHANGE_SIMPLE_IP_CONTROL);
        registerReceiver(this.mSimpleIpControlStateReceiver, filterSimpleIpControlState);
        this.mNotifyWakeOnLanReceiver = new NotifyWakeOnLanReceiver();
        IntentFilter filterNotifyWakeOnLan = new IntentFilter();
        filterNotifyWakeOnLan.addAction(NOTIFY_WAKE_ON_LAN);
        registerReceiver(this.mNotifyWakeOnLanReceiver, filterNotifyWakeOnLan);
    }

    private void registerControlRemotelyObserver(@NonNull ContentResolver contentResolver) {
        Preconditions.checkNotNull(contentResolver, "contentResolver == null");
        this.mControlRemotelyObserver = new ContentObserver(new Handler()) { // from class: com.sony.dtv.b2b.vendorprotocol.VendorProtocolService.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                Logger.i("selfChange: %b", Boolean.valueOf(selfChange));
                VendorProtocolService.this.mIsControlRemotelyOn = VendorProtocolService.this.mSonySettings.queryRemoteControl(SonySettings.CONTROL_REMOTELY_SELECTION_ARGS);
                Logger.i("ControlRemotely: %1$b", Boolean.valueOf(VendorProtocolService.this.mIsControlRemotelyOn));
                if (VendorProtocolService.this.mIsControlRemotelyOn) {
                    VendorProtocolService.this.notifyStartSddp();
                    VendorProtocolService.this.notifyStartSsip();
                } else {
                    VendorProtocolService.this.notifyStopSddp();
                    VendorProtocolService.this.notifyStopSsip();
                }
            }
        };
        contentResolver.registerContentObserver(SonySettings.CONTROL_REMOTELY_URI, true, this.mControlRemotelyObserver);
    }

    @Override // android.app.Service
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {
        Logger.i("IN", new Object[0]);
        Preconditions.checkNotNull(intent, "intent == null");
        Notification notification = this.mNotificationManagerWrapper.createNotification();
        startForeground(101, notification);
        onProcessByReceiveIntent(intent);
        return 3;
    }

    private void onProcessByReceiveIntent(@NonNull Intent intent) {
        Preconditions.checkNotNull(intent, "intent == null");
        String action = (String) Preconditions.checkNotNull(intent.getAction(), "action == null");
        Logger.i("Receive broadcast %s", action);
        switch (action) {
            case "android.intent.action.BOOT_COMPLETED":
                notifyStartSddp();
                notifyStartSsip();
                requestNetwork(this.mConnectivityManager);
                break;
            case "android.intent.action.SCREEN_OFF":
                notifyStopSddp();
                break;
            case "android.intent.action.SCREEN_ON":
                notifyStartSddp();
                break;
            case "com.sony.dtv.homenetwork.broadcast.change_simple_ip_control":
                Bundle extra = (Bundle) Preconditions.checkNotNull(intent.getExtras(), "extra == null");
                int simpleIpControlValue = extra.getInt(SIMPLE_IP_CONTROL_BUNDLE_KEY);
                if (simpleIpControlValue == 1) {
                    this.mIsSsipControlOn = true;
                    notifyStartSsip();
                    break;
                } else {
                    if (simpleIpControlValue == 0) {
                        this.mIsSsipControlOn = false;
                        notifyStopSsip();
                    }
                    break;
                }
                break;
            case "com.sony.dtv.magicpacketnotifyservice.ACTION_RECEIVE_MAGICPACKET":
                notifyForceStartSddp();
                break;
            default:
                Logger.i("unknown action %s", action);
                break;
        }
    }

    private void requestNetwork(@NonNull final ConnectivityManager connectivityManager) {
        Preconditions.checkNotNull(connectivityManager, "connectivityManager == null");
        this.mNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.sony.dtv.b2b.vendorprotocol.VendorProtocolService.3
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                super.onAvailable(network);
                VendorProtocolService.this.networkStateChange(connectivityManager);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLosing(Network network, int maxMsToLive) {
                super.onLosing(network, maxMsToLive);
                VendorProtocolService.this.networkStateChange(connectivityManager);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                super.onLost(network);
                VendorProtocolService.this.networkStateChange(connectivityManager);
            }
        };
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(13);
        connectivityManager.registerNetworkCallback(builder.build(), this.mNetworkCallback);
    }

    @Override // android.app.Service
    public void onDestroy() {
        Logger.i("IN", new Object[0]);
        if (this.mConnectivityManager != null && this.mNetworkCallback != null) {
            this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
            this.mNetworkCallback = null;
        }
        unregisterReceiver(this.mActionScreenStateReceiver);
        unregisterReceiver(this.mSimpleIpControlStateReceiver);
        unregisterReceiver(this.mNotifyWakeOnLanReceiver);
        if (this.mControlRemotelyObserver != null && this.mContentResolver != null) {
            this.mContentResolver.unregisterContentObserver(this.mControlRemotelyObserver);
        }
        notifyStopSddp();
        this.mSddpProcess = null;
        notifyStopSsip();
        this.mSsipProcess = null;
        this.mAssetManager = null;
        if (this.mIsBindWebApiCore) {
            unbindService(this.mServiceConnection);
        }
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean bindWebApiCoreService() {
        Logger.d("Bind service(" + IWebApiCoreConfig.PACKAGE_NAME + "/" + IWebApiCoreConfig.CLASS_NAME + ")", new Object[0]);
        Intent intent = new Intent(IWebApiCore.class.getName());
        intent.setClassName(IWebApiCoreConfig.PACKAGE_NAME, IWebApiCoreConfig.CLASS_NAME);
        try {
            if (!bindService(intent, this.mServiceConnection, 0)) {
                Logger.e("bind service failed(" + IWebApiCoreConfig.CLASS_NAME + ")", new Object[0]);
                return false;
            }
            return true;
        } catch (SecurityException e) {
            Logger.e("bind service failed(" + IWebApiCoreConfig.CLASS_NAME + ")", e, new Object[0]);
            return false;
        }
    }

    private void retryBindWebApiCoreService() {
        Logger.d("Set schedule for the retry bind WebApiCoreService", new Object[0]);
        this.mScheduler = Executors.newSingleThreadScheduledExecutor();
        RetryBindWebApiCoreServiceTask bindWebApiCoreServiceTask = new RetryBindWebApiCoreServiceTask();
        this.mScheduledFuture = this.mScheduler.scheduleAtFixedRate(bindWebApiCoreServiceTask, START_SDDP_DELAY_MILLIS, START_SDDP_DELAY_MILLIS, TimeUnit.MILLISECONDS);
    }

    private class RetryBindWebApiCoreServiceTask implements Runnable {
        private RetryBindWebApiCoreServiceTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Logger.d("Retry bind WebApiCoreService", new Object[0]);
            if (VendorProtocolService.this.bindWebApiCoreService()) {
                VendorProtocolService.this.mScheduledFuture.cancel(true);
                VendorProtocolService.this.mScheduler.shutdownNow();
                Logger.d("Unset schedule for the retry bind WebApiCoreService", new Object[0]);
            }
        }
    }

    private void initializeNetworkInfo(@NonNull ConnectivityManager connectivityManager) {
        Preconditions.checkNotNull(connectivityManager, "connectivityManager == null");
        this.mMacAddress = getMacAddress(NETWORK_TYPE_NAME_ETHERNET);
        this.mIpAddress = null;
        this.mNetworkType = NETWORK_TYPE_NONE;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            this.mNetworkType = 9;
            this.mIpAddress = getIpAddress(NETWORK_TYPE_NAME_ETHERNET);
            return;
        }
        int networkType = networkInfo.getType();
        if (networkType == 9) {
            this.mNetworkType = 9;
            this.mIpAddress = getIpAddress(NETWORK_TYPE_NAME_ETHERNET);
        } else if (networkType == 1) {
            this.mNetworkType = 1;
            this.mIpAddress = getIpAddress(NETWORK_TYPE_NAME_WIFI);
        } else {
            Logger.e("Unknown network type(" + networkType + ")", new Object[0]);
        }
        Logger.i("network type(" + networkType + ")", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void networkStateChange(@NonNull ConnectivityManager connectivityManager) {
        String newIpAddress;
        Preconditions.checkNotNull(connectivityManager, "connectivityManager == null");
        Logger.d("networkStateChange()", new Object[0]);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        int beforeNetworkType = this.mNetworkType;
        if (activeNetworkInfo == null) {
            Logger.d("    activeNetworkInfo is null. Get eth0's IpAddress.", new Object[0]);
            this.mNetworkType = 9;
            newIpAddress = getIpAddress(NETWORK_TYPE_NAME_ETHERNET);
        } else {
            int networkType = activeNetworkInfo.getType();
            if (networkType == 9) {
                Logger.d("    activeNetworkInfo is Ethernet. Get eth0's IpAddress.", new Object[0]);
                this.mNetworkType = 9;
                newIpAddress = getIpAddress(NETWORK_TYPE_NAME_ETHERNET);
            } else if (networkType == 1) {
                Logger.d("    activeNetworkInfo is WiFi. Get wifi0's IpAddress.", new Object[0]);
                this.mNetworkType = 1;
                newIpAddress = getIpAddress(NETWORK_TYPE_NAME_WIFI);
            } else {
                Logger.e("Unknown network type(" + networkType + ")", new Object[0]);
                return;
            }
        }
        if (newIpAddress != null && !newIpAddress.equals(com.sony.dtv.webapi.aidl.BuildConfig.FLAVOR) && (this.mIpAddress == null || !newIpAddress.equals(this.mIpAddress))) {
            Logger.i("    IpAddress Changed to %s", newIpAddress);
            notifyStopSddp();
            this.mIpAddress = newIpAddress;
            if (this.mSddpProcess != null) {
                this.mSddpProcess.setIpAddress(this.mIpAddress);
            }
            notifyForceStartSddp();
        }
        if (this.mIpAddress != null && newIpAddress == null) {
            Logger.d("    Not got NewIpAddress.", new Object[0]);
            if (beforeNetworkType != 9 || this.mNetworkType != 9) {
                notifyStopSddp();
                this.mIpAddress = null;
            }
        }
        if (this.mIpAddress != null && newIpAddress != null && newIpAddress.equals(this.mIpAddress)) {
            Logger.d("    NewIpAddress was the same as the previous IpAddress.", new Object[0]);
            notifyStartSddp();
        }
    }

    private String getMacAddress(String type) {
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName(type);
            if (networkInterface == null) {
                Logger.e("null is returned from NetworkInterface.getByName()", new Object[0]);
                return null;
            }
            try {
                byte[] raw = networkInterface.getHardwareAddress();
                if (raw == null) {
                    return null;
                }
                StringBuilder builder = new StringBuilder();
                builder.setLength(0);
                for (byte aRaw : raw) {
                    builder.append(String.format("%02x", Byte.valueOf(aRaw)));
                }
                String macAddress = builder.toString();
                StringBuilder builder2 = new StringBuilder();
                builder2.setLength(0);
                for (int count = 0; count < macAddress.length(); count++) {
                    builder2.append(macAddress.substring(count, count + 1));
                }
                return builder2.toString().toUpperCase(Locale.getDefault());
            } catch (SocketException e) {
                Logger.e("getMacAddressString() exception:" + e.getMessage(), e, new Object[0]);
                return null;
            }
        } catch (SocketException e2) {
            Logger.e("Error has occurred with the NetworkInterface.getByName() " + e2, new Object[0]);
            return null;
        }
    }

    private String getIpAddress(String type) {
        String ipAddress = null;
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName(type);
            if (networkInterface != null) {
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    if (inetAddress instanceof Inet4Address) {
                        ipAddress = inetAddress.toString().replace("/", com.sony.dtv.webapi.aidl.BuildConfig.FLAVOR);
                        break;
                    }
                }
            }
            Logger.i("OUT ipAddress: %s", ipAddress);
            return ipAddress;
        } catch (SocketException e) {
            Logger.e("Error has occurred with the NetworkInterface.getByName() " + e, new Object[0]);
            return null;
        }
    }

    private void deploymentAsset(String path, boolean isRoot) throws Throwable {
        String[] fileList = null;
        try {
            fileList = this.mAssetManager.list(path);
        } catch (IOException e) {
            Logger.e("Error has occurred with the AssetManager.list() " + e, new Object[0]);
        }
        if (fileList == null) {
            Logger.e("null is returned from AssetManager.list()", new Object[0]);
            return;
        }
        String[] fileListSub = null;
        for (String aFileList : fileList) {
            if (!isRoot || aFileList.equals(ASSETS_ROOT_DIRECTORY_NAME)) {
                String delimiter = com.sony.dtv.webapi.aidl.BuildConfig.FLAVOR;
                if (!isRoot) {
                    delimiter = "/";
                }
                try {
                    fileListSub = this.mAssetManager.list(path + delimiter + aFileList);
                } catch (IOException e2) {
                    Logger.e("Error has occurred with the AssetManager.list() " + e2, new Object[0]);
                }
                if (fileListSub == null) {
                    Logger.e("null is returned from AssetManager.list()", new Object[0]);
                } else if (fileListSub.length == 0) {
                    copyFile(path + delimiter + aFileList, getWorkDirectory() + path + delimiter + aFileList);
                } else {
                    Logger.d("createFolder() FolderPath=" + getWorkDirectory() + path + delimiter + aFileList + "/", new Object[0]);
                    if (createFolder(getWorkDirectory() + path + delimiter + aFileList + "/")) {
                        deploymentAsset(path + delimiter + aFileList, false);
                    }
                }
            }
        }
    }

    private String getWorkDirectory() {
        return (String) Optional.ofNullable(getApplicationInfo().dataDir).filter(VendorProtocolService$$Lambda$0.$instance).map(VendorProtocolService$$Lambda$1.$instance).orElse(com.sony.dtv.webapi.aidl.BuildConfig.FLAVOR);
    }

    static final /* synthetic */ boolean lambda$getWorkDirectory$0$VendorProtocolService(String dirPath) {
        return !TextUtils.isEmpty(dirPath);
    }

    static final /* synthetic */ String lambda$getWorkDirectory$1$VendorProtocolService(String dirPath) {
        return dirPath + "/";
    }

    private boolean createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                Logger.e("Failed to create the folder", new Object[0]);
                return false;
            }
            boolean isExecutable = folder.setExecutable(true, true);
            boolean isWritable = folder.setWritable(true, true);
            boolean isReadable = folder.setReadable(true, true);
            Logger.d("isExecutable: %1$b, isWritable: %2$b, isReadable: %3$b", Boolean.valueOf(isExecutable), Boolean.valueOf(isWritable), Boolean.valueOf(isReadable));
        }
        return true;
    }

    /* JADX WARN: Code duplicated, block: B:36:0x011b A[Catch: IOException -> 0x0158, TRY_LEAVE, TryCatch #2 {IOException -> 0x0158, blocks: (B:34:0x0116, B:36:0x011b), top: B:50:0x0116 }] */
    private void copyFile(String copyFile, String destination) throws Throwable {
        File file = new File(destination);
        if (file.exists()) {
            Logger.d("deleteFile() File=" + destination, new Object[0]);
            boolean isDelete = file.delete();
            Logger.d("isDelete: %b", Boolean.valueOf(isDelete));
        }
        Logger.d("copyFile() File=" + destination, new Object[0]);
        InputStream in = null;
        FileOutputStream out = null;
        try {
            try {
                in = getAssets().open(copyFile);
                FileOutputStream out2 = new FileOutputStream(destination);
                try {
                    byte[] buffer = new byte[8192];
                    while (true) {
                        int actualRead = in.read(buffer);
                        if (actualRead == NETWORK_TYPE_NONE) {
                            break;
                        } else {
                            out2.write(buffer, 0, actualRead);
                        }
                    }
                    in.close();
                    out2.flush();
                    out2.close();
                    File newFile = new File(destination);
                    if (newFile.exists()) {
                        boolean isExecutable = newFile.setExecutable(true, true);
                        boolean isWritable = newFile.setWritable(true, true);
                        boolean isReadable = newFile.setReadable(true, true);
                        Logger.d("isExecutable: %1$b, isWritable: %2$b, isReadable: %3$b", Boolean.valueOf(isExecutable), Boolean.valueOf(isWritable), Boolean.valueOf(isReadable));
                    } else {
                        Logger.e("File is not found. file=" + destination, new Object[0]);
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            Logger.e("Error has occurred with the close stream " + e, new Object[0]);
                            return;
                        }
                    }
                    if (out2 != null) {
                        out2.close();
                    }
                } catch (IOException e2) {
                    e = e2;
                    out = out2;
                    Logger.e("Error has occurred with the copyFile() " + e, new Object[0]);
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e3) {
                            Logger.e("Error has occurred with the close stream " + e3, new Object[0]);
                            return;
                        }
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    out = out2;
                    if (in != null) {
                        try {
                            in.close();
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e4) {
                            Logger.e("Error has occurred with the close stream " + e4, new Object[0]);
                            throw th;
                        }
                    } else if (out != null) {
                        out.close();
                    }
                    throw th;
                }
            } catch (IOException e5) {
                e = e5;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private boolean getSsipControlSettings() {
        MtkTvHotel mtkTvHotel = new MtkTvHotel(getApplicationContext());
        return mtkTvHotel.getStartupApp() == 1;
    }
}
