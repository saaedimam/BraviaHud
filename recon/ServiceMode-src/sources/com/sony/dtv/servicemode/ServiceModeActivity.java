package com.sony.dtv.servicemode;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.tv.TvContract;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import android.media.tv.TvView;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import com.mediatek.twoworlds.tv.MtkTvKeyEvent;
import com.mediatek.twoworlds.tv.MtkTvSpecialMode;
import com.sony.dtv.arib.AribTvAribStack;
import com.sony.dtv.arib.IAribConnectCallback;
import com.sony.dtv.arib.system.AribTvServiceMode;
import com.sony.dtv.osdplanevisibilitymanager.ActivityState;
import com.sony.dtv.osdplanevisibilitymanager.OsdPlaneVisibilityManager;
import com.sony.dtv.tvinput.provider.SonyTvContract;

/* JADX INFO: loaded from: classes.dex */
public class ServiceModeActivity extends Activity {
    private static final String ACTION_SELF_DIAG_MODE = "com.sony.dtv.intent.action.SELF_DIAG_MODE";
    private static final boolean LOCAL_LOGD = false;
    private static final String TAG = "ServiceMode";
    private AribTvAribStack mAribTvAribStack;
    private AribTvServiceMode mAribTvServiceMode;
    private State mCurrentState;
    private boolean mIsJpModel;
    private boolean mIsSelfDiagMode;
    private MtkTvKeyEvent mMtkTvKeyEvent;
    private MtkTvSpecialMode mMtkTvSpecialMode;
    private State mNextState;
    private TvView mTvView;

    /* JADX INFO: renamed from: -assertionsDisabled, reason: not valid java name */
    static final /* synthetic */ boolean f0assertionsDisabled = !ServiceModeActivity.class.desiredAssertionStatus();
    private static int DELAY_BEFORE_TUNE = 1000;
    private static int DELAY_AFTER_TUNE = 3000;
    private Handler mHandler = new Handler();
    private Runnable mTune = new Runnable() { // from class: com.sony.dtv.servicemode.ServiceModeActivity.1
        @Override // java.lang.Runnable
        public void run() throws Throwable {
            ServiceModeActivity.this.tune();
            ServiceModeActivity.this.mHandler.removeCallbacks(ServiceModeActivity.this.mGoToNextState);
            ServiceModeActivity.this.mHandler.postDelayed(ServiceModeActivity.this.mGoToNextState, ServiceModeActivity.DELAY_AFTER_TUNE);
        }
    };
    private Runnable mGoToNextState = new Runnable() { // from class: com.sony.dtv.servicemode.ServiceModeActivity.2
        @Override // java.lang.Runnable
        public void run() {
            ServiceModeActivity.this.mCurrentState = ServiceModeActivity.this.mNextState;
            ServiceModeActivity.this.mCurrentState.enter(ServiceModeActivity.this);
        }
    };

    private interface StateHandler {
        void enter(ServiceModeActivity serviceModeActivity);

        void leave(ServiceModeActivity serviceModeActivity);
    }

    private enum State implements StateHandler {
        INIT { // from class: com.sony.dtv.servicemode.ServiceModeActivity.State.1
            @Override // com.sony.dtv.servicemode.ServiceModeActivity.StateHandler
            public void enter(ServiceModeActivity activity) {
            }

            @Override // com.sony.dtv.servicemode.ServiceModeActivity.StateHandler
            public void leave(ServiceModeActivity activity) {
            }
        },
        MTK_SERVICE_MODE { // from class: com.sony.dtv.servicemode.ServiceModeActivity.State.2
            @Override // com.sony.dtv.servicemode.ServiceModeActivity.StateHandler
            public void enter(ServiceModeActivity activity) {
                activity.mMtkTvSpecialMode.enterSpecialMode(2);
            }

            @Override // com.sony.dtv.servicemode.ServiceModeActivity.StateHandler
            public void leave(ServiceModeActivity activity) {
                activity.mMtkTvSpecialMode.quitSpecialMode(2);
            }
        },
        ARIB_SERVICE_MODE { // from class: com.sony.dtv.servicemode.ServiceModeActivity.State.3
            @Override // com.sony.dtv.servicemode.ServiceModeActivity.StateHandler
            public void enter(ServiceModeActivity activity) {
                activity.mAribTvServiceMode.startAribServiceMode();
            }

            @Override // com.sony.dtv.servicemode.ServiceModeActivity.StateHandler
            public void leave(ServiceModeActivity activity) {
                activity.mAribTvServiceMode.endAribServiceMode();
            }
        },
        SELF_DIAG_MODE { // from class: com.sony.dtv.servicemode.ServiceModeActivity.State.4
            @Override // com.sony.dtv.servicemode.ServiceModeActivity.StateHandler
            public void enter(ServiceModeActivity activity) {
                activity.mMtkTvSpecialMode.enterSpecialMode(3);
            }

            @Override // com.sony.dtv.servicemode.ServiceModeActivity.StateHandler
            public void leave(ServiceModeActivity activity) {
                activity.mMtkTvSpecialMode.quitSpecialMode(3);
            }
        };

        /* synthetic */ State(State state) {
            this();
        }

        /* JADX INFO: renamed from: values, reason: to resolve conflict with enum method */
        public static State[] valuesCustom() {
            return values();
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tvview_layout);
        this.mTvView = (TvView) findViewById(R.id.tvview);
        this.mIsJpModel = getResources().getBoolean(R.bool.isJpModel);
        Log.i(TAG, "isJpModel: " + this.mIsJpModel);
        Intent intent = getIntent();
        this.mIsSelfDiagMode = ACTION_SELF_DIAG_MODE.equals(intent.getAction());
        this.mMtkTvSpecialMode = MtkTvSpecialMode.getInstance();
        this.mMtkTvKeyEvent = new MtkTvKeyEvent();
        this.mCurrentState = State.INIT;
        this.mNextState = State.INIT;
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        this.mIsSelfDiagMode = ACTION_SELF_DIAG_MODE.equals(intent.getAction());
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        OsdPlaneVisibilityManager.setOsdPlaneVisible(this, true);
        this.mTvView.setStreamVolume(1.0f);
        String text = getString(this.mIsSelfDiagMode ? R.string.start_self_diag_mode : R.string.start_service_mode);
        Toast toast = Toast.makeText(this, text, 1);
        toast.show();
        final State nextState = this.mIsSelfDiagMode ? State.SELF_DIAG_MODE : State.MTK_SERVICE_MODE;
        if (this.mIsJpModel) {
            connecAribStack(new Runnable() { // from class: com.sony.dtv.servicemode.ServiceModeActivity.3
                @Override // java.lang.Runnable
                public void run() {
                    ServiceModeActivity.this.mAribTvServiceMode.disableAribCaption();
                    ServiceModeActivity.this.transitTo(nextState);
                }
            });
        } else {
            transitTo(nextState);
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "exit " + this.mNextState);
        this.mCurrentState.leave(this);
        this.mCurrentState = State.INIT;
        this.mNextState = State.INIT;
        this.mHandler.removeCallbacks(this.mGoToNextState);
        this.mHandler.removeCallbacks(this.mTune);
        if (this.mIsJpModel) {
            this.mAribTvServiceMode.enableAribCaption();
            disconnectAribStack();
        }
        this.mTvView.reset();
        OsdPlaneVisibilityManager.setOsdPlaneVisible(this, LOCAL_LOGD);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 183:
                if (!this.mIsSelfDiagMode) {
                    transitTo(State.MTK_SERVICE_MODE);
                }
                return true;
            case 184:
            default:
                return passThroughKeyEventToMw(keyCode, event);
            case 185:
                if (this.mIsJpModel && (!this.mIsSelfDiagMode)) {
                    transitTo(State.ARIB_SERVICE_MODE);
                }
                return true;
            case 186:
                finish();
                return true;
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 183:
            case 185:
            case 186:
                return true;
            case 184:
            default:
                return passThroughKeyEventToMw(keyCode, event);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void transitTo(State nextState) {
        if (nextState == this.mNextState) {
            return;
        }
        Log.i(TAG, "change to " + nextState);
        if (nextState == State.ARIB_SERVICE_MODE && (!this.mIsJpModel)) {
            Log.w(TAG, "not JP model. ignored");
            return;
        }
        if (this.mNextState == this.mCurrentState) {
            this.mCurrentState.leave(this);
        }
        this.mNextState = nextState;
        this.mHandler.removeCallbacks(this.mGoToNextState);
        this.mHandler.removeCallbacks(this.mTune);
        this.mHandler.postDelayed(this.mTune, DELAY_BEFORE_TUNE);
    }

    private boolean passThroughKeyEventToMw(int keyCode, KeyEvent event) {
        int dfbKeyCode = this.mMtkTvKeyEvent.androidKeyToDFBkey(keyCode);
        if (dfbKeyCode == -1) {
            return LOCAL_LOGD;
        }
        if (event.getRepeatCount() != 0) {
            return true;
        }
        switch (event.getAction()) {
            case 0:
                this.mMtkTvKeyEvent.sendKey(0, dfbKeyCode);
                return true;
            case ActivityState.CREATED /* 1 */:
                this.mMtkTvKeyEvent.sendKey(1, dfbKeyCode);
                return true;
            default:
                return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tune() throws Throwable {
        Uri channelUri;
        TvInputInfo lastInput = getLastInput();
        if (lastInput == null) {
            return;
        }
        if (lastInput.isPassthroughInput()) {
            channelUri = TvContract.buildChannelUriForPassthroughInput(lastInput.getId());
        } else {
            channelUri = TvContract.buildChannelUri(getLastChannel());
        }
        this.mTvView.tune(lastInput.getId(), channelUri);
    }

    /* JADX WARN: Code duplicated, block: B:23:0x003c  */
    /* JADX WARN: Code duplicated, block: B:29:0x0048  */
    /* JADX WARN: Code duplicated, block: B:38:0x0037 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    private TvInputInfo getLastInput() throws Throwable {
        Throwable th;
        String lastInput;
        Throwable th2 = null;
        ContentResolver cr = getContentResolver();
        Cursor cursor = null;
        try {
            Cursor c = queryLastInput(cr);
            if (c != null && c.moveToNext()) {
                lastInput = c.getString(0);
            } else {
                Log.w(TAG, "failed to get last input id. Set to HDMI1");
                lastInput = "HDMI1";
            }
            if (c != null) {
                try {
                    c.close();
                } catch (Throwable th3) {
                    th2 = th3;
                }
            }
            if (th2 != null) {
                throw th2;
            }
            TvInputManager manager = (TvInputManager) getSystemService("tv_input");
            TvInputInfo result = manager.getTvInputInfo(lastInput);
            if (result == null) {
                Log.e(TAG, "failed to get TvInputInfo of the last input");
            }
            return result;
        } catch (Throwable th4) {
            try {
                throw th4;
            } catch (Throwable th5) {
                th2 = th4;
                th = th5;
                if (0 != 0) {
                    cursor.close();
                }
                if (th2 != null) {
                    throw th2;
                }
                throw th;
            }
        }
    }

    private static Cursor queryLastInput(ContentResolver cr) {
        Uri contentUri = SonyTvContract.LastWatchedInput.CONTENT_URI;
        String[] projection = {"input_id"};
        return cr.query(contentUri, projection, null, null, null);
    }

    /* JADX WARN: Code duplicated, block: B:31:0x0045  */
    /* JADX WARN: Code duplicated, block: B:37:0x0051  */
    /* JADX WARN: Code duplicated, block: B:46:0x0040 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    private long getLastChannel() throws Throwable {
        Throwable th;
        Throwable th2 = null;
        ContentResolver cr = getContentResolver();
        Cursor cursor = null;
        try {
            Cursor c = queryLastChannel(cr);
            if (c != null && c.moveToNext()) {
                long j = c.getLong(0);
                if (c != null) {
                    try {
                        c.close();
                    } catch (Throwable th3) {
                        th2 = th3;
                    }
                }
                if (th2 != null) {
                    throw th2;
                }
                return j;
            }
            Log.w(TAG, "failed to get last channel");
            if (c != null) {
                try {
                    c.close();
                } catch (Throwable th4) {
                    th2 = th4;
                }
            }
            if (th2 != null) {
                throw th2;
            }
            return -1L;
        } catch (Throwable th5) {
            try {
                throw th5;
            } catch (Throwable th6) {
                th2 = th5;
                th = th6;
                if (0 != 0) {
                    cursor.close();
                }
                if (th2 != null) {
                    throw th2;
                }
                throw th;
            }
        }
    }

    private static Cursor queryLastChannel(ContentResolver cr) {
        Uri contentUri = SonyTvContract.LastWatchedChannel.CONTENT_URI;
        String[] projection = {"channel_id"};
        return cr.query(contentUri, projection, null, null, "sequence_no DESC");
    }

    private void connecAribStack(final Runnable taskToRunAfter) {
        if (!f0assertionsDisabled && !this.mIsJpModel) {
            throw new AssertionError();
        }
        this.mAribTvAribStack = new AribTvAribStack();
        this.mAribTvServiceMode = new AribTvServiceMode();
        AribTvAribStack aribTvAribStack = this.mAribTvAribStack;
        AribTvAribStack.asyncConnect(new IAribConnectCallback() { // from class: com.sony.dtv.servicemode.ServiceModeActivity.4
            public void onConnectionResult(int result, long connectId) {
                if (result != 0) {
                    Log.e(ServiceModeActivity.TAG, "failed to connect ARIB Stack");
                }
                ServiceModeActivity.this.runOnUiThread(taskToRunAfter);
            }
        });
    }

    private void disconnectAribStack() {
        if (!f0assertionsDisabled && !this.mIsJpModel) {
            throw new AssertionError();
        }
        this.mAribTvAribStack.disconnect();
        this.mAribTvAribStack = null;
        this.mAribTvServiceMode = null;
    }
}
