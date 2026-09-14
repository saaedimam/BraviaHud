package com.sony.dtv.b2b.vendorprotocol.logging;

import android.support.annotation.NonNull;
import android.util.Log;
import com.sony.dtv.b2b.vendorprotocol.util.ArrayUtil;
import com.sony.dtv.webapi.aidl.BuildConfig;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Optional;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public final class Logger {
    private static final int INDEX_CALLER_METHOD = 5;

    private Logger() {
        throw new AssertionError();
    }

    public static void v(String message, Object... args) {
        printLog(2, message, args);
    }

    public static void d(String message, Object... args) {
    }

    public static void i(String message, Object... args) {
        printLog(4, message, args);
    }

    public static void w(String message, Object... args) {
        printLog(5, message, args);
    }

    public static void w(String message, Throwable e, Object... args) {
        printLog(5, message, e, args);
    }

    public static void e(String message, Object... args) {
        printLog(6, message, args);
    }

    public static void e(String message, Throwable t, Object... args) {
        printLog(6, message, t, args);
    }

    private static void printLog(int priority, String message, final Object... args) {
        String msg = (String) Optional.ofNullable(message).map(new Function(args) { // from class: com.sony.dtv.b2b.vendorprotocol.logging.Logger$$Lambda$0
            private final Object[] arg$1;

            {
                this.arg$1 = args;
            }

            @Override // java.util.function.Function
            public Object apply(Object obj) {
                return Logger.lambda$printLog$0$Logger(this.arg$1, (String) obj);
            }
        }).orElse(BuildConfig.FLAVOR);
        if (msg.length() != 0) {
            Log.println(priority, getMetaInfo(), msg);
        }
    }

    static final /* synthetic */ String lambda$printLog$0$Logger(Object[] args, String m) {
        return ArrayUtil.isEmpty(args) ? m : String.format(m, args);
    }

    private static void printLog(int priority, String message, Throwable t, final Object... args) {
        String msg = (String) Optional.ofNullable(message).map(new Function(args) { // from class: com.sony.dtv.b2b.vendorprotocol.logging.Logger$$Lambda$1
            private final Object[] arg$1;

            {
                this.arg$1 = args;
            }

            @Override // java.util.function.Function
            public Object apply(Object obj) {
                return Logger.lambda$printLog$1$Logger(this.arg$1, (String) obj);
            }
        }).orElse(BuildConfig.FLAVOR);
        if (t != null || msg.length() != 0) {
            String stacktrace = (String) Optional.ofNullable(t).map(Logger$$Lambda$2.$instance).orElse(BuildConfig.FLAVOR);
            String sb = msg + "\n" + stacktrace;
            Log.println(priority, getMetaInfo(), sb);
        }
    }

    static final /* synthetic */ String lambda$printLog$1$Logger(Object[] args, String m) {
        return ArrayUtil.isEmpty(args) ? m : String.format(m, args);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @NonNull
    public static String getStackTraceString(@NonNull Throwable t) {
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter((Writer) sw, false);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    private static String getMetaInfo() {
        StackTraceElement element = Thread.currentThread().getStackTrace()[5];
        return getMetaInfo(element);
    }

    private static String getMetaInfo(@NonNull StackTraceElement element) {
        String fullClassName = element.getClassName();
        String methodName = element.getMethodName();
        int lineNumber = element.getLineNumber();
        return "[" + fullClassName + "#" + methodName + ":" + lineNumber + "]";
    }
}
