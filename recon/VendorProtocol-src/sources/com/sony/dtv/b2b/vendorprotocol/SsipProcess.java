package com.sony.dtv.b2b.vendorprotocol;

import android.os.PowerManager;
import com.sony.dtv.b2b.vendorprotocol.logging.Logger;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
class SsipProcess extends NodeProcess {
    private static final String FILE_NAME_SERVER_JS = "ssip/ssip.js";
    private static final String SSIP_SERVER_LISTEN_PORT = "20060";
    private String mApplicationToken;
    private final VendorProtocolService mVendorProtocolService;
    private final PowerManager.WakeLock mWakeLock;

    SsipProcess(String pathNode, String pathJs, String token, PowerManager.WakeLock wake, VendorProtocolService service) {
        super(pathNode, pathJs);
        this.mApplicationToken = com.sony.dtv.webapi.aidl.BuildConfig.FLAVOR;
        this.mApplicationToken = token;
        this.mWakeLock = wake;
        this.mVendorProtocolService = service;
    }

    @Override // com.sony.dtv.b2b.vendorprotocol.NodeProcess
    protected void executeCommand() {
        if (!getProcessStopFlg()) {
            this.mWakeLock.acquire();
            try {
                this.mProcess = Runtime.getRuntime().exec(getNodePath() + "node " + getJsPath() + FILE_NAME_SERVER_JS + " " + getLogMode() + " " + this.mApplicationToken + " " + SSIP_SERVER_LISTEN_PORT);
                Logger.d("[SSIP] Process wait start", new Object[0]);
                try {
                    this.mProcess.waitFor();
                } catch (InterruptedException e) {
                    Logger.e("[SSIP] Error has occurred with the Process.waitFor() " + e, new Object[0]);
                }
                Logger.d("[SSIP] Process wait end", new Object[0]);
                if (!getProcessStopFlg()) {
                    Logger.e("[SSIP] Node process was terminated by unexpected error, restart Node process", new Object[0]);
                    this.mProcess = null;
                    setProcessStopFlg(false);
                    setIsThreadStart(false);
                    this.mVendorProtocolService.notifyTerminatedByUnexpectedErrorSsip();
                } else {
                    this.mProcess = null;
                    setProcessStopFlg(false);
                }
                this.mWakeLock.release();
            } catch (IOException e2) {
                Logger.e("[SSIP] Unexpected error has occurred:" + e2.getMessage(), e2, new Object[0]);
                this.mWakeLock.release();
            }
        }
    }

    final void setApplicationToken(String token) {
        this.mApplicationToken = token;
    }
}
