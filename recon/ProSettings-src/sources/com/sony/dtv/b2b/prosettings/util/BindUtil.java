package com.sony.dtv.b2b.prosettings.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import com.sony.dtv.b2b.prosettings.util.J8Compat.BiConsumer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BindUtil {
    private static final String TAG = "BindUtil";
    private static final int TIMEOUT = 60000;
    private final List<BiConsumer<ComponentName, IBinder>> mAsyncCallbacks;
    private final String mBindAction;
    private IBinder mBinder;
    private final String mClassName;
    private final Context mContext;
    private ComponentName mName;
    private final String mPackageName;
    private final ServiceConnection mServiceConnection;

    protected void onBindingDied(ComponentName componentName) {
    }

    protected void onServiceConnected(ComponentName componentName, IBinder iBinder) {
    }

    protected void onServiceDisconnected(ComponentName componentName) {
        this.mContext.unbindService(this.mServiceConnection);
    }

    public BindUtil(Context context, String str, String str2) {
        this.mBinder = null;
        this.mName = null;
        this.mServiceConnection = new ServiceConnection() { // from class: com.sony.dtv.b2b.prosettings.util.BindUtil.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                LogUtil.LogD(BindUtil.TAG, "onServiceConnected: " + componentName.getPackageName() + "/" + componentName.getClassName());
                synchronized (BindUtil.this) {
                    BindUtil.this.mBinder = iBinder;
                    BindUtil.this.mName = componentName;
                    BindUtil.this.onServiceConnected(componentName, iBinder);
                    Iterator it = BindUtil.this.mAsyncCallbacks.iterator();
                    while (it.hasNext()) {
                        ((BiConsumer) it.next()).accept(componentName, iBinder);
                    }
                    BindUtil.this.mAsyncCallbacks.clear();
                    BindUtil.this.notifyAll();
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                LogUtil.LogD(BindUtil.TAG, "onServiceDisconnected: " + componentName.getPackageName() + "/" + componentName.getClassName());
                synchronized (BindUtil.this) {
                    BindUtil.this.mBinder = null;
                    BindUtil.this.mName = null;
                    BindUtil.this.onServiceDisconnected(componentName);
                    BindUtil.this.notifyAll();
                }
            }

            @Override // android.content.ServiceConnection
            public void onBindingDied(ComponentName componentName) {
                LogUtil.LogD(BindUtil.TAG, "onBindingDied: " + componentName.getPackageName() + "/" + componentName.getClassName());
                synchronized (BindUtil.this) {
                    BindUtil.this.mBinder = null;
                    BindUtil.this.mName = null;
                    BindUtil.this.onBindingDied(componentName);
                    BindUtil.this.notifyAll();
                }
            }
        };
        this.mContext = context;
        this.mPackageName = str;
        this.mClassName = str2;
        this.mBindAction = "";
        this.mAsyncCallbacks = new ArrayList();
    }

    public BindUtil(Context context, String str, String str2, String str3) {
        this.mBinder = null;
        this.mName = null;
        this.mServiceConnection = new ServiceConnection() { // from class: com.sony.dtv.b2b.prosettings.util.BindUtil.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                LogUtil.LogD(BindUtil.TAG, "onServiceConnected: " + componentName.getPackageName() + "/" + componentName.getClassName());
                synchronized (BindUtil.this) {
                    BindUtil.this.mBinder = iBinder;
                    BindUtil.this.mName = componentName;
                    BindUtil.this.onServiceConnected(componentName, iBinder);
                    Iterator it = BindUtil.this.mAsyncCallbacks.iterator();
                    while (it.hasNext()) {
                        ((BiConsumer) it.next()).accept(componentName, iBinder);
                    }
                    BindUtil.this.mAsyncCallbacks.clear();
                    BindUtil.this.notifyAll();
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                LogUtil.LogD(BindUtil.TAG, "onServiceDisconnected: " + componentName.getPackageName() + "/" + componentName.getClassName());
                synchronized (BindUtil.this) {
                    BindUtil.this.mBinder = null;
                    BindUtil.this.mName = null;
                    BindUtil.this.onServiceDisconnected(componentName);
                    BindUtil.this.notifyAll();
                }
            }

            @Override // android.content.ServiceConnection
            public void onBindingDied(ComponentName componentName) {
                LogUtil.LogD(BindUtil.TAG, "onBindingDied: " + componentName.getPackageName() + "/" + componentName.getClassName());
                synchronized (BindUtil.this) {
                    BindUtil.this.mBinder = null;
                    BindUtil.this.mName = null;
                    BindUtil.this.onBindingDied(componentName);
                    BindUtil.this.notifyAll();
                }
            }
        };
        this.mContext = context;
        this.mPackageName = str;
        this.mClassName = str2;
        this.mBindAction = str3;
        this.mAsyncCallbacks = new ArrayList();
    }

    public synchronized IBinder getBinder() {
        return this.mBinder;
    }

    public synchronized boolean isBind() {
        return this.mBinder != null;
    }

    public boolean bind() {
        LogUtil.LogD(TAG, "bind(): " + this.mPackageName + "/" + this.mClassName);
        return bindImpl();
    }

    private synchronized boolean bindImpl() {
        if (isBind()) {
            LogUtil.LogD(TAG, "bindImpl: Already bineded. " + this.mPackageName + "/" + this.mClassName);
            return false;
        }
        Intent className = new Intent().setClassName(this.mPackageName, this.mClassName);
        if (!this.mBindAction.isEmpty()) {
            className.setAction(this.mBindAction);
        }
        if (this.mContext.bindService(className, this.mServiceConnection, 1)) {
            return true;
        }
        LogUtil.LogE(TAG, "bindImpl: Fail to bind. " + this.mPackageName + "/" + this.mClassName);
        return false;
    }

    private static void execCallOnMainThread(final BiConsumer<ComponentName, IBinder> biConsumer, final ComponentName componentName, final IBinder iBinder) {
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.sony.dtv.b2b.prosettings.util.BindUtil.2
            @Override // java.lang.Runnable
            public void run() {
                biConsumer.accept(componentName, iBinder);
            }
        });
    }

    public synchronized void bindAsync(BiConsumer<ComponentName, IBinder> biConsumer) {
        LogUtil.LogD(TAG, "bindAsync(): " + this.mPackageName + "/" + this.mClassName);
        if (bindImpl()) {
            this.mAsyncCallbacks.add(biConsumer);
        } else {
            execCallOnMainThread(biConsumer, this.mName, getBinder());
        }
    }

    public synchronized boolean syncBind() {
        LogUtil.LogD(TAG, "syncBind(): " + this.mPackageName + "/" + this.mClassName);
        if (Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
            throw new IllegalThreadStateException("syncBind MUST NOT be called from UI thread.");
        }
        if (this.mBinder != null) {
            LogUtil.LogD(TAG, "syncBind: Already bineded. " + this.mPackageName + "/" + this.mClassName);
            return false;
        }
        Intent className = new Intent().setClassName(this.mPackageName, this.mClassName);
        if (!this.mBindAction.isEmpty()) {
            className.setAction(this.mBindAction);
        }
        if (!this.mContext.bindService(className, this.mServiceConnection, 1)) {
            LogUtil.LogE(TAG, "syncBind: Fail to bind. " + this.mPackageName + "/" + this.mClassName);
            return false;
        }
        return waitBind();
    }

    public synchronized void unbind() {
        if (this.mBinder == null) {
            LogUtil.LogD(TAG, "unbind: Not bind. " + this.mPackageName + "/" + this.mClassName);
        }
        LogUtil.LogD(TAG, "unbind(): " + this.mPackageName + "/" + this.mClassName);
        this.mBinder = null;
        this.mContext.unbindService(this.mServiceConnection);
    }

    public synchronized boolean waitBind() {
        if (Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
            throw new IllegalThreadStateException("syncBind MUST NOT be called from UI thread.");
        }
        if (this.mBinder == null) {
            LogUtil.LogD(TAG, "waitBind(): " + this.mPackageName + "/" + this.mClassName);
            try {
                wait(60000L);
                if (this.mBinder == null) {
                    LogUtil.LogE(TAG, "waitBind: Bind timeout. " + this.mPackageName + "/" + this.mClassName);
                    return false;
                }
            } catch (InterruptedException e) {
                LogUtil.LogE(TAG, "waitBind: " + e.getMessage());
                return false;
            }
        }
        return true;
    }
}
