#!/usr/bin/env python3
"""Generate simple square PNG launcher icons for each density."""
import struct
import zlib
import os

def make_png(width, height, r, g, b):
    """Create a solid-color PNG with rounded-corner style (a square with a center circle)."""
    def chunk(ctype, data):
        c = ctype + data
        crc = struct.pack('>I', zlib.crc32(c) & 0xffffffff)
        return struct.pack('>I', len(data)) + c + crc

    signature = b'\x89PNG\r\n\x1a\n'

    # IHDR
    ihdr = struct.pack('>IIBBBBB', width, height, 8, 2, 0, 0, 0)
    ihdr_data = zlib.compress(ihdr)
    ihdr_chunk = chunk(b'IHDR', ihdr_data)

    # IDAT - each row is (width) RGB pixels
    raw = b''
    for y in range(height):
        raw += b'\x00'  # filter: None
        for x in range(width):
            # Draw a circle in the center for a nice icon look
            cx, cy = width // 2, height // 2
            rad = min(width, height) // 3
            dx, dy = x - cx, y - cy
            dist = (dx*dx + dy*dy) ** 0.5
            if dist <= rad:
                raw += struct.pack('BBB', r, g, b)
            else:
                raw += struct.pack('BBB', 33, 150, 243)  # Material blue for background

    idat = zlib.compress(raw)
    idat_chunk = chunk(b'IDAT', idat)

    # IEND
    iend_chunk = chunk(b'IEND', b'')

    return signature + ihdr_chunk + idat_chunk + iend_chunk

# Each mipmap density needs: mdpi=48, hdpi=72, xhdpi=96, xxhdpi=144, xxxhdpi=192
densities = {
    'mdpi': 48,
    'hdpi': 72,
    'xhdpi': 96,
    'xxhdpi': 144,
    'xxxhdpi': 192,
}

# OpenCode blue accent: RGB(30, 136, 229) — matches 0xFF1E88E5
r, g, b = 30, 136, 229

base = '/root/opencode-android/app/src/main/res'

for density, size in densities.items():
    outdir = os.path.join(base, f'mipmap-{density}')
    png = make_png(size, size, r, g, b)
    for name in ['ic_launcher', 'ic_launcher_round']:
        path = os.path.join(outdir, f'{name}.png')
        with open(path, 'wb') as f:
            f.write(png)
    print(f"Generated {size}x{size} PNGs for {density}")

print("All icons generated.")
