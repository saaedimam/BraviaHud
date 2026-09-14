package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/* JADX INFO: loaded from: classes.dex */
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TypefaceCompatUtil {
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";

    private TypefaceCompatUtil() {
    }

    public static File getTempFile(Context context) {
        String str = CACHE_FILE_PREFIX + Process.myPid() + "-" + Process.myTid() + "-";
        for (int i = 0; i < 100; i++) {
            File file = new File(context.getCacheDir(), str + i);
            try {
                if (file.createNewFile()) {
                    return file;
                }
            } catch (IOException unused) {
            }
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:15:0x0027 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:20:0x0032 A[Catch: IOException -> 0x0036, TryCatch #3 {IOException -> 0x0036, blocks: (B:3:0x0001, B:6:0x0018, B:16:0x0029, B:20:0x0032, B:19:0x002e, B:21:0x0035), top: B:27:0x0001, inners: #1 }] */
    /* JADX WARN: Code duplicated, block: B:25:0x0029 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:30:? A[Catch: IOException -> 0x0036, SYNTHETIC, TRY_LEAVE, TryCatch #3 {IOException -> 0x0036, blocks: (B:3:0x0001, B:6:0x0018, B:16:0x0029, B:20:0x0032, B:19:0x002e, B:21:0x0035), top: B:27:0x0001, inners: #1 }] */
    @RequiresApi(19)
    private static ByteBuffer mmap(File file) throws Throwable {
        Throwable th;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                FileChannel channel = fileInputStream.getChannel();
                MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return map;
            } catch (Throwable th2) {
                th = th2;
                th = null;
                if (fileInputStream != null) {
                    throw th;
                }
                if (th == null) {
                    fileInputStream.close();
                    throw th;
                }
                fileInputStream.close();
                throw th;
            }
        } catch (IOException unused) {
            return null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:18:0x003a A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:23:0x0045 A[Catch: all -> 0x0049, Throwable -> 0x004c, TryCatch #1 {Throwable -> 0x004c, blocks: (B:4:0x000b, B:7:0x0026, B:23:0x0045, B:22:0x0041, B:24:0x0048), top: B:42:0x000b }] */
    /* JADX WARN: Code duplicated, block: B:32:0x0054 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:37:0x005f A[Catch: IOException -> 0x0063, TryCatch #3 {IOException -> 0x0063, blocks: (B:3:0x0005, B:9:0x002b, B:33:0x0056, B:37:0x005f, B:36:0x005b, B:38:0x0062), top: B:45:0x0005, inners: #0 }] */
    /* JADX WARN: Code duplicated, block: B:40:0x0056 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:46:0x003c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:52:? A[Catch: all -> 0x0049, Throwable -> 0x004c, SYNTHETIC, TRY_LEAVE, TryCatch #1 {Throwable -> 0x004c, blocks: (B:4:0x000b, B:7:0x0026, B:23:0x0045, B:22:0x0041, B:24:0x0048), top: B:42:0x000b }] */
    /* JADX WARN: Code duplicated, block: B:55:? A[Catch: IOException -> 0x0063, SYNTHETIC, TRY_LEAVE, TryCatch #3 {IOException -> 0x0063, blocks: (B:3:0x0005, B:9:0x002b, B:33:0x0056, B:37:0x005f, B:36:0x005b, B:38:0x0062), top: B:45:0x0005, inners: #0 }] */
    @RequiresApi(19)
    public static ByteBuffer mmap(Context context, CancellationSignal cancellationSignal, Uri uri) throws Throwable {
        Throwable th;
        Throwable th2;
        try {
            ParcelFileDescriptor parcelFileDescriptorOpenFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r", cancellationSignal);
            try {
                try {
                    FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptorOpenFileDescriptor.getFileDescriptor());
                    try {
                        FileChannel channel = fileInputStream.getChannel();
                        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        if (parcelFileDescriptorOpenFileDescriptor != null) {
                            parcelFileDescriptorOpenFileDescriptor.close();
                        }
                        return map;
                    } catch (Throwable th3) {
                        th = th3;
                        th2 = null;
                        if (fileInputStream != null) {
                            throw th;
                        }
                        if (th2 == null) {
                            fileInputStream.close();
                            throw th;
                        }
                        fileInputStream.close();
                        throw th;
                    }
                } catch (Throwable th4) {
                    try {
                        throw th4;
                    } catch (Throwable th5) {
                        th = th4;
                        th = th5;
                        if (parcelFileDescriptorOpenFileDescriptor != null) {
                            throw th;
                        }
                        if (th == null) {
                            parcelFileDescriptorOpenFileDescriptor.close();
                            throw th;
                        }
                        try {
                            parcelFileDescriptorOpenFileDescriptor.close();
                            throw th;
                        } catch (Throwable th6) {
                            th.addSuppressed(th6);
                            throw th;
                        }
                    }
                }
            } catch (Throwable th7) {
                th = th7;
                th = null;
                if (parcelFileDescriptorOpenFileDescriptor != null) {
                    throw th;
                }
                if (th == null) {
                    parcelFileDescriptorOpenFileDescriptor.close();
                    throw th;
                }
                parcelFileDescriptorOpenFileDescriptor.close();
                throw th;
            }
        } catch (IOException unused) {
            return null;
        }
    }

    @RequiresApi(19)
    public static ByteBuffer copyToDirectBuffer(Context context, Resources resources, int i) {
        File tempFile = getTempFile(context);
        if (tempFile == null) {
            return null;
        }
        try {
            if (copyToFile(tempFile, resources, i)) {
                return mmap(tempFile);
            }
            return null;
        } finally {
            tempFile.delete();
        }
    }

    public static boolean copyToFile(File file, InputStream inputStream) throws Throwable {
        FileOutputStream fileOutputStream = null;
        try {
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(file, false);
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int i = inputStream.read(bArr);
                        if (i != -1) {
                            fileOutputStream2.write(bArr, 0, i);
                        } else {
                            closeQuietly(fileOutputStream2);
                            return true;
                        }
                    }
                } catch (IOException e) {
                    e = e;
                    fileOutputStream = fileOutputStream2;
                    Log.e(TAG, "Error copying resource contents to temp file: " + e.getMessage());
                    closeQuietly(fileOutputStream);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = fileOutputStream2;
                    closeQuietly(fileOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e2) {
            e = e2;
        }
    }

    public static boolean copyToFile(File file, Resources resources, int i) throws Throwable {
        InputStream inputStreamOpenRawResource;
        try {
            inputStreamOpenRawResource = resources.openRawResource(i);
            try {
                boolean zCopyToFile = copyToFile(file, inputStreamOpenRawResource);
                closeQuietly(inputStreamOpenRawResource);
                return zCopyToFile;
            } catch (Throwable th) {
                th = th;
                closeQuietly(inputStreamOpenRawResource);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStreamOpenRawResource = null;
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }
}
