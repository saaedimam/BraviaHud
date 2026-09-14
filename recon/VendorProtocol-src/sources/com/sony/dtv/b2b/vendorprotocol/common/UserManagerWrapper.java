package com.sony.dtv.b2b.vendorprotocol.common;

import android.content.Context;
import android.os.Process;
import android.os.UserHandle;
import android.os.UserManager;
import com.sony.dtv.b2b.vendorprotocol.util.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public class UserManagerWrapper {
    private static final long USER_SERIAL_NUMBER_OWNER = 0;
    private final UserManager mUserManager;

    public UserManagerWrapper(Context context) {
        Preconditions.checkNotNull(context, "context == null");
        this.mUserManager = (UserManager) context.getSystemService("user");
        Preconditions.checkNotNull(this.mUserManager, "mUserManager == null");
    }

    public boolean isParentUser() {
        UserHandle handle = Process.myUserHandle();
        long number = this.mUserManager.getSerialNumberForUser(handle);
        return number == 0;
    }
}
