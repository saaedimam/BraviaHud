package com.sony.dtv.b2b.prosettings.platform;

import android.support.v7.widget.ActivityChooserView;
import android.util.Log;
import com.sony.dtv.b2b.prosettings.GV;
import com.sony.dtv.b2b.prosettings.util.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes.dex */
public class B2bcmdManager {
    public static final String B2BCMD_CLEANUP_FWWRITE = "filewrite_cleanup";
    public static final String B2BCMD_DECRYPT_PHRASE = "decrypt_phrase";
    public static final String B2BCMD_EEPROM_READ = "eeprom_read";
    public static final String B2BCMD_EXPORT_START = "export_start";
    public static final String B2BCMD_IMPORT_START = "import_start";
    private static final String B2BCMD_LOCKFILE;
    public static final String B2BCMD_PREPARE_FWWRITE = "fwwrite_prepare";
    public static final String B2BCMD_TUNER_DISABLE = "tuner_disable";
    public static final String B2BCMD_TUNER_ENABLE = "tuner_enable";
    private static final String B2B_CMD_DATA_ROOT;
    private static final String DATADIRPATH_N = "/data/vendor/b2b/";
    private static final String DATADIRPATH_O = "/data/vendor/sony/b2b/";
    private static final String LOCKFILE_NAME = "b2bcmd.lock";
    public static final int RESULT_ERR_CMD_NOT_FOUND = -1002;
    public static final int RESULT_ERR_EXEC_FAIL = -1001;
    public static final int RESULT_ERR_EXEC_TIMEOUT = -1003;
    public static final int RESULT_ERR_INTERNAL_ERROR = -1000;
    public static final int RESULT_OK = 0;
    private static final String SYSPROP_KEY_B2BCMD = "sys.svp.b2bcmd.state";
    private static final String TAG = "B2bcmdManager";
    private static final String TMPDIRPATH_N = "/tmp/b2b/";
    private static final String TMPDIRPATH_TO = "/tmp/odm/b2b/";
    private static final String TMPDIRPATH_UO = "/vendor/tmp/odm/b2b/";
    private static final B2bcmdManager mInstance = new B2bcmdManager();
    private static final HashMap<String, CmdSpec> sCmdSpecs = new HashMap<>();
    private FileOutputStream mFs;
    private FileLock mFileLock = null;
    private Lock mLock = new ReentrantLock();
    private B2bcmdOperator mCurrentOwner = null;

    public static abstract class execCmdAsyncCB {
        protected abstract void apply(int i, String str, String str2);
    }

    static {
        File file = new File(TMPDIRPATH_N);
        File file2 = new File(TMPDIRPATH_TO);
        if (file.exists()) {
            B2BCMD_LOCKFILE = "/tmp/b2b/b2bcmd.lock";
        } else if (file2.exists()) {
            B2BCMD_LOCKFILE = "/tmp/odm/b2b/b2bcmd.lock";
        } else {
            B2BCMD_LOCKFILE = "/vendor/tmp/odm/b2b/b2bcmd.lock";
        }
        if (new File(DATADIRPATH_N).exists()) {
            B2B_CMD_DATA_ROOT = DATADIRPATH_N;
        } else {
            B2B_CMD_DATA_ROOT = DATADIRPATH_O;
        }
        AddCmdSpec(B2BCMD_DECRYPT_PHRASE, "decrypt_phrase.tmp", "decrypt_phrase.tmp", "", 5000);
        AddCmdSpec(B2BCMD_EEPROM_READ, B2BCMD_EEPROM_READ, "eeprom_read_result", "eeprom_read_finished", 5000);
        AddCmdSpec(B2BCMD_TUNER_DISABLE, null, null, "tuner_disable_finished", 10000);
        AddCmdSpec(B2BCMD_TUNER_ENABLE, null, null, "tuner_enable_finished", 10000);
        if (GV.BEFORE_TREBLE) {
            AddCmdSpec(B2BCMD_EXPORT_START, "mountpoint", null, "export_finished", -1);
            AddCmdSpec(B2BCMD_IMPORT_START, "mountpoint", null, "import_finished", -1);
            AddCmdSpec(B2BCMD_PREPARE_FWWRITE, null, "b2bcmd_sock_name.tmp", "fwwrite_prepare_ok", -1);
            AddCmdSpec(B2BCMD_CLEANUP_FWWRITE, null, null, "", -1);
        }
    }

    static void AddCmdSpec(String str, String str2, String str3, String str4, int i) {
        sCmdSpecs.put(str, new CmdSpec(str, str2, str3, str4, i));
    }

    private class B2bcmdOperator implements AutoCloseable {
        private boolean mReleased;
        final B2bcmdManager manager;

        private B2bcmdOperator(B2bcmdManager b2bcmdManager) {
            this.mReleased = false;
            this.manager = b2bcmdManager;
        }

        public synchronized boolean setState(String str) {
            return this.manager.setStateImpl(this, str);
        }

        public synchronized String getState() {
            return this.manager.getStateImpl(this);
        }

        public synchronized void release() {
            if (!this.mReleased) {
                this.manager.releaseImpl(this);
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() throws Exception {
            release();
        }
    }

    private B2bcmdManager() {
    }

    protected static B2bcmdOperator obtain() {
        return mInstance.obtainImpl();
    }

    protected B2bcmdOperator obtainImpl() {
        B2bcmdOperator b2bcmdOperator;
        this.mLock.lock();
        synchronized (this) {
            if (this.mFileLock == null) {
                try {
                    this.mFs = new FileOutputStream(B2BCMD_LOCKFILE);
                    this.mFileLock = this.mFs.getChannel().lock();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return null;
                }
            }
            this.mCurrentOwner = new B2bcmdOperator(this);
            b2bcmdOperator = this.mCurrentOwner;
        }
        return b2bcmdOperator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean setStateImpl(B2bcmdOperator b2bcmdOperator, String str) {
        if (this.mCurrentOwner != b2bcmdOperator) {
            throw new IllegalThreadStateException();
        }
        return SystemProperty.invokeSystemPropertiesSet("sys.svp.b2bcmd.state", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized String getStateImpl(B2bcmdOperator b2bcmdOperator) {
        if (this.mCurrentOwner != b2bcmdOperator) {
            throw new IllegalThreadStateException();
        }
        return SystemProperty.invokeSystemPropertiesGet("sys.svp.b2bcmd.state", "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void releaseImpl(B2bcmdOperator b2bcmdOperator) {
        if (this.mCurrentOwner != b2bcmdOperator) {
            throw new IllegalThreadStateException();
        }
        try {
            this.mFileLock.release();
            this.mFs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mFileLock = null;
        this.mFs = null;
        this.mCurrentOwner = null;
        this.mLock.unlock();
    }

    private static class CmdSpec {
        private static final String B2B_CMD_KILL_PROCESS_MSG = "KILL_PROCESS";
        private static final int mExecWaitInterval = 250;
        private final String mCmd;
        private final String mRequestPath;
        private final String mResultPath;
        private final String mSuccessCode;
        private final int mTimeout;

        private CmdSpec(String str, String str2, String str3, String str4, int i) {
            String str5;
            this.mCmd = str;
            String str6 = null;
            if (str2 == null) {
                str5 = null;
            } else {
                str5 = B2bcmdManager.B2B_CMD_DATA_ROOT + str2;
            }
            this.mRequestPath = str5;
            if (str3 != null) {
                str6 = B2bcmdManager.B2B_CMD_DATA_ROOT + str3;
            }
            this.mResultPath = str6;
            this.mSuccessCode = str4;
            this.mTimeout = i;
        }

        /* JADX WARN: Code duplicated, block: B:14:0x0025 A[Catch: IOException -> 0x003b, TRY_ENTER, TRY_LEAVE, TryCatch #4 {IOException -> 0x003b, blocks: (B:4:0x0007, B:14:0x0025, B:19:0x002e, B:23:0x0037, B:22:0x0033, B:24:0x003a, B:7:0x0011, B:12:0x001e, B:16:0x0029), top: B:47:0x0007, inners: #2, #3 }] */
        public boolean createBridgeFiles(String str) {
            boolean z;
            clenupBridgeFile();
            if (this.mRequestPath != null) {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(this.mRequestPath);
                    Throwable th = null;
                    if (str != null) {
                        try {
                            try {
                                fileOutputStream.write(str.getBytes());
                                FileUtil.ChangePermission660(this.mRequestPath);
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                throw th;
                            }
                        } catch (Throwable th3) {
                            if (fileOutputStream != null) {
                                if (th != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (Throwable th4) {
                                        th.addSuppressed(th4);
                                    }
                                } else {
                                    fileOutputStream.close();
                                }
                            }
                            throw th3;
                        }
                    } else {
                        FileUtil.ChangePermission660(this.mRequestPath);
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    }
                } catch (IOException e) {
                    Log.e(B2bcmdManager.TAG, e.toString());
                    z = false;
                }
            }
            z = true;
            if (this.mResultPath != null && (this.mRequestPath == null || !this.mResultPath.contentEquals(this.mRequestPath))) {
                File file = new File(this.mResultPath);
                try {
                    file.createNewFile();
                    FileUtil.ChangePermission660(this.mResultPath);
                } catch (IOException e2) {
                    Log.e(B2bcmdManager.TAG, e2.toString() + "  " + file.getPath());
                    e2.printStackTrace();
                }
            }
            return z;
        }

        public boolean readResult(StringBuilder sb) {
            if (sb != null) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(this.mResultPath);
                    int iAvailable = fileInputStream.available();
                    byte[] bArr = new byte[iAvailable];
                    int i = fileInputStream.read(bArr);
                    fileInputStream.close();
                    if (i != iAvailable) {
                        throw new Exception("resultFile.read fail");
                    }
                    sb.setLength(0);
                    sb.append(new String(bArr));
                } catch (Exception unused) {
                    return false;
                }
            }
            return true;
        }

        public void clenupBridgeFile() {
            if (this.mRequestPath != null) {
                File file = new File(this.mRequestPath);
                if (file.exists()) {
                    file.delete();
                }
            }
            if (this.mResultPath != null) {
                File file2 = new File(this.mResultPath);
                if (file2.exists()) {
                    file2.delete();
                }
            }
        }

        public int exec(B2bcmdOperator b2bcmdOperator, StringBuilder sb) {
            String state;
            b2bcmdOperator.setState(this.mCmd);
            int i = this.mTimeout < 0 ? ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED : this.mTimeout / mExecWaitInterval;
            do {
                i--;
                if (i < 0) {
                    b2bcmdOperator.setState(B2B_CMD_KILL_PROCESS_MSG);
                    return -1003;
                }
                try {
                    Thread.sleep(250L);
                } catch (InterruptedException unused) {
                }
                state = b2bcmdOperator.getState();
                if (!state.contentEquals(this.mCmd)) {
                    break;
                }
            } while (i > 0);
            if (sb != null) {
                sb.setLength(0);
                sb.append(state);
            }
            return state.contentEquals(this.mSuccessCode) ? 0 : -1001;
        }
    }

    public static int execCmd(String str, String str2, StringBuilder sb, StringBuilder sb2) {
        if (!sCmdSpecs.containsKey(str)) {
            return -1002;
        }
        try {
            B2bcmdOperator b2bcmdOperatorObtain = obtain();
            Throwable th = null;
            try {
                CmdSpec cmdSpec = sCmdSpecs.get(str);
                if (!cmdSpec.createBridgeFiles(str2)) {
                    throw new Exception("createBridgeFiles() fail");
                }
                int iExec = cmdSpec.exec(b2bcmdOperatorObtain, sb2);
                if (iExec != 0) {
                    if (b2bcmdOperatorObtain != null) {
                        b2bcmdOperatorObtain.close();
                    }
                    return iExec;
                }
                if (!cmdSpec.readResult(sb)) {
                    throw new Exception("readResult() fail");
                }
                cmdSpec.clenupBridgeFile();
                if (b2bcmdOperatorObtain == null) {
                    return iExec;
                }
                b2bcmdOperatorObtain.close();
                return iExec;
            } catch (Throwable th2) {
                if (b2bcmdOperatorObtain != null) {
                    if (0 != 0) {
                        try {
                            b2bcmdOperatorObtain.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                        }
                    } else {
                        b2bcmdOperatorObtain.close();
                    }
                }
                throw th2;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return -1000;
        }
    }

    public static void execCmdAsync(final String str, final String str2, final execCmdAsyncCB execcmdasynccb) {
        new Thread(new Runnable() { // from class: com.sony.dtv.b2b.prosettings.platform.B2bcmdManager.1
            @Override // java.lang.Runnable
            public void run() {
                StringBuilder sb = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                execcmdasynccb.apply(B2bcmdManager.execCmd(str, str2, sb, sb2), sb.toString(), sb2.toString());
            }
        }).start();
    }
}
