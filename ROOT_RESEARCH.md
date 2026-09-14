# Root Research: Sony BRAVIA KDL-43W800C (SVP-DTV15, 6.827)
**Date:** 2026-09-14  
**Kernel:** 3.10.79, armv7l, security_patch 2020-01-01  
**Status:** Read-only reconnaissance — no destructive actions taken

---

## Verified Facts (live ADB)

| Check | Result |
|---|---|
| Shell user | uid=2000(shell), SELinux context `u:r:shell:s0` |
| SELinux | Enforcing (cannot setenforce 0) |
| /system | squashfs ro (mmcblk0p44) |
| /data | ext4 rw (mmcblk0p45) |
| /perm | ext4 ro (mmcblk0p44, context u:object_r:perm_fs:s0) |
| debugfs | rw, mode=755 but shell gets EACCES |
| /dev/binder | present (crw-rw-rw- root:system) |
| ro.oem_unlock_supported | 1 |
| ro.boot.verifiedbootstate | empty |
| setenforce 0 | Permission denied |

## ServiceModeActivity — ENTERS MTK SPECIAL MODE

Launched via:
```
am start -n com.sony.dtv.servicemode/.ServiceModeActivity
```

- Activity resumes (confirmed via dumpsys)
- MediaTek special mode 2 ACTIVATED (logcat: `notifySpecialModeChange: mode 2, status 1`)
- UI invisible (MediaTek OSD plane, not Android SurfaceView)
- Key combos (F1=183, F3=185, F4=186) not reachable via `input keyevent`

## Privileged Packages (system uid)

| Package | Path | Key Permissions |
|---|---|---|
| com.sony.dtv.b2b.prosettings | priv-app/ProSettings | WRITE_SECURE_SETTINGS, REBOOT, MASTER_CLEAR |
| com.sony.dtv.b2b.vendorprotocol | priv-app/VendorProtocol | MTPF, BINUPDATE |
| com.sony.dtv.b2b.rs232csupport | priv-app/Rs232cSupport | serial control |
| com.sony.dtv.b2b.noderuntime.privilege | priv-app/NodeRuntimePrivilege | elevated JS runtime |
| com.mediatek.dmagent | vendor/app/DMRemoteserviceagent | MTK firmware agent |

## B2B Command Channel (ProSettings)

B2bcmdManager exposes whitelisted commands via bridge files in `/data/vendor/b2b/`:
- `eeprom_read` — read TV EEPROM
- `decrypt_phrase` — decrypt stored phrase
- `prepare_fwwrite` / `cleanup_fwwrite` — firmware write
- `export_start` / `import_start` — USB export/import
- `tuner_disable` / `tuner_enable`

**BLOCKED:** `/data/vendor/b2b/` not accessible from shell (SELinux).

## Firmware Update Path (VendorProtocol)

VendorProtocolService handles firmware updates:
- Uses MTK DM agent (DMRemoteServiceAgent) for communication
- SsipProcess.java — Sony Service Interface Protocol
- NodeProcess.java — embedded Node.js runtime
- ManageActivity — internal (not exported)

**KEY QUESTION:** Can the firmware update be triggered from a local file on USB/SD card?

## What We CAN Do From Shell

1. ✅ `settings put secure/global ...` (WRITE_SECURE implicit)
2. ✅ `am start` privileged activities (some work, some crash)
3. ✅ `am broadcast` to system receivers
4. ❌ `setenforce 0` — blocked by SELinux
5. ❌ Write to `/system` — squashfs ro
6. ❌ Write to `/data/vendor/` — SELinux blocked
7. ❌ Trigger firmware write — bridge file path inaccessible

## Attack Vectors Under Investigation

### Path 1: Service Mode → MTK Debug Interface
ServiceModeActivity enters MTK special mode 2. If the MTK middleware exposes a debug shell or serial interface in this mode, that's the path.

**Status:** Mode confirmed active. Next step: probe MTK middleware for debug endpoints.

### Path 2: Firmware Update via USB
VendorProtocol + DMAgent may accept firmware files from USB. If we can craft or modify a firmware package...

**Status:** Need to pull more APKs and analyze firmware update protocol.

### Path 3: RS232 Serial Control
Rs232cSupport priv-app may expose serial port commands. Some Sony TVs have debug serial ports.

**Status:** Need to decompile Rs232Support.apk and check for UART/debug commands.

### Path 4: Node Runtime Privilege
NodeRuntimePrivilege runs JavaScript in a privileged context. If we can inject code...

**Status:** Need to find how it's invoked and what APIs it exposes.

---

*This file is read-only research. No destructive operations have been performed.*
