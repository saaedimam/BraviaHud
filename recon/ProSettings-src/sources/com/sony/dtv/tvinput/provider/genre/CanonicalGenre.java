package com.sony.dtv.tvinput.provider.genre;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.tv.TvContract;
import com.sony.dtv.tvinput.provider.internal.utils.LogUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
final class CanonicalGenre extends GenreBase {
    private static final String ARIB_CANONICAL_GENRES = "arib_canonical_genres";
    private static final String ATSC_CANONICAL_GENRES = "atsc_canonical_genres";
    private static final String AUS_CANONICAL_GENRES = "aus_canonical_genres";
    private static final String BRZ_CANONICAL_GENRES = "brz_canonical_genres";
    private static final String DTMB_CANONICAL_GENRES = "dtmb_canonical_genres";
    private static final String DVB_CANONICAL_GENRES = "dvb_canonical_genres";
    private static final String TAG = "CanonicalGenres";
    private static final String UK_CANONICAL_GENRES = "uk_canonical_genres";
    private static CanonicalGenre sInstance;
    private static Map<String, String> sDVBMap = new HashMap();
    private static Map<String, String> sATSCMap = new HashMap();
    private static Map<String, String> sDTMBMap = new HashMap();
    private static Map<String, String> sUKMap = new HashMap();
    private static Map<String, String> sAUSMap = new HashMap();
    private static Map<String, String> sBRZMap = new HashMap();
    private static Map<String, String> sARIBMap = new HashMap();
    private static Object mLock = new Object();

    private CanonicalGenre() {
    }

    public static CanonicalGenre getInstance() {
        CanonicalGenre canonicalGenre;
        synchronized (mLock) {
            if (sInstance == null) {
                sInstance = new CanonicalGenre();
            }
            canonicalGenre = sInstance;
        }
        return canonicalGenre;
    }

    private String getCanonicalGenre(Context context, String str, Map<String, String> map, String str2) {
        if (map.isEmpty() && !buildGenreMap(context, str, map)) {
            return "";
        }
        LogUtil.d(TAG, "broadcastGenres : " + str2);
        String[] strArrDecode = TvContract.Programs.Genres.decode(str2);
        ArrayList arrayList = new ArrayList();
        for (String str3 : strArrDecode) {
            for (String str4 : str3.split(":", 0)) {
                if (map.containsKey(str4) && !arrayList.contains(map.get(str4))) {
                    arrayList.add(map.get(str4));
                }
            }
        }
        String strEncode = arrayList.isEmpty() ? "" : TvContract.Programs.Genres.encode((String[]) arrayList.toArray(new String[arrayList.size()]));
        LogUtil.d(TAG, "canonicalGenre : " + strEncode);
        return strEncode;
    }

    private boolean buildGenreMap(Context context, String str, Map<String, String> map) {
        Resources tISResources = getTISResources(context);
        if (tISResources == null) {
            return false;
        }
        TypedArray typedArrayObtainTypedArray = tISResources.obtainTypedArray(tISResources.getIdentifier(str, GenreBase.RESOURCE_FILE_TYPE, "com.sony.dtv.tvinput.tuner"));
        int resourceId = typedArrayObtainTypedArray.getResourceId(0, 0);
        int resourceId2 = typedArrayObtainTypedArray.getResourceId(1, 0);
        typedArrayObtainTypedArray.recycle();
        if (resourceId == 0 || resourceId2 == 0) {
            return false;
        }
        String[] stringArray = tISResources.getStringArray(resourceId);
        String[] stringArray2 = tISResources.getStringArray(resourceId2);
        LogUtil.d(TAG, "name : " + str);
        int length = stringArray.length;
        for (int i = 0; i < length; i++) {
            LogUtil.d(TAG, "broadcastArray : " + stringArray[i]);
            LogUtil.d(TAG, "canonicalArray : " + stringArray2[i]);
            map.put(stringArray[i], stringArray2[i]);
        }
        return true;
    }

    private String getCanonicalGenresFromDVB(Context context, String str) {
        return getCanonicalGenre(context, DVB_CANONICAL_GENRES, sDVBMap, str);
    }

    private String getCanonicalGenresFromDTMB(Context context, String str) {
        return getCanonicalGenre(context, DTMB_CANONICAL_GENRES, sDTMBMap, str);
    }

    private String getCanonicalGenresFromATSC(Context context, String str) {
        return getCanonicalGenre(context, ATSC_CANONICAL_GENRES, sATSCMap, str);
    }

    private String getCanonicalGenresFromUK(Context context, String str) {
        return getCanonicalGenre(context, UK_CANONICAL_GENRES, sUKMap, str);
    }

    private String getCanonicalGenresFromAUS(Context context, String str) {
        return getCanonicalGenre(context, AUS_CANONICAL_GENRES, sAUSMap, str);
    }

    private String getCanonicalGenresFromBRZ(Context context, String str) {
        return getCanonicalGenre(context, BRZ_CANONICAL_GENRES, sBRZMap, str);
    }

    private String getCanonicalGenresFromARIB(Context context, String str) {
        return getCanonicalGenre(context, ARIB_CANONICAL_GENRES, sARIBMap, str);
    }

    @Override // com.sony.dtv.tvinput.provider.genre.GenreBase
    protected GenreBase.GenreType getGenreType() {
        return GenreBase.GenreType.CANONICAL_GENRE;
    }

    @Override // com.sony.dtv.tvinput.provider.genre.GenreBase
    protected String getCanonicalGenres(Context context, GenreBase.TunerType tunerType, int i, String str, String str2) {
        switch (tunerType) {
            case ATSC:
                return getCanonicalGenresFromATSC(context, str2);
            case DVB:
                if (GenreBase.COUNTRY_CODE_UK.equals(str)) {
                    return getCanonicalGenresFromUK(context, str2);
                }
                if (!GenreBase.COUNTRY_CODE_AUS.equals(str)) {
                    return getCanonicalGenresFromDVB(context, str2);
                }
                if (i == 16908288) {
                    return getCanonicalGenresFromAUS(context, str2);
                }
                return getCanonicalGenresFromDVB(context, str2);
            case DTMB:
                return getCanonicalGenresFromDTMB(context, str2);
            case ISDB:
                return getCanonicalGenresFromBRZ(context, str2);
            case ARIB:
                return getCanonicalGenresFromARIB(context, str2);
            default:
                return "";
        }
    }
}
