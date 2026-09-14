package com.sony.dtv.tvinput.provider.genre;

import android.content.Context;
import com.sony.dtv.tvinput.provider.internal.utils.LogUtil;

/* JADX INFO: loaded from: classes.dex */
final class CountryGenres extends GenreBase {
    private static final String AUS_BROADCAST_GENRES = "aus_broadcast_genres";
    private static final String TAG = "CountryGenres";
    private static final String UK_BROADCAST_GENRES = "uk_broadcast_genres";
    private static Object mLock = new Object();
    private static CountryGenres sInstance;
    private String mCountryCode;
    private String[] mGenres;

    private CountryGenres(Context context, String str) {
        createGenreArray(context, str);
    }

    private void createGenreArray(Context context, String str) {
        if (GenreBase.COUNTRY_CODE_UK.equals(str)) {
            this.mGenres = initGenreArrayList(context, UK_BROADCAST_GENRES);
        } else {
            this.mGenres = initGenreArrayList(context, AUS_BROADCAST_GENRES);
        }
        this.mCountryCode = str;
    }

    public static CountryGenres getInstance(Context context, String str) {
        CountryGenres countryGenres;
        synchronized (mLock) {
            if (sInstance == null) {
                sInstance = new CountryGenres(context, str);
            } else if (!str.equals(sInstance.mCountryCode)) {
                sInstance.createGenreArray(context, str);
            }
            countryGenres = sInstance;
        }
        return countryGenres;
    }

    private String getGenre(int i) {
        int i2 = i / 16;
        LogUtil.d(TAG, "id " + i2);
        return (i2 < 0 || this.mGenres.length <= i2) ? "" : this.mGenres[i2];
    }

    @Override // com.sony.dtv.tvinput.provider.genre.GenreBase
    protected String getBroadcastGenres(Context context, int i, int i2, GenreBase.TunerType tunerType) {
        return getGenre(i);
    }

    @Override // com.sony.dtv.tvinput.provider.genre.GenreBase
    protected GenreBase.GenreType getGenreType() {
        return GenreBase.GenreType.COUNTRY_GENRE;
    }
}
