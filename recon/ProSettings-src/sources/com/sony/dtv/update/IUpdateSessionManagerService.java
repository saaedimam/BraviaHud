package com.sony.dtv.update;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IUpdateSessionManagerService extends IInterface {
    boolean acquireLock(String str, boolean z) throws RemoteException;

    boolean cancelUpdate(String str) throws RemoteException;

    boolean forceReleaseLock() throws RemoteException;

    String getRunnungVersion() throws RemoteException;

    boolean isLockHeld() throws RemoteException;

    boolean offerPayload(String str, PayloadInformation payloadInformation) throws RemoteException;

    boolean releaseLock(String str) throws RemoteException;

    boolean resumeUpdate(String str) throws RemoteException;

    boolean setListener(String str, IUpdateProgressListener iUpdateProgressListener) throws RemoteException;

    boolean suspendUpdate(String str) throws RemoteException;

    public static abstract class Stub extends Binder implements IUpdateSessionManagerService {
        private static final String DESCRIPTOR = "com.sony.dtv.update.IUpdateSessionManagerService";
        static final int TRANSACTION_acquireLock = 2;
        static final int TRANSACTION_cancelUpdate = 8;
        static final int TRANSACTION_forceReleaseLock = 4;
        static final int TRANSACTION_getRunnungVersion = 7;
        static final int TRANSACTION_isLockHeld = 5;
        static final int TRANSACTION_offerPayload = 6;
        static final int TRANSACTION_releaseLock = 3;
        static final int TRANSACTION_resumeUpdate = 10;
        static final int TRANSACTION_setListener = 1;
        static final int TRANSACTION_suspendUpdate = 9;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IUpdateSessionManagerService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IUpdateSessionManagerService)) {
                return (IUpdateSessionManagerService) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean listener = setListener(parcel.readString(), IUpdateProgressListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(listener ? 1 : 0);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zAcquireLock = acquireLock(parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(zAcquireLock ? 1 : 0);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zReleaseLock = releaseLock(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(zReleaseLock ? 1 : 0);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zForceReleaseLock = forceReleaseLock();
                    parcel2.writeNoException();
                    parcel2.writeInt(zForceReleaseLock ? 1 : 0);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zIsLockHeld = isLockHeld();
                    parcel2.writeNoException();
                    parcel2.writeInt(zIsLockHeld ? 1 : 0);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zOfferPayload = offerPayload(parcel.readString(), parcel.readInt() != 0 ? PayloadInformation.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(zOfferPayload ? 1 : 0);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    String runnungVersion = getRunnungVersion();
                    parcel2.writeNoException();
                    parcel2.writeString(runnungVersion);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zCancelUpdate = cancelUpdate(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(zCancelUpdate ? 1 : 0);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zSuspendUpdate = suspendUpdate(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(zSuspendUpdate ? 1 : 0);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zResumeUpdate = resumeUpdate(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(zResumeUpdate ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        private static class Proxy implements IUpdateSessionManagerService {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.sony.dtv.update.IUpdateSessionManagerService
            public boolean setListener(String str, IUpdateProgressListener iUpdateProgressListener) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeStrongBinder(iUpdateProgressListener != null ? iUpdateProgressListener.asBinder() : null);
                    this.mRemote.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.update.IUpdateSessionManagerService
            public boolean acquireLock(String str, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.update.IUpdateSessionManagerService
            public boolean releaseLock(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(3, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.update.IUpdateSessionManagerService
            public boolean forceReleaseLock() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.update.IUpdateSessionManagerService
            public boolean isLockHeld() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.update.IUpdateSessionManagerService
            public boolean offerPayload(String str, PayloadInformation payloadInformation) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    if (payloadInformation != null) {
                        parcelObtain.writeInt(1);
                        payloadInformation.writeToParcel(parcelObtain, 0);
                    } else {
                        parcelObtain.writeInt(0);
                    }
                    this.mRemote.transact(6, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.update.IUpdateSessionManagerService
            public String getRunnungVersion() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.update.IUpdateSessionManagerService
            public boolean cancelUpdate(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(8, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.update.IUpdateSessionManagerService
            public boolean suspendUpdate(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(9, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.update.IUpdateSessionManagerService
            public boolean resumeUpdate(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(10, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }
    }
}
