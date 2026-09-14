package com.sony.dtv.b2b.prosettings.platform;

import com.sony.dtv.b2b.prosettings.util.LogUtil;
import com.sony.dtv.provider.modelvariation.util.ModelVariationUtil;
import com.sony.dtv.provider.modelvariation.util.ModelVariationValue;

/* JADX INFO: loaded from: classes.dex */
public class ModelVariationInfo extends PlatformBase {
    public static int GENERATION_SAKURA = 0;
    public static int GENERATION_TRINITY = 1;
    public static int GENERATION_UNKNOWN = -1;
    public static int GENERATION_UROBOROS = 2;
    private static final String TAG = "ModelVariationInfo";

    public static String GetModelVariationValue(int i) {
        try {
            return ModelVariationUtil.get(PlatformBase.mContext.getContentResolver(), i);
        } catch (Exception e) {
            LogUtil.LogE(TAG, "ModelVariationValue: Exception: key = " + i, e);
            return "";
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static int getGeneration() {
        byte b = 1;
        String strGetModelVariationValue = GetModelVariationValue(1);
        if (strGetModelVariationValue == null) {
            return GENERATION_UNKNOWN;
        }
        int iHashCode = strGetModelVariationValue.hashCode();
        if (iHashCode != 842962474) {
            switch (iHashCode) {
                case -588324335:
                    if (!strGetModelVariationValue.equals(ModelVariationValue.STRUCTURE_VERSION_VER_02)) {
                        b = -1;
                    }
                    break;
                case -588324334:
                    b = !strGetModelVariationValue.equals(ModelVariationValue.STRUCTURE_VERSION_VER_03) ? (byte) -1 : (byte) 2;
                    break;
                default:
                    b = -1;
                    break;
            }
        } else {
            b = strGetModelVariationValue.equals(ModelVariationValue.STRUCTURE_VERSION_INITIAL_VERSION) ? (byte) 0 : (byte) -1;
        }
        switch (b) {
            case 0:
                return GENERATION_SAKURA;
            case 1:
                return GENERATION_TRINITY;
            case 2:
                return GENERATION_UROBOROS;
            default:
                return GENERATION_UNKNOWN;
        }
    }
}
