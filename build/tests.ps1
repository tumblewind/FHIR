###############################################################################
# (C) Copyright IBM Corp. 2020
# 
# SPDX-License-Identifier: Apache-2.0
###############################################################################
$ErrorActionPreference = 'Stop'
Write-Host 'Performing Tests'
$PROC = Start-Process -FilePath "mvn.cmd" -ArgumentList "-B test -DskipTests=false -f .\fhir-server-test\pom.xml -DskipWebSocketTest=true --no-transfer-progress" -PassThru
Wait-Process -InputObject $PROC

Write-Host "Exit Code is " $PROC.ExitCode
exit $PROC.ExitCode
# End of Script
