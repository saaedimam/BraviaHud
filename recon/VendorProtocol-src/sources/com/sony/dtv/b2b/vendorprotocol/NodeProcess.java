package com.sony.dtv.b2b.vendorprotocol;

import android.support.annotation.NonNull;
import com.sony.dtv.b2b.vendorprotocol.logging.Logger;
import com.sony.dtv.b2b.vendorprotocol.util.Preconditions;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
abstract class NodeProcess {
    private static final String COMMAND_KILL = "/system/bin/kill";
    static final String FILE_NAME_NODE = "node";
    private static final String NODE_MODE_RELEASE = "Release";
    private static final String SIGTERM = "-TERM";
    private static final String S_LOG_MODE = "Release";
    private final String mJsPath;
    private final String mNodePath;
    private Thread mThreadNode = null;
    private boolean mIsThreadStart = false;
    Process mProcess = null;
    private volatile boolean mProcessStopFlg = false;

    protected abstract void executeCommand();

    NodeProcess(@NonNull String pathNode, @NonNull String pathJs) {
        this.mNodePath = (String) Preconditions.checkNotNull(pathNode, "pathNode == null");
        this.mJsPath = (String) Preconditions.checkNotNull(pathJs, "pathJs == null");
    }

    final void start() {
        if (!this.mIsThreadStart) {
            this.mThreadNode = null;
            this.mThreadNode = new Thread(new Runnable(this) { // from class: com.sony.dtv.b2b.vendorprotocol.NodeProcess$$Lambda$0
                private final NodeProcess arg$1;

                {
                    this.arg$1 = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    this.arg$1.executeCommand();
                }
            });
            Logger.d("Start the Node process", new Object[0]);
            this.mIsThreadStart = true;
            this.mThreadNode.start();
            return;
        }
        Logger.d("Node has already started, skip", new Object[0]);
    }

    final void stop() {
        Logger.i("mIsThreadStart: %b", Boolean.valueOf(this.mIsThreadStart));
        if (this.mIsThreadStart) {
            if (this.mProcess != null) {
                this.mProcessStopFlg = true;
                String pid = this.mProcess.toString().replace("Process[pid=", com.sony.dtv.webapi.aidl.BuildConfig.FLAVOR).replace(", hasExited=false]", com.sony.dtv.webapi.aidl.BuildConfig.FLAVOR);
                try {
                    Runtime.getRuntime().exec(new String[]{COMMAND_KILL, SIGTERM, pid});
                } catch (IOException e) {
                    Logger.e("Error has occurred with the Runtime.getRuntime().exec() " + e, new Object[0]);
                }
            }
            try {
                this.mThreadNode.join();
            } catch (InterruptedException e2) {
                Logger.e("Error has occurred with the Thread.join() " + e2, new Object[0]);
            }
            this.mIsThreadStart = false;
            Logger.d("Stop the Node process", new Object[0]);
            return;
        }
        Logger.d("Node is not started, skip", new Object[0]);
    }

    static String getLogMode() {
        return "Release";
    }

    final boolean getIsThreadStart() {
        return this.mIsThreadStart;
    }

    final boolean getProcessStopFlg() {
        return this.mProcessStopFlg;
    }

    String getNodePath() {
        return this.mNodePath;
    }

    String getJsPath() {
        return this.mJsPath;
    }

    void setIsThreadStart(boolean isThreadStart) {
        this.mIsThreadStart = isThreadStart;
    }

    void setProcessStopFlg(boolean processStopFlg) {
        this.mProcessStopFlg = processStopFlg;
    }
}
