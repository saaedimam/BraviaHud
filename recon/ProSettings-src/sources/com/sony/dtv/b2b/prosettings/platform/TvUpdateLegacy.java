package com.sony.dtv.b2b.prosettings.platform;

import android.os.PowerManager;
import com.mediatek.twoworlds.tv.MtkTvSwUpgrade;
import com.sony.dtv.b2b.prosettings.util.LogUtil;

/* JADX INFO: loaded from: classes.dex */
public class TvUpdateLegacy extends PlatformBase {
    private static final String TAG = "TvUpdateLegacy";
    private static int mDryRunDmyStatus;
    private static int mDryRunMode;
    protected static MtkTvSwUpgrade mMtkTvSwUpgrade;
    private static int DRYRUN_MODE_NORMAL = 0;
    private static int DRYRUN_MODE_OFFERPAYLOAD = DRYRUN_MODE_NORMAL + 1;
    private static int DRYRUN_MODE_FULL = DRYRUN_MODE_OFFERPAYLOAD + 1;

    private static void init() {
        if (mMtkTvSwUpgrade == null) {
            mMtkTvSwUpgrade = new MtkTvSwUpgrade();
            mDryRunMode = DRYRUN_MODE_NORMAL;
        }
    }

    public static String getSoftwareVersionDisplay() {
        LogUtil.LogI(TAG, "getSoftwareVersionDisplay()");
        init();
        return mMtkTvSwUpgrade.getSoftwareVersionDisplay();
    }

    public static int getSoftwareUpgradeStatus() {
        LogUtil.LogI(TAG, "getSoftwareUpgradeStatus()");
        init();
        if (mDryRunMode == DRYRUN_MODE_NORMAL) {
            return mMtkTvSwUpgrade.getSoftwareUpgradeStatus();
        }
        return mDryRunDmyStatus;
    }

    private static void sleepSync(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException unused) {
        }
    }

    public static int startSoftwareUpdateLegacy() {
        LogUtil.LogI(TAG, "startSoftwareUpdateLegacy()");
        init();
        if (mDryRunMode == DRYRUN_MODE_NORMAL) {
            new Thread(new Runnable() { // from class: com.sony.dtv.b2b.prosettings.platform.TvUpdateLegacy.1
                @Override // java.lang.Runnable
                public void run() {
                    LogUtil.LogI(TvUpdateLegacy.TAG, "Delay 3 sec executing start update.");
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    TvUpdateLegacy.mMtkTvSwUpgrade.startSoftwareUpdate();
                }
            }).start();
            return 0;
        }
        sleepSync(2000);
        mDryRunDmyStatus = 1;
        sleepSync(2000);
        mDryRunDmyStatus = 3;
        new Thread(new Runnable() { // from class: com.sony.dtv.b2b.prosettings.platform.TvUpdateLegacy.2
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.LogI(TvUpdateLegacy.TAG, "Dryrun reboot after 3 sec....");
                B2bcmdManager.execCmd(B2bcmdManager.B2BCMD_CLEANUP_FWWRITE, null, null, null);
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ((PowerManager) PlatformBase.mContext.getSystemService("power")).reboot(null);
            }
        }).start();
        return 0;
    }

    public static String getPackageName() {
        LogUtil.LogI(TAG, "getPackageName()");
        init();
        return mMtkTvSwUpgrade.getPackageName();
    }

    public static void setDryRunMode(int i) {
        LogUtil.LogI(TAG, "setDryRunMode(" + i + ")");
        init();
        mDryRunMode = i;
        mDryRunDmyStatus = -1;
    }
}
