package com.sony.dtv.b2b.prosettings.util;

import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class FileUtil {
    private static final String FILE_PERMISSION = "700";
    private static final String TAG = "FileUtil";

    public static void ChangePermission660(String str) {
        File file = new File(str);
        if (file.exists()) {
            file.setExecutable(true, true);
            file.setWritable(true, false);
            file.setReadable(true, false);
        } else {
            LogUtil.LogE(TAG, "ChangePermission: File is not found. file=" + str);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r0v16 */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r6v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r6v10, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r6v11, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r6v12, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r6v13, types: [java.io.FileInputStream, java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r6v3, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r6v6 */
    /* JADX WARN: Type inference failed for: r6v7 */
    /* JADX WARN: Type inference failed for: r6v8 */
    /* JADX WARN: Type inference failed for: r6v9 */
    public static JSONObject LoadJson2(String str) throws Throwable {
        BufferedReader bufferedReader;
        ?? file = new File(str);
        ?? Exists = file.exists();
        try {
            try {
                if (Exists == 0) {
                    LogUtil.LogW(TAG, "LoadJson2: file doesn't exists = " + file.getAbsolutePath());
                    return null;
                }
                try {
                    Exists = new FileInputStream((File) file);
                    try {
                        bufferedReader = new BufferedReader(new InputStreamReader((InputStream) Exists, "UTF-8"));
                        try {
                            StringBuilder sb = new StringBuilder(4096);
                            while (true) {
                                String line = bufferedReader.readLine();
                                if (line == null) {
                                    break;
                                }
                                sb.append(line);
                            }
                            JSONObject jSONObject = new JSONObject(sb.toString());
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e) {
                                    LogUtil.LogE(TAG, "onCreate: IOException", e);
                                }
                            }
                            if (Exists != 0) {
                                try {
                                    Exists.close();
                                } catch (IOException e2) {
                                    LogUtil.LogE(TAG, "onCreate: IOException", e2);
                                }
                            }
                            return jSONObject;
                        } catch (FileNotFoundException e3) {
                            e = e3;
                            LogUtil.LogE(TAG, "LoadJson2: FileNotFoundException", e);
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e4) {
                                    LogUtil.LogE(TAG, "onCreate: IOException", e4);
                                }
                            }
                            if (Exists == 0) {
                                return null;
                            }
                            Exists.close();
                            return null;
                        } catch (IOException e5) {
                            e = e5;
                            LogUtil.LogE(TAG, "LoadJson2: IOException", e);
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e6) {
                                    LogUtil.LogE(TAG, "onCreate: IOException", e6);
                                }
                            }
                            if (Exists == 0) {
                                return null;
                            }
                            Exists.close();
                            return null;
                        } catch (JSONException e7) {
                            e = e7;
                            LogUtil.LogE(TAG, "LoadJson2: JSONException", e);
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e8) {
                                    LogUtil.LogE(TAG, "onCreate: IOException", e8);
                                }
                            }
                            if (Exists == 0) {
                                return null;
                            }
                            Exists.close();
                            return null;
                        }
                    } catch (FileNotFoundException e9) {
                        e = e9;
                        bufferedReader = null;
                    } catch (IOException e10) {
                        e = e10;
                        bufferedReader = null;
                    } catch (JSONException e11) {
                        e = e11;
                        bufferedReader = null;
                    } catch (Throwable th) {
                        th = th;
                        file = 0;
                        if (file != 0) {
                            try {
                                file.close();
                            } catch (IOException e12) {
                                LogUtil.LogE(TAG, "onCreate: IOException", e12);
                            }
                        }
                        if (Exists == 0) {
                            throw th;
                        }
                        try {
                            Exists.close();
                            throw th;
                        } catch (IOException e13) {
                            LogUtil.LogE(TAG, "onCreate: IOException", e13);
                            throw th;
                        }
                    }
                } catch (FileNotFoundException e14) {
                    e = e14;
                    Exists = 0;
                    bufferedReader = null;
                } catch (IOException e15) {
                    e = e15;
                    Exists = 0;
                    bufferedReader = null;
                } catch (JSONException e16) {
                    e = e16;
                    Exists = 0;
                    bufferedReader = null;
                } catch (Throwable th2) {
                    file = 0;
                    th = th2;
                    Exists = 0;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (IOException e17) {
            LogUtil.LogE(TAG, "onCreate: IOException", e17);
            return null;
        }
    }

    public static void deploymentAsset(AssetManager assetManager, String str, String str2, String str3, boolean z) throws Throwable {
        String[] list;
        try {
            list = assetManager.list(str);
        } catch (IOException e) {
            LogUtil.LogE(TAG, "Error has occured with the AssetManager.list()", e);
            list = null;
        }
        if (list == null) {
            LogUtil.LogD(TAG, "null is returned from AssetManager.list()");
            return;
        }
        String[] list2 = null;
        for (int i = 0; i < list.length; i++) {
            if (!z || list[i].equals(str3)) {
                String str4 = z ? "" : "/";
                try {
                    list2 = assetManager.list(str + str4 + list[i]);
                } catch (IOException e2) {
                    LogUtil.LogE(TAG, "Error has occurred with the AssetManager.list() ", e2);
                }
                if (list2 != null) {
                    if (list2.length == 0) {
                        copyFileFromAssets(assetManager, str + str4 + list[i], str2 + "/" + str + str4 + list[i], FILE_PERMISSION);
                    } else {
                        if (createFolder(str2 + "/" + str + str4 + list[i] + "/", FILE_PERMISSION)) {
                            deploymentAsset(assetManager, str + str4 + list[i], str2, str3, false);
                        }
                    }
                }
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:51:0x0097 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:59:0x009c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:73:? A[SYNTHETIC] */
    private static void copyFileFromAssets(AssetManager assetManager, String str, String str2, String str3) throws Throwable {
        InputStream inputStreamOpen;
        FileOutputStream fileOutputStream;
        InputStream inputStream = null;
        try {
            inputStreamOpen = assetManager.open(str);
            try {
                fileOutputStream = new FileOutputStream(str2);
                try {
                    byte[] bArr = new byte[8192];
                    while (true) {
                        int i = inputStreamOpen.read(bArr);
                        if (i == -1) {
                            break;
                        } else {
                            fileOutputStream.write(bArr, 0, i);
                        }
                    }
                    fileOutputStream.flush();
                    File file = new File(str2);
                    if (file.exists()) {
                        file.setExecutable(true, true);
                        file.setWritable(true, true);
                        file.setReadable(true, false);
                    } else {
                        LogUtil.LogE(TAG, "File is not found. file=" + str2);
                    }
                    LogUtil.LogD(TAG, "copyFile: src = " + str + ", dst = " + str2);
                    if (inputStreamOpen != null) {
                        try {
                            inputStreamOpen.close();
                        } catch (IOException unused) {
                        }
                    }
                    if (fileOutputStream == null) {
                        return;
                    }
                } catch (IOException e) {
                    e = e;
                    inputStream = inputStreamOpen;
                    try {
                        LogUtil.LogE(TAG, "Error has occurred with the copyFile()", e);
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException unused2) {
                            }
                        }
                        if (fileOutputStream == null) {
                            return;
                        }
                    } catch (Throwable th) {
                        th = th;
                        inputStreamOpen = inputStream;
                        if (inputStreamOpen != null) {
                            try {
                                inputStreamOpen.close();
                            } catch (IOException unused3) {
                            }
                        }
                        if (fileOutputStream != null) {
                            throw th;
                        }
                        try {
                            fileOutputStream.close();
                            throw th;
                        } catch (IOException unused4) {
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (inputStreamOpen != null) {
                        inputStreamOpen.close();
                    }
                    if (fileOutputStream != null) {
                        throw th;
                    }
                    fileOutputStream.close();
                    throw th;
                }
            } catch (IOException e2) {
                e = e2;
                fileOutputStream = null;
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream = null;
            }
        } catch (IOException e3) {
            e = e3;
            fileOutputStream = null;
        } catch (Throwable th4) {
            th = th4;
            inputStreamOpen = null;
            fileOutputStream = null;
        }
        try {
            fileOutputStream.close();
        } catch (IOException unused5) {
        }
    }

    private static boolean createFolder(String str, String str2) {
        File file = new File(str);
        if (file.exists()) {
            return true;
        }
        if (!file.mkdirs()) {
            LogUtil.LogE(TAG, "Failed to create the folder: " + str);
            return false;
        }
        LogUtil.LogD(TAG, "Create Forlder: " + str);
        return true;
    }
}
