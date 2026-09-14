package com.sony.dtv.b2b.prosettings;

import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class GV {
    public static final String ABUPDATEPACKAGE_FILENAME = "abupdatepkg.zip";
    private static final String B2B_DATADIRPATH_N = "/data/vendor/b2b/";
    private static final String B2B_DATADIRPATH_O = "/data/vendor/sony/b2b/";
    public static final String B2B_DATADIR_ROOT;
    public static final boolean BEFORE_TREBLE;
    public static final String BOOTANIMATION_ZIP_PATH;
    public static final String PACKAGE_NAME_NODERUNTIME_NORMAL = "com.sony.dtv.b2b.noderuntime.normal";
    public static final String PACKAGE_NAME_NODERUNTIME_PRIVILEGE = "com.sony.dtv.b2b.noderuntime.privilege";
    public static final String SYSPROP_KEY_B2BCMD = "sys.svp.b2bcmd.state";
    private static final String TAG = "GV";

    static {
        if (new File(B2B_DATADIRPATH_N).exists()) {
            B2B_DATADIR_ROOT = B2B_DATADIRPATH_N;
            BEFORE_TREBLE = true;
        } else {
            B2B_DATADIR_ROOT = B2B_DATADIRPATH_O;
            BEFORE_TREBLE = false;
        }
        BOOTANIMATION_ZIP_PATH = B2B_DATADIR_ROOT + "bootanimation.zip";
    }
}
