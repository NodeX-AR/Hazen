# Hazen installer for Windows
#   irm https://raw.githubusercontent.com/NodeX-AR/Hazen/main/install.ps1 | iex
$ErrorActionPreference = 'Stop'
$repo = 'NodeX-AR/Hazen'
$url  = "https://github.com/$repo/releases/latest/download/Hazen-Setup-x64.exe"

Write-Host ''
Write-Host '  H A Z E N' -ForegroundColor DarkYellow
Write-Host '  One camera. Every screen.' -ForegroundColor DarkGray
Write-Host ''

if (-not [Environment]::Is64BitOperatingSystem) {
  Write-Host '  Hazen needs 64-bit Windows.' -ForegroundColor Red
  return
}

$tmp = Join-Path $env:TEMP 'Hazen-Setup.exe'
Write-Host '  * Downloading Hazen for Windows' -ForegroundColor DarkYellow
curl.exe -fL --progress-bar $url -o $tmp
if ($LASTEXITCODE -ne 0 -or -not (Test-Path $tmp)) {
  Write-Host '  x Download failed. Has a release been published yet?' -ForegroundColor Red
  return
}

Write-Host '  * Starting the installer' -ForegroundColor DarkYellow
Start-Process -FilePath $tmp -Wait
Remove-Item $tmp -ErrorAction SilentlyContinue
Write-Host '  + Done. Open Hazen from the Start menu.' -ForegroundColor Green
Write-Host '    The on-device AI is already inside the app, so it works fully offline.' -ForegroundColor DarkGray
Write-Host ''
