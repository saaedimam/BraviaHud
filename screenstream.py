#!/usr/bin/env python3
"""Tiny HTTP server that pipes ffmpeg screen-capture to a live MPEG-TS stream."""
import subprocess, threading, time
from http.server import HTTPServer, BaseHTTPRequestHandler

HOST, PORT = '0.0.0.0', 8095
FF = [
    'ffmpeg', '-f', 'avfoundation', '-capture_cursor', '1',
    '-i', 'Capture screen 0',
    '-vf', 'scale=1280:-2',
    '-c:v', 'h264_videotoolbox', '-b:v', '3500k',
    '-preset', 'ultrafast', '-tune', 'zerolatency', '-g', '30',
    '-f', 'mpegts', 'pipe:1'
]

# One ffmpeg process feeds all clients; each client gets it broadcast via threads.
ffproc = subprocess.Popen(FF, stdout=subprocess.PIPE, stderr=subprocess.DEVNULL, bufsize=0)
clients = []
lock = threading.Lock()

def broadcaster():
    while True:
        chunk = ffproc.stdout.read(65536)
        if not chunk:
            break
        with lock:
            for c in list(clients):
                try:
                    c[1].write(chunk)
                    c[1].flush()
                except Exception:
                    try: clients.remove(c)
                    except ValueError: pass
threading.Thread(target=broadcaster, daemon=True).start()

class H(BaseHTTPRequestHandler):
    def do_GET(self):
        if self.path == '/' or self.path == '/index.html':
            html = b'''<!DOCTYPE html><html><head><meta charset="utf-8"><title>Mac Screen</title></head>
<body style="margin:0;background:#000">
<video autoplay muted controls playsinline style="width:100vw;height:100vh;object-fit:contain" src="/live"></video>
</body></html>'''
            self.send_response(200)
            self.send_header('Content-Type', 'text/html; charset=utf-8')
            self.send_header('Cache-Control', 'no-cache')
            self.end_headers()
            self.wfile.write(html)
            return
        if self.path == '/live':
            self.send_response(200)
            self.send_header('Content-Type', 'video/mp2t')
            self.send_header('Cache-Control', 'no-cache')
            self.end_headers()
            with lock:
                clients.append((id(self), self.wfile))
            try:
                while True:
                    time.sleep(1)
            except Exception:
                pass
            finally:
                with lock:
                    try: clients.remove((id(self), self.wfile))
                    except ValueError: pass
            return
        self.send_response(404)
        self.end_headers()
    def log_message(self, *a): pass

print('Mac screen stream: http://<mac-ip>:8095/live.ts', flush=True)
HTTPServer(('', PORT), H).serve_forever()