package com.sony.dtv.b2b.prosettings.platform;

import android.net.ProxyInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import com.sony.dtv.b2b.prosettings.util.LogUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class NetworkSettings extends PlatformBase {
    private static final String TAG = "NetworkSettings";

    public static class DmNetWirelessAccessPoint {
        public BitSet m_allowedAuthAlgorithms = new BitSet();
        public BitSet m_allowedGroupCiphers = new BitSet();
        public BitSet m_allowedKeyManagement = new BitSet();
        public BitSet m_allowedPairwiseCiphers = new BitSet();
        public BitSet m_allowedProtocols = new BitSet();
        public String m_key;
        public DmNetWirelessSecurity m_security;
        public String m_ssid;
    }

    enum DmNetWirelessSecurity {
        INVALID,
        NONE,
        WEP,
        WPA_TKIP,
        WPA_AES,
        WPA2_TKIP,
        WPA2_AES,
        WPA_AUTO_TKIP,
        WPA_AUTO_AES,
        WPA_AUTO,
        UNKNOWN
    }

    private static String getCIDR(String str, String str2) {
        HashMap<String, String> map = new HashMap<String, String>() { // from class: com.sony.dtv.b2b.prosettings.platform.NetworkSettings.1
            {
                put("255.255.255.255", "32");
                put("255.255.255.254", "31");
                put("255.255.255.252", "30");
                put("255.255.255.248", "29");
                put("255.255.255.240", "28");
                put("255.255.255.224", "27");
                put("255.255.255.192", "26");
                put("255.255.255.128", "25");
                put("255.255.255.0", "24");
                put("255.255.254.0", "23");
                put("255.255.252.0", "22");
                put("255.255.248.0", "21");
                put("255.255.240.0", "20");
                put("255.255.224.0", "19");
                put("255.255.192.0", "18");
                put("255.255.128.0", "17");
                put("255.255.0.0", "16");
                put("255.254.0.0", "15");
                put("255.252.0.0", "14");
                put("255.248.0.0", "13");
                put("255.240.0.0", "12");
                put("255.224.0.0", "11");
                put("255.192.0.0", "10");
                put("255.128.0.0", "9");
                put("255.0.0.0", "8");
                put("254.0.0.0", "7");
                put("252.0.0.0", "6");
                put("248.0.0.0", "5");
                put("240.0.0.0", "4");
                put("224.0.0.0", "3");
                put("192.0.0.0", "2");
                put("128.0.0.0", "1");
                put("0.0.0.0", "0");
            }
        };
        String strReplace = str2.replace("000", "0").replace("00", "0");
        return str + "/" + (map.containsKey(strReplace) ? map.get(strReplace) : "24");
    }

    public static boolean setNetworkSetting(Map<String, String> map) {
        Object systemService;
        boolean z;
        boolean z2;
        Class<?> classByName = getClassByName("android.net.EthernetManager");
        Method methodByName = getMethodByName(classByName, "getConfiguration", new Class[0]);
        try {
            systemService = mContext.getSystemService("ethernet");
        } catch (Exception e) {
            LogUtil.LogE(TAG, "setNetworkSetting:", e);
            systemService = null;
        }
        if (classByName == null || methodByName == null || systemService == null) {
            LogUtil.LogE(TAG, "setNetworkSetting: clsEthernetManagert/methodGetConfiguration/emInstance == null, fail.");
            LogUtil.LogE(TAG, "    clsEthernetManager = " + classByName);
            LogUtil.LogE(TAG, "    methodGetConfiguration = " + methodByName);
            LogUtil.LogE(TAG, "    emInstance = " + systemService);
            return false;
        }
        Object objInvokeMethod = invokeMethod(systemService, methodByName, new Object[0]);
        Class<?> classByName2 = getClassByName("android.net.IpConfiguration");
        if (classByName2 == null) {
            LogUtil.LogE(TAG, "setNetworkSetting: clsIpConfiguration == null, fail.");
            return false;
        }
        Class<?> classByName3 = getClassByName("android.net.IpConfiguration$IpAssignment");
        if (classByName3 == null) {
            LogUtil.LogE(TAG, "setNetworkSetting: enumIpAssignment == null, fail.");
            return false;
        }
        Enum enumValueOf = Enum.valueOf(classByName3, "DHCP");
        String str = map.get("ip4.m_isDhcpEnabled");
        String str2 = map.get("ip4.m_ipAddress.m_ipAddress");
        String str3 = map.get("ip4.m_ipAddress.m_subnetMask");
        String str4 = map.get("ip4.m_ipAddress.m_defaultGateway");
        String str5 = map.get("ip4.m_dns.m_primaryDns");
        String str6 = map.get("ip4.m_dns.m_secondaryDns");
        String str7 = map.get("proxy.m_isEnabled");
        String str8 = map.get("proxy.m_proxyName");
        Enum enumValueOf2 = enumValueOf;
        int i = Integer.parseInt(map.get("proxy.m_portNumber"));
        if (!str.equals("true")) {
            enumValueOf2 = Enum.valueOf(classByName3, "STATIC");
        }
        Object obj = systemService;
        Method methodByName2 = getMethodByName(classByName2, "setIpAssignment", classByName3);
        if (methodByName2 == null) {
            LogUtil.LogE(TAG, "setNetworkSetting: methodSetIpAssignment == null, fail");
            return false;
        }
        if (!invokeSetMethod(objInvokeMethod, methodByName2, enumValueOf2)) {
            LogUtil.LogE(TAG, "setNetworkSetting: IpAssignment_STATIC invokeSetMethod fail.");
            return false;
        }
        Class<?> classByName4 = getClassByName("android.net.StaticIpConfiguration");
        try {
            Object objNewInstance = classByName4.newInstance();
            if (objNewInstance == null) {
                LogUtil.LogE(TAG, "setNetworkSetting: staticIpConfiguration == null");
                return false;
            }
            if (str.equals("true")) {
                z = false;
            } else {
                String cidr = getCIDR(str2, str3);
                Class<?> classByName5 = getClassByName("android.net.LinkAddress");
                try {
                    Class<?>[] clsArr = new Class[1];
                    try {
                        clsArr[0] = String.class;
                        Object objNewInstance2 = classByName5.getConstructor(clsArr).newInstance(cidr);
                        try {
                            InetAddress byName = InetAddress.getByName(str4);
                            try {
                                InetAddress byName2 = InetAddress.getByName(str5);
                                try {
                                    InetAddress byName3 = InetAddress.getByName(str6);
                                    if (!setField(objNewInstance, "ipAddress", objNewInstance2)) {
                                        LogUtil.LogE(TAG, "setNetworkSetting: ipAddress setField fail.");
                                        return false;
                                    }
                                    if (!setField(objNewInstance, "gateway", byName)) {
                                        LogUtil.LogE(TAG, "setNetworkSetting: gateway setField fail.");
                                        return false;
                                    }
                                    try {
                                        try {
                                            ArrayList arrayList = (ArrayList) objNewInstance.getClass().getField("dnsServers").get(objNewInstance);
                                            if (!str5.isEmpty()) {
                                                arrayList.add(byName2);
                                            }
                                            if (!str6.isEmpty()) {
                                                arrayList.add(byName3);
                                            }
                                            z = false;
                                            Method methodByName3 = getMethodByName(classByName2, "setStaticIpConfiguration", classByName4);
                                            if (methodByName3 == null) {
                                                LogUtil.LogE(TAG, "setNetworkSetting: methodSetStaticIpConfiguration getMethod fail.");
                                                return false;
                                            }
                                            if (!invokeSetMethod(objInvokeMethod, methodByName3, objNewInstance)) {
                                                LogUtil.LogE(TAG, "setNetworkSetting: methodSetStaticIpConfiguration invokeSetMethod fail.");
                                                return false;
                                            }
                                        } catch (IllegalAccessException | IllegalArgumentException e2) {
                                            LogUtil.LogE(TAG, "setNetworkSetting: fieldDnsServers get object fail.", e2);
                                            return false;
                                        }
                                    } catch (NoSuchFieldException unused) {
                                        LogUtil.LogE(TAG, "setNetworkSetting: dnsServers getField fail.");
                                        return false;
                                    }
                                } catch (UnknownHostException e3) {
                                    LogUtil.LogE(TAG, "setNetworkSetting: primaryDns InetAddress fail; " + str6, e3);
                                    return false;
                                }
                            } catch (UnknownHostException e4) {
                                LogUtil.LogE(TAG, "setNetworkSetting: primaryDns InetAddress fail; " + str5, e4);
                                return false;
                            }
                        } catch (UnknownHostException unused2) {
                            LogUtil.LogE(TAG, "setNetworkSetting: gateway InetAddress fail; " + str4);
                            return false;
                        }
                    } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException unused3) {
                        z2 = false;
                        LogUtil.LogE(TAG, "setNetworkSetting: clsLinkAddress.getConstructor fail.");
                        return z2;
                    }
                } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException unused4) {
                    z2 = false;
                }
            }
            Class<?> classByName6 = getClassByName("android.net.IpConfiguration$ProxySettings");
            if (classByName6 == null) {
                LogUtil.LogE(TAG, "setNetworkSetting: enumProxySettings == null, fail");
                return z;
            }
            Enum enumValueOf3 = Enum.valueOf(classByName6, "NONE");
            if (str7.equals("true")) {
                enumValueOf3 = Enum.valueOf(classByName6, "STATIC");
            }
            Method methodByName4 = getMethodByName(classByName2, "setProxySettings", classByName6);
            if (methodByName4 == null) {
                LogUtil.LogE(TAG, "setNetworkSetting: methodSetProxySettings == null");
                return false;
            }
            if (!invokeSetMethod(objInvokeMethod, methodByName4, enumValueOf3)) {
                LogUtil.LogE(TAG, "setNetworkSetting: proxySettings invokeSetMethod fail.");
                return false;
            }
            Method methodByName5 = getMethodByName(classByName2, "setHttpProxy", ProxyInfo.class);
            if (methodByName5 == null) {
                LogUtil.LogE(TAG, "setNetworkSetting: methodSetHttpProxy getMethod fail.");
                return false;
            }
            if (!invokeSetMethod(objInvokeMethod, methodByName5, str7.equals("true") ? ProxyInfo.buildDirectProxy(str8, i) : null)) {
                LogUtil.LogE(TAG, "setNetworkSetting: methodSetHttpProxy invokeSetMethod fail.");
                return false;
            }
            Method methodByName6 = getMethodByName(classByName, "setConfiguration", classByName2);
            if (methodByName6 == null) {
                LogUtil.LogE(TAG, "setNetworkSetting: methodSetConfiguration getMethod fail.");
                return false;
            }
            if (invokeSetMethod(obj, methodByName6, objInvokeMethod)) {
                return true;
            }
            LogUtil.LogE(TAG, "setNetworkSetting: methodSetConfiguration invokeSetMethod fail.");
            return false;
        } catch (IllegalAccessException | InstantiationException unused5) {
            LogUtil.LogE(TAG, "setNetworkSetting: clsStaticIpConfiguration.newInstance fail.");
            return false;
        }
    }

    private static DmNetWirelessAccessPoint getAccessPoint(String str, String str2, String str3) {
        DmNetWirelessAccessPoint dmNetWirelessAccessPoint = new DmNetWirelessAccessPoint();
        dmNetWirelessAccessPoint.m_ssid = str;
        dmNetWirelessAccessPoint.m_key = str3;
        if (!str.isEmpty() && !str2.isEmpty()) {
            dmNetWirelessAccessPoint.m_ssid = str;
            switch (str2) {
                case "none":
                    dmNetWirelessAccessPoint.m_security = DmNetWirelessSecurity.NONE;
                    break;
                case "wep":
                    dmNetWirelessAccessPoint.m_security = DmNetWirelessSecurity.WEP;
                    dmNetWirelessAccessPoint.m_allowedAuthAlgorithms.set(1);
                    dmNetWirelessAccessPoint.m_allowedGroupCiphers.set(0);
                    dmNetWirelessAccessPoint.m_allowedGroupCiphers.set(1);
                    dmNetWirelessAccessPoint.m_allowedKeyManagement.set(0);
                    dmNetWirelessAccessPoint.m_allowedPairwiseCiphers.set(0);
                    break;
                case "wpa_tkip":
                    dmNetWirelessAccessPoint.m_security = DmNetWirelessSecurity.WPA_TKIP;
                    dmNetWirelessAccessPoint.m_allowedAuthAlgorithms.set(0);
                    dmNetWirelessAccessPoint.m_allowedGroupCiphers.set(2);
                    dmNetWirelessAccessPoint.m_allowedKeyManagement.set(1);
                    dmNetWirelessAccessPoint.m_allowedPairwiseCiphers.set(1);
                    dmNetWirelessAccessPoint.m_allowedProtocols.set(0);
                    break;
                case "wpa_aes":
                    dmNetWirelessAccessPoint.m_security = DmNetWirelessSecurity.WPA_AES;
                    dmNetWirelessAccessPoint.m_allowedAuthAlgorithms.set(0);
                    dmNetWirelessAccessPoint.m_allowedGroupCiphers.set(3);
                    dmNetWirelessAccessPoint.m_allowedKeyManagement.set(1);
                    dmNetWirelessAccessPoint.m_allowedPairwiseCiphers.set(2);
                    dmNetWirelessAccessPoint.m_allowedProtocols.set(0);
                    break;
                case "wpa2_tkip":
                    dmNetWirelessAccessPoint.m_security = DmNetWirelessSecurity.WPA2_TKIP;
                    dmNetWirelessAccessPoint.m_allowedAuthAlgorithms.set(0);
                    dmNetWirelessAccessPoint.m_allowedGroupCiphers.set(2);
                    dmNetWirelessAccessPoint.m_allowedKeyManagement.set(1);
                    dmNetWirelessAccessPoint.m_allowedPairwiseCiphers.set(1);
                    dmNetWirelessAccessPoint.m_allowedProtocols.set(1);
                    break;
                case "wpa2_aes":
                    dmNetWirelessAccessPoint.m_security = DmNetWirelessSecurity.WPA2_AES;
                    dmNetWirelessAccessPoint.m_allowedAuthAlgorithms.set(0);
                    dmNetWirelessAccessPoint.m_allowedGroupCiphers.set(3);
                    dmNetWirelessAccessPoint.m_allowedKeyManagement.set(1);
                    dmNetWirelessAccessPoint.m_allowedPairwiseCiphers.set(2);
                    dmNetWirelessAccessPoint.m_allowedProtocols.set(1);
                    break;
                case "wpa_auto_tkip":
                    dmNetWirelessAccessPoint.m_security = DmNetWirelessSecurity.WPA_AUTO_TKIP;
                    dmNetWirelessAccessPoint.m_allowedAuthAlgorithms.set(0);
                    dmNetWirelessAccessPoint.m_allowedGroupCiphers.set(2);
                    dmNetWirelessAccessPoint.m_allowedKeyManagement.set(1);
                    dmNetWirelessAccessPoint.m_allowedPairwiseCiphers.set(1);
                    dmNetWirelessAccessPoint.m_allowedProtocols.set(0);
                    break;
                case "wpa_auto_aes":
                    dmNetWirelessAccessPoint.m_security = DmNetWirelessSecurity.WPA_AUTO_AES;
                    dmNetWirelessAccessPoint.m_allowedAuthAlgorithms.set(0);
                    dmNetWirelessAccessPoint.m_allowedGroupCiphers.set(3);
                    dmNetWirelessAccessPoint.m_allowedKeyManagement.set(1);
                    dmNetWirelessAccessPoint.m_allowedPairwiseCiphers.set(2);
                    dmNetWirelessAccessPoint.m_allowedProtocols.set(0);
                    break;
                case "wpa_auto":
                    dmNetWirelessAccessPoint.m_security = DmNetWirelessSecurity.WPA_AUTO;
                    dmNetWirelessAccessPoint.m_allowedAuthAlgorithms.set(0);
                    dmNetWirelessAccessPoint.m_allowedGroupCiphers.set(2);
                    dmNetWirelessAccessPoint.m_allowedGroupCiphers.set(3);
                    dmNetWirelessAccessPoint.m_allowedKeyManagement.set(1);
                    dmNetWirelessAccessPoint.m_allowedPairwiseCiphers.set(1);
                    dmNetWirelessAccessPoint.m_allowedPairwiseCiphers.set(2);
                    dmNetWirelessAccessPoint.m_allowedProtocols.set(0);
                    dmNetWirelessAccessPoint.m_allowedProtocols.set(1);
                    break;
                default:
                    dmNetWirelessAccessPoint.m_security = DmNetWirelessSecurity.INVALID;
                    break;
            }
        }
        return dmNetWirelessAccessPoint;
    }

    public static boolean setWirelessSetting(HashMap<String, String> map) {
        DmNetWirelessAccessPoint accessPoint = getAccessPoint(map.get("access_point.m_ssid"), map.get("access_point.security"), map.get("access_point.m_key"));
        if (accessPoint.m_security.equals("INVALID") || accessPoint.m_security.equals("UNKNOWN")) {
            LogUtil.LogE(TAG, "setWirelessSettings: skipped; m_security = " + accessPoint.m_security);
            return false;
        }
        WifiManager wifiManager = (WifiManager) mContext.getSystemService("wifi");
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "\"" + accessPoint.m_ssid + "\"";
        wifiConfiguration.preSharedKey = "\"" + accessPoint.m_key + "\"";
        wifiConfiguration.allowedAuthAlgorithms = accessPoint.m_allowedAuthAlgorithms;
        wifiConfiguration.allowedGroupCiphers = accessPoint.m_allowedGroupCiphers;
        wifiConfiguration.allowedKeyManagement = accessPoint.m_allowedKeyManagement;
        wifiConfiguration.allowedPairwiseCiphers = accessPoint.m_allowedPairwiseCiphers;
        wifiConfiguration.allowedProtocols = accessPoint.m_allowedProtocols;
        LogUtil.LogD(TAG, "setWirelessSetting: SSID                   = " + wifiConfiguration.SSID);
        LogUtil.LogD(TAG, "setWirelessSetting: preSharedKey           = " + wifiConfiguration.preSharedKey);
        LogUtil.LogD(TAG, "setWirelessSetting: allowedAuthAlgorithms  = " + wifiConfiguration.allowedAuthAlgorithms);
        LogUtil.LogD(TAG, "setWirelessSetting: allowedGroupCiphers    = " + wifiConfiguration.allowedGroupCiphers);
        LogUtil.LogD(TAG, "setWirelessSetting: allowedKeyManagement   = " + wifiConfiguration.allowedKeyManagement);
        LogUtil.LogD(TAG, "setWirelessSetting: allowedPairwiseCiphers = " + wifiConfiguration.allowedPairwiseCiphers);
        LogUtil.LogD(TAG, "setWirelessSetting: allowedProtocols       = " + wifiConfiguration.allowedProtocols);
        int iAddNetwork = wifiManager.addNetwork(wifiConfiguration);
        if (iAddNetwork < 0) {
            LogUtil.LogE(TAG, "setWirelessSetting: WiFiManager.addNetwork() fail.");
            return false;
        }
        LogUtil.LogD(TAG, "setWirelessSetting: networkId = " + iAddNetwork + "  config.networkId = " + wifiConfiguration.networkId);
        if (!wifiManager.enableNetwork(iAddNetwork, true)) {
            LogUtil.LogE(TAG, "setWirelessSetting: enableNetwork fail." + wifiConfiguration.SSID);
            return false;
        }
        LogUtil.LogI(TAG, "setWirelessSetting: enableNetwork success. " + wifiConfiguration.SSID);
        if (!wifiManager.setWifiEnabled(true)) {
            LogUtil.LogE(TAG, "setWirelessSetting: setWiFiEnabled(true) fail. " + wifiConfiguration.SSID);
        } else {
            LogUtil.LogD(TAG, "setWirelessSetting: setWiFiEnabled(true) success. " + wifiConfiguration.SSID);
        }
        return true;
    }

    private static Class<?> getClassByName(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            LogUtil.LogE(TAG, "getClassByName: " + str + " fail.", e);
            return null;
        }
    }

    public static Method getMethodByName(Class<?> cls, String str, Class<?>... clsArr) {
        try {
            return cls.getMethod(str, clsArr);
        } catch (NoSuchMethodException unused) {
            LogUtil.LogE(TAG, "getMethodByName: " + str + " fail.");
            return null;
        }
    }

    public static Object invokeMethod(Object obj, Method method, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            LogUtil.LogE(TAG, "invokeMethod: " + method.getName() + " fail.", e);
            return null;
        } catch (NullPointerException e2) {
            LogUtil.LogE(TAG, "invokeMethod: NullPointerException.", e2);
            return null;
        }
    }

    public static boolean invokeSetMethod(Object obj, Method method, Object... objArr) {
        try {
            method.invoke(obj, objArr);
            return true;
        } catch (IllegalAccessException | IllegalArgumentException e) {
            LogUtil.LogE(TAG, "invokeSetMethod: " + method.getName() + " fail.", e);
            return false;
        } catch (NullPointerException e2) {
            LogUtil.LogE(TAG, "invokeSetMethod: NullPointerException.", e2);
            return false;
        } catch (InvocationTargetException e3) {
            LogUtil.LogE(TAG, "invokeSetMethod: InvocationTargetException; cause = " + e3.getCause().getMessage());
            return false;
        }
    }

    private static boolean setField(Object obj, String str, Object obj2) {
        try {
            obj.getClass().getField(str).set(obj, obj2);
            return true;
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
            return false;
        }
    }
}
