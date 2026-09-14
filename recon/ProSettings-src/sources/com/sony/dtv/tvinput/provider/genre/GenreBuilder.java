package com.sony.dtv.tvinput.provider.genre;

import android.content.Context;
import android.database.Cursor;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.SystemProperties;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import com.sony.dtv.b2b.hotelmode.HotelSettings;
import com.sony.dtv.tvinput.provider.internal.utils.LogUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class GenreBuilder {
    private static final String COUNTRY_PROPERTY = "persist.svp.country";
    private static final int DESTINATION = 39;
    private static final String DESTINATION_ATSC_KR = "DESTINATION_ATSC_KR";
    private static final String DESTINATION_ATSC_LTN = "DESTINATION_ATSC_LTN";
    private static final String DESTINATION_ATSC_UC = "DESTINATION_ATSC_UC";
    private static final String DESTINATION_DTMB_CN = "DESTINATION_DTMB_CN";
    private static final String DESTINATION_DTMB_HN = "DESTINATION_DTMB_HN";
    private static final String DESTINATION_DVB_AEP_S2 = "DESTINATION_DVB_AEP_S2";
    private static final String DESTINATION_DVB_AEP_STD = "DESTINATION_DVB_AEP_STD";
    private static final String DESTINATION_DVB_AEP_T2 = "DESTINATION_DVB_AEP_T2";
    private static final String DESTINATION_DVB_AEP_T2S2 = "DESTINATION_DVB_AEP_T2S2";
    private static final String DESTINATION_DVB_GA_T = "DESTINATION_DVB_GA_T";
    private static final String DESTINATION_DVB_GA_T2 = "DESTINATION_DVB_GA_T2";
    private static final String DESTINATION_DVB_LTN = "DESTINATION_DVB_LTN";
    private static final String DESTINATION_DVB_TW = "DESTINATION_DVB_TW";
    private static final String DESTINATION_ISDB_JPN = "DESTINATION_ISDB_JPN";
    private static final String DESTINATION_ISDB_LTN = "DESTINATION_ISDB_LTN";
    private static final String DESTINATION_ISDB_PH = "DESTINATION_ISDB_PH";
    private static final String TAG = "GenreBuilder";
    private static GenreBuilder sInstance;
    private static final Uri CONTENT_URI_MODEL_VARIATION = Uri.parse("content://com.sony.dtv.provider.modelvariation/info");
    private static Object mLock = new Object();
    private static final Map<String, GenreBase.TunerType> MAP_DESTINATION = new HashMap();
    private String mDestination = null;
    private CanonicalGenre mCanonicalGenre = null;
    private GenreBase mGoogleBroadcastGenre = null;
    private String mCountryCodeForTest = null;
    private GenreBase.TunerType mTunerTypeForTest = null;

    static {
        MAP_DESTINATION.put("DESTINATION_ATSC_UC", GenreBase.TunerType.ATSC);
        MAP_DESTINATION.put("DESTINATION_ATSC_KR", GenreBase.TunerType.ATSC);
        MAP_DESTINATION.put("DESTINATION_ATSC_LTN", GenreBase.TunerType.ATSC);
        MAP_DESTINATION.put("DESTINATION_DTMB_CN", GenreBase.TunerType.DTMB);
        MAP_DESTINATION.put("DESTINATION_DTMB_HN", GenreBase.TunerType.DTMB);
        MAP_DESTINATION.put("DESTINATION_DVB_TW", GenreBase.TunerType.DVB);
        MAP_DESTINATION.put("DESTINATION_DVB_GA_T", GenreBase.TunerType.DVB);
        MAP_DESTINATION.put("DESTINATION_DVB_GA_T2", GenreBase.TunerType.DVB);
        MAP_DESTINATION.put("DESTINATION_DVB_LTN", GenreBase.TunerType.DVB);
        MAP_DESTINATION.put("DESTINATION_DVB_AEP_STD", GenreBase.TunerType.DVB);
        MAP_DESTINATION.put("DESTINATION_DVB_AEP_T2", GenreBase.TunerType.DVB);
        MAP_DESTINATION.put("DESTINATION_DVB_AEP_S2", GenreBase.TunerType.DVB);
        MAP_DESTINATION.put("DESTINATION_DVB_AEP_T2S2", GenreBase.TunerType.DVB);
        MAP_DESTINATION.put("DESTINATION_ISDB_LTN", GenreBase.TunerType.ISDB);
        MAP_DESTINATION.put("DESTINATION_ISDB_PH", GenreBase.TunerType.ISDB);
        MAP_DESTINATION.put("DESTINATION_ISDB_JPN", GenreBase.TunerType.ARIB);
    }

    private GenreBuilder() {
    }

    public static GenreBuilder getInstance() {
        GenreBuilder genreBuilder;
        synchronized (mLock) {
            if (sInstance == null) {
                sInstance = new GenreBuilder();
            }
            genreBuilder = sInstance;
        }
        return genreBuilder;
    }

    public String getCanonicalGenre(Context context, int i, int i2, int i3) {
        LogUtil.d(TAG, "getCanonicalGenre");
        return getCanonicalGenres(context, i, getBroadcastGenres(context, i, i2, i3));
    }

    public String getCanonicalGenre(Context context, int i, String str) {
        LogUtil.d(TAG, "getCanonicalGenre");
        return getCanonicalGenres(context, i, str);
    }

    public String getBroadcastGenres(Context context, int i, int i2, int i3, String str) {
        ArrayList arrayList;
        LogUtil.d(TAG, "getBroadcastGenres");
        if (!TextUtils.isEmpty(str)) {
            arrayList = new ArrayList(Arrays.asList(TvContract.Programs.Genres.decode(str)));
        } else {
            arrayList = new ArrayList();
        }
        String broadcastGenres = getBroadcastGenres(context, i, i2, i3);
        if (!TextUtils.isEmpty(broadcastGenres)) {
            for (String str2 : TvContract.Programs.Genres.decode(broadcastGenres)) {
                if (!arrayList.contains(str2)) {
                    arrayList.add(str2);
                }
            }
        }
        return TvContract.Programs.Genres.encode((String[]) arrayList.toArray(new String[arrayList.size()]));
    }

    public String getBroadcastGenres(Context context, int i, int i2, int i3) {
        LogUtil.d(TAG, "getBroadcastGenres");
        GenreBase.TunerType tunerType = getTunerType(context);
        switch (tunerType) {
            case ATSC:
            case DTMB:
            case ISDB:
            case ARIB:
                return getGoogleBroadcastGenre(context, i2, i3, tunerType);
            case DVB:
                String countryCode = getCountryCode();
                if (GenreBase.COUNTRY_CODE_UK.equals(countryCode)) {
                    return getCountryGenre(context, i2, i3, tunerType, countryCode);
                }
                if (GenreBase.COUNTRY_CODE_AUS.equals(countryCode) && i == 16908288) {
                    return getCountryGenre(context, i2, i3, tunerType, countryCode);
                }
                return getGoogleBroadcastGenre(context, i2, i3, tunerType);
            default:
                return "";
        }
    }

    private String getCountryGenre(Context context, int i, int i2, GenreBase.TunerType tunerType, String str) {
        return CountryGenres.getInstance(context, str).getBroadcastGenres(context, i, i2, tunerType);
    }

    private String getGoogleBroadcastGenre(Context context, int i, int i2, GenreBase.TunerType tunerType) {
        if (this.mGoogleBroadcastGenre == null) {
            this.mGoogleBroadcastGenre = GoogleBroadcastGenre.getInstance();
        }
        return this.mGoogleBroadcastGenre.getBroadcastGenres(context, i, i2, tunerType);
    }

    private String getDestination(Context context) throws Throwable {
        Cursor cursor = null;
        string = null;
        String string = null;
        try {
            Cursor cursorQuery = context.getContentResolver().query(CONTENT_URI_MODEL_VARIATION, new String[]{HotelSettings.VALUE}, "key = ?", new String[]{Integer.toString(39)}, null);
            if (cursorQuery != null) {
                try {
                    if (cursorQuery.moveToNext()) {
                        string = cursorQuery.getString(0);
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor = cursorQuery;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            return string;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private String getCanonicalGenres(Context context, int i, String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (this.mCanonicalGenre == null) {
            this.mCanonicalGenre = CanonicalGenre.getInstance();
        }
        return this.mCanonicalGenre.getCanonicalGenres(context, getTunerType(context), i, getCountryCode(), str);
    }

    private String getCountryCode() {
        if (this.mCountryCodeForTest == null) {
            return SystemProperties.get(COUNTRY_PROPERTY, "");
        }
        return this.mCountryCodeForTest;
    }

    private GenreBase.TunerType getTunerType(Context context) {
        if (this.mTunerTypeForTest == null) {
            if (this.mDestination == null) {
                this.mDestination = getDestination(context);
            }
            GenreBase.TunerType tunerType = MAP_DESTINATION.get(this.mDestination);
            return tunerType == null ? GenreBase.TunerType.UN_KNOWN : tunerType;
        }
        return this.mTunerTypeForTest;
    }

    @VisibleForTesting
    void setCountryCodeForTest(String str) {
        this.mCountryCodeForTest = str;
    }

    @VisibleForTesting
    void setTunerTypeForTest(GenreBase.TunerType tunerType) {
        this.mTunerTypeForTest = tunerType;
    }
}
