#!/system/bin/sh
cd /data/user/0/com.imtaqin.andropi/files/home/workspace/opencode/app/src/main/res/mipmap-hdpi

# Generate 48x48 image data (each row: filter byte + 48 blue pixels)
# Each pixel: \x1E\x88\xE5 (blue #1E88E5)
# Each row: 1 + 48*3 = 145 bytes, 48 rows = 6,960 bytes

# Create one row
ROW=""
for i in $(seq 0 47); do
  ROW="${ROW}\x1E\x88\xE5"
done

# All 48 rows with filter byte
{
  y=0
  while [ "$y" -lt 48 ]; do
    printf '\x00'
    printf "%b" "$ROW"
    y=$((y + 1))
  done
} > /tmp/raw_img.bin

echo "Raw: $(wc -c < /tmp/raw_img.bin) bytes"

# Gzip
gzip -9 < /tmp/raw_img.bin > /tmp/idat.bin

GZ_SIZE=$(wc -c < /tmp/idat.bin)

# Build PNG
{
  printf '\x89PNG\r\n\x1a\n'
  printf '\x00\x00\x00\x0dIHDR'
  H48=$(printf '%02x' $((48 >> 8)))
  L48=$(printf '%02x' $((48 & 0xFF)))
  printf "\x00\x00${H48}${L48}\x00\x00\x00\x01\x08\x06\x00\x00\x00"
  printf '\x7f\x7f\x7f\x7f'
  GH=$((GZ_SIZE >> 8))
  GL=$((GZ_SIZE & 0xFF))
  printf "\x00\x00$(printf '%02x' $GH)$(printf '%02x' $GL)"
  printf 'IDAT'
  cat /tmp/idat.bin
  printf '\x00\x00\x00\x00IEND\xae\x42\x60\x82'
} > ic_launcher.png

echo "ic_launcher.png: $(wc -c < ic_launcher.png) bytes"
