package com.sony.dtv.tvinput.provider.genre;

import android.content.Context;
import android.media.tv.TvContract;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class GoogleBroadcastGenre extends GenreBase {
    private static final String ARIB_MAJOR_BROADCAST_GENRES = "arib_major_broadcast_genres";
    private static final String ARIB_MINOR_BROADCAST_GENRES = "arib_minor_broadcast_genres";
    private static final String ARIB_SKP_MAJOR_BROADCAST_GENRES = "arib_skp_major_broadcast_genres";
    private static final String ARIB_SKP_MINOR_BROADCAST_GENRES = "arib_skp_minor_broadcast_genres";
    private static final String ATSC_BROADCAST_GENRES = "atsc_broadcast_genres";
    private static final String BRZ_MAJOR_BROADCAST_GENRES = "brz_major_broadcast_genres";
    private static final String BRZ_MINOR_BROADCAST_GENRES = "brz_minor_broadcast_genres";
    private static final String DTMB_MAJOR_BROADCAST_GENRES = "dtmb_major_broadcast_genres";
    private static final String DTMB_MINOR_BROADCAST_GENRES = "dtmb_minor_broadcast_genres";
    private static final String DVB_MAJOR_BROADCAST_GENRES = "dvb_major_broadcast_genres";
    private static final String DVB_MINOR_BROADCAST_GENRES = "dvb_minor_broadcast_genres";
    private static final int MAJOR_GENRE = 0;
    private static final int MAJOR_GENRE_ONLY = 1;
    private static final int MINOR_GENRE = 1;
    private static Object mLock = new Object();
    private static GoogleBroadcastGenre sInstance;
    private String[] mAribMajorGenre;
    private String[] mAribMinorGenre;
    private String[] mAribSkpMajorGenre;
    private String[] mAribSkpMinorGenre;
    private String[] mAtscGenre;
    private String[] mBrzMajorGenre;
    private String[] mBrzMinorGenre;
    private String[] mDtmbMajorGenre;
    private String[] mDtmbMinorGenre;
    private String[] mDvbMajorGenre;
    private String[] mDvbMinorGenre;

    GoogleBroadcastGenre() {
    }

    public static GoogleBroadcastGenre getInstance() {
        GoogleBroadcastGenre googleBroadcastGenre;
        synchronized (mLock) {
            if (sInstance == null) {
                sInstance = new GoogleBroadcastGenre();
            }
            googleBroadcastGenre = sInstance;
        }
        return googleBroadcastGenre;
    }

    private void addAtscGenre(Context context, int i, List<String> list) {
        if (this.mAtscGenre == null) {
            this.mAtscGenre = initGenreArrayList(context, ATSC_BROADCAST_GENRES);
        }
        addMinorGenre(list, this.mAtscGenre, i);
    }

    private void addDvbGenre(Context context, int i, List<String> list) {
        if (this.mDvbMajorGenre == null) {
            this.mDvbMajorGenre = initGenreArrayList(context, DVB_MAJOR_BROADCAST_GENRES);
        }
        addMajorGenre(list, this.mDvbMajorGenre, i);
        if (this.mDvbMinorGenre == null) {
            this.mDvbMinorGenre = initGenreArrayList(context, DVB_MINOR_BROADCAST_GENRES);
        }
        addMinorGenre(list, this.mDvbMinorGenre, i);
    }

    private void addDtmbGenre(Context context, int i, List<String> list) {
        if (this.mDtmbMajorGenre == null) {
            this.mDtmbMajorGenre = initGenreArrayList(context, DTMB_MAJOR_BROADCAST_GENRES);
        }
        addMajorGenre(list, this.mDtmbMajorGenre, i);
        if (this.mDtmbMinorGenre == null) {
            this.mDtmbMinorGenre = initGenreArrayList(context, DTMB_MINOR_BROADCAST_GENRES);
        }
        addMinorGenre(list, this.mDtmbMinorGenre, i);
    }

    private void addBrzGenre(Context context, int i, List<String> list) {
        if (this.mBrzMajorGenre == null) {
            this.mBrzMajorGenre = initGenreArrayList(context, BRZ_MAJOR_BROADCAST_GENRES);
        }
        addMajorGenre(list, this.mBrzMajorGenre, i);
        if (this.mBrzMinorGenre == null) {
            this.mBrzMinorGenre = initGenreArrayList(context, BRZ_MINOR_BROADCAST_GENRES);
        }
        addMinorGenre(list, this.mBrzMinorGenre, i);
    }

    private void addAribGenre(Context context, int i, List<String> list, int i2) {
        if (i == 225) {
            if (this.mAribSkpMajorGenre == null) {
                this.mAribSkpMajorGenre = initGenreArrayList(context, ARIB_SKP_MAJOR_BROADCAST_GENRES);
            }
            addMajorGenre(list, this.mAribSkpMajorGenre, i2);
            if (this.mAribSkpMinorGenre == null) {
                this.mAribSkpMinorGenre = initGenreArrayList(context, ARIB_SKP_MINOR_BROADCAST_GENRES);
            }
            addMinorGenre(list, this.mAribSkpMinorGenre, i2);
            return;
        }
        if (i != 224) {
            if (this.mAribMajorGenre == null) {
                this.mAribMajorGenre = initGenreArrayList(context, ARIB_MAJOR_BROADCAST_GENRES);
            }
            addMajorGenre(list, this.mAribMajorGenre, i);
            if (this.mAribMinorGenre == null) {
                this.mAribMinorGenre = initGenreArrayList(context, ARIB_MINOR_BROADCAST_GENRES);
            }
            addMinorGenre(list, this.mAribMinorGenre, i);
        }
    }

    @Override // com.sony.dtv.tvinput.provider.genre.GenreBase
    protected GenreBase.GenreType getGenreType() {
        return GenreBase.GenreType.GOOGLE_BROADCAST_GENRE;
    }

    private void addMajorGenre(List<String> list, String[] strArr, int i) {
        int i2 = i / 16;
        if (i2 < 0 || i2 > strArr.length - 1 || TextUtils.isEmpty(strArr[i2])) {
            return;
        }
        list.add(strArr[i2]);
    }

    private void addMinorGenre(List<String> list, String[] strArr, int i) {
        if (i < 0 || i > strArr.length - 1 || TextUtils.isEmpty(strArr[i]) || list.contains(strArr[i])) {
            return;
        }
        list.add(strArr[i]);
    }

    @Override // com.sony.dtv.tvinput.provider.genre.GenreBase
    protected String getBroadcastGenres(Context context, int i, int i2, GenreBase.TunerType tunerType) {
        ArrayList arrayList = new ArrayList();
        switch (tunerType) {
            case ATSC:
                addAtscGenre(context, i, arrayList);
                break;
            case DVB:
                addDvbGenre(context, i, arrayList);
                break;
            case DTMB:
                addDtmbGenre(context, i, arrayList);
                break;
            case ISDB:
                addBrzGenre(context, i, arrayList);
                break;
            case ARIB:
                addAribGenre(context, i, arrayList, i2);
                break;
        }
        return convertGenreSetDivisionColon(arrayList);
    }

    private String convertGenreSetDivisionColon(List<String> list) {
        String string = "";
        if (!list.isEmpty()) {
            if (list.size() == 1) {
                string = list.get(0);
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(list.get(0));
                stringBuffer.append(':');
                stringBuffer.append(list.get(1));
                string = stringBuffer.toString();
            }
        }
        return TvContract.Programs.Genres.encode(string);
    }
}
