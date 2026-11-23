#!/bin/sh
# Hazen installer for macOS and Linux
#   curl -fsSL https://raw.githubusercontent.com/NodeX-AR/Hazen/main/install.sh | sh
set -eu

REPO="NodeX-AR/Hazen"
BASE="https://github.com/$REPO/releases/latest/download"
if [ -t 1 ]; then
  O='\033[38;5;208m'; B='\033[38;5;33m'; G='\033[38;5;41m'; D='\033[2m'; R='\033[31m'; X='\033[0m'
else
  O=''; B=''; G=''; D=''; R=''; X=''
fi
say()  { printf '%b\n' "$1"; }
step() { say "  ${O}●${X} $1"; }
ok()   { say "  ${G}✔${X} $1"; }
fail() { say "  ${R}✘ $1${X}"; exit 1; }
need() { command -v "$1" >/dev/null 2>&1 || fail "Please install '$1' first."; }

say ""
say "  ${B}◢${G}◣${X}  ${O}H A Z E N${X}"
say "  ${D}One camera. Every screen.${X}"
say ""
need curl

OS=$(uname -s)
ARCH=$(uname -m)
TMP=$(mktemp -d)
trap 'rm -rf "$TMP"' EXIT

case "$OS" in
  Linux)
    case "$ARCH" in
      x86_64|amd64) ;;
      *) fail "The Linux app is for 64-bit Intel/AMD (x86_64); this machine is $ARCH. Use the web app instead: https://nodex-ar.github.io/Hazen/demo.html" ;;
    esac
    step "Downloading Hazen for Linux"
    curl -fL --progress-bar "$BASE/Hazen-linux-x64.AppImage" -o "$TMP/Hazen.AppImage" || fail "Download failed. Has a release been published yet?"
    DIR="$HOME/.local/share/hazen"; BIN="$HOME/.local/bin"; APPS="$HOME/.local/share/applications"; ICONS="$HOME/.local/share/icons"
    step "Installing"
    mkdir -p "$DIR" "$BIN" "$APPS" "$ICONS"
    mv "$TMP/Hazen.AppImage" "$DIR/Hazen.AppImage"
    chmod +x "$DIR/Hazen.AppImage"
    ln -sf "$DIR/Hazen.AppImage" "$BIN/hazen"
    curl -fsSL "https://raw.githubusercontent.com/$REPO/main/icons/icon-512.png" -o "$ICONS/hazen.png" 2>/dev/null || true
    cat > "$APPS/hazen.desktop" <<DESK
[Desktop Entry]
Type=Application
Name=Hazen
Comment=Pro camera, editor and album
Exec=$DIR/Hazen.AppImage --no-sandbox %U
Icon=$ICONS/hazen.png
Terminal=false
Categories=Graphics;Photography;
DESK
    ok "Installed. Open Hazen from your app menu, or run: hazen"
    ;;
  Darwin)
    case "$ARCH" in arm64) FILE="Hazen-mac-arm64.dmg" ;; *) FILE="Hazen-mac-x64.dmg" ;; esac
    step "Downloading Hazen for macOS ($ARCH)"
    curl -fL --progress-bar "$BASE/$FILE" -o "$TMP/Hazen.dmg" || fail "Download failed. Has a release been published yet?"
    step "Installing"
    MNT=$(hdiutil attach -nobrowse "$TMP/Hazen.dmg" | awk -F'\t' '/\/Volumes\//{print $NF}' | tail -1)
    [ -d "$MNT/Hazen.app" ] || fail "Could not open the disk image."
    DEST="/Applications"
    [ -w "$DEST" ] || { DEST="$HOME/Applications"; mkdir -p "$DEST"; }
    rm -rf "$DEST/Hazen.app"
    cp -R "$MNT/Hazen.app" "$DEST/"
    hdiutil detach -quiet "$MNT" || true
    xattr -dr com.apple.quarantine "$DEST/Hazen.app" 2>/dev/null || true
    ok "Installed in $DEST. Open Hazen from Launchpad."
    ;;
  *)
    fail "This installer supports macOS and Linux. On Windows use: irm https://raw.githubusercontent.com/$REPO/main/install.ps1 | iex"
    ;;
esac
say ""
say "  ${D}The on-device AI is already inside the app, so Hazen works fully offline.${X}"
say ""
