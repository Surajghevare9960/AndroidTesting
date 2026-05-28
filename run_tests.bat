@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-21
set MVN=C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.1.2\plugins\maven\lib\maven3\bin\mvn.cmd

:MENU
cls
echo ================================================================
echo   HRMS Automation Suite  -  Test Runner
echo ================================================================
echo.
echo  TESTS
echo  ─────────────────────────────────────────────────────────────
echo  [1]  Run ALL Tests
echo  [2]  Login
echo  [3]  Dashboard
echo  [4]  Attendance
echo  [5]  Leave
echo  [6]  Clocking
echo  [7]  Approvals
echo  [8]  Travel
echo  [9]  Salary
echo  [10] Claim
echo  [11] Holiday
echo  [12] Birthday ^& Anniversary
echo  [13] Ticket Application
echo  [14] Survey
echo  [15] Policy ^& Documents
echo  [16] My Team Attendance
echo  [17] Exit Application
echo  [18] Settings
echo  [19] DB Validation (needs DB connection)
echo.
echo  TOOLS
echo  ─────────────────────────────────────────────────────────────
echo  [20] Compile only (no device needed)
echo  [21] Open Extent Report (latest)
echo  [22] Open TestNG Report
echo  [23] Generate Allure Report  (needs allure CLI installed)
echo  [24] Open Allure Report
echo  [25] DB Query Runner  (no device needed)
echo.
echo  [0]  Exit
echo.
set /p choice=Enter choice: 

if "%choice%"=="1"  goto ALL
if "%choice%"=="2"  goto LOGIN
if "%choice%"=="3"  goto DASHBOARD
if "%choice%"=="4"  goto ATTENDANCE
if "%choice%"=="5"  goto LEAVE
if "%choice%"=="6"  goto CLOCKING
if "%choice%"=="7"  goto APPROVALS
if "%choice%"=="8"  goto TRAVEL
if "%choice%"=="9"  goto SALARY
if "%choice%"=="10" goto CLAIM
if "%choice%"=="11" goto HOLIDAY
if "%choice%"=="12" goto BIRTHDAY
if "%choice%"=="13" goto TICKET
if "%choice%"=="14" goto SURVEY
if "%choice%"=="15" goto POLICY
if "%choice%"=="16" goto TEAMATTENDANCE
if "%choice%"=="17" goto EXIT_APP
if "%choice%"=="18" goto SETTINGS
if "%choice%"=="19" goto DBVALIDATION
if "%choice%"=="20" goto COMPILE
if "%choice%"=="21" goto OPEN_EXTENT
if "%choice%"=="22" goto OPEN_TESTNG
if "%choice%"=="23" goto GEN_ALLURE
if "%choice%"=="24" goto OPEN_ALLURE
if "%choice%"=="25" goto DB_QUERY
if "%choice%"=="0"  goto END
goto MENU

:ALL
echo Running ALL tests...
"%MVN%" test -f pom.xml
goto DONE

:LOGIN
"%MVN%" test -f pom.xml -Dtest=tests.LoginTest -DsuiteXmlFile=""
goto DONE

:DASHBOARD
"%MVN%" test -f pom.xml -Dtest=tests.DashboardTest -DsuiteXmlFile=""
goto DONE

:ATTENDANCE
"%MVN%" test -f pom.xml -Dtest=tests.AttendanceTest -DsuiteXmlFile=""
goto DONE

:LEAVE
"%MVN%" test -f pom.xml -Dtest=tests.LeaveTest -DsuiteXmlFile=""
goto DONE

:CLOCKING
"%MVN%" test -f pom.xml -Dtest=tests.ClockingTest -DsuiteXmlFile=""
goto DONE

:APPROVALS
"%MVN%" test -f pom.xml -Dtest=tests.ApprovalsTest -DsuiteXmlFile=""
goto DONE

:TRAVEL
"%MVN%" test -f pom.xml -Dtest=tests.TravelTest -DsuiteXmlFile=""
goto DONE

:SALARY
"%MVN%" test -f pom.xml -Dtest=tests.SalaryTest -DsuiteXmlFile=""
goto DONE

:CLAIM
"%MVN%" test -f pom.xml -Dtest=tests.ClaimTest -DsuiteXmlFile=""
goto DONE

:HOLIDAY
"%MVN%" test -f pom.xml -Dtest=tests.HolidayTest -DsuiteXmlFile=""
goto DONE

:BIRTHDAY
"%MVN%" test -f pom.xml -Dtest=tests.BirthdayAnniversaryTest -DsuiteXmlFile=""
goto DONE

:TICKET
"%MVN%" test -f pom.xml -Dtest=tests.TicketApplicationTest -DsuiteXmlFile=""
goto DONE

:SURVEY
"%MVN%" test -f pom.xml -Dtest=tests.SurveyTest -DsuiteXmlFile=""
goto DONE

:POLICY
"%MVN%" test -f pom.xml -Dtest=tests.PolicyDocumentsTest -DsuiteXmlFile=""
goto DONE

:TEAMATTENDANCE
"%MVN%" test -f pom.xml -Dtest=tests.MyTeamAttendanceTest -DsuiteXmlFile=""
goto DONE

:EXIT_APP
"%MVN%" test -f pom.xml -Dtest=tests.ExitApplicationTest -DsuiteXmlFile=""
goto DONE

:SETTINGS
"%MVN%" test -f pom.xml -Dtest=tests.SettingsTest -DsuiteXmlFile=""
goto DONE

:DBVALIDATION
"%MVN%" test -f pom.xml -Dtest=tests.DBValidationTest -DsuiteXmlFile=""
goto DONE

:COMPILE
echo Compiling...
"%MVN%" compile test-compile -f pom.xml
goto DONE

:OPEN_EXTENT
echo Opening latest Extent Report...
for /f "delims=" %%f in ('dir /b /o-d "test-output\ExtentReport\*.html" 2^>nul') do (
    start "" "test-output\ExtentReport\%%f"
    goto DONE
)
echo No Extent Report found. Run tests first.
goto DONE

:OPEN_TESTNG
echo Opening TestNG Report...
if exist "test-output\TestNGReport\HRMS_TestNG_Report.html" (
    start "" "test-output\TestNGReport\HRMS_TestNG_Report.html"
) else (
    echo No TestNG Report found. Run tests first.
)
goto DONE

:GEN_ALLURE
echo Generating Allure Report...
where allure >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Allure CLI not found. Install from: https://allurereport.org/docs/install/
    echo Or run: scoop install allure  /  choco install allure
    goto DONE
)
allure generate allure-results --clean -o test-output\AllureReport
echo Allure report generated at test-output\AllureReport
goto DONE

:OPEN_ALLURE
echo Opening Allure Report...
where allure >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Allure CLI not found. Install from: https://allurereport.org/docs/install/
    goto DONE
)
allure serve allure-results
goto DONE

:DB_QUERY
echo Running DB Query Runner...
"%MVN%" test-compile -f pom.xml -q
"%MVN%" exec:java -f pom.xml -Dexec.mainClass="utils.DBQueryRunner" -Dexec.classpathScope="test"
goto DONE

:DONE
echo.
echo ================================================================
echo  Reports location:
echo    Extent  → test-output\ExtentReport\HRMS_Report_*.html
echo    TestNG  → test-output\TestNGReport\HRMS_TestNG_Report.html
echo    Allure  → run option [23] then [24]
echo    Excel   → test-output\HRMS_TestReport.xlsx
echo    DB Data → test-output\DB_Query_Results_*.xlsx
echo ================================================================
pause
goto MENU

:END
