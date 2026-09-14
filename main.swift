import Cocoa
import SwiftUI
import Combine

class AdbSession {
    static let shared = AdbSession()
    private var process: Process?
    private var stdinPipe: Pipe?
    private let queue = DispatchQueue(label: "adb.session.queue", qos: .userInteractive)
    static func resolveADB() -> String {
        let candidates = [
            "/opt/homebrew/bin/adb",
            "/usr/local/bin/adb",
            "/usr/bin/adb",
            "/Users/\(NSUserName())/Library/Android/sdk/platform-tools/adb"
        ]
        for c in candidates where FileManager.default.isExecutableFile(atPath: c) { return c }
        // last resort: rely on PATH
        return "adb"
    }
    var adbPath: String = AdbSession.resolveADB()
    var tvIP: String = "192.168.0.100"

    init() {
        start()
    }

    func start() {
        queue.async { [weak self] in
            guard let self = self else { return }
            self.stop()
            let p = Process()
            p.executableURL = URL(fileURLWithPath: self.adbPath)
            p.arguments = ["-s", "\(self.tvIP):5555", "shell"]
            let inPipe = Pipe()
            p.standardInput = inPipe
            p.standardOutput = Pipe()
            p.standardError = Pipe()
            do {
                try p.run()
                self.process = p
                self.stdinPipe = inPipe
            } catch {
                print("AdbSession error: \(error)")
            }
        }
    }

    func stop() {
        if let process = process, process.isRunning {
            process.terminate()
        }
        process = nil
        stdinPipe = nil
    }

    func sendCommand(_ cmd: String) {
        queue.async { [weak self] in
            guard let self = self else { return }
            if self.process == nil || !(self.process?.isRunning ?? false) {
                self.start()
                usleep(50000)
            }
            if let handle = self.stdinPipe?.fileHandleForWriting,
               let data = (cmd + "\n").data(using: .utf8) {
                handle.write(data)
            }
        }
    }

    func pushFile(_ localPath: String, to remoteDir: String = "/sdcard/mac-Hud/",
                  completion: ((Bool, String) -> Void)? = nil) {
        queue.async { [weak self] in
            guard let self = self else { return }
            self.stop()
            usleep(50000)

            // Ensure remote folder exists
            let mkdir = Process()
            mkdir.executableURL = URL(fileURLWithPath: self.adbPath)
            mkdir.arguments = ["-s", "\(self.tvIP):5555", "shell", "mkdir", "-p", remoteDir]
            do { try mkdir.run(); mkdir.waitUntilExit() } catch {}

            let push = Process()
            push.executableURL = URL(fileURLWithPath: self.adbPath)
            push.arguments = ["-s", "\(self.tvIP):5555", "push", localPath, remoteDir]
            let pipe = Pipe()
            push.standardOutput = pipe
            push.standardError = pipe
            do {
                try push.run()
                push.waitUntilExit()
                let data = pipe.fileHandleForReading.readDataToEndOfFile()
                let output = String(data: data, encoding: .utf8) ?? ""
                let ok = push.terminationStatus == 0
                DispatchQueue.main.async {
                    completion?(ok, output)
                }
            } catch {
                DispatchQueue.main.async {
                    completion?(false, "\(error)")
                }
            }

            self.start() // re-establish shell session
        }
    }
}

class BraviaService: ObservableObject {
    @Published var tvIP: String = "192.168.0.100" { didSet { saveSettings() } }
    @Published var psk: String = "3404" { didSet { saveSettings() } }
    @Published var isConnected: Bool = true
    @Published var statusMessage: String = "Ready"
    
    // Persist settings
    private let defaults = UserDefaults.standard
    func saveSettings() {
        defaults.set(tvIP, forKey: "tvIP")
        defaults.set(psk, forKey: "psk")
    }
    @Published var screenImage: NSImage? = nil
    @Published var isCapturing: Bool = false
    @Published var gestureFeedback: String = "Swipe, Tap or Type"

    static let shared = BraviaService()

    private let irccCodes: [String: String] = [
        "Up": "AAAAAQAAAAEAAAB0Aw==",
        "Down": "AAAAAQAAAAEAAAB1Aw==",
        "Left": "AAAAAQAAAAEAAAA0Aw==",
        "Right": "AAAAAQAAAAEAAAAzAw==",
        "Confirm": "AAAAAQAAAAEAAABlAw==",
        "Home": "AAAAAQAAAAEAAABgAw==",
        "Return": "AAAAAgAAAJcAAAAjAw==",
        "Options": "AAAAAgAAAJcAAAA2Aw==",
        "ActionMenu": "AAAAAgAAAMQAAABLAw==",
        "VolumeUp": "AAAAAQAAAAEAAAASAw==",
        "VolumeDown": "AAAAAQAAAAEAAAATAw==",
        "Mute": "AAAAAQAAAAEAAAAUAw==",
        "TvPower": "AAAAAQAAAAEAAAAVAw==",
        "Play": "AAAAAgAAAJcAAAAaAw==",
        "Wide": "AAAAAgAAAKQAAAA9Aw==",
        "PicOff": "AAAAAQAAAAEAAAA+Aw==",
        "PictureMode": "AAAAAQAAAAEAAABkAw==",
        "Display": "AAAAAQAAAAEAAAA6Aw=="
    ]

    init() {
        checkHealth()
    }

    func sendIRCC(_ command: String) {
        guard let code = irccCodes[command] else {
            sendAdbKey(command)
            return
        }

        guard let url = URL(string: "http://\(tvIP)/sony/ircc") else { return }
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.timeoutInterval = 1.0
        request.setValue(psk, forHTTPHeaderField: "X-Auth-PSK")
        request.setValue("text/xml; charset=UTF-8", forHTTPHeaderField: "Content-Type")
        request.setValue("\"urn:schemas-sony-com:service:IRCC:1#X_SendIRCC\"", forHTTPHeaderField: "SOAPACTION")

        let xml = "<?xml version=\"1.0\"?><s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:X_SendIRCC xmlns:u=\"urn:schemas-sony-com:service:IRCC:1\"><IRCCCode>\(code)</IRCCCode></u:X_SendIRCC></s:Body></s:Envelope>"
        request.httpBody = xml.data(using: .utf8)

        URLSession.shared.dataTask(with: request) { [weak self] _, response, _ in
            DispatchQueue.main.async {
                if let http = response as? HTTPURLResponse, http.statusCode == 200 {
                    self?.isConnected = true
                    self?.statusMessage = "\(command)"
                } else {
                    self?.sendAdbKey(command)
                }
            }
        }.resume()
    }

    func sendAdbKey(_ key: String) {
        let keyMap: [String: String] = [
            "Up": "19", "Down": "20", "Left": "21", "Right": "22",
            "Confirm": "66", "Home": "3", "Return": "4", "Back": "4", "Minimize": "187", "AppSwitch": "187",
            "Menu": "82", "VolumeUp": "24", "VolumeDown": "25",
            "Mute": "164", "Play": "126", "Pause": "127", "PlayPause": "85", "Stop": "86", "Next": "87", "Prev": "88",
            "Copy": "278", "Paste": "279", "Cut": "277", "SelectAll": "29",
            "Backspace": "67", "Space": "62", "Enter": "66", "Tab": "61"
        ]
        let code = keyMap[key] ?? key
        AdbSession.shared.sendCommand("input keyevent \(code)")
    }

    //Video Full Screen Display Controls
        func toggleFullScreenVideo(){
            //Launch fullscreen video player overlay use screen compatibility
            AdbSession.shared.sendCommand("settings put global hide_error_dialogs 1")
            sendIRCC("Wide")
            statusMessage = "Fullscreen Toggled"
        }

        
    private func resolveBinary(_ names: [String]) -> String? {
        for name in names {
            let c = "/opt/homebrew/bin/\(name)"
            if FileManager.default.isExecutableFile(atPath: c) { return c }
            let c2 = "/usr/local/bin/\(name)"
            if FileManager.default.isExecutableFile(atPath: c2) { return c2 }
        }
        let which = Process()
        which.executableURL = URL(fileURLWithPath: "/usr/bin/which")
        which.arguments = ["-a"] + names
        let pipe = Pipe()
        which.standardOutput = pipe
        try? which.run()
        let data = pipe.fileHandleForReading.readDataToEndOfFile()
        if let line = String(data: data, encoding: .utf8)?.split(separator: "\n").first {
            return String(line)
        }
        return nil
    }

    // MARK: - Mirroring (Mac → TV via ffmpeg HLS + VLC; TV → Mac via scrcpy)
        private var macToTVProcess: Process?
        private var httpServerProcess: Process?
        private var scrcpyProcess: Process?

        func startMacToTV() {
            stopMacToTV()
            let macIP = localIPAddress() ?? "192.168.0.206"
            let hlsDir = "/tmp/hlsout"
            try? FileManager.default.createDirectory(atPath: hlsDir, withIntermediateDirectories: true)

            // ffmpeg: capture screen -> HLS segments (libx264, 24fps, 2s)
            guard let ffmpeg = resolveBinary(["ffmpeg"]) else {
                statusMessage = "ffmpeg not found — run: brew install ffmpeg"; return
            }
            let ff = Process()
            ff.executableURL = URL(fileURLWithPath: ffmpeg)
            ff.arguments = ["-y", "-f", "avfoundation", "-capture_cursor", "1",
                            "-i", "Capture screen 0",
                            "-vf", "scale=1280:-2,fps=24",
                            "-c:v", "libx264", "-preset", "veryfast", "-b:v", "2500k",
                            "-x264opts", "keyint=30:min-keyint=30", "-g", "30",
                            "-preset", "veryfast", "-tune", "zerolatency",
                        "-hls_time", "1", "-hls_list_size", "4", "-hls_flags", "delete_segments",
                            "-f", "hls", "\(hlsDir)/live.m3u8"]
            ff.standardOutput = Pipe()
            ff.standardError = Pipe()
            try? ff.run()
            macToTVProcess = ff

            // HTTP server for HLS dir
            let hs = Process()
            hs.executableURL = URL(fileURLWithPath: "/usr/bin/python3")
            hs.arguments = ["-m", "http.server", "8096", "--directory", hlsDir]
            hs.standardOutput = Pipe()
            hs.standardError = Pipe()
            try? hs.run()
            httpServerProcess = hs

            statusMessage = "Mirror Mac→TV: starting…"
            DispatchQueue.main.asyncAfter(deadline: .now() + 6) { [weak self] in
                guard let self = self else { return }
                AdbSession.shared.sendCommand("am start -a android.intent.action.VIEW -d http://\(macIP):8096/live.m3u8 -t application/x-mpegURL")
                self.statusMessage = "Mirror Mac→TV LIVE"
            }
        }

        func stopMacToTV() {
            macToTVProcess?.terminate()
            macToTVProcess = nil
            httpServerProcess?.terminate()
            httpServerProcess = nil
            statusMessage = "Mirror Mac→TV stopped"
        }

        func startTVToMac() {
            stopTVToMac()
            guard let scrcpy = resolveBinary(["scrcpy"]) else {
                statusMessage = "scrcpy not found — run: brew install scrcpy"; return
            }
            let sc = Process()
            sc.executableURL = URL(fileURLWithPath: scrcpy)
            sc.arguments = ["--serial", "\(tvIP):5555", "--stay-awake", "--no-audio", "--max-size=1280", "--video-bit-rate=4M"]
            sc.standardOutput = Pipe()
        var env = ProcessInfo.processInfo.environment
        env["PATH"] = "/opt/homebrew/bin:/usr/local/bin:/usr/bin:/bin:" + (env["PATH"] ?? "")
        sc.environment = env

            sc.standardError = Pipe()
            try? sc.run()
            scrcpyProcess = sc
            statusMessage = "TV→Mac mirror (scrcpy) started"
        }

        func stopTVToMac() {
            scrcpyProcess?.terminate()
            scrcpyProcess = nil
            // also kill any adb-forwards from scrcpy server
            AdbSession.shared.sendCommand("exit")
        }

        func isMacToTVRunning() -> Bool { macToTVProcess != nil }
        func isTVToMacRunning() -> Bool { scrcpyProcess != nil }

        private func localIPAddress() -> String? {
            var addr: UnsafeMutablePointer<ifaddrs>?
            guard getifaddrs(&addr) == 0 else { return nil }
            var result: String?
            while let a = addr {
                let family = a.pointee.ifa_addr.pointee.sa_family
                if family == UInt8(AF_INET) {
                    let host = String(cString: inet_ntoa(a.pointee.ifa_addr.withMemoryRebound(to: sockaddr_in.self, capacity: 1) { $0.pointee.sin_addr }))
                    if String(cString: a.pointee.ifa_name) == "en0" || String(cString: a.pointee.ifa_name) == "en1" {
                        if !host.hasPrefix("127.") { result = host }
                    }
                }
                addr = a.pointee.ifa_next
            }
            freeifaddrs(nil)
            return result
        }

    func copyText() {
        AdbSession.shared.sendCommand("input keyevent 278")
        statusMessage = "Copied"
    }

    func selectAll() {
        AdbSession.shared.sendCommand("input keyevent 29 --meta 113")
        statusMessage = "Selected All"
    }

    func pasteText() {
        AdbSession.shared.sendCommand("input keyevent 279")
        statusMessage = "Pasted"
    }

    func cutText() {
        AdbSession.shared.sendCommand("input keyevent 277")
        statusMessage = "Cut"
    }

    func pasteMacClipboard() {
        guard let clipString = NSPasteboard.general.string(forType: .string), !clipString.isEmpty else {
            statusMessage = "Mac Clipboard Empty"
            return
        }
        sendText(clipString)
        statusMessage = "Pasted Mac"
    }

    func clearField() {
        AdbSession.shared.sendCommand("input keyevent 29 --meta 113; sleep 0.05; input keyevent 67")
        statusMessage = "Cleared"
    }

    func sendChar(_ char: Character) {
        if char == " " {
            AdbSession.shared.sendCommand("input keyevent 62")
            return
        }
        let s = String(char)
        let escaped = s.replacingOccurrences(of: "&", with: "\\&")
            .replacingOccurrences(of: ";", with: "\\;")
            .replacingOccurrences(of: "\"", with: "\\\"")
            .replacingOccurrences(of: "'", with: "\\'")
        AdbSession.shared.sendCommand("input text \(escaped)")
        DispatchQueue.main.async {
            self.statusMessage = "Key: '\(char)'"
        }
    }

    func tapTV(x: Int = 960, y: Int = 540) {
        AdbSession.shared.sendCommand("input tap \(x) \(y)")
        DispatchQueue.main.async {
            self.gestureFeedback = "Tap (\(x), \(y))"
            self.statusMessage = "Tapped"
        }
    }

    func scrollTV(direction: String) {
        switch direction {
        case "up":
            AdbSession.shared.sendCommand("input swipe 960 750 960 250 150")
            gestureFeedback = "Scrolled Down"
        case "down":
            AdbSession.shared.sendCommand("input swipe 960 250 960 750 150")
            gestureFeedback = "Scrolled Up"
        case "left":
            AdbSession.shared.sendCommand("input swipe 1400 540 500 540 150")
            gestureFeedback = "Scrolled Right"
        case "right":
            AdbSession.shared.sendCommand("input swipe 500 540 1400 540 150")
            gestureFeedback = "Scrolled Left"
        default:
            break
        }
    }

    func launchApp(_ pkg: String, restart: Bool = false) {
        if restart {
            AdbSession.shared.sendCommand("am force-stop \(pkg)")
        }
        AdbSession.shared.sendCommand("monkey \(pkg) android.intent.category.LAUNCHER 1")
        statusMessage = restart ? "Restarted \(pkg)" : "Launched \(pkg)"
    }

    func closeAllApps() {
        let script = "for pkg in $(pm list packages -3 | cut -d: -f2); do force-stop $pkg; done; kill-all; input keyevent 3"
        AdbSession.shared.sendCommand(script)
        statusMessage = "RAM Boosted / All Closed"
    }

    func sendText(_ text: String) {
        let escaped = text.replacingOccurrences(of: " ", with: "%s")
            .replacingOccurrences(of: "&", with: "\\&")
            .replacingOccurrences(of: ";", with: "\\;")
            .replacingOccurrences(of: "\"", with: "\\\"")
            .replacingOccurrences(of: "'", with: "\\'")
        AdbSession.shared.sendCommand("input text \(escaped)")
        statusMessage = "Sent text"
    }

    func captureScreen() {
        isCapturing = true
        DispatchQueue.global(qos: .userInitiated).async { [weak self] in
            guard let self = self else { return }
            let process = Process()
            process.executableURL = URL(fileURLWithPath: AdbSession.shared.adbPath)
            process.arguments = ["-s", "\(self.tvIP):5555", "exec-out", "screencap", "-p"]
            let pipe = Pipe()
            process.standardOutput = pipe
            do {
                try process.run()
                let data = pipe.fileHandleForReading.readDataToEndOfFile()
                process.waitUntilExit()
                if let image = NSImage(data: data) {
                    DispatchQueue.main.async {
                        self.screenImage = image
                        self.isCapturing = false
                        self.statusMessage = "Screenshot OK"
                    }
                    return
                }
            } catch {}
            DispatchQueue.main.async {
                self.isCapturing = false
                self.statusMessage = "Capture failed"
            }
        }
    }

    func checkHealth() {
        guard let url = URL(string: "http://\(tvIP)/sony/system") else { return }
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.timeoutInterval = 1.0
        request.setValue(psk, forHTTPHeaderField: "X-Auth-PSK")
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = "{\"method\":\"getPowerStatus\",\"params\":[],\"id\":1,\"version\":\"1.0\"}".data(using: .utf8)

        URLSession.shared.dataTask(with: request) { [weak self] _, response, _ in
            DispatchQueue.main.async {
                self?.isConnected = (response as? HTTPURLResponse)?.statusCode == 200
            }
        }.resume()
    }

    func pushFile(_ path: String) {
        AdbSession.shared.pushFile(path) { [weak self] ok, output in
            DispatchQueue.main.async {
                self?.statusMessage = ok
                    ? "Pushed \(URL(fileURLWithPath: path).lastPathComponent) → /sdcard/mac-Hud/"
                    : "Push failed: \(output.prefix(80))"
            }
        }
    }

    // MARK: - 4K Look (lands user at Picture → Advanced settings, Reality Creation)
    // Runs ONLY from Home/launcher: opens Android Settings, Display → Picture → Advanced,
    // then stops — user adjusts Reality Creation / sharpness visually (OSD nav is fragile).
    func openRealityCreation() {
        let script = """
        am start -a android.settings.SETTINGS
        sleep 2.5
        input tap 1344 500
        sleep 2
        input tap 1344 230
        sleep 2
        input tap 1344 910
        sleep 2.5
        """
        AdbSession.shared.sendCommand(script)
        statusMessage = "4K Look: Picture → Advanced open"
    }
}

// MARK: Touchpad Gesture Surface
struct TouchpadSurfaceView: View {
    @ObservedObject var service = BraviaService.shared
    @State private var touchLocation: CGPoint = .zero
    @State private var isTouching: Bool = false

    let width: CGFloat = 250
    let height: CGFloat = 135

    var body: some View {
        VStack(spacing: 7) {
            ZStack {
                RoundedRectangle(cornerRadius: 12)
                    .fill(LinearGradient(
                        colors: [Color.black.opacity(0.6), Color.blue.opacity(0.15)],
                        startPoint: .topLeading,
                        endPoint: .bottomTrailing
                    ))
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(isTouching ? Color.blue : Color.white.opacity(0.2), lineWidth: 1.5)
                    )

                Path { path in
                    path.move(to: CGPoint(x: width/2, y: 15))
                    path.addLine(to: CGPoint(x: width/2, y: height - 15))
                    path.move(to: CGPoint(x: 20, y: height/2))
                    path.addLine(to: CGPoint(x: width - 20, y: height/2))
                }
                .stroke(Color.white.opacity(0.08), lineWidth: 1)

                if isTouching {
                    Circle()
                        .fill(Color.blue.opacity(0.8))
                        .frame(width: 24, height: 24)
                        .position(touchLocation)
                }

                VStack {
                    HStack {
                        Image(systemName: "hand.point.up.fill")
                            .font(.system(size: 11))
                            .foregroundColor(.blue)
                        Text("GESTURE TOUCHPAD")
                            .font(.system(size: 9, weight: .bold, design: .rounded))
                            .foregroundColor(.gray)
                        Spacer()
                        Text(service.gestureFeedback)
                            .font(.system(size: 8, weight: .regular, design: .monospaced))
                            .foregroundColor(Color(red: 0.1, green: 0.8, blue: 1.0))
                    }
                    .padding(.horizontal, 10)
                    .padding(.top, 8)

                    Spacer()

                    Text("Swipe ⬆⬇⬅➡ to scroll • Tap to Select")
                        .font(.system(size: 8, weight: .medium))
                        .foregroundColor(Color.white.opacity(0.5))
                        .padding(.bottom, 6)
                }
            }
            .frame(width: width, height: height)
            .gesture(
                DragGesture(minimumDistance: 0)
                    .onChanged { value in
                        touchLocation = value.location
                        isTouching = true
                    }
                    .onEnded { value in
                        isTouching = false
                        let dx = value.translation.width
                        let dy = value.translation.height
                        let dist = hypot(dx, dy)

                        if dist < 8 {
                            service.sendIRCC("Confirm")
                            service.gestureFeedback = "Tapped (OK)"
                        } else if abs(dy) > abs(dx) {
                            if dy < -20 {
                                service.scrollTV(direction: "up")
                            } else if dy > 20 {
                                service.scrollTV(direction: "down")
                            }
                        } else {
                            if dx < -20 {
                                service.scrollTV(direction: "left")
                            } else if dx > 20 {
                                service.scrollTV(direction: "right")
                            }
                        }
                    }
            )

            HStack(spacing: 6) {
                Button(action: { service.sendIRCC("Confirm") }) {
                    Label("Click (OK)", systemImage: "hand.tap.fill")
                        .font(.system(size: 9, weight: .semibold))
                }
                .buttonStyle(ModernButtonStyle(tint: .blue))

                Button(action: { service.tapTV(x: 960, y: 540) }) {
                    Label("Center Tap", systemImage: "scope")
                        .font(.system(size: 9, weight: .semibold))
                }
                .buttonStyle(ModernButtonStyle(tint: .purple))

                Button(action: { service.sendIRCC("Return") }) {
                    Label("Back", systemImage: "arrow.uturn.backward")
                        .font(.system(size: 9, weight: .semibold))
                }
            }
        }
    }
}

// MARK: Main SwiftUI HUD View
struct RemoteHUDView: View {
    @ObservedObject var service = BraviaService.shared
    @State private var mode = 1
    @State private var inputText = ""
    private let bg = Color(red: 0.082, green: 0.086, blue: 0.102)
    private let raised = Color(red: 0.118, green: 0.125, blue: 0.145)
    private let line = Color(red: 0.165, green: 0.176, blue: 0.204)
    private let ink = Color(red: 0.945, green: 0.949, blue: 0.957)
    private let ink2 = Color(red: 0.608, green: 0.631, blue: 0.675)
    private let accent = Color(red: 0.2, green: 0.9, blue: 0.76)
    private let accentInk = Color(red: 0.03, green: 0.19, blue: 0.16)
    private let danger = Color(red: 1.0, green: 0.38, blue: 0.35)
    var body: some View {
        VStack(spacing: 0) {
            header
            modeSwitcher
            if mode == 1 { touchpad } else { dpad }
            navGrid
            Divider().background(line)
            videoControls
            Divider().background(line)
            clipboardSection
            Spacer()
            Button(action: { service.closeAllApps() }) {
                HStack {
                    Image(systemName: "power").font(.system(size: 14))
                    Text("Clean RAM / Force Stop").font(.system(size: 10))
                }
                .frame(maxWidth: .infinity)
                .padding(8)
                .background(danger.opacity(0.14))
                .foregroundColor(danger)
                .cornerRadius(8)
            }
            .buttonStyle(PlainButtonStyle())
            .padding(10)
        }
        .frame(width: 296, height: 780)
        .background(bg)
        .foregroundColor(ink)
    }
    private var header: some View {
        HStack {
            Circle().fill(service.isConnected ? accent : .red).frame(width: 7, height: 7)
            Text("Bravia HUD").font(.system(size: 13, weight: .semibold))
            Spacer()
            Text("192.168.0.42").font(.system(.caption2, design: .monospaced)).foregroundColor(ink2)
        }
        .padding(14)
    }
    private var modeSwitcher: some View {
        HStack(spacing: 6) {
            modeBtn(label: "Live", icon: "play.tv", tag: 1)
            modeBtn(label: "Touch", icon: "hand.tap", tag: 2)
            modeBtn(label: "D-pad", icon: "square.grid.3x3", tag: 3)
        }
        .padding(.horizontal, 14)
        .padding(.bottom, 10)
    }
    private func modeBtn(label: String, icon: String, tag: Int) -> some View {
        Button(action: { mode = tag }) {
            HStack(spacing: 4) {
                Image(systemName: icon).font(.system(size: 11))
                Text(label).font(.system(size: 9))
            }
            .frame(maxWidth: .infinity)
            .padding(7)
            .background(mode == tag ? accent : raised)
            .foregroundColor(mode == tag ? accentInk : ink2)
            .cornerRadius(8)
        }
        .buttonStyle(PlainButtonStyle())
    }
    private var dpad: some View {
        ZStack {
            Button(action: { service.sendIRCC("Up") }) { Image(systemName: "chevron.up").padding(12) }.offset(y: -40)
            Button(action: { service.sendIRCC("Down") }) { Image(systemName: "chevron.down").padding(12) }.offset(y: 40)
            Button(action: { service.sendIRCC("Left") }) { Image(systemName: "chevron.left").padding(12) }.offset(x: -40)
            Button(action: { service.sendIRCC("Right") }) { Image(systemName: "chevron.right").padding(12) }.offset(x: 40)
            Button(action: { service.sendIRCC("Confirm") }) { Text("OK").font(.caption).bold().padding(14).background(raised).cornerRadius(20) }
        }
        .frame(height: 120)
    }
    private var touchpad: some View {
        RoundedRectangle(cornerRadius: 10)
            .fill(Color.black.opacity(0.3))
            .frame(height: 100)
            .overlay(Text("Tap/Swipe here").font(.caption2).foregroundColor(ink2))
            .padding(.horizontal, 14)
    }
    private var navGrid: some View {
        HStack(spacing: 6) {
            gridBtn(icon: "arrow.uturn.left", title: "Back", action: { service.sendIRCC("Return") })
            gridBtn(icon: "house", title: "Home", action: { service.sendIRCC("Home") })
            gridBtn(icon: "list.bullet", title: "Menu", action: { service.sendIRCC("ActionMenu") })
            gridBtn(icon: "minus.square", title: "Hide", action: { NSApp.hide(nil) })
        }
        .padding(.horizontal, 14)
        .padding(.bottom, 10)
    }
    private var videoControls: some View {
        VStack(spacing: 6) {
            HStack(spacing: 6) {
                gridBtn(icon: "speaker.wave.1", title: "Vol-", action: { service.sendIRCC("VolumeDown") })
                gridBtn(icon: "speaker.slash", title: "Mute", action: { service.sendIRCC("Mute") })
                gridBtn(icon: "speaker.wave.3", title: "Vol+", action: { service.sendIRCC("VolumeUp") })
                gridBtn(icon: "playpause", title: "Play", action: { service.sendIRCC("Play") })
            }
            HStack(spacing: 6) {
                gridBtn(icon: "arrow.up.left.and.arrow.down.right", title: "Full", action: { service.toggleFullScreenVideo() })
                gridBtn(icon: "aspectratio", title: "Aspect", action: { service.sendIRCC("Wide") })
                gridBtn(icon: "sun.max", title: "Pic", action: { service.sendIRCC("PictureMode") })
                gridBtn(icon: "info.circle", title: "Info", action: { service.sendIRCC("Display") })
            }
        }
        .padding(.horizontal, 14)
    }
    private var clipboardSection: some View {
        VStack(spacing: 6) {
            HStack(spacing: 6) {
                gridBtn(icon: "doc.on.clipboard", title: "Paste", action: { service.pasteMacClipboard() })
                gridBtn(icon: "doc.on.doc", title: "Copy", action: { service.copyText() })
                gridBtn(icon: "checkmark.square", title: "All", action: { service.selectAll() })
                gridBtn(icon: "scissors", title: "Cut", action: { service.cutText() })
            }
            HStack(spacing: 6) {
                TextField("Type text TV...", text: $inputText)
                    .textFieldStyle(PlainTextFieldStyle())
                    .padding(8).background(raised).cornerRadius(8)
                Button(action: { service.sendText(inputText); inputText = "" }) {
                    Image(systemName: "arrow.right").padding(8).background(accent).cornerRadius(8)
                }
                .buttonStyle(PlainButtonStyle())
            }
        }
        .padding(14)
    }
    private func gridBtn(icon: String, title: String, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            VStack(spacing: 4) {
                Image(systemName: icon).font(.system(size: 12))
                Text(title).font(.system(size: 8))
            }
            .frame(maxWidth: .infinity)
            .padding(8)
            .background(raised)
            .cornerRadius(8)
        }
        .buttonStyle(PlainButtonStyle())
    }
}
struct IconButton: View {
    let icon: String
    let title: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            VStack(spacing: 2) {
                Image(systemName: icon)
                    .font(.system(size: 13, weight: .bold))
                Text(title)
                    .font(.system(size: 8, weight: .medium))
            }
            .foregroundColor(.white)
            .frame(width: 42, height: 32)
            .background(Color.white.opacity(0.08))
            .cornerRadius(6)
        }
        .buttonStyle(PlainButtonStyle())
    }
}

struct AppButton: View {
    let title: String
    let color: Color
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.system(size: 9, weight: .semibold))
                .foregroundColor(.white)
                .frame(maxWidth: .infinity, minHeight: 24)
                .background(color.opacity(0.4))
                .cornerRadius(5)
                .overlay(RoundedRectangle(cornerRadius: 5).stroke(color.opacity(0.8), lineWidth: 1))
        }
        .buttonStyle(PlainButtonStyle())
    }
}

struct ModernButtonStyle: ButtonStyle {
    var tint: Color = .white

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .foregroundColor(.white)
            .padding(.horizontal, 6)
            .padding(.vertical, 4)
            .background(tint.opacity(configuration.isPressed ? 0.35 : 0.12))
            .cornerRadius(5)
            .scaleEffect(configuration.isPressed ? 0.96 : 1.0)
    }
}

struct VisualEffectView: NSViewRepresentable {
    let material: NSVisualEffectView.Material
    let blendingMode: NSVisualEffectView.BlendingMode

    func makeNSView(context: Context) -> NSVisualEffectView {
        let view = NSVisualEffectView()
        view.material = material
        view.blendingMode = blendingMode
        view.state = .active
        return view
    }

    func updateNSView(_ nsView: NSVisualEffectView, context: Context) {}
}

// MARK: Floating Panel Direct Mac Keyboard Passthrough
class FloatingPanel: NSPanel {
    init(contentRect: NSRect) {
        super.init(
            contentRect: contentRect,
            styleMask: [.nonactivatingPanel, .titled, .closable, .miniaturizable, .fullSizeContentView],
            backing: .buffered,
            defer: false
        )
        self.level = .floating
        self.isFloatingPanel = true
        self.titleVisibility = .hidden
        self.titlebarAppearsTransparent = true
        self.isMovableByWindowBackground = true
        self.isReleasedWhenClosed = false
        self.collectionBehavior = [.canJoinAllSpaces, .fullScreenAuxiliary]
        self.backgroundColor = .clear
        self.hasShadow = true
    }

    override var canBecomeKey: Bool { true }
    override var canBecomeMain: Bool { true }

    override func keyDown(with event: NSEvent) {
        let service = BraviaService.shared

        // Cmd shortcuts
        if event.modifierFlags.contains(.command) {
            switch event.charactersIgnoringModifiers?.lowercased() {
            case "v":
                service.pasteMacClipboard()
                return
            case "c":
                service.copyText()
                return
            case "a":
                service.selectAll()
                return
            case "x":
                service.cutText()
                return
            case "f":
                service.toggleFullScreenVideo()
                return
            case "h":
                service.sendIRCC("Home")
                return
            default:
                break
            }
        }

        // Special keys
        switch event.keyCode {
        case 126: // Arrow Up
            service.sendIRCC("Up")
            return
        case 125: // Arrow Down
            service.sendIRCC("Down")
            return
        case 123: // Arrow Left
            service.sendIRCC("Left")
            return
        case 124: // Arrow Right
            service.sendIRCC("Right")
            return
        case 36, 76: // Enter / Return
            service.sendIRCC("Confirm")
            return
        case 51, 117: // Backspace / Delete
            service.sendAdbKey("Backspace")
            return
        case 53: // Escape
            service.sendIRCC("Return")
            return
        case 48: // Tab
            service.sendAdbKey("Tab")
            return
        case 49: // Space
            service.sendAdbKey("Space")
            return
        default:
            break
        }

        // Live typing passthrough letters, numbers, symbols
        if let chars = event.characters, !chars.isEmpty {
            for char in chars {
                if char.isASCII && !char.isNewline && char != "\r" && char != "\t" {
                    service.sendChar(char)
                }
            }
            return
        }

        super.keyDown(with: event)
    }
}

class AppDelegate: NSObject, NSApplicationDelegate {
    var panel: FloatingPanel!

    func applicationDidFinishLaunching(_ notification: Notification) {
        let contentView = RemoteHUDView()
        let hostingView = NSHostingView(rootView: contentView)

        let initialRect = NSRect(x: 100, y: 100, width: 296, height: 780)
        panel = FloatingPanel(contentRect: initialRect)
        panel.contentView = hostingView
        panel.center()
        panel.makeKeyAndOrderFront(nil)
        panel.orderFrontRegardless()
    }
}

let app = NSApplication.shared
let delegate = AppDelegate()
app.delegate = delegate
app.setActivationPolicy(.accessory)
app.run()
