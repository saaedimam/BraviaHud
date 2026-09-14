package com.sony.dtv.b2b.vendorprotocol;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.sony.dtv.b2b.vendorprotocol.logging.Logger;
import com.sony.dtv.b2b.vendorprotocol.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
class SonySettings {
    private static final String GLOBAL_SELECTION_CLAUSE = "key =?";
    private static final int GLOBAL_TABLE_COLUMN_INDEX = 0;
    private final ContentResolver mContentResolver;
    private static final Uri GLOBAL_URI = Uri.parse("content://com.sony.dtv.settingsprovider/global");
    static final Uri CONTROL_REMOTELY_URI = Uri.parse(GLOBAL_URI + "/seconddisp.remotecontrol");
    static final Uri SIMPLE_IP_CONTROL_URI = Uri.parse(GLOBAL_URI + "/vendorprotocol.ssip");
    static final Uri SDDP_CONTROL_URI = Uri.parse(GLOBAL_URI + "/vendorprotocol.sddp");
    private static final String[] GLOBAL_PROJECTION_KEY = {"value"};
    static final String[] CONTROL_REMOTELY_SELECTION_ARGS = {"seconddisp.remotecontrol"};
    static final String[] SIMPLE_IP_CONTROL_SELECTION_ARGS = {"vendorprotocol.ssip"};
    static final String[] SDDP_CONTROL_SELECTION_ARGS = {"vendorprotocol.sddp"};

    SonySettings(@NonNull Context context) {
        Preconditions.checkNotNull(context, "context == null");
        this.mContentResolver = (ContentResolver) Preconditions.checkNotNull(context.getContentResolver(), "contentResolver == null");
    }

    /* JADX WARN: Code duplicated, block: B:19:0x0050 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:24:0x005b  */
    /* JADX WARN: Code duplicated, block: B:31:0x0052 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @CheckResult
    boolean queryRemoteControl(@NonNull String... selectionArgs) throws Throwable {
        Throwable th = null;
        Preconditions.checkNotNull(selectionArgs, "selectionArgs == null");
        boolean result = false;
        Cursor cursor = this.mContentResolver.query(GLOBAL_URI, GLOBAL_PROJECTION_KEY, GLOBAL_SELECTION_CLAUSE, selectionArgs, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    result = Boolean.valueOf(cursor.getString(0)).booleanValue();
                    Logger.i("result: %b", Boolean.valueOf(result));
                }
            } catch (Throwable th2) {
                th = th2;
                if (cursor != null) {
                    if (th != null) {
                        cursor.close();
                    } else {
                        cursor.close();
                    }
                }
                throw th;
            }
        }
        if (cursor != null) {
            if (0 != 0) {
                try {
                    cursor.close();
                } catch (Throwable th3) {
                    th.addSuppressed(th3);
                }
            } else {
                cursor.close();
            }
        }
        return result;
    }

    /* JADX WARN: Code duplicated, block: B:21:0x0051 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:26:0x005c  */
    /* JADX WARN: Code duplicated, block: B:32:0x0053 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @CheckResult
    boolean query(@NonNull String... selectionArgs) throws Throwable {
        Throwable th = null;
        Preconditions.checkNotNull(selectionArgs, "selectionArgs == null");
        int result = 0;
        Cursor cursor = this.mContentResolver.query(GLOBAL_URI, GLOBAL_PROJECTION_KEY, GLOBAL_SELECTION_CLAUSE, selectionArgs, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    result = Integer.parseInt(cursor.getString(0));
                    Logger.i("result: %b", Integer.valueOf(result));
                }
            } catch (Throwable th2) {
                th = th2;
                if (cursor != null) {
                    if (th != null) {
                        cursor.close();
                    } else {
                        cursor.close();
                    }
                }
                throw th;
            }
        }
        if (cursor != null) {
            if (0 != 0) {
                try {
                    cursor.close();
                } catch (Throwable th3) {
                    th.addSuppressed(th3);
                }
            } else {
                cursor.close();
            }
        }
        return result == 1;
    }
}
