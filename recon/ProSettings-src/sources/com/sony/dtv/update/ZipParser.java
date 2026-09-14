package com.sony.dtv.update;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* JADX INFO: loaded from: classes.dex */
class ZipParser {
    private static final String METADATAPATH = "META-INF/com/android/metadata";
    private static final String TAG = "UpdateSessionManager";
    public boolean delta;
    public String device;
    public String targetVersion;
    public String version;

    ZipParser() {
        clear();
    }

    public void clear() {
        this.device = "";
        this.delta = false;
        this.targetVersion = "";
        this.version = "";
    }

    /* JADX WARN: Code duplicated, block: B:46:0x0104 A[Catch: Exception -> 0x0100, TryCatch #0 {Exception -> 0x0100, blocks: (B:42:0x00fc, B:46:0x0104, B:48:0x0109), top: B:52:0x00fc }] */
    /* JADX WARN: Code duplicated, block: B:48:0x0109 A[Catch: Exception -> 0x0100, TRY_LEAVE, TryCatch #0 {Exception -> 0x0100, blocks: (B:42:0x00fc, B:46:0x0104, B:48:0x0109), top: B:52:0x00fc }] */
    public boolean parse(String str) {
        InputStream inputStream;
        ZipFile zipFile;
        BufferedReader bufferedReader;
        Log.d(TAG, "parse path=" + str);
        clear();
        boolean z = true;
        try {
            zipFile = new ZipFile(str);
            try {
                ZipEntry entry = zipFile.getEntry(METADATAPATH);
                if (entry == null) {
                    throw new Exception("metadata doesn't exist");
                }
                inputStream = zipFile.getInputStream(entry);
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    try {
                        for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                            if (line.startsWith("pre-device=")) {
                                Log.i(TAG, "pre-device found");
                                this.device = line.substring(line.indexOf("=") + 1).trim();
                            } else if (line.startsWith("pre-build=")) {
                                Log.i(TAG, "pre-build found");
                                this.delta = true;
                            } else if (line.startsWith("pre-package=")) {
                                Log.i(TAG, "pre-package found");
                                this.targetVersion = line.substring(line.indexOf("=") + 1).trim();
                            } else if (line.startsWith("post-package=")) {
                                Log.i(TAG, "post-package found");
                                this.version = line.substring(line.indexOf("=") + 1).trim();
                            }
                        }
                        if (this.delta) {
                            if (this.targetVersion.isEmpty()) {
                                Log.e(TAG, "this package seems DELTA, but \"pre-package=\"\\targetVersion does not exist.");
                            }
                        } else {
                            this.targetVersion = "";
                        }
                    } catch (Exception e) {
                        e = e;
                        Log.e(TAG, "failed to parse the metadata:" + e);
                        z = false;
                    }
                } catch (Exception e2) {
                    e = e2;
                    bufferedReader = null;
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (zipFile != null) {
                            zipFile.close();
                        }
                    } catch (Exception e3) {
                        Log.e(TAG, "error:" + e3);
                    }
                } else {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (zipFile != null) {
                        zipFile.close();
                    }
                }
                return z;
            } catch (Exception e4) {
                e = e4;
                inputStream = null;
                bufferedReader = null;
            }
        } catch (Exception e5) {
            e = e5;
            inputStream = null;
            zipFile = null;
            bufferedReader = null;
        }
    }
}
