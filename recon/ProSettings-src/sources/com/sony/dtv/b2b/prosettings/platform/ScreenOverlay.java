package com.sony.dtv.b2b.prosettings.platform;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.sony.dtv.b2b.prosettings.R;
import com.sony.dtv.b2b.prosettings.util.LogUtil;

/* JADX INFO: loaded from: classes.dex */
public class ScreenOverlay extends PlatformBase {
    private static final String TAG = "ScreenOverlay";
    private static View mBlindfoldOverlayView;
    private static final Object mLockObj = new Object();

    private static boolean checkOverrayPermission() {
        return Settings.canDrawOverlays(mContext.getApplicationContext());
    }

    public static int showBlindfold(final boolean z) {
        int i;
        synchronized (mLockObj) {
            LogUtil.LogD(TAG, "showBlindfold(" + z + ")");
            if (!checkOverrayPermission()) {
                LogUtil.LogE(TAG, "no overlay permission");
            }
            final Handler handler = new Handler(Looper.getMainLooper());
            final int[] iArr = {-1};
            synchronized (handler) {
                handler.post(new Runnable() { // from class: com.sony.dtv.b2b.prosettings.platform.ScreenOverlay.1
                    @Override // java.lang.Runnable
                    public void run() {
                        WindowManager.LayoutParams layoutParams;
                        WindowManager windowManager = (WindowManager) PlatformBase.mContext.getApplicationContext().getSystemService("window");
                        if (z) {
                            if (ScreenOverlay.mBlindfoldOverlayView == null) {
                                LayoutInflater layoutInflaterFrom = LayoutInflater.from(PlatformBase.mContext.getApplicationContext());
                                if (Build.VERSION.SDK_INT < 26) {
                                    layoutParams = new WindowManager.LayoutParams(-2, -2, 2003, 1064, -3);
                                } else {
                                    layoutParams = new WindowManager.LayoutParams(-1, -1, 2038, 1064, -3);
                                }
                                View unused = ScreenOverlay.mBlindfoldOverlayView = layoutInflaterFrom.inflate(R.layout.blindfold_overlay, (ViewGroup) null);
                                ScreenOverlay.mBlindfoldOverlayView.setBackgroundColor(Color.parseColor("#ff000000"));
                                windowManager.addView(ScreenOverlay.mBlindfoldOverlayView, layoutParams);
                            }
                        } else if (ScreenOverlay.mBlindfoldOverlayView != null) {
                            windowManager.removeViewImmediate(ScreenOverlay.mBlindfoldOverlayView);
                            View unused2 = ScreenOverlay.mBlindfoldOverlayView = null;
                        }
                        synchronized (handler) {
                            iArr[0] = 0;
                            handler.notifyAll();
                        }
                    }
                });
                try {
                    handler.wait();
                } catch (InterruptedException unused) {
                }
            }
            i = iArr[0];
        }
        return i;
    }
}
