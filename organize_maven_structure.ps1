# place_files_by_package.ps1
# Run from project root (where pom.xml sits)

$ErrorActionPreference = "Stop"
$projectRoot = (Get-Location).Path
$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$backupDir = Join-Path $projectRoot "backup_before_move_$timestamp"

# Exclusions
$excludePatterns = @("\\target\\","\\.git\\","backup_before_move_")

# mapping: filename -> destination package path (relative to src/main/java)
$map = @{
    "MediaLibraryApp.java" = "com\student\media_library\gui"
    "Book.java" = "com\student\media_library\model"
    "Film.java" = "com\student\media_library\model"
    "Media.java" = "com\student\media_library\model"
    "Music.java" = "com\student\media_library\model"
    "MediaLibrary.java" = "media_library"
    "PersistenceUtil.java" = "util"
    "LibraryDisplayObserver.java" = "patterns"
    "Observable.java" = "patterns"
    "Observer.java" = "patterns"
    "BookAdapter.java" = "patterns"
    "GenreIterator.java" = "patterns"
    "LegacyBook.java" = "patterns"
}

function IsExcluded($path) {
    foreach ($p in $excludePatterns) { if ($path -match [regex]::Escape($p)) { return $true } }
    return $false
}

Write-Host "Project root: $projectRoot"
Write-Host "Backup dir: $backupDir"
New-Item -Path $backupDir -ItemType Directory -Force | Out-Null

# Backup all source files (exclude target + backup dirs)
Get-ChildItem -Path $projectRoot -Recurse -File -ErrorAction SilentlyContinue |
        Where-Object { -not (IsExcluded($_.FullName)) } |
        ForEach-Object {
            $rel = $_.FullName.Substring($projectRoot.Length).TrimStart('\')
            $dest = Join-Path $backupDir $rel
            $destDir = Split-Path $dest
            if (-not (Test-Path $destDir)) { New-Item -ItemType Directory -Path $destDir -Force | Out-Null }
            Copy-Item -Path $_.FullName -Destination $dest -Force
        }

Write-Host "Backup created."

# Create src/main/java root
$javaRoot = Join-Path $projectRoot "src\main\java"
New-Item -ItemType Directory -Path $javaRoot -Force | Out-Null

$notFound = @()
$moved = @()

foreach ($entry in $map.GetEnumerator()) {
    $fileName = $entry.Key
    $destPackagePath = $entry.Value
    # Find file anywhere (exclude target and backups)
    $found = Get-ChildItem -Path $projectRoot -Recurse -File -Filter $fileName -ErrorAction SilentlyContinue |
            Where-Object { -not (IsExcluded($_.FullName)) } | Select-Object -First 1

    if ($null -eq $found) {
        $notFound += $fileName
        continue
    }

    $destDir = Join-Path $javaRoot $destPackagePath
    if (-not (Test-Path $destDir)) { New-Item -ItemType Directory -Path $destDir -Force | Out-Null }

    $destPath = Join-Path $destDir $fileName

    # Avoid moving if it's already at the destination path
    if ($found.FullName -ieq $destPath) {
        Write-Host "SKIP (already correct): $fileName"
        $moved += @{name=$fileName; from=$found.FullName; to=$destPath; skipped=$true}
        continue
    }

    Write-Host "Moving $fileName`n  from: $($found.FullName)`n  to:   $destPath"
    Move-Item -Path $found.FullName -Destination $destPath -Force
    $moved += @{name=$fileName; from=$found.FullName; to=$destPath}
}

Write-Host "`n--- Summary ---"
Write-Host "Moved files:"
foreach ($m in $moved) { Write-Host " - $($m.name) -> $($m.to)" }

if ($notFound.Count -gt 0) {
    Write-Host "`nFiles not found (check names or add them manually):"
    foreach ($n in $notFound) { Write-Host " - $n" }
}

# Quick scan: files containing multiple 'package' declarations (possible corruption)
Write-Host "`nScanning for files that contain multiple 'package' lines (possible malformed files)..."
$multiPackageFiles = Get-ChildItem -Path $projectRoot -Recurse -File -Filter *.java -ErrorAction SilentlyContinue |
        Where-Object { -not (IsExcluded($_.FullName)) } |
        Where-Object {
            ($content = Get-Content -Path $_.FullName -ErrorAction SilentlyContinue) -and
                    ( ($content | Select-String -Pattern '^\s*package\s+' -SimpleMatch).Count -gt 1 )
        } |
        Select-Object -ExpandProperty FullName

if ($multiPackageFiles) {
    Write-Host "Files with more than one package declaration (open and fix manually):"
    $multiPackageFiles | ForEach-Object { Write-Host " - $_" }
} else {
    Write-Host "No multiple-package issues detected."
}

Write-Host "`nDone. Backup at: $backupDir"
