param([string]$JavaHome = '')
$ErrorActionPreference = 'Stop'
Set-StrictMode -Version Latest

if ($JavaHome) {
    $javac = Join-Path $JavaHome 'bin\javac.exe'
    $java = Join-Path $JavaHome 'bin\java.exe'
} else {
    $javac = (Get-Command javac -ErrorAction Stop).Source
    $java = (Get-Command java -ErrorAction Stop).Source
}

$projectRoot = (Resolve-Path -LiteralPath (Join-Path $PSScriptRoot '..')).Path
$tempRoot = [System.IO.Path]::GetFullPath([System.IO.Path]::GetTempPath())
$build = Join-Path $tempRoot ('DAA_Assignment4_' + [guid]::NewGuid().ToString('N'))
$sources = @(Get-ChildItem -LiteralPath (Join-Path $projectRoot 'src') -Filter '*.java' |
    Sort-Object Name | ForEach-Object { $_.FullName })
$sources += Join-Path $PSScriptRoot 'Assignment4Tests.java'

function Show-Command([string]$Executable, [string[]]$Arguments) {
    $quoted = @($Arguments | ForEach-Object { "'" + $_.Replace("'", "''") + "'" })
    Write-Output ("& '" + $Executable.Replace("'", "''") + "' " + ($quoted -join ' '))
}

New-Item -ItemType Directory -Path $build | Out-Null
try {
    Show-Command $javac @('-version')
    & $javac -version
    if ($LASTEXITCODE -ne 0) { throw 'Cannot run javac.' }
    Show-Command $java @('--version')
    & $java --version
    if ($LASTEXITCODE -ne 0) { throw 'Cannot run Java 17+. Supply -JavaHome pointing to a JDK.' }
    $compileArguments = @('--release', '17', '-encoding', 'UTF-8', '-Xlint:all', '-Werror', '-d', $build) + $sources
    Show-Command $javac $compileArguments
    & $javac @compileArguments
    if ($LASTEXITCODE -ne 0) { throw 'Compilation failed.' }
    Write-Output 'PASS compilation: Java 17 release, all lint warnings treated as errors'
    $runArguments = @('-cp', $build, 'Assignment4Tests')
    Show-Command $java $runArguments
    & $java @runArguments
    if ($LASTEXITCODE -ne 0) { throw 'Tests failed.' }
} finally {
    # Delete only the uniquely named temporary build directory created above.
    $resolvedBuild = [System.IO.Path]::GetFullPath($build)
    $expectedPrefix = (Join-Path $tempRoot 'DAA_Assignment4_')
    if (-not $resolvedBuild.StartsWith($expectedPrefix, [StringComparison]::OrdinalIgnoreCase)) {
        throw 'Refusing cleanup outside the temporary build directory.'
    }
    if (Test-Path -LiteralPath $resolvedBuild) {
        Remove-Item -LiteralPath $resolvedBuild -Recurse -Force
    }
}
