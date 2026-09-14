package com.sony.dtv.b2b.prosettings;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import com.sony.dtv.b2b.hotelmode.HotelSettings;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class DataProvider extends ContentProvider {
    private static final String AUTHORITY = "com.sony.dtv.b2b.prosettings.dataprovider";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.dir/vnd.example.users";
    public static final String CONTENT_TYPE = "vnd.android.cursor.item/vnd.example.users";
    private static final int FEATURE = 1;
    private static final int FEATURE_ID = 2;
    private static final String TABLE_FEATURE = "feature";
    private static SharedPreferences mPrefsFeature;
    private static SharedPreferences mPrefsFeatureNormal;
    private static SharedPreferences mPrefsFeaturePrivilege;
    private static final UriMatcher sUriMatcher = new UriMatcher(-1);
    private final String TAG = getClass().getSimpleName();

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_FEATURE, 1);
        sUriMatcher.addURI(AUTHORITY, "feature/#", 2);
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        LogUtil.LogD(this.TAG, "onCreate:");
        mPrefsFeature = getContext().getSharedPreferences(TABLE_FEATURE, 0);
        mPrefsFeatureNormal = getContext().getSharedPreferences("feature_normal", 0);
        mPrefsFeaturePrivilege = getContext().getSharedPreferences("feature_privilege", 0);
        if (!mPrefsFeature.getAll().isEmpty()) {
            return true;
        }
        putStringFeature("0", "0");
        putStringFeature("1", "0");
        putStringFeature("2", "0");
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        if (strArr != null) {
            for (String str3 : strArr) {
                LogUtil.LogD(this.TAG, "projection = " + str3);
            }
        }
        if (strArr2 != null) {
            for (String str4 : strArr2) {
                LogUtil.LogD(this.TAG, "selection = " + str4);
            }
        }
        switch (sUriMatcher.match(uri)) {
            case 1:
                LogUtil.LogD(this.TAG, "sUriMatcher = FEATURE");
                Map<String, ?> all = mPrefsFeature.getAll();
                MatrixCursor matrixCursor = new MatrixCursor(new String[]{"key", HotelSettings.VALUE});
                for (Map.Entry<String, ?> entry : all.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    matrixCursor.addRow(new Object[]{key, value});
                    LogUtil.LogD(this.TAG, "key = " + key + " value = " + value);
                }
                return matrixCursor;
            case 2:
                String str5 = uri.getPathSegments().get(1);
                LogUtil.LogD(this.TAG, "sUriMatcher = FEATURE_ID: key = " + str5);
                MatrixCursor matrixCursor2 = new MatrixCursor(new String[]{"key", HotelSettings.VALUE});
                if (!mPrefsFeature.contains(str5)) {
                    return matrixCursor2;
                }
                matrixCursor2.addRow(new Object[]{str5, mPrefsFeature.getString(str5, "0")});
                return matrixCursor2;
            default:
                throw new UnsupportedOperationException("");
        }
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException();
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new UnsupportedOperationException();
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        throw new UnsupportedOperationException();
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case 1:
                return CONTENT_TYPE;
            case 2:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    public static void putStringFeature(String str, String str2) {
        SharedPreferences.Editor editorEdit = mPrefsFeature.edit();
        editorEdit.putString(str, str2);
        editorEdit.commit();
    }

    public static String getStringFeature(String str) {
        return mPrefsFeature.getString(str, "0");
    }

    public static void putStringFeatureNormal(String str, String str2) {
        SharedPreferences.Editor editorEdit = mPrefsFeatureNormal.edit();
        editorEdit.putString(str, str2);
        editorEdit.commit();
    }

    public static String getStringFeatureNormal(String str) {
        return mPrefsFeatureNormal.getString(str, "0");
    }

    public static void putStringFeaturePrivilege(String str, String str2) {
        SharedPreferences.Editor editorEdit = mPrefsFeaturePrivilege.edit();
        editorEdit.putString(str, str2);
        editorEdit.commit();
    }

    public static String getStringFeaturePrivilege(String str) {
        return mPrefsFeaturePrivilege.getString(str, "0");
    }

    public static void mergeFeatures() {
        HashMap map = (HashMap) mPrefsFeatureNormal.getAll();
        HashMap map2 = (HashMap) mPrefsFeaturePrivilege.getAll();
        Set setKeySet = map.keySet();
        Set setKeySet2 = map2.keySet();
        HashSet<String> hashSet = new HashSet();
        hashSet.addAll(setKeySet);
        hashSet.addAll(setKeySet2);
        SharedPreferences.Editor editorEdit = mPrefsFeature.edit();
        for (String str : hashSet) {
            String str2 = (String) map.get(str);
            String str3 = (String) map2.get(str);
            if (str3 == null) {
                str3 = "0";
            }
            if (str2 == null) {
                str2 = str3;
            }
            editorEdit.putString(str, str2);
        }
        editorEdit.commit();
    }

    public static void clearFeatures() {
        SharedPreferences.Editor editorEdit = mPrefsFeature.edit();
        editorEdit.clear();
        editorEdit.commit();
    }

    public static void clearFeaturesNormal() {
        SharedPreferences.Editor editorEdit = mPrefsFeatureNormal.edit();
        editorEdit.clear();
        editorEdit.commit();
    }

    public static void clearFeaturesPrivilege() {
        SharedPreferences.Editor editorEdit = mPrefsFeaturePrivilege.edit();
        editorEdit.clear();
        editorEdit.commit();
    }

    public static boolean setFeature(Map map, String str) {
        if (str.equals("NodeRuntimeNormal")) {
            clearFeaturesNormal();
        } else if (str.equals("NodeRuntimePrivilege")) {
            clearFeaturesPrivilege();
        }
        for (Object obj : map.keySet()) {
            String str2 = (String) map.get(obj);
            if (str.equals("NodeRuntimeNormal")) {
                putStringFeatureNormal((String) obj, str2);
            } else if (str.equals("NodeRuntimePrivilege")) {
                putStringFeaturePrivilege((String) obj, str2);
            }
        }
        clearFeatures();
        mergeFeatures();
        return true;
    }
}
