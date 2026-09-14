# BraviaHUD 🖥️

A lightweight macOS menu-bar remote control for **Sony Bravia Android TVs** via ADB + IRCC (DLNA).

![macOS](https://img.shields.io/badge/macOS-12+-blue) ![Swift](https://img.shields.io/badge/Swift-5.9-orange) ![License](https://img.shields.io/badge/License-MIT-green)

<p align="center">
  <img src="https://img.shields.io/badge/Target-TV%20KDL--43W800C-red" />
  <img src="https://img.shields.io/badge/Backend-ADB%20Wireless-yellow" />
  <img src="https://img.shields.io/badge/Mirror-scrcpy%20%2B%20HLS-purple" />
</p>

---

## ✨ Features

### 🎮 Remote Control
- **D-pad** navigation (Up / Down / Left / Right / OK)
- **Player controls** — Previous, Play/Pause, Next
- **Volume** — Up / Down / Mute
- **Home / Back / Menu** buttons
- **Minimise** (hide BraviaHUD to Dock)

### 📺 Screen Mirroring
| Direction | Method | Latency | Control |
|-----------|--------|---------|---------|
| **TV → Mac** | scrcpy | <1s | ✅ Mouse = touch, keyboard = keys |
| **Mac → TV** | ffmpeg HLS → VLC | ~3s | Video only (desktop share) |

One-click toggle buttons in the toolbar — start/stop either mirror.

### 🔧 System Tools
| Button | What it does |
|--------|-------------|
| **✨4K Mode** | Opens Settings → Display → Picture → Advanced (user adjusts Reality Creation) |
| **Clean RAM** | Kills background apps, force-stops third-party packages |
| **Preview** | Captures current TV screen to Mac (screencap) |

### 📁 File Transfer
Drag-and-drop any file onto BraviaHUD → saved to **`/sdcard/mac-Hud/`** on TV (ADB push).

### ⌨️ Keyboard Passthrough
Focus BraviaHUD → type on Mac keyboard → characters sent to TV (`input text`).

### 📋 Clipboard Sync
Copy text on Mac → tap **Paste to TV** → pasted into the active text field.

---

## 🚀 Quick Start

### Prerequisites
- macOS 12+ (Apple Silicon or Intel)
- **ADB** installed (`brew install android-platform-tools`)
- **scrcpy** installed (`brew install scrcpy`)
- **ffmpeg** installed (`brew install ffmpeg`)
- Sony Bravia Android TV on the **same LAN**

### 1. Enable ADB on your TV
```
Settings → Device Preferences → About → Build (tap 7 times)
Settings → Device Preferences → Developer Options → USB debugging → ON
```

### 2. Connect wirelessly
```bash
adb connect 192.168.x.x:5555
```

### 3. Configure BraviaHUD
Open `BraviaHUD.app` → paste your TV's IP and PSK (for IRCC) → click **Connect**.

Default PSK location: `Settings → Network → Home network → IP control → Authentication → Normal and predefined credential`

---

## 📦 Build from Source

```bash
git clone https://github.com/saaedimam/BraviaHud.git
cd BraviaHud
xcrun swiftc -O -o /Applications/BraviaHUD.app/Contents/MacOS/BraviaHUD main.swift
```

Or open in Xcode and Build (⌘B).

---

## 📂 Project Structure

```
BraviaHud/
├── main.swift            # Full app: AdbSession, BraviaService, SwiftUI UI
├── screenstream.py       # HTTP server for Mac→TV HLS mirror (used by app)
├── README.md
└── .gitignore
```

---

## 🎯 Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      BraviaHUD.app                         │
│                    (macOS Menu Bar)                         │
├──────────┬───────────────┬──────────────────────────────────┤
│ AdbSession│ BraviaService │           SwiftUI UI            │
│  (adb shell│ (IRCC + ADB  │  ┌──────┬──────┬──────┬──────┐  │
│   muxer)  │  commands)    │  │DPad │Player│Mirror│Tools │  │
└──────┬───┴───────┬───────┘  └──────┴──────┴──────┴──────┘  │
       │           │                                          │
       ▼           ▼                                          │
   ADB Wireless   Sony IRCC (SOAP/XML)                       │
   port 5555      port 80                                     │
       │           │                                          │
       ▼           ▼                                          │
┌─────────────────────────────────────────────────────────────┐
│              Sony Bravia Android TV                        │
│              192.168.x.x                                    │
└─────────────────────────────────────────────────────────────┘
```

---

## ⚠️ Requirements

| Item | Value |
|------|-------|
| TV | Sony Bravia Android TV (2015+ with ADB support) |
| TV OS | Android 8.0+ (tested on KDL-43W800C) |
| ADB | v37+ (wireless) |
| scrcpy | v3.x (for TV→Mac mirror) |
| ffmpeg | v9+ with libx264 + avfoundation |
| Python | 3.9+ (for HLS HTTP server) |
| VLC on TV | Required for Mac→TV mirror playback |

---

## 🔒 Security Note

BraviaHUD connects over your **local LAN only**. IRCC commands use the TV's pre-shared key (PSK). No data leaves your network.

---

## 📝 License

MIT License — see [LICENSE](LICENSE) for details.

---

## 🙏 Credits

- [scrcpy](https://github.com/Genymobile/scrcpy) — screen mirroring engine
- [ffmpeg](https://ffmpeg.org/) — video capture & HLS encoding
- Sony IRCC protocol documentation
