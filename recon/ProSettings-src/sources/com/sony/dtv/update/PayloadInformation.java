package com.sony.dtv.update;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/* JADX INFO: loaded from: classes.dex */
public class PayloadInformation implements Parcelable {
    public static final Parcelable.Creator<PayloadInformation> CREATOR = new Parcelable.Creator<PayloadInformation>() { // from class: com.sony.dtv.update.PayloadInformation.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PayloadInformation createFromParcel(Parcel parcel) {
            return new PayloadInformation(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PayloadInformation[] newArray(int i) {
            return new PayloadInformation[i];
        }
    };
    private static final String TAG = "UpdateSessionManager";
    public int estimatedDownloadTime;
    public PayloadType payloadType;
    public RequestType requestType;
    public String targetDevice;
    public String targetProduct;
    public String targetVersion;
    public String uri;
    public String version;

    public enum PayloadType {
        FULL,
        DELTA
    }

    public enum RequestType {
        USER_REQUESTED,
        NOT_USER_REQUESTED
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public PayloadInformation(String str, String str2, String str3, String str4, String str5, PayloadType payloadType, RequestType requestType, int i) {
        this.uri = str;
        this.targetDevice = str2;
        this.targetProduct = str3;
        this.targetVersion = str4;
        this.version = str5;
        this.payloadType = payloadType;
        this.requestType = requestType;
        this.estimatedDownloadTime = i;
    }

    private PayloadInformation(Parcel parcel) {
        readFromParcel(parcel);
    }

    private static void logprint(Object... objArr) {
        StringBuffer stringBuffer = new StringBuffer();
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        stringBuffer.append("[");
        stringBuffer.append(stackTraceElement.getClassName());
        stringBuffer.append("::");
        stringBuffer.append(stackTraceElement.getMethodName());
        stringBuffer.append("] ");
        for (int i = 0; i < objArr.length; i++) {
            if (i > 0) {
                stringBuffer.append(" ");
            }
            stringBuffer.append(objArr[i]);
        }
        Log.e(TAG, stringBuffer.toString());
    }

    private static String getFileNamefromUri(String str, Boolean bool) {
        String strTrim = Paths.get(str, new String[0]).getFileName().toString().trim();
        int iLastIndexOf = strTrim.lastIndexOf(".");
        if (!bool.booleanValue() || iLastIndexOf <= -1) {
            return strTrim;
        }
        Log.i(TAG, "filename with extension=.zip lastindex=" + strTrim.lastIndexOf("."));
        return strTrim.substring(0, strTrim.lastIndexOf("."));
    }

    public static PayloadInformation getPayloadInfoFromFile(String str) {
        String str2;
        PayloadType payloadType;
        String str3;
        String str4;
        String str5;
        boolean z;
        PayloadType payloadType2;
        String str6;
        String str7;
        String str8;
        String str9 = "";
        String str10 = "";
        String str11 = "";
        PayloadType payloadType3 = PayloadType.FULL;
        ZipParser zipParser = new ZipParser();
        try {
            str2 = str;
            try {
                if (zipParser.parse(new URI(str2).getPath().trim())) {
                    String str12 = zipParser.device;
                    if (!zipParser.version.isEmpty()) {
                        str10 = "sony_dtv" + zipParser.version;
                    }
                    if (zipParser.delta) {
                        PayloadType payloadType4 = PayloadType.DELTA;
                        try {
                            if (!zipParser.targetVersion.isEmpty()) {
                                str11 = "sony_dtv" + zipParser.targetVersion;
                            }
                            payloadType3 = payloadType4;
                        } catch (URISyntaxException unused) {
                            payloadType3 = payloadType4;
                            str9 = "";
                            str5 = "";
                            str3 = "";
                            str4 = "";
                            payloadType = payloadType3;
                            z = false;
                        }
                    }
                    payloadType2 = payloadType3;
                    str6 = str11;
                    str7 = str10;
                    str8 = str12;
                    z = true;
                } else {
                    payloadType2 = payloadType3;
                    str6 = "";
                    str7 = "";
                    str8 = "";
                    z = false;
                }
                str5 = str8;
                str3 = str7;
                str4 = str6;
                payloadType = payloadType2;
            } catch (URISyntaxException unused2) {
            }
        } catch (URISyntaxException unused3) {
            str2 = str;
        }
        Log.i(TAG, "parse the version string read from the metadata.");
        PackageVersionParser packageVersionParser = new PackageVersionParser(str3);
        if (packageVersionParser.complete) {
            str9 = packageVersionParser.region;
        }
        PayloadInformation payloadInformation = new PayloadInformation(str2, str5, str9, str4, str3, payloadType, RequestType.NOT_USER_REQUESTED, 0);
        Object[] objArr = new Object[1];
        StringBuilder sb = new StringBuilder();
        sb.append("parsed zip file ");
        sb.append((z && packageVersionParser.complete) ? "successfully." : "might something error happened.");
        objArr[0] = sb.toString();
        logprint(objArr);
        return payloadInformation;
    }

    public void dump() {
        Log.d(TAG, "dump PayloadInformation:");
        Log.d(TAG, "uri=" + this.uri);
        Log.d(TAG, "targetDevice=" + this.targetDevice);
        Log.d(TAG, "targetProduct=" + this.targetProduct);
        Log.d(TAG, "targetVersion=" + this.targetVersion);
        Log.d(TAG, "version=" + this.version);
        Log.d(TAG, "payloadType=" + this.payloadType);
        Log.d(TAG, "requestType=" + this.requestType);
        Log.d(TAG, "estimatedDownloadTime=" + this.estimatedDownloadTime);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.uri);
        parcel.writeString(this.targetDevice);
        parcel.writeString(this.targetProduct);
        parcel.writeString(this.targetVersion);
        parcel.writeString(this.version);
        parcel.writeInt(this.payloadType.ordinal());
        parcel.writeInt(this.requestType.ordinal());
        parcel.writeInt(this.estimatedDownloadTime);
    }

    public void readFromParcel(Parcel parcel) {
        this.uri = parcel.readString();
        this.targetDevice = parcel.readString();
        this.targetProduct = parcel.readString();
        this.targetVersion = parcel.readString();
        this.version = parcel.readString();
        this.payloadType = PayloadType.values()[parcel.readInt()];
        this.requestType = RequestType.values()[parcel.readInt()];
        this.estimatedDownloadTime = parcel.readInt();
    }

    boolean isValidTarget(String str) {
        if (str.isEmpty()) {
            Log.e(TAG, "isValidTarget: invalid arguments.");
            return false;
        }
        PackageVersionParser packageVersionParser = new PackageVersionParser(this.version);
        PackageVersionParser packageVersionParser2 = new PackageVersionParser(str);
        if (!packageVersionParser.complete || !packageVersionParser2.complete) {
            Log.e(TAG, "failed to parse the version string");
            return false;
        }
        if (packageVersionParser.update_id.isEmpty() || packageVersionParser2.update_id.isEmpty()) {
            Log.e(TAG, "UPDATE_ID does not exist.");
            return false;
        }
        if (packageVersionParser.update_id.matches(packageVersionParser2.update_id)) {
            Log.i(TAG, "update_id is matched. pkg=" + packageVersionParser.update_id + ", system=" + packageVersionParser2.update_id);
            if (packageVersionParser.model_id.isEmpty() || packageVersionParser2.model_id.isEmpty()) {
                Log.e(TAG, "model_id does not exist.");
                return false;
            }
            if (packageVersionParser.model_id.matches(packageVersionParser2.model_id)) {
                Log.i(TAG, "model_id is matched. pkg=" + packageVersionParser.model_id + ", system=" + packageVersionParser2.model_id);
                if (this.payloadType == PayloadType.DELTA) {
                    Log.i(TAG, "zip is DELTA, check the base version.");
                    PackageVersionParser packageVersionParser3 = new PackageVersionParser(this.targetVersion);
                    if (!packageVersionParser3.complete) {
                        Log.e(TAG, "failed to parse the version string");
                        return false;
                    }
                    if (packageVersionParser3.majorver == packageVersionParser2.majorver && packageVersionParser3.minorver == packageVersionParser2.minorver) {
                        Log.i(TAG, "base version is matched.");
                    } else {
                        Log.e(TAG, "base version is not matched. zip=" + packageVersionParser3.majorver + "." + packageVersionParser3.minorver + ", system=" + packageVersionParser2.majorver + "." + packageVersionParser2.minorver);
                        return false;
                    }
                }
                return true;
            }
            Log.i(TAG, "model_id is not matched. pkg=" + packageVersionParser.model_id + ", system=" + packageVersionParser2.model_id);
            return false;
        }
        Log.i(TAG, "update_id is not matched. pkg=" + packageVersionParser.update_id + ", system=" + packageVersionParser2.update_id);
        return false;
    }

    boolean isNewerThanRunningVersion(String str) {
        if (str.isEmpty()) {
            Log.e(TAG, "isNewerThanRunningVersion: invalid arguments.");
            return false;
        }
        PackageVersionParser packageVersionParser = new PackageVersionParser(this.version);
        PackageVersionParser packageVersionParser2 = new PackageVersionParser(str);
        if (!packageVersionParser.complete || !packageVersionParser2.complete) {
            Log.e(TAG, "failed to parse the version string");
            return false;
        }
        Log.i(TAG, "isNewerThanRunningVersion() conpare str package ver=" + packageVersionParser.getPackageVersionString() + ", system ver=" + packageVersionParser2.getPackageVersionString());
        int iCompareUnsigned = Integer.compareUnsigned(Integer.parseUnsignedInt(packageVersionParser.getPackageVersionString()), Integer.parseUnsignedInt(packageVersionParser2.getPackageVersionString()));
        if (iCompareUnsigned <= 0) {
            Log.i(TAG, "isNewerThanRunningVersion(): package version is lower than system.");
        }
        return iCompareUnsigned > 0;
    }
}
