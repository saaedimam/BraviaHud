package com.sony.dtv.b2b.prosettings;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface IProSettingsService extends IInterface {
    void cancelFwUpdate() throws RemoteException;

    void cleanupFwWrite() throws RemoteException;

    void clearAllAccount(IProSettingsCallback iProSettingsCallback) throws RemoteException;

    void clearApplicationUserData(List list, IProSettingsCallback iProSettingsCallback) throws RemoteException;

    String decryptPhrase(String str, String str2) throws RemoteException;

    void disableAppPackage(String str) throws RemoteException;

    void disableAppPackageIfEnabled(String str) throws RemoteException;

    void enableAppPackage(String str) throws RemoteException;

    void enableAppPackageIfChanged(String str) throws RemoteException;

    List get4KBEGeneralControlCommand() throws RemoteException;

    int getAppComponentStateSetting(String str, String str2) throws RemoteException;

    boolean getAppHiddenSettingAsUser(String str) throws RemoteException;

    boolean getAppPackageEnabled(String str) throws RemoteException;

    int getAppPackageStateSetting(String str) throws RemoteException;

    String getDeviceId() throws RemoteException;

    void getDiagnosisInformation(IProSettingsCallback iProSettingsCallback) throws RemoteException;

    int getIpControlAuthentication() throws RemoteException;

    String getIpControlPSK() throws RemoteException;

    List getMtkHotelMode() throws RemoteException;

    String getPackageName() throws RemoteException;

    int getSoftwareUpgradeStatus() throws RemoteException;

    String getSoftwareVersionDisplay() throws RemoteException;

    boolean installLogo(String str) throws RemoteException;

    String prepareFwWrite() throws RemoteException;

    void processRequest(String str, IProSettingsCallback iProSettingsCallback, int i) throws RemoteException;

    String processRequestSync(String str) throws RemoteException;

    void registerCallback(String str, IProSettingsCallback iProSettingsCallback, int i) throws RemoteException;

    int set4KBEGeneralControlCommand(List list) throws RemoteException;

    boolean setAppComponentStateSetting(String str, String str2, int i, int i2) throws RemoteException;

    boolean setAppHiddenSettingAsUser(String str, boolean z) throws RemoteException;

    boolean setAppPackageStateSetting(String str, int i, int i2) throws RemoteException;

    boolean setCustomLabels(Map map) throws RemoteException;

    boolean setFeatures(Map map, String str) throws RemoteException;

    void setFwUpdateDryRunMode(int i) throws RemoteException;

    boolean setHotelMenuSetting(List list) throws RemoteException;

    boolean setHotelMode(Map map) throws RemoteException;

    boolean setHotelSetting(List list) throws RemoteException;

    boolean setIPControl(Map map) throws RemoteException;

    boolean setNetworkSetting(Map map) throws RemoteException;

    int setScreenOverlay(boolean z) throws RemoteException;

    boolean setSoundPictureSetting(List list) throws RemoteException;

    boolean setStartupUrl(Map map) throws RemoteException;

    boolean setSystemProperty(String str, String str2) throws RemoteException;

    boolean setSystemSetting(Map map) throws RemoteException;

    boolean setWirelessSetting(Map map) throws RemoteException;

    void startFwUpdate(IProSettingsCallback iProSettingsCallback, IProSettingsCallback iProSettingsCallback2) throws RemoteException;

    int startSoftwareUpdateLegacy() throws RemoteException;

    boolean uninstallLogo() throws RemoteException;

    void unregisterCallback(IProSettingsCallback iProSettingsCallback) throws RemoteException;

    public static abstract class Stub extends Binder implements IProSettingsService {
        private static final String DESCRIPTOR = "com.sony.dtv.b2b.prosettings.IProSettingsService";
        static final int TRANSACTION_cancelFwUpdate = 34;
        static final int TRANSACTION_cleanupFwWrite = 32;
        static final int TRANSACTION_clearAllAccount = 46;
        static final int TRANSACTION_clearApplicationUserData = 47;
        static final int TRANSACTION_decryptPhrase = 30;
        static final int TRANSACTION_disableAppPackage = 8;
        static final int TRANSACTION_disableAppPackageIfEnabled = 5;
        static final int TRANSACTION_enableAppPackage = 9;
        static final int TRANSACTION_enableAppPackageIfChanged = 7;
        static final int TRANSACTION_get4KBEGeneralControlCommand = 44;
        static final int TRANSACTION_getAppComponentStateSetting = 13;
        static final int TRANSACTION_getAppHiddenSettingAsUser = 15;
        static final int TRANSACTION_getAppPackageEnabled = 6;
        static final int TRANSACTION_getAppPackageStateSetting = 11;
        static final int TRANSACTION_getDeviceId = 36;
        static final int TRANSACTION_getDiagnosisInformation = 48;
        static final int TRANSACTION_getIpControlAuthentication = 42;
        static final int TRANSACTION_getIpControlPSK = 39;
        static final int TRANSACTION_getMtkHotelMode = 45;
        static final int TRANSACTION_getPackageName = 37;
        static final int TRANSACTION_getSoftwareUpgradeStatus = 40;
        static final int TRANSACTION_getSoftwareVersionDisplay = 38;
        static final int TRANSACTION_installLogo = 28;
        static final int TRANSACTION_prepareFwWrite = 31;
        static final int TRANSACTION_processRequest = 1;
        static final int TRANSACTION_processRequestSync = 4;
        static final int TRANSACTION_registerCallback = 2;
        static final int TRANSACTION_set4KBEGeneralControlCommand = 43;
        static final int TRANSACTION_setAppComponentStateSetting = 12;
        static final int TRANSACTION_setAppHiddenSettingAsUser = 14;
        static final int TRANSACTION_setAppPackageStateSetting = 10;
        static final int TRANSACTION_setCustomLabels = 16;
        static final int TRANSACTION_setFeatures = 17;
        static final int TRANSACTION_setFwUpdateDryRunMode = 35;
        static final int TRANSACTION_setHotelMenuSetting = 18;
        static final int TRANSACTION_setHotelMode = 19;
        static final int TRANSACTION_setHotelSetting = 20;
        static final int TRANSACTION_setIPControl = 21;
        static final int TRANSACTION_setNetworkSetting = 26;
        static final int TRANSACTION_setScreenOverlay = 49;
        static final int TRANSACTION_setSoundPictureSetting = 22;
        static final int TRANSACTION_setStartupUrl = 23;
        static final int TRANSACTION_setSystemProperty = 25;
        static final int TRANSACTION_setSystemSetting = 24;
        static final int TRANSACTION_setWirelessSetting = 27;
        static final int TRANSACTION_startFwUpdate = 33;
        static final int TRANSACTION_startSoftwareUpdateLegacy = 41;
        static final int TRANSACTION_uninstallLogo = 29;
        static final int TRANSACTION_unregisterCallback = 3;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IProSettingsService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IProSettingsService)) {
                return (IProSettingsService) iInterfaceQueryLocalInterface;
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
                    processRequest(parcel.readString(), IProSettingsCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerCallback(parcel.readString(), IProSettingsCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregisterCallback(IProSettingsCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    String strProcessRequestSync = processRequestSync(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(strProcessRequestSync);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    disableAppPackageIfEnabled(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean appPackageEnabled = getAppPackageEnabled(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(appPackageEnabled ? 1 : 0);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    enableAppPackageIfChanged(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    disableAppPackage(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    enableAppPackage(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean appPackageStateSetting = setAppPackageStateSetting(parcel.readString(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(appPackageStateSetting ? 1 : 0);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    int appPackageStateSetting2 = getAppPackageStateSetting(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(appPackageStateSetting2);
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean appComponentStateSetting = setAppComponentStateSetting(parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(appComponentStateSetting ? 1 : 0);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    int appComponentStateSetting2 = getAppComponentStateSetting(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(appComponentStateSetting2);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean appHiddenSettingAsUser = setAppHiddenSettingAsUser(parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(appHiddenSettingAsUser ? 1 : 0);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean appHiddenSettingAsUser2 = getAppHiddenSettingAsUser(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(appHiddenSettingAsUser2 ? 1 : 0);
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean customLabels = setCustomLabels(parcel.readHashMap(getClass().getClassLoader()));
                    parcel2.writeNoException();
                    parcel2.writeInt(customLabels ? 1 : 0);
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean features = setFeatures(parcel.readHashMap(getClass().getClassLoader()), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(features ? 1 : 0);
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean hotelMenuSetting = setHotelMenuSetting(parcel.readArrayList(getClass().getClassLoader()));
                    parcel2.writeNoException();
                    parcel2.writeInt(hotelMenuSetting ? 1 : 0);
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean hotelMode = setHotelMode(parcel.readHashMap(getClass().getClassLoader()));
                    parcel2.writeNoException();
                    parcel2.writeInt(hotelMode ? 1 : 0);
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean hotelSetting = setHotelSetting(parcel.readArrayList(getClass().getClassLoader()));
                    parcel2.writeNoException();
                    parcel2.writeInt(hotelSetting ? 1 : 0);
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean iPControl = setIPControl(parcel.readHashMap(getClass().getClassLoader()));
                    parcel2.writeNoException();
                    parcel2.writeInt(iPControl ? 1 : 0);
                    return true;
                case 22:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean soundPictureSetting = setSoundPictureSetting(parcel.readArrayList(getClass().getClassLoader()));
                    parcel2.writeNoException();
                    parcel2.writeInt(soundPictureSetting ? 1 : 0);
                    return true;
                case 23:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean startupUrl = setStartupUrl(parcel.readHashMap(getClass().getClassLoader()));
                    parcel2.writeNoException();
                    parcel2.writeInt(startupUrl ? 1 : 0);
                    return true;
                case 24:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean systemSetting = setSystemSetting(parcel.readHashMap(getClass().getClassLoader()));
                    parcel2.writeNoException();
                    parcel2.writeInt(systemSetting ? 1 : 0);
                    return true;
                case 25:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean systemProperty = setSystemProperty(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(systemProperty ? 1 : 0);
                    return true;
                case 26:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean networkSetting = setNetworkSetting(parcel.readHashMap(getClass().getClassLoader()));
                    parcel2.writeNoException();
                    parcel2.writeInt(networkSetting ? 1 : 0);
                    return true;
                case 27:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean wirelessSetting = setWirelessSetting(parcel.readHashMap(getClass().getClassLoader()));
                    parcel2.writeNoException();
                    parcel2.writeInt(wirelessSetting ? 1 : 0);
                    return true;
                case 28:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zInstallLogo = installLogo(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(zInstallLogo ? 1 : 0);
                    return true;
                case 29:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zUninstallLogo = uninstallLogo();
                    parcel2.writeNoException();
                    parcel2.writeInt(zUninstallLogo ? 1 : 0);
                    return true;
                case 30:
                    parcel.enforceInterface(DESCRIPTOR);
                    String strDecryptPhrase = decryptPhrase(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(strDecryptPhrase);
                    return true;
                case 31:
                    parcel.enforceInterface(DESCRIPTOR);
                    String strPrepareFwWrite = prepareFwWrite();
                    parcel2.writeNoException();
                    parcel2.writeString(strPrepareFwWrite);
                    return true;
                case 32:
                    parcel.enforceInterface(DESCRIPTOR);
                    cleanupFwWrite();
                    parcel2.writeNoException();
                    return true;
                case 33:
                    parcel.enforceInterface(DESCRIPTOR);
                    startFwUpdate(IProSettingsCallback.Stub.asInterface(parcel.readStrongBinder()), IProSettingsCallback.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 34:
                    parcel.enforceInterface(DESCRIPTOR);
                    cancelFwUpdate();
                    return true;
                case 35:
                    parcel.enforceInterface(DESCRIPTOR);
                    setFwUpdateDryRunMode(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 36:
                    parcel.enforceInterface(DESCRIPTOR);
                    String deviceId = getDeviceId();
                    parcel2.writeNoException();
                    parcel2.writeString(deviceId);
                    return true;
                case 37:
                    parcel.enforceInterface(DESCRIPTOR);
                    String packageName = getPackageName();
                    parcel2.writeNoException();
                    parcel2.writeString(packageName);
                    return true;
                case 38:
                    parcel.enforceInterface(DESCRIPTOR);
                    String softwareVersionDisplay = getSoftwareVersionDisplay();
                    parcel2.writeNoException();
                    parcel2.writeString(softwareVersionDisplay);
                    return true;
                case 39:
                    parcel.enforceInterface(DESCRIPTOR);
                    String ipControlPSK = getIpControlPSK();
                    parcel2.writeNoException();
                    parcel2.writeString(ipControlPSK);
                    return true;
                case 40:
                    parcel.enforceInterface(DESCRIPTOR);
                    int softwareUpgradeStatus = getSoftwareUpgradeStatus();
                    parcel2.writeNoException();
                    parcel2.writeInt(softwareUpgradeStatus);
                    return true;
                case 41:
                    parcel.enforceInterface(DESCRIPTOR);
                    int iStartSoftwareUpdateLegacy = startSoftwareUpdateLegacy();
                    parcel2.writeNoException();
                    parcel2.writeInt(iStartSoftwareUpdateLegacy);
                    return true;
                case 42:
                    parcel.enforceInterface(DESCRIPTOR);
                    int ipControlAuthentication = getIpControlAuthentication();
                    parcel2.writeNoException();
                    parcel2.writeInt(ipControlAuthentication);
                    return true;
                case 43:
                    parcel.enforceInterface(DESCRIPTOR);
                    int i3 = set4KBEGeneralControlCommand(parcel.readArrayList(getClass().getClassLoader()));
                    parcel2.writeNoException();
                    parcel2.writeInt(i3);
                    return true;
                case 44:
                    parcel.enforceInterface(DESCRIPTOR);
                    List list = get4KBEGeneralControlCommand();
                    parcel2.writeNoException();
                    parcel2.writeList(list);
                    return true;
                case 45:
                    parcel.enforceInterface(DESCRIPTOR);
                    List mtkHotelMode = getMtkHotelMode();
                    parcel2.writeNoException();
                    parcel2.writeList(mtkHotelMode);
                    return true;
                case 46:
                    parcel.enforceInterface(DESCRIPTOR);
                    clearAllAccount(IProSettingsCallback.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 47:
                    parcel.enforceInterface(DESCRIPTOR);
                    clearApplicationUserData(parcel.readArrayList(getClass().getClassLoader()), IProSettingsCallback.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 48:
                    parcel.enforceInterface(DESCRIPTOR);
                    getDiagnosisInformation(IProSettingsCallback.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 49:
                    parcel.enforceInterface(DESCRIPTOR);
                    int screenOverlay = setScreenOverlay(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(screenOverlay);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        private static class Proxy implements IProSettingsService {
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

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public void processRequest(String str, IProSettingsCallback iProSettingsCallback, int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeStrongBinder(iProSettingsCallback != null ? iProSettingsCallback.asBinder() : null);
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public void registerCallback(String str, IProSettingsCallback iProSettingsCallback, int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeStrongBinder(iProSettingsCallback != null ? iProSettingsCallback.asBinder() : null);
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public void unregisterCallback(IProSettingsCallback iProSettingsCallback) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeStrongBinder(iProSettingsCallback != null ? iProSettingsCallback.asBinder() : null);
                    this.mRemote.transact(3, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public String processRequestSync(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(4, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public void disableAppPackageIfEnabled(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(5, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean getAppPackageEnabled(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(6, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public void enableAppPackageIfChanged(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(7, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public void disableAppPackage(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(8, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public void enableAppPackage(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(9, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setAppPackageStateSetting(String str, int i, int i2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    this.mRemote.transact(10, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public int getAppPackageStateSetting(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(11, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setAppComponentStateSetting(String str, String str2, int i, int i2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    parcelObtain.writeInt(i);
                    parcelObtain.writeInt(i2);
                    this.mRemote.transact(12, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public int getAppComponentStateSetting(String str, String str2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    this.mRemote.transact(13, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setAppHiddenSettingAsUser(String str, boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(14, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean getAppHiddenSettingAsUser(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(15, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setCustomLabels(Map map) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeMap(map);
                    this.mRemote.transact(16, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setFeatures(Map map, String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeMap(map);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(17, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setHotelMenuSetting(List list) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeList(list);
                    this.mRemote.transact(18, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setHotelMode(Map map) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeMap(map);
                    this.mRemote.transact(19, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setHotelSetting(List list) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeList(list);
                    this.mRemote.transact(20, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setIPControl(Map map) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeMap(map);
                    this.mRemote.transact(21, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setSoundPictureSetting(List list) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeList(list);
                    this.mRemote.transact(22, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setStartupUrl(Map map) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeMap(map);
                    this.mRemote.transact(23, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setSystemSetting(Map map) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeMap(map);
                    this.mRemote.transact(24, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setSystemProperty(String str, String str2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    this.mRemote.transact(25, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setNetworkSetting(Map map) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeMap(map);
                    this.mRemote.transact(26, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean setWirelessSetting(Map map) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeMap(map);
                    this.mRemote.transact(27, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean installLogo(String str) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    this.mRemote.transact(28, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public boolean uninstallLogo() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(29, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public String decryptPhrase(String str, String str2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeString(str2);
                    this.mRemote.transact(30, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public String prepareFwWrite() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(31, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public void cleanupFwWrite() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(32, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public void startFwUpdate(IProSettingsCallback iProSettingsCallback, IProSettingsCallback iProSettingsCallback2) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeStrongBinder(iProSettingsCallback != null ? iProSettingsCallback.asBinder() : null);
                    parcelObtain.writeStrongBinder(iProSettingsCallback2 != null ? iProSettingsCallback2.asBinder() : null);
                    this.mRemote.transact(33, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public void cancelFwUpdate() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(34, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public void setFwUpdateDryRunMode(int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    this.mRemote.transact(35, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public String getDeviceId() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(36, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public String getPackageName() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public String getSoftwareVersionDisplay() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public String getIpControlPSK() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public int getSoftwareUpgradeStatus() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(40, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public int startSoftwareUpdateLegacy() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(41, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public int getIpControlAuthentication() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(42, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public int set4KBEGeneralControlCommand(List list) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeList(list);
                    this.mRemote.transact(43, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public List get4KBEGeneralControlCommand() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readArrayList(getClass().getClassLoader());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public List getMtkHotelMode() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(45, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readArrayList(getClass().getClassLoader());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public void clearAllAccount(IProSettingsCallback iProSettingsCallback) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeStrongBinder(iProSettingsCallback != null ? iProSettingsCallback.asBinder() : null);
                    this.mRemote.transact(46, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public void clearApplicationUserData(List list, IProSettingsCallback iProSettingsCallback) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeList(list);
                    parcelObtain.writeStrongBinder(iProSettingsCallback != null ? iProSettingsCallback.asBinder() : null);
                    this.mRemote.transact(47, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public void getDiagnosisInformation(IProSettingsCallback iProSettingsCallback) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeStrongBinder(iProSettingsCallback != null ? iProSettingsCallback.asBinder() : null);
                    this.mRemote.transact(48, parcelObtain, null, 1);
                } finally {
                    parcelObtain.recycle();
                }
            }

            @Override // com.sony.dtv.b2b.prosettings.IProSettingsService
            public int setScreenOverlay(boolean z) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(49, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }
    }
}
