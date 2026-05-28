@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-21
set MVN=C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.1.2\plugins\maven\lib\maven3\bin\mvn.cmd

echo ============================================================
echo   HRMS DB Query Runner  -  No device / Appium needed
echo ============================================================
echo.
echo This will:
echo   1. Connect to your database (config.properties credentials)
echo   2. Run all queries defined in DBQueryRunner.java
echo   3. Save results to test-output\DB_Query_Results_<timestamp>.xlsx
echo.
echo Make sure DB credentials are set in:
echo   src\test\resources\config\config.properties
echo   OR hardcoded in src\test\java\utils\DBQueryRunner.java
echo.
pause

echo.
echo Compiling...
"%MVN%" test-compile -f pom.xml -q
if %ERRORLEVEL% NEQ 0 (
    echo COMPILE FAILED. Fix errors and retry.
    pause
    exit /b 1
)

echo Running DB Query Runner...
"%MVN%" exec:java -f pom.xml ^
  -Dexec.mainClass="utils.DBQueryRunner" ^
  -Dexec.classpathScope="test"

echo.
echo ============================================================
echo  Done. Check test-output\ for the Excel file.
echo ============================================================
pause
