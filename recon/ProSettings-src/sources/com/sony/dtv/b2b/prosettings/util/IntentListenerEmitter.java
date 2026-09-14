package com.sony.dtv.b2b.prosettings.util;

import android.content.Intent;
import com.sony.dtv.b2b.prosettings.util.J8Compat.Function;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class IntentListenerEmitter {
    private final String TAG = "IntentListenerEmitter";
    private int mListenerCounter = 0;
    private List<IntentListenerItem> mIntentListenerArray = new ArrayList();

    private static class IntentListenerItem {
        public final String mAction;
        public final Function<Intent, Boolean> mCallback;
        public final int mHandleId;

        public IntentListenerItem(String str, Function<Intent, Boolean> function, int i) {
            this.mAction = str;
            this.mCallback = function;
            this.mHandleId = i;
        }
    }

    public synchronized int addListener(String str, Function<Intent, Boolean> function) {
        if (str == null) {
            str = "";
        }
        try {
            LogUtil.LogD("IntentListenerEmitter", "addListener(" + str + ")");
            this.mIntentListenerArray.add(new IntentListenerItem(str, function, this.mListenerCounter));
            this.mListenerCounter = this.mListenerCounter + 1;
        } catch (Throwable th) {
            throw th;
        }
        return this.mListenerCounter;
    }

    private void removeListener(int i) {
        Iterator<IntentListenerItem> it = this.mIntentListenerArray.iterator();
        while (it.hasNext()) {
            if (it.next().mHandleId == i) {
                it.remove();
            }
        }
    }

    public synchronized void removeAllListener() {
        this.mIntentListenerArray.clear();
    }

    public synchronized void emitListener(Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            action = "";
        }
        Iterator<IntentListenerItem> it = this.mIntentListenerArray.iterator();
        while (it.hasNext()) {
            IntentListenerItem next = it.next();
            LogUtil.LogI("IntentListenerEmitter", "emitListener: " + action + " : " + next.mAction);
            if (action.equals(next.mAction) && !next.mCallback.apply(intent).booleanValue()) {
                it.remove();
            }
        }
    }
}
