#!/system/bin/sh
cd /data/user/0/com.imtaqin.andropi/files/home/workspace/opencode/app/src/main/res/mipmap-hdpi

# Create a 96x96 PNG with blue background and white circle
# We'll use a 96x96 image where pixels inside a circle are white, others blue

# For simplicity, create a solid blue PNG
# Each row: 1 filter byte + 96 pixels
BLUE="\x1E\x88\xE5"
WHITE="\xFF\xFF\xFF"

# Create rows
{
  y=0
  while [ "$y" -lt 96 ]; do
    printf '\x00'  # filter byte
    x=0
    while [ "$x" -lt 96 ]; do
      # Calculate if this pixel is inside a circle centered at (48,48) with radius ~40
      dx=$((x - 48))
      dy=$((y - 48))
      # dx*dx + dy*dy < r*r, r=40, r*r=1600
      dist=$((dx * dx + dy * dy))
      if [ "$dist" -lt 1600 ]; then
        printf '%s' "$WHITE"
      else
        printf '%s' "$BLUE"
      fi
      x=$((x + 1))
    done
    y=$((y + 1))
  done
} > /tmp/raw_round.bin

echo "Raw round: $(wc -c < /tmp/raw_round.bin) bytes"

# Gzip
gzip -9 < /tmp/raw_round.bin > /tmp/idat_round.bin

GZ_SIZE=$(wc -c < /tmp/idat_round.bin)

# Build PNG
{
  printf '\x89PNG\r\n\x1a\n'
  printf '\x00\x00\x00\x0dIHDR'
  printf '\x00\x00\x00\x60\x00\x00\x00\x01\x08\x06\x00\x00\x00'
  printf '\x7f\x7f\x7f\x7f'
  GH=$((GZ_SIZE >> 8))
  GL=$((GZ_SIZE & 0xFF))
  printf "\x00\x00$(printf '%02x' $GH)$(printf '%02x' $GL)"
  printf 'IDAT'
  cat /tmp/idat_round.bin
  printf '\x00\x00\x00\x00IEND\xae\x42\x60\x82'
} > ic_launcher_round.png

echo "ic_launcher_round.png: $(wc -c < ic_launcher_round.png) bytes"
