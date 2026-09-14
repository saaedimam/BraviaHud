package com.sony.dtv.provider.modelvariation.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class ModelVariationUtil {
    private static final int First_ID = 0;
    private static final boolean LOCAL_LOG = false;
    public static final Uri CONTENT_URI = Uri.parse("content://com.sony.dtv.provider.modelvariation/info");
    private static HashMap<Integer, String> mMap = new HashMap<>();
    private static HashMap<Integer, String> mMapInt = new HashMap<>();

    public static void clearCache() {
        mMap.clear();
        mMapInt.clear();
    }

    public static String get(ContentResolver cr, int key) {
        if (mMap.containsKey(Integer.valueOf(key))) {
            return mMap.get(Integer.valueOf(key));
        }
        Cursor c = cr.query(CONTENT_URI, new String[]{"value"}, "key = ?", new String[]{Integer.toString(key)}, null);
        if (c == null) {
            return null;
        }
        String value = c.moveToNext() ? c.getString(0) : null;
        c.close();
        if (value != null) {
            mMap.put(Integer.valueOf(key), value);
        }
        return value;
    }

    public static String getInt(ContentResolver cr, int key) {
        if (mMapInt.containsKey(Integer.valueOf(key))) {
            return mMapInt.get(Integer.valueOf(key));
        }
        Cursor c = cr.query(CONTENT_URI, new String[]{"int"}, "key = ?", new String[]{Integer.toString(key)}, null);
        if (c == null) {
            return null;
        }
        String value = c.moveToNext() ? c.getString(0) : null;
        c.close();
        if (value != null) {
            mMapInt.put(Integer.valueOf(key), value);
        }
        return value;
    }
}
