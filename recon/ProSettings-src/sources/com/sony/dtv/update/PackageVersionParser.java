package com.sony.dtv.update;

import android.util.Log;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.MatchResult;

/* JADX INFO: loaded from: classes.dex */
class PackageVersionParser {
    public static final Integer PKG = 0;
    public static final Integer SONY_DTV = 1;
    private static final String TAG = "UpdateSessionManager";
    public int broadcastver;
    public int build;
    public boolean complete;
    public int download;
    public int factoryver;
    public String function;
    public int majorver;
    public int minorver;
    public String model_id;
    public int operation;
    public String region;
    public int release;
    public int revision;
    public int type_;
    public String update_id;

    PackageVersionParser() {
        clear();
    }

    PackageVersionParser(String str) throws Throwable {
        parse(str);
    }

    public static String toRegion(Integer num) {
        int iIntValue = num.intValue();
        if (iIntValue == 16640) {
            return "EU";
        }
        if (iIntValue == 16896) {
            return "PA";
        }
        if (iIntValue == 17152) {
            return "NA";
        }
        if (iIntValue == 17408) {
            return "BR";
        }
        if (iIntValue == 17664) {
            return "JP";
        }
        if (iIntValue == 17920) {
            return "CN";
        }
        if (iIntValue == 18432) {
            return "LA";
        }
        if (iIntValue == 18944) {
            return "CO";
        }
        if (iIntValue != 19200) {
            return iIntValue != 19456 ? "NO" : "TW";
        }
        return "HK";
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:55:0x00a0  */
    /* JADX WARN: Failed to clean up code after switch over string restore
    jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r0v0 int, still in use, count: 9, list:
      (r0v0 int) from 0x0007: IF  (r0v0 int) != (2128 int)  -> B:4:0x0009 A[HIDDEN]
      (r0v0 int) from 0x000b: IF  (r0v0 int) != (2224 int)  -> B:6:0x000d A[HIDDEN]
      (r0v0 int) from 0x000f: IF  (r0v0 int) != (2307 int)  -> B:8:0x0011 A[HIDDEN]
      (r0v0 int) from 0x0013: IF  (r0v0 int) != (2374 int)  -> B:10:0x0015 A[HIDDEN]
      (r0v0 int) from 0x0017: IF  (r0v0 int) != (2421 int)  -> B:12:0x0019 A[HIDDEN]
      (r0v0 int) from 0x001b: IF  (r0v0 int) != (2483 int)  -> B:14:0x001d A[HIDDEN]
      (r0v0 int) from 0x001f: IF  (r0v0 int) != (2497 int)  -> B:16:0x0021 A[HIDDEN]
      (r0v0 int) from 0x0023: IF  (r0v0 int) != (2545 int)  -> B:18:0x0025 A[HIDDEN]
      (r0v0 int) from 0x0027: IF  (r0v0 int) != (2691 int)  -> B:20:0x0029 A[HIDDEN]
    	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:164)
    	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:129)
    	at jadx.core.utils.InsnRemover.unbindInsn(InsnRemover.java:93)
    	at jadx.core.utils.InsnRemover.remove(InsnRemover.java:226)
    	at jadx.core.utils.InsnRemover.remove(InsnRemover.java:215)
    	at jadx.core.dex.visitors.regions.SwitchOverStringVisitor.replaceWithMergedSwitch(SwitchOverStringVisitor.java:355)
    	at jadx.core.dex.visitors.regions.SwitchOverStringVisitor.restoreSwitchOverString(SwitchOverStringVisitor.java:111)
    	at jadx.core.dex.visitors.regions.SwitchOverStringVisitor.visitRegion(SwitchOverStringVisitor.java:72)
    	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:140)
    	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterative(DepthRegionTraversal.java:47)
    	at jadx.core.dex.visitors.regions.SwitchOverStringVisitor.visit(SwitchOverStringVisitor.java:66)
     */
    public static int toRegionCode(String str) {
        byte b;
        if (iHashCode != 2128) {
            if (iHashCode != 2224) {
                if (iHashCode != 2307) {
                    if (iHashCode != 2374) {
                        if (iHashCode != 2421) {
                            if (iHashCode != 2483) {
                                if (iHashCode != 2497) {
                                    if (iHashCode != 2545) {
                                        if (iHashCode != 2691) {
                                            switch (str) {
                                                case "CN":
                                                    b = 5;
                                                    break;
                                                case "CO":
                                                    b = 7;
                                                    break;
                                                default:
                                                    b = -1;
                                                    break;
                                            }
                                        } else if (str.equals("TW")) {
                                            b = 9;
                                        } else {
                                            b = -1;
                                        }
                                    } else if (str.equals("PA")) {
                                        b = 1;
                                    } else {
                                        b = -1;
                                    }
                                } else if (str.equals("NO")) {
                                    b = 11;
                                } else {
                                    b = -1;
                                }
                            } else if (str.equals("NA")) {
                                b = 2;
                            } else {
                                b = -1;
                            }
                        } else if (str.equals("LA")) {
                            b = 6;
                        } else {
                            b = -1;
                        }
                    } else if (str.equals("JP")) {
                        b = 4;
                    } else {
                        b = -1;
                    }
                } else if (str.equals("HK")) {
                    b = 8;
                } else {
                    b = -1;
                }
            } else if (str.equals("EU")) {
                b = 0;
            } else {
                b = -1;
            }
        } else if (str.equals("BR")) {
            b = 3;
        } else {
            b = -1;
        }
        switch (b) {
            case 0:
                return 16640;
            case 1:
                return 16896;
            case 2:
                return 17152;
            case 3:
                return 17408;
            case 4:
                return 17664;
            case 5:
                return 17920;
            case 6:
                return 18432;
            case 7:
                return 18944;
            case 8:
                return 19200;
            case 9:
                return 19456;
            default:
                return 0;
        }
    }

    public void clear() {
        this.type_ = 0;
        this.build = 0;
        this.release = 0;
        this.download = 0;
        this.majorver = 0;
        this.minorver = 0;
        this.revision = 0;
        this.factoryver = 0;
        this.broadcastver = 0;
        this.operation = 0;
        this.region = "";
        this.function = "";
        this.update_id = "";
        this.model_id = "";
        this.complete = false;
    }

    /* JADX WARN: Code duplicated, block: B:42:0x01cf A[PHI: r9 r13
      0x01cf: PHI (r9v5 java.util.Scanner) = (r9v4 java.util.Scanner), (r9v9 java.util.Scanner) binds: [B:41:0x01cd, B:26:0x01b1] A[DONT_GENERATE, DONT_INLINE]
      0x01cf: PHI (r13v5 ??) = (r13v4 ??), (r13v19 ??) binds: [B:41:0x01cd, B:26:0x01b1] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v0 */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v2, types: [java.util.Scanner] */
    /* JADX WARN: Type inference failed for: r10v4, types: [java.util.Scanner] */
    /* JADX WARN: Type inference failed for: r13v19, types: [int] */
    /* JADX WARN: Type inference failed for: r13v2 */
    /* JADX WARN: Type inference failed for: r13v25 */
    /* JADX WARN: Type inference failed for: r13v26 */
    /* JADX WARN: Type inference failed for: r13v3 */
    /* JADX WARN: Type inference failed for: r13v4, types: [java.util.Scanner] */
    /* JADX WARN: Type inference failed for: r13v5 */
    /* JADX WARN: Type inference failed for: r13v6 */
    /* JADX WARN: Type inference failed for: r13v7 */
    public void parse(String str) throws Throwable {
        Scanner scanner;
        ?? scanner2;
        clear();
        if (str.startsWith("PKG")) {
            this.type_ = PKG.intValue();
            Scanner scanner3 = new Scanner(str);
            try {
                try {
                    scanner3.findInLine("(\\w{3})(\\d{1}).(\\d{1}).(\\d{1}).(\\d{2}).(\\d{2}).(\\d{1}).(\\d{2}).(\\d{4})(\\w{3})");
                    MatchResult matchResultMatch = scanner3.match();
                    this.build = Integer.parseInt(matchResultMatch.group(2));
                    this.release = Integer.parseInt(matchResultMatch.group(3));
                    this.download = Integer.parseInt(matchResultMatch.group(4));
                    this.majorver = Integer.parseInt(matchResultMatch.group(5));
                    this.minorver = Integer.parseInt(matchResultMatch.group(6));
                    this.revision = Integer.parseInt(matchResultMatch.group(7));
                    this.factoryver = Integer.parseInt(matchResultMatch.group(8));
                    this.broadcastver = Integer.parseInt(matchResultMatch.group(9));
                    this.region = matchResultMatch.group(10).substring(0, 2);
                    this.function = matchResultMatch.group(10).substring(2);
                    this.complete = true;
                } finally {
                    scanner3.close();
                }
            } catch (Exception e) {
                Log.e(TAG, "failed to parse PKG string", e);
            }
            return;
        }
        if (str.startsWith("sony_dtv")) {
            this.type_ = SONY_DTV.intValue();
            Scanner scanner4 = new Scanner(str);
            ?? r13 = 0;
            r13 = 0;
            r13 = 0;
            try {
                try {
                    scanner4.findInLine("(\\w{8})(\\p{XDigit}{12})_(\\p{XDigit}{8})_(\\p{Digit}{10})(\\p{Digit}{4})");
                    MatchResult matchResultMatch2 = scanner4.match();
                    this.update_id = matchResultMatch2.group(2);
                    this.model_id = matchResultMatch2.group(3);
                    scanner = new Scanner(this.model_id);
                    try {
                        scanner2 = new Scanner(matchResultMatch2.group(4));
                        try {
                            this.broadcastver = Integer.parseInt(matchResultMatch2.group(5));
                            scanner.findInLine("(\\p{XDigit}{2})(\\p{XDigit}{2})(\\p{XDigit}{4})");
                            MatchResult matchResultMatch3 = scanner.match();
                            this.operation = Integer.decode("0x" + matchResultMatch3.group(1)).intValue();
                            StringBuilder sb = new StringBuilder();
                            sb.append("0x");
                            sb.append(matchResultMatch3.group(2));
                            this.function = Integer.decode(sb.toString()).intValue() > 0 ? "B" : "A";
                            this.region = toRegion(Integer.decode("0x" + matchResultMatch3.group(3)));
                            scanner2.findInLine("(\\p{Digit}{1})(\\p{Digit}{1})(\\p{Digit}{1})(\\p{Digit}{2})(\\p{Digit}{2})(\\p{Digit}{1})(\\p{Digit}{2})");
                            MatchResult matchResultMatch4 = scanner2.match();
                            this.build = Integer.parseInt(matchResultMatch4.group(1));
                            this.release = Integer.parseInt(matchResultMatch4.group(2));
                            this.download = Integer.parseInt(matchResultMatch4.group(3));
                            this.majorver = Integer.parseInt(matchResultMatch4.group(4));
                            this.minorver = Integer.parseInt(matchResultMatch4.group(5));
                            this.revision = Integer.parseInt(matchResultMatch4.group(6));
                            r13 = Integer.parseInt(matchResultMatch4.group(7));
                            this.factoryver = r13;
                            this.complete = true;
                            if (scanner2 != 0) {
                                scanner2.close();
                            }
                            if (scanner != null) {
                                scanner.close();
                            }
                        } catch (Exception e2) {
                            e = e2;
                            r13 = scanner2;
                            Log.e(TAG, "failed to parse sony_dtv string", e);
                            if (r13 != 0) {
                                r13.close();
                            }
                            if (scanner != null) {
                                scanner.close();
                            }
                        } catch (Throwable th) {
                            th = th;
                            if (scanner2 != 0) {
                                scanner2.close();
                            }
                            if (scanner != null) {
                                scanner.close();
                            }
                            scanner4.close();
                            throw th;
                        }
                    } catch (Exception e3) {
                        e = e3;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    scanner2 = r13;
                }
            } catch (Exception e4) {
                e = e4;
                scanner = null;
            } catch (Throwable th3) {
                th = th3;
                scanner = null;
                scanner2 = 0;
            }
            scanner4.close();
            return;
        }
        Log.e(TAG, "this string is not a version string because there is no prefix.");
    }

    public String getPackageVersionString() {
        return String.format(Locale.US, "%1d%1d%1d%02d%02d%1d%02d", Integer.valueOf(this.build), Integer.valueOf(this.release), Integer.valueOf(this.download), Integer.valueOf(this.majorver), Integer.valueOf(this.minorver), Integer.valueOf(this.revision), Integer.valueOf(this.factoryver));
    }

    public String toString() {
        return toString(this.type_);
    }

    public String toString(int i) {
        if (!this.complete) {
            return "";
        }
        if (i == PKG.intValue()) {
            return String.format(Locale.US, "PKG%1d.%1d.%1d.%02d.%02d.%1d.%02d.%04d%s%s", Integer.valueOf(this.build), Integer.valueOf(this.release), Integer.valueOf(this.download), Integer.valueOf(this.majorver), Integer.valueOf(this.minorver), Integer.valueOf(this.revision), Integer.valueOf(this.factoryver), Integer.valueOf(this.broadcastver), this.region, this.function);
        }
        if (i == SONY_DTV.intValue()) {
            return String.format(Locale.US, "sony_dtv%s_%02x%02x%04x_%1d%1d%1d%02d%02d%1d%02d%04d", this.update_id, Integer.valueOf(this.operation), Integer.valueOf(!this.function.equals("A") ? 1 : 0), Integer.valueOf(toRegionCode(this.region)), Integer.valueOf(this.build), Integer.valueOf(this.release), Integer.valueOf(this.download), Integer.valueOf(this.majorver), Integer.valueOf(this.minorver), Integer.valueOf(this.revision), Integer.valueOf(this.factoryver), Integer.valueOf(this.broadcastver));
        }
        return "";
    }
}
