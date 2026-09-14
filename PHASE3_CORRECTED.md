# PHASE 3 CORRECTED — ProSettingsService Binder: VERIFIED SURFACE & UNVERIFIED ROOT

**Status:** Interface verified by decompilation. Root-daemon execution **NOT verified** (TV offline).

---

## ✅ VERIFIED (decompiled IProSettingsService.java from ProSettings.apk)

### Binder interface: `com.sony.dtv.b2b.prosettings.IProSettingsService`
**Service exported=true, permission=WRITE_SECURE_SETTINGS** (shell has this implicitly)

### Transaction map (exact codes for `service call`):
```
1  processRequest(String, callback, int)
2  registerCallback(String, callback, int)
3  unregisterCallback(callback)
4  processRequestSync(String) -> String
5  disableAppPackageIfEnabled(String)
6  getAppPackageEnabled(String)
7  enableAppPackageIfChanged(String)
8  disableAppPackage(String)
9  enableAppPackage(String)
10 setAppPackageStateSetting(String, int, int)
11 getAppPackageStateSetting(String)
13 getAppComponentStateSetting(String, String)
15 getAppHiddenSettingAsUser(String)
28 installLogo(String) -> boolean
29 uninstallLogo() -> boolean
30 decryptPhrase(String, String) -> String
31 prepareFwWrite() -> String
32 cleanupFwWrite()
34 cancelFwUpdate()
36 getDeviceId() -> String
37 getPackageName() -> String
38 getSoftwareVersionDisplay() -> String
39 getIpControlPSK() -> String
40 getSoftwareUpgradeStatus() -> int
42 getIpControlAuthentication() -> int
43 set4KBEGeneralControlCommand(...)
44 get4KBEGeneralControlCommand()
45 getMtkHotelMode()
46 clearAllAccount(callback)
47 clearApplicationUserData(List, callback)
48 getDiagnosisInformation(callback)
```

### Permission check (the CRITICAL correction):
```java
private boolean isPermittedCaller() {
    String nameForUid = pm.getNameForUid(Binder.getCallingUid());
    if (nameForUid.equals(GV.PACKAGE_NAME_NODERUNTIME_NORMAL) ||
        nameForUid.equals(GV.PACKAGE_NAME_NODERUNTIME_PRIVILEGE))
        return true;
    return false;
}
```
- **`processRequest` (1), `processRequestSync` (4): GATED by isPermittedCaller()** — only NodeRuntime normal/privilege packages pass. **Shell (uid 2000) REJECTED.**

### UNGATED methods (NO caller check — shell-callable):
- `installLogo(String)` (28) → `new BootLogo().installLogo(str)`
- `uninstallLogo()` (29)
- `decryptPhrase(String, String)` (30) → `B2bcmdManager.execCmd(B2BCMD_DECRYPT_PHRASE, {key,data}, ...)`
- `prepareFwWrite()` (31) → `mAbupdate.prepareFwWrite()`
- `cleanupFwWrite()` (32)
- `enableAppPackage/disableAppPackage` (8/9) → `Package.enableAppPackage(str)` — **system-uid package control**
- `getIpControlPSK()` (39) — read PSK
- `getSoftwareUpgradeStatus()` (40)

---

## ⚠️ UNVERIFIED (needs device ON):

### B2bcmdManager write chain
- `B2B_CMD_DATA_ROOT` = `/data/vendor/b2b/` (if exists) else `/data/vendor/sony/b2b/`
- `decryptPhrase("k","d")` → system writes JSON to `/data/vendor/b2b/decrypt_phrase.tmp` (or sony/b2b/)
- Sets sysprop `sys.svp.b2bcmd.state`
- **Question:** Is there a root daemon polling that dir/sysprop that executes content? **NOT VERIFIED.**

### Local observation:
- `/data/vendor/b2b/` — **does not exist on this specific TV** (checked earlier, EACCES to /data/vendor)
- `/data/vendor/sony/b2b/` — **does not exist** (earlier) — this model may use a DIFFERENT implementation
- **The claimed `/vendor/tmp/odm/b2b/` + `/tmp/odm/b2b/` and `/tmp/b2b/` are CONSTANTS in decompile but NOT confirmed as actual runtime dirs on this build**

---

## VERDICT

| Claim in Phase 3 FINAL | Reality |
|---|---|
| "NO permission checks on processRequestSync" | ❌ **FALSE** — isPermittedCaller() gates it, shell rejected |
| processRequest/processRequestSync reachable from shell | ❌ **FALSE** — caller must be NodeRuntime pkg |
| /vendor/tmp/odm/b2b/ world-writable, root daemon executes | ⚠️ **UNVERIFIED** — dir not observed; TV offline |
| B2bcmdManager writes command files | ✅ **TRUE** — but via system uid to /data/vendor/*/b2b/ |
| Ungated decryptPhrase/installLogo/prepareFwWrite | ✅ **TRUE** — no caller check in decompiled code |
| Root code execution => CRITICAL LPE | ❌ **NOT PROVEN** — no daemon/exec primitive confirmed |

---

## HONEST NEXT ACTIONS (device ON required)

1. **Start ProSettingsService from shell** (works — WRITE_SECURE_SETTINGS)
2. **Bind + call transaction 30 `decryptPhrase("test","test")`** from shell:
   `service call ProSettingsService 4 s16 'key' s16 'data' s16 '0'` (needs exact arg layout — verify from service by watching logcat)
3. **Watch logcat** for: `B2bcmdManager: ...` + `sys.svp.b2bcmd.state` change + any root daemon activity
4. **Check if /data/vendor/sony/b2b/ materializes after the call** (system uid writes it)
5. **IF a root daemon executes content → THAT is the RCE. Otherwise decryptPhrase is only a system-uid write.**

# CONCLUSION
The Phase 3 doc's **exact exploit chain (shell → processRequestSync → root daemon) is not proven**.
The REAL verified surface is: **ungated decryptPhrase/installLogo/prepareFwWrite via system uid writes to /data/vendor/*/b2b/ + sysprop**. Whether any root daemon consumes those = the actual remaining question.