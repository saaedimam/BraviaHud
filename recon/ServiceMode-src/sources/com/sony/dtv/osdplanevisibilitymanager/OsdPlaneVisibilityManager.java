package com.sony.dtv.osdplanevisibilitymanager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class OsdPlaneVisibilityManager {
    private static final boolean DBG = true;
    private static final String VERSION = "ver.0.1";
    private Application mApplication;
    private List<OsdPlaneVisibilityRequestClient> mList = new ArrayList();
    private Application.ActivityLifecycleCallbacks mObserver = null;
    private static final String LOCAL_TAG = OsdPlaneVisibilityManager.class.getSimpleName();
    private static OsdPlaneVisibilityManager sInstance = new OsdPlaneVisibilityManager();

    public static synchronized void declareOsdPlaneVisibility(Activity activity, int state) {
        getInstance().declareOsdPlaneVisibilityInternal(activity, state);
    }

    public static synchronized void setOsdPlaneVisible(Context context, boolean visible) {
        getInstance().setOsdPlaneVisibleInternal(context, visible);
    }

    public static synchronized void forceOsdPlaneInvisible(Context context, boolean invisible) {
        getInstance().forceOsdPlaneInvisibleInternal(context, invisible);
    }

    public static String getVersion() {
        return VERSION;
    }

    public static synchronized OsdPlaneVisibilityManager getInstance() {
        if (sInstance == null) {
            sInstance = new OsdPlaneVisibilityManager();
        }
        return sInstance;
    }

    private synchronized void declareOsdPlaneVisibilityInternal(Activity activity, int state) {
        try {
            if (activity == null) {
                Log.e(LOCAL_TAG, "invalid activity : " + activity);
                return;
            }
            this.mApplication = activity.getApplication();
            if (this.mObserver != null && this.mApplication != null) {
                this.mApplication.unregisterActivityLifecycleCallbacks(this.mObserver);
                this.mObserver = null;
            }
            OsdPlaneVisibilityRequestClient client = addClientToList(activity, activity, state);
            this.mObserver = new Application.ActivityLifecycleCallbacks() { // from class: com.sony.dtv.osdplanevisibilitymanager.OsdPlaneVisibilityManager.1
                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityCreated(Activity activity2, Bundle savedInstanceState) {
                    OsdPlaneVisibilityManager.this.changeOsdPlaneInvisibleIfNecessary(activity2, 1);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityStarted(Activity activity2) {
                    OsdPlaneVisibilityManager.this.changeOsdPlaneInvisibleIfNecessary(activity2, 2);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityResumed(Activity activity2) {
                    OsdPlaneVisibilityManager.this.changeOsdPlaneInvisibleIfNecessary(activity2, 3);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityDestroyed(Activity activity2) {
                    OsdPlaneVisibilityManager.this.changeOsdPlaneInvisibleIfNecessary(activity2, 6);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityStopped(Activity activity2) {
                    OsdPlaneVisibilityManager.this.changeOsdPlaneInvisibleIfNecessary(activity2, 5);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityPaused(Activity activity2) {
                    OsdPlaneVisibilityManager.this.changeOsdPlaneInvisibleIfNecessary(activity2, 4);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivitySaveInstanceState(Activity activity2, Bundle savedInstanceState) {
                }
            };
            this.mApplication.registerActivityLifecycleCallbacks(this.mObserver);
            if (client != null) {
                client.setOsdVisibleRequest();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void setOsdPlaneVisibleInternal(Context context, boolean visible) {
        if (context == null) {
            Log.e(LOCAL_TAG, "invalid context : " + context);
        }
        if (visible) {
            OsdPlaneVisibilityRequestClient client = addClientToList(null, context, -1);
            if (client != null) {
                client.setOsdVisibleRequest();
            }
        } else {
            OsdPlaneVisibilityRequestClient client2 = getClient(context);
            if (client2 != null) {
                client2.removeOsdVisibleRequest();
                if (client2.getDisableRequest() == null) {
                    this.mList.remove(client2);
                    Log.i(LOCAL_TAG, "remained client is " + this.mList.size());
                }
            }
        }
    }

    private synchronized void forceOsdPlaneInvisibleInternal(Context context, boolean invisible) {
        if (context == null) {
            Log.e(LOCAL_TAG, "invalid context : " + context);
        }
        if (invisible) {
            OsdPlaneVisibilityRequestClient client = addClientToList(null, context, -1);
            if (client != null) {
                client.setOsdForceInvisibleRequest();
            }
        } else {
            OsdPlaneVisibilityRequestClient client2 = getClient(context);
            if (client2 != null) {
                client2.removeOsdForceInvisibleRequest();
                if (client2.getEnableRequest() == null) {
                    this.mList.remove(client2);
                    Log.i(LOCAL_TAG, "remained client is " + this.mList.size());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void changeOsdPlaneInvisibleIfNecessary(Activity activity, int state) {
        OsdPlaneVisibilityRequestClient client = getClient(activity, state);
        Log.i(LOCAL_TAG, "Updated Activity : " + activity);
        Log.i(LOCAL_TAG, "Updated state : " + state);
        if (client == null) {
            Log.i(LOCAL_TAG, "not change OSD status :");
            return;
        }
        Log.i(LOCAL_TAG, "remove OSD visible request");
        client.removeOsdVisibleRequest();
        this.mApplication.unregisterActivityLifecycleCallbacks(this.mObserver);
        this.mObserver = null;
        if (client.getDisableRequest() == null) {
            this.mList.remove(client);
            Log.i(LOCAL_TAG, "remained client is " + this.mList.size());
        }
    }

    private synchronized OsdPlaneVisibilityRequestClient addClientToList(Activity activity, Context context, int state) {
        OsdPlaneVisibilityRequestClient client;
        OsdPlaneVisibilityRequestClient.Builder builder = new OsdPlaneVisibilityRequestClient.Builder();
        client = builder.setActivity(activity).setContext(context).setDesiredState(state).build();
        OsdPlaneVisibilityRequestClient duplicatedClient = checkDuplicatedClient(client);
        if (duplicatedClient == null) {
            this.mList.add(client);
        } else {
            client = duplicatedClient;
        }
        return client;
    }

    private synchronized OsdPlaneVisibilityRequestClient checkDuplicatedClient(OsdPlaneVisibilityRequestClient client) {
        if (client == null) {
            return null;
        }
        for (OsdPlaneVisibilityRequestClient registeredClient : this.mList) {
            Context context = registeredClient.getContext();
            if (context.equals(client.getContext())) {
                return registeredClient;
            }
        }
        return null;
    }

    private synchronized OsdPlaneVisibilityRequestClient getClient(Activity activity, int state) {
        if (activity == null) {
            return null;
        }
        for (OsdPlaneVisibilityRequestClient client : this.mList) {
            if (activity.equals(client.getActivity()) && state == client.getDesiredState()) {
                return client;
            }
        }
        return null;
    }

    private synchronized OsdPlaneVisibilityRequestClient getClient(Context context) {
        if (context == null) {
            return null;
        }
        for (OsdPlaneVisibilityRequestClient client : this.mList) {
            if (context.equals(client.getContext())) {
                return client;
            }
        }
        return null;
    }
}
