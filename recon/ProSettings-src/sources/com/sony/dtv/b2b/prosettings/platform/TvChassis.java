package com.sony.dtv.b2b.prosettings.platform;

import com.mediatek.twoworlds.tv.MtkTvBivl;
import com.mediatek.twoworlds.tv.MtkTvConfig;
import com.mediatek.twoworlds.tv.MtkTvSony;
import com.sony.dtv.b2b.prosettings.util.ArrayUtil;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class TvChassis {
    private static MtkTvBivl mMtkTvBivl;
    private static MtkTvConfig mMtkTvConfig;
    private static MtkTvSony mMtkTvSony;

    private static void init() {
        if (mMtkTvBivl == null) {
            mMtkTvBivl = new MtkTvBivl();
        }
        if (mMtkTvSony == null) {
            mMtkTvSony = new MtkTvSony();
        }
        if (mMtkTvConfig == null) {
            mMtkTvConfig = new MtkTvConfig();
        }
    }

    public static String getDeviceId() {
        init();
        return mMtkTvBivl.getDeviceId();
    }

    public static int set4KBEGeneralControlCommand(List<Byte> list) {
        init();
        return mMtkTvSony.set4KBEGeneralControlCommand(ArrayUtil.toByteArray(list));
    }

    public static List get4KBEGeneralControlCommand() {
        init();
        return ArrayUtil.toByteList(mMtkTvSony.get4KBEGeneralControlCommand());
    }

    public static int getTotalOperationTime() {
        init();
        return mMtkTvConfig.getConfigValue("g_misc__total_op_time");
    }

    public static int getPanelOperationTime() {
        init();
        return mMtkTvConfig.getConfigValue("g_misc__panel_op_time");
    }

    public static int getBootCount() {
        init();
        return mMtkTvConfig.getConfigValue("g_misc__boot_count");
    }
}
