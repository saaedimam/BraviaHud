package com.sony.dtv.b2b.prosettings.platform;

import android.net.Uri;
import android.os.RemoteException;
import com.sony.dtv.b2b.prosettings.GV;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public class BootLogo extends PlatformBase {
    private final String TAG = getClass().getSimpleName();

    public boolean installLogo(String str) throws RemoteException {
        LogUtil.LogD(this.TAG, "installLogo:");
        File file = new File(GV.BOOTANIMATION_ZIP_PATH);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            InputStream inputStreamOpenInputStream = mContext.getContentResolver().openInputStream(Uri.parse(str));
            if (inputStreamOpenInputStream == null) {
                LogUtil.LogE(this.TAG, "installLogo: fis is null");
                fileOutputStream.close();
                return false;
            }
            byte[] bArr = new byte[8192];
            while (true) {
                int i = inputStreamOpenInputStream.read(bArr, 0, 8192);
                if (i < 0) {
                    break;
                }
                fileOutputStream.write(bArr, 0, i);
            }
            fileOutputStream.flush();
            fileOutputStream.getFD().sync();
            fileOutputStream.close();
            inputStreamOpenInputStream.close();
            if (file.setReadable(true, false)) {
                return true;
            }
            LogUtil.LogE(this.TAG, "installLogo: Cannot set readable.");
            return false;
        } catch (FileNotFoundException e) {
            LogUtil.LogE(this.TAG, "installLogo: FileNotFoundException", e);
            return false;
        } catch (IOException e2) {
            LogUtil.LogE(this.TAG, "installLogo: IOException", e2);
            return false;
        } catch (UnsupportedOperationException e3) {
            LogUtil.LogE(this.TAG, "installLogo: UnsupportedOperationException", e3);
            return false;
        }
    }

    public boolean uninstallLogo() throws RemoteException {
        LogUtil.LogD(this.TAG, "uninstallLogo:");
        File file = new File(GV.BOOTANIMATION_ZIP_PATH);
        if (!file.exists()) {
            return true;
        }
        file.delete();
        return true;
    }
}
