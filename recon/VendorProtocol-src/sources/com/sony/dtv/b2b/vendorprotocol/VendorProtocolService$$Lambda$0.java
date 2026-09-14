package com.sony.dtv.b2b.vendorprotocol;

import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
final /* synthetic */ class VendorProtocolService$$Lambda$0 implements Predicate {
    static final Predicate $instance = new VendorProtocolService$$Lambda$0();

    private VendorProtocolService$$Lambda$0() {
    }

    @Override // java.util.function.Predicate
    public boolean test(Object obj) {
        return VendorProtocolService.lambda$getWorkDirectory$0$VendorProtocolService((String) obj);
    }
}
