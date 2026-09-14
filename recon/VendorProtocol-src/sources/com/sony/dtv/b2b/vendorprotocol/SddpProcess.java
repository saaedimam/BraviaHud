package com.sony.dtv.b2b.vendorprotocol;

import com.sony.dtv.b2b.vendorprotocol.logging.Logger;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
class SddpProcess extends NodeProcess {
    private static final String FILE_NAME_SERVER_JS = "sddp/sddp.js";
    private static final int MAX_RETRY_COUNT = 30;
    private String mIpAddress;
    private final String mMacAddress;
    private final String mModelName;
    private int mRetryCount;

    SddpProcess(String pathNode, String pathJs, String model, String mac, String ip) {
        super(pathNode, pathJs);
        this.mIpAddress = com.sony.dtv.webapi.aidl.BuildConfig.FLAVOR;
        this.mRetryCount = 0;
        this.mModelName = model;
        this.mMacAddress = mac;
        this.mIpAddress = ip;
    }

    @Override // com.sony.dtv.b2b.vendorprotocol.NodeProcess
    protected void executeCommand() {
        Logger.i("[SDDP] mRetryCount (in) = " + this.mRetryCount, new Object[0]);
        Logger.i("[SDDP] mProcessStopFlg (in) = " + getProcessStopFlg(), new Object[0]);
        this.mRetryCount++;
        if (this.mRetryCount > 30) {
            Logger.i("[SDDP] Unexpected error has occurred, can't restart", new Object[0]);
            this.mRetryCount = 0;
            return;
        }
        if (!getProcessStopFlg()) {
            try {
                Logger.i("[SDDP] try getRuntime ----- ", new Object[0]);
                this.mProcess = Runtime.getRuntime().exec(getNodePath() + "node " + getJsPath() + FILE_NAME_SERVER_JS + " " + getLogMode() + " " + this.mModelName + " " + this.mMacAddress + " " + this.mIpAddress);
                Logger.i("[SDDP] Process wait start", new Object[0]);
                try {
                    this.mProcess.waitFor();
                } catch (InterruptedException e) {
                    Logger.e("[SDDP] Error has occurred with the Process.waitFor() ", e, new Object[0]);
                }
                Logger.i("[SDDP] Process wait end", new Object[0]);
                Logger.i("[SDDP] mProcessStopFlg (last) = " + getProcessStopFlg(), new Object[0]);
                if (!getProcessStopFlg()) {
                    Logger.e("[SDDP] Node process was terminated by unexpected error, restart Node process", new Object[0]);
                    this.mProcess = null;
                    Logger.e("[SDDP] *** recursive executeCommand call", new Object[0]);
                    executeCommand();
                }
                this.mProcess = null;
                setProcessStopFlg(false);
                this.mRetryCount = 0;
            } catch (IOException e2) {
                Logger.e("[SDDP] Unexpected error has occurred: %s", e2, e2.getMessage());
            }
        }
    }

    final void setIpAddress(String ip) {
        this.mIpAddress = ip;
    }
}
