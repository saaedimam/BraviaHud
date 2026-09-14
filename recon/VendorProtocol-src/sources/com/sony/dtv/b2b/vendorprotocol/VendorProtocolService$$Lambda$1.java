package com.sony.dtv.b2b.vendorprotocol;

import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
final /* synthetic */ class VendorProtocolService$$Lambda$1 implements Function {
    static final Function $instance = new VendorProtocolService$$Lambda$1();

    private VendorProtocolService$$Lambda$1() {
    }

    @Override // java.util.function.Function
    public Object apply(Object obj) {
        return VendorProtocolService.lambda$getWorkDirectory$1$VendorProtocolService((String) obj);
    }
}
