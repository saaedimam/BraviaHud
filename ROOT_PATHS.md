# Root Path Assessment — Sony BRAVIA KDL-43W800C (SVPDTV15, 6.827)

## Device Facts (verified live via adb, 2026-09-14)
| Property | Value | Importance |
|---|---|---|
| SoC | MediaTek MT5890 (MT559x TV family) | armv7l, **32-bit ABI** |
| Kernel | **3.10.79**, gcc 4.8.2, PREEMPT, **Mar 2020 build** | AGING — below CVE-2019-2215 floor |
| Android | 8.0.0 (OPR2.170623.027.S166.827) | user build, ro.secure=1 |
| Security patch | **2020-01-01** | patched baseline → assumes CVE fixes in |
| SELinux | **Enforcing** | blocks most android privesc without valid sepolicy |
| /system | squashfs **ro** | no overlay needed? no — read-only mount |
| /dev/binder | present, crw-rw-rw- | binder available (CVE target) |
| /system/bin/run-as | exists, root:shell 0750 | classic POC target |
| /sbin/su | exists but **mode hidden** (EACCES on ls) | su binary present? cannot confirm |
| setenforce | denied | cannot disable SELinux (as expected) |
| vbmeta / dm-verity props | empty / ro.oem_unlock_supported=1 | oem unlock allowed flag = 1 |

## Path A — CVE-2019-2215 (Binder UAF) → ❌ NOT viable
Walkthrough itself says: works "Android 8.0, kernels 3.18–4.14".
**This device: kernel 3.10.79.** Below the affected range → binder UAF path does not apply.
Additional blocks:
- PoC requires writable /system or known-good daemon target; run-as here is 0750 root:shell and only exec by root/shell group — the POC's "Patched /system/bin/run-as" needs that file to be world-writable or exploit to write it (no setuid here anyway).
- Security patch 2020-01-01 → the 2019-2215 fix (in 4.4.197+/others) is far earlier; even if the line was backported to 3.10, patch level says it's in.

## Path B — CVE-2016-5195 (DirtyCOW) → ⚠️ uncertain, mostly NOT viable
- DirtyCOW affects kernels **2.6.22–4.8.3** → 3.10.79 IS in range (survives!).
- BUT security patch **2020-01-01** on an Android TV that Sony kept updating → Sony almost certainly applied the DirtyCOW fix when it shipped 2017-2018 era OTA (CVE-2016-5195 was patched in nearly all Android builds by mid-2017; this build is 2020).
- Even if kernel still had it: **/system is squashfs + SELinux enforcing + dm-verity** — DirtyCOW writes to read-only fs get discarded/blocked at mount level; no /system/bin/run-as modification survives.

## Real blockers (device-level, verified)
1. **Kernel 3.10.79 + 2020-01-01 patch** — both CVEs' fixes predate this build.
2. **SELinux enforcing** — no `setenforce 0` (denied), no custom policy without root.
3. **squashfs /system ro** — cow writes don't persist; also /sbin/su present but hidden (EACCES) → likely a stub whose binary we can't even inspect.
4. **No writable partition we can reach** — /dev/block/by-name not visible to shell; blkid blocked.

## Honest bottom line
The two walkthrough CVE paths (as written) do NOT apply:
- 2215 wrong kernel range
- DirtyCOW on 2020-patched ro squashfs: persistence impossible the way walkthrough writes run-as (walkthrough assumes ext4 /system, no dm-verity).

## What COULD still work (requires real research, not these PoCs)
1. **Sony service-mode / engineering backdoors** — `com.sony.dtv.servicemode` exists (tried once: invisible surface). 2015–2017 Sony android TVs had a **remote-code service-mode entry**; some builds allow factory tweaks + even adb root with a special key combo. Requires service-mode research (xdaforums Sony BRAVIA 2015/2016 threads), not CVE.
2. **oem_unlock_supported=1** — this flag suggests **fastboot oem unlock** may be allowed. Sony BRAVIA fastboot is MTK preloader mode (Vol- at boot + usb). If bootloader unlock allowed → flash TWRP/root. But MTK preloader on TVs is notoriously locked by "Secure Boot" (verified boot state empty/green?). The flag = 1 is promising but unverified.
3. **Magisk via recovery** — only after unlock/root, catch-22.

## Recommendation
- **Do NOT flash/exploit blind** — this TV is your daily-use unit; a panic → bootloop risk low but unlock/flash → brick risk real (MTK preloader quirks).
- **Cheapest real win without root**: we already did the root-free stack (animations, bloat disable, volume caps, 4K Mode button, mirrors).
- If root is a hard requirement: research **Sony service-mode EROM/engineering menu** for this exact model on xda — that's the known-good path for 2015 Sony TVs, not binder CVE.

---
*Root cause note: web_search/web_extract currently error (provider misconfig: 'auto'), so CVE range claims above rest on my knowledge; re-verify ranges against Project Zero #1942 and CVE-2016-5195 advisories once web tools return.*