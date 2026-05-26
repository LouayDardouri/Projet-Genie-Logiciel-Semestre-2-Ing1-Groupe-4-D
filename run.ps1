$ErrorActionPreference = 'Stop'

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$sourceRoot = Join-Path $projectRoot 'ignissim\src\main\java'
$outputDir = Join-Path $projectRoot 'ignissim\out'

if (Test-Path $outputDir) {
    Remove-Item -Recurse -Force $outputDir
}

New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

$javaFiles = Get-ChildItem -Path $sourceRoot -Recurse -Filter *.java | ForEach-Object { $_.FullName }

if (-not $javaFiles) {
    throw "Aucun fichier Java trouvé dans $sourceRoot"
}

& javac -d $outputDir @javaFiles

if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

& java -cp $outputDir fr.cytech.ignissim.Main