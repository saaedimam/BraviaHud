package com.sony.dtv.tvinput.provider.genre;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import com.sony.dtv.tvinput.provider.internal.utils.LogUtil;

/* JADX INFO: loaded from: classes.dex */
class GenreBase {
    public static final String COUNTRY_CODE_AUS = "AU";
    public static final String COUNTRY_CODE_UK = "GB";
    public static final int EXTENDED_CODE = 224;
    public static final int EXTENDED_PARTITION = 14;
    public static final int MAJOR_GENRES_CONVERT = 16;
    public static final String RESOURCE_FILE_TYPE = "array";
    public static final int SKP_GENRE = 225;
    private static final String TAG = "GenreBase";
    public static final String TIS_PACKAGE_NAME = "com.sony.dtv.tvinput.tuner";

    enum GenreType {
        GOOGLE_BROADCAST_GENRE,
        COUNTRY_GENRE,
        CANONICAL_GENRE
    }

    enum TunerType {
        ATSC,
        DVB,
        DTMB,
        ISDB,
        ARIB,
        UN_KNOWN
    }

    protected String getBroadcastGenres(Context context, int i, int i2, TunerType tunerType) {
        return "";
    }

    protected String getCanonicalGenres(Context context, TunerType tunerType, int i, String str, String str2) {
        return "";
    }

    protected GenreType getGenreType() {
        return null;
    }

    GenreBase() {
    }

    protected Resources getTISResources(Context context) {
        try {
            return context.getPackageManager().getResourcesForApplication("com.sony.dtv.tvinput.tuner");
        } catch (PackageManager.NameNotFoundException unused) {
            LogUtil.w(TAG, "getTISResources NameNotFoundException");
            return null;
        }
    }

    protected String[] initGenreArrayList(Context context, String str) {
        Resources tISResources = getTISResources(context);
        if (tISResources == null) {
            return new String[0];
        }
        return tISResources.getStringArray(tISResources.getIdentifier(str, RESOURCE_FILE_TYPE, "com.sony.dtv.tvinput.tuner"));
    }
}
