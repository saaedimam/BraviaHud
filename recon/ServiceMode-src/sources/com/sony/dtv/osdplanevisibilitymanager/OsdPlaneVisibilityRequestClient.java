package com.sony.dtv.osdplanevisibilitymanager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class OsdPlaneVisibilityRequestClient {
    private static final boolean DBG = true;
    private static final String LOCAL_TAG = OsdPlaneVisibilityRequestClient.class.getSimpleName();
    private static final String OSD_DISABLER_CLASS_NAME = "com.sony.dtv.osdplanevisibilitymanager.OsdDisabler";
    private static final String OSD_ENABLER_CLASS_NAME = "com.sony.dtv.osdplanevisibilitymanager.OsdEnabler";
    private static final String OSD_MANAGER_PKG_NAME = "com.sony.dtv.osdplanevisibilitymanager";
    private Activity mActivity;
    private Context mContext;
    private int mDesiredState;
    private IOsdDisabler mDisabler;
    private ServiceConnection mDisablerConnection;
    private IOsdEnabler mEnabler;
    private ServiceConnection mEnablerConnection;

    private OsdPlaneVisibilityRequestClient(Activity activity, Context context, int state) {
        this.mActivity = null;
        this.mContext = null;
        this.mDesiredState = -1;
        this.mEnabler = null;
        this.mDisabler = null;
        this.mEnablerConnection = new ServiceConnection() { // from class: com.sony.dtv.osdplanevisibilitymanager.OsdPlaneVisibilityRequestClient.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(OsdPlaneVisibilityRequestClient.LOCAL_TAG, "OSD visible request has been set");
                OsdPlaneVisibilityRequestClient.this.mEnabler = IOsdEnabler.Stub.asInterface(service);
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName name) {
                Log.i(OsdPlaneVisibilityRequestClient.LOCAL_TAG, "OSD visible request has been removed");
                OsdPlaneVisibilityRequestClient.this.mEnabler = null;
            }
        };
        this.mDisablerConnection = new ServiceConnection() { // from class: com.sony.dtv.osdplanevisibilitymanager.OsdPlaneVisibilityRequestClient.2
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(OsdPlaneVisibilityRequestClient.LOCAL_TAG, "OSD invisible request has been set");
                OsdPlaneVisibilityRequestClient.this.mDisabler = IOsdDisabler.Stub.asInterface(service);
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName name) {
                Log.i(OsdPlaneVisibilityRequestClient.LOCAL_TAG, "OSD invisible request has been removed");
                OsdPlaneVisibilityRequestClient.this.mDisabler = null;
            }
        };
        this.mActivity = activity;
        this.mContext = context;
        this.mDesiredState = state;
    }

    public synchronized Activity getActivity() {
        return this.mActivity;
    }

    public synchronized Context getContext() {
        return this.mContext;
    }

    public synchronized int getDesiredState() {
        return this.mDesiredState;
    }

    public synchronized IOsdEnabler getEnableRequest() {
        return this.mEnabler;
    }

    public synchronized IOsdDisabler getDisableRequest() {
        return this.mDisabler;
    }

    public synchronized void setOsdVisibleRequest() {
        if (this.mEnabler != null) {
            Log.i(LOCAL_TAG, "already set OSD visible request");
            return;
        }
        Log.i(LOCAL_TAG, "set OSD visible request");
        Intent intent = new Intent();
        intent.setClassName(OSD_MANAGER_PKG_NAME, OSD_ENABLER_CLASS_NAME);
        this.mContext.bindService(intent, this.mEnablerConnection, 1);
    }

    public synchronized void removeOsdVisibleRequest() {
        if (this.mContext == null) {
            Log.e(LOCAL_TAG, "no valid context");
            return;
        }
        if (this.mEnabler != null) {
            Log.i(LOCAL_TAG, "remove OSD visible request");
            this.mContext.unbindService(this.mEnablerConnection);
        }
    }

    public synchronized void setOsdForceInvisibleRequest() {
        if (this.mDisabler != null) {
            Log.i(LOCAL_TAG, "already set OSD invisible request");
            return;
        }
        Log.i(LOCAL_TAG, "set OSD invisible request");
        Intent intent = new Intent();
        intent.setClassName(OSD_MANAGER_PKG_NAME, OSD_DISABLER_CLASS_NAME);
        this.mContext.bindService(intent, this.mDisablerConnection, 1);
    }

    public synchronized void removeOsdForceInvisibleRequest() {
        if (this.mContext == null) {
            Log.e(LOCAL_TAG, "no valid context");
            return;
        }
        if (this.mDisabler != null) {
            Log.i(LOCAL_TAG, "remove OSD visible request");
            this.mContext.unbindService(this.mDisablerConnection);
        }
    }

    public static final class Builder {
        private Activity mActivity = null;
        private Context mContext = null;
        private int mDesiredState = -1;

        public final Builder setActivity(Activity activity) {
            this.mActivity = activity;
            return this;
        }

        public final Builder setContext(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("context is NULL");
            }
            this.mContext = context;
            return this;
        }

        public final Builder setDesiredState(int state) {
            this.mDesiredState = state;
            return this;
        }

        public OsdPlaneVisibilityRequestClient build() {
            return new OsdPlaneVisibilityRequestClient(this.mActivity, this.mContext, this.mDesiredState);
        }
    }
}
