package com.sony.dtv.tvinput.provider;

import java.io.IOException;
import java.util.ArrayList;
import org.msgpack.MessageTypeException;
import org.msgpack.io.EndOfBufferException;
import org.msgpack.packer.Packer;
import org.msgpack.template.AbstractTemplate;
import org.msgpack.unpacker.Unpacker;

/* JADX INFO: loaded from: classes.dex */
public final class InternalProviderDataMsgPackTemplate extends AbstractTemplate<InternalProviderDataMsgPack> {
    private static final InternalProviderDataMsgPackTemplate INSTANCE = new InternalProviderDataMsgPackTemplate();

    private InternalProviderDataMsgPackTemplate() {
    }

    public static InternalProviderDataMsgPackTemplate getInstance() {
        return INSTANCE;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.msgpack.MessageTypeException */
    public void write(Packer packer, InternalProviderDataMsgPack internalProviderDataMsgPack, boolean z) throws MessageTypeException, IOException {
        if (internalProviderDataMsgPack != null) {
            writeTo(packer, internalProviderDataMsgPack);
        } else {
            if (z) {
                throw new MessageTypeException("Attempted to write null");
            }
            packer.writeNil();
        }
    }

    public InternalProviderDataMsgPack read(Unpacker unpacker, InternalProviderDataMsgPack internalProviderDataMsgPack, boolean z) throws IOException {
        return readTo(unpacker);
    }

    private void writeTo(Packer packer, InternalProviderDataMsgPack internalProviderDataMsgPack) throws IOException {
        packer.write(internalProviderDataMsgPack.getVersion());
        packer.write(internalProviderDataMsgPack.getEventId());
        packer.write(internalProviderDataMsgPack.getTimeType());
        packer.write(internalProviderDataMsgPack.getEventType());
        packer.write(internalProviderDataMsgPack.getIsScrambled());
        packer.write(internalProviderDataMsgPack.getSharedServiceId());
    }

    private InternalProviderDataMsgPack readTo(Unpacker unpacker) throws IOException {
        InternalProviderDataMsgPack internalProviderDataMsgPackBuild = new InternalProviderDataMsgPack.Builder().build();
        try {
            int i = unpacker.readInt();
            internalProviderDataMsgPackBuild.setVersion(i);
            unpacker.getNextType();
            internalProviderDataMsgPackBuild.setEventId(unpacker.readInt());
            unpacker.getNextType();
            readTimeType(i, unpacker, internalProviderDataMsgPackBuild);
            internalProviderDataMsgPackBuild.setEventType(unpacker.readInt());
            unpacker.getNextType();
            internalProviderDataMsgPackBuild.setIsScrambled(unpacker.readInt());
            unpacker.getNextType();
            if (!unpacker.trySkipNil()) {
                int arrayBegin = unpacker.readArrayBegin();
                ArrayList arrayList = new ArrayList(arrayBegin);
                for (int i2 = 0; i2 < arrayBegin; i2++) {
                    arrayList.add(Integer.valueOf(unpacker.readInt()));
                }
                internalProviderDataMsgPackBuild.setSharedServiceId(arrayList);
                unpacker.readArrayEnd();
            }
            return internalProviderDataMsgPackBuild;
        } catch (EndOfBufferException unused) {
            return internalProviderDataMsgPackBuild;
        } catch (MessageTypeException unused2) {
            return null;
        }
    }

    private void readTimeType(int i, Unpacker unpacker, InternalProviderDataMsgPack internalProviderDataMsgPack) throws IOException {
        if (1 == i) {
            long j = unpacker.readLong();
            unpacker.getNextType();
            internalProviderDataMsgPack.setTimeType(j, unpacker.readLong());
        } else {
            internalProviderDataMsgPack.setTimeType(unpacker.readInt());
        }
        unpacker.getNextType();
    }
}
