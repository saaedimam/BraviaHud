package com.sony.dtv.b2b.vendorprotocol.logging;

import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final /* synthetic */ class Logger$$Lambda$2 implements Function {
    static final Function $instance = new Logger$$Lambda$2();

    private Logger$$Lambda$2() {
    }

    @Override // java.util.function.Function
    public Object apply(Object obj) {
        return Logger.getStackTraceString((Throwable) obj);
    }
}
