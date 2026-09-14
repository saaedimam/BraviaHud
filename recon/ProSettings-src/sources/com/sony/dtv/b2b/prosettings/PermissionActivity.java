package com.sony.dtv.b2b.prosettings;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class PermissionActivity extends FragmentActivity {
    private final String TAG = getClass().getSimpleName();

    @Override // android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        boolean z;
        super.onCreate(bundle);
        String[] strArr = new String[0];
        if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
            Log.d(this.TAG, "Do Not Have READ_EXTERNAL_STORAGE Permission");
            strArr[0] = "android.permission.READ_EXTERNAL_STORAGE";
            z = true;
        } else {
            Log.d(this.TAG, "Have READ_EXTERNAL_STORAGE Permission");
            z = false;
        }
        if (!z) {
            setResult(-1);
            finish();
        } else {
            requestPermissions(strArr, 0);
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        Log.d(this.TAG, "Stop Permission Activity");
        setResult(0);
        finish();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity, android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2].equals("android.permission.READ_EXTERNAL_STORAGE")) {
                if (iArr[i2] == 0) {
                    Log.d(this.TAG, "Grant READ_EXTERNAL_STORAGE Permission");
                } else {
                    Log.d(this.TAG, "Fail to grant READ_EXTERNAL_STORAGE Permission");
                    setResult(0);
                    finish();
                }
            }
        }
        setResult(-1);
        finish();
    }
}
