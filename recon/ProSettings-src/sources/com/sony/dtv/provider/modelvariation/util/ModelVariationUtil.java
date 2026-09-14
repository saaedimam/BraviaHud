package com.sony.dtv.provider.modelvariation.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import com.sony.dtv.b2b.hotelmode.HotelSettings;
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

    public static String get(ContentResolver contentResolver, int i) {
        if (mMap.containsKey(Integer.valueOf(i))) {
            return mMap.get(Integer.valueOf(i));
        }
        Cursor cursorQuery = contentResolver.query(CONTENT_URI, new String[]{HotelSettings.VALUE}, "key = ?", new String[]{Integer.toString(i)}, null);
        if (cursorQuery == null) {
            return null;
        }
        String string = cursorQuery.moveToNext() ? cursorQuery.getString(0) : null;
        cursorQuery.close();
        if (string != null) {
            mMap.put(Integer.valueOf(i), string);
        }
        return string;
    }

    public static String getInt(ContentResolver contentResolver, int i) {
        if (mMapInt.containsKey(Integer.valueOf(i))) {
            return mMapInt.get(Integer.valueOf(i));
        }
        Cursor cursorQuery = contentResolver.query(CONTENT_URI, new String[]{"int"}, "key = ?", new String[]{Integer.toString(i)}, null);
        if (cursorQuery == null) {
            return null;
        }
        String string = cursorQuery.moveToNext() ? cursorQuery.getString(0) : null;
        cursorQuery.close();
        if (string != null) {
            mMapInt.put(Integer.valueOf(i), string);
        }
        return string;
    }
}
