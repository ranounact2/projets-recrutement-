# Lance les tests Maven sans avoir mvn dans le PATH.
# Utilise le Maven d'IntelliJ si present, sinon cherche mvn dans PATH.

$mvnPaths = @(
    "${env:ProgramFiles}\JetBrains\IntelliJ IDEA*\plugins\maven\lib\maven3\bin\mvn.cmd",
    "${env:ProgramFiles(x86)}\JetBrains\IntelliJ IDEA*\plugins\maven\lib\maven3\bin\mvn.cmd",
    "C:\Program Files\Apache\maven\bin\mvn.cmd"
)

$mvn = $null
foreach ($p in $mvnPaths) {
    $resolved = Get-Item $p -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($resolved) { $mvn = $resolved.FullName; break }
}
if (-not $mvn) { $mvn = "mvn" }

Push-Location $PSScriptRoot
try {
    & $mvn -B clean test @args
} finally {
    Pop-Location
}
