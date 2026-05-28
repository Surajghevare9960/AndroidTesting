package tests;

import Base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.*;
import Reusable_methods.Menu_drawer;
import utils.DBUtils;
import utils.HRMSDataExcelManager;
import utils.WaitUtils;

import java.util.List;
import java.util.Map;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 *  DBValidationTest
 *  ─────────────────────────────────────────────────────────────────────────
 *  FLOW (read this before running)
 *  ────────────────────────────────
 *
 *  STEP 1 – @BeforeClass  buildDataExcel()
 *    • Connects to DB
 *    • Calls HRMSDataExcelManager.buildExcel()
 *    • Creates  test-output/HRMS_DB_Data.xlsx  with:
 *        – Settings block  (DB config / master settings per module)
 *        – Data block      (actual records: leave apps, salary, etc.)
 *        – Comparison block (pre-filled DB values, APK column = PENDING)
 *
 *  STEP 2 – @Test methods  (Appium running, device connected)
 *    Each test:
 *      a) Reads the DB value via DBUtils
 *      b) Navigates to the module in the APK
 *      c) Reads the displayed value from the screen
 *      d) Calls HRMSDataExcelManager.writeComparisonResult(...)
 *         → updates the Comparison block in HRMS_DB_Data.xlsx
 *         → colours the row GREEN (MATCH) or RED (MISMATCH)
 *
 *  STEP 3 – @AfterClass  disconnectDB()
 *    • Closes DB connection
 *    • Prints path of the final Excel file
 *
 *  HOW TO ADD A NEW VALIDATION
 *  ────────────────────────────
 *  1. Add the module's queries to HRMSDataExcelManager.buildRegistry()
 *  2. Add a @Test method here following the pattern below
 *  3. Run – the Excel Comparison sheet updates automatically
 * ═══════════════════════════════════════════════════════════════════════════
 */
@Feature("DB vs APK Validation")
public class DBValidationTest extends BaseTest {

    private DBUtils     db;
    private Menu_drawer menu;

    // ── Setup ────────────────────────────────────────────────────────────────

    @BeforeClass
    @Description("Connect to DB and build HRMS_DB_Data.xlsx with all module data")
    public void buildDataExcel() {
        menu = new Menu_drawer(driver);
        db   = new DBUtils();

        try {
            db.connect();
            System.out.println("\n📊 Building HRMS_DB_Data.xlsx from DB...");
            String path = HRMSDataExcelManager.buildExcel();
            System.out.println("📊 Excel ready → " + path);
        } catch (Exception e) {
            System.err.println("⚠️  DB setup failed – DB tests will be skipped: " + e.getMessage());
        }    }

    @AfterClass
    public void disconnectDB() {
        if (db != null) db.disconnect();
        System.out.println("\n📊 Final Excel → " + HRMSDataExcelManager.EXCEL_PATH);
    }


    // ═══════════════════════════════════════════════════════════════════════
    //  TC-DB-01  Leave Settings – verify leave types enabled in APK
    // ═══════════════════════════════════════════════════════════════════════
    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Read leave master settings from DB, check each leave type is visible in APK")
    public void validateLeaveSettings() throws Exception {

        // DB: get all active leave types from settings
        List<Map<String, String>> settings = db.executeQuery(
            "SELECT setting_key, setting_value FROM leave_master_settings " +
            "WHERE setting_key LIKE '%leave_type%' OR setting_key LIKE '%enabled%' " +
            "ORDER BY setting_key ASC"
        );

        // APK: navigate to Leave module
        menu.goToLeaves();
        WaitUtils.sleep(2000);

        String pageSource = driver.getPageSource();

        for (Map<String, String> row : settings) {
            String key      = row.get("setting_key");
            String dbValue  = row.get("setting_value");
            // Check if the setting value (e.g. leave type name) appears in APK
            boolean found   = pageSource.contains(dbValue);
            String apkValue = found ? dbValue : "(not visible)";

            HRMSDataExcelManager.writeComparisonResult(
                "Leave", key, dbValue, apkValue,
                found ? "Setting visible in APK" : "Setting NOT found in APK"
            );
            System.out.println("Leave setting [" + key + "] DB=" + dbValue
                + " | APK=" + (found ? "FOUND" : "NOT FOUND"));
        }

        driver.navigate().back();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  TC-DB-02  Leave Applications – compare DB records vs APK list
    // ═══════════════════════════════════════════════════════════════════════
    @Test(priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Compare leave application records from DB with what APK shows")
    public void validateLeaveApplications() throws Exception {

        // DB: get latest 5 leave applications for the logged-in employee
        String empCode = utils.ConfigReader.get("username").split("@")[0];
        List<Map<String, String>> dbRows = db.executeQuery(
            "SELECT leave_id, leave_type, from_date, to_date, period, status " +
            "FROM leave_applications " +
            "WHERE emp_code = '" + empCode + "' " +
            "ORDER BY applied_on DESC LIMIT 5"
        );

        // APK: navigate to Leave → Status tab
        menu.goToLeaves();
        WaitUtils.sleep(2000);

        // Click Status tab directly by text
        try {
            driver.findElement(org.openqa.selenium.By.xpath(
                "//android.widget.LinearLayout[@content-desc='Status']")).click();
        } catch (Exception ignored) {}
        WaitUtils.sleep(2000);

        String pageSource = driver.getPageSource();

        for (Map<String, String> row : dbRows) {
            String leaveId   = row.get("leave_id");
            String leaveType = row.get("leave_type");
            String fromDate  = row.get("from_date");
            String status    = row.get("status");

            // Check if leave type and from_date appear in APK
            boolean typeFound = pageSource.contains(leaveType);
            boolean dateFound = pageSource.contains(fromDate);
            boolean found     = typeFound && dateFound;

            HRMSDataExcelManager.writeComparisonResult(
                "Leave",
                "leave_id=" + leaveId + " | " + leaveType,
                leaveType + " | " + fromDate + " | " + status,
                found ? leaveType + " | " + fromDate : "(not visible in APK)",
                found ? "" : "Type found=" + typeFound + " Date found=" + dateFound
            );
        }

        driver.navigate().back();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  TC-DB-03  Attendance – compare DB in/out times vs APK
    // ═══════════════════════════════════════════════════════════════════════
    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Description("Compare today's attendance in/out time from DB with APK display")
    public void validateAttendance() throws Exception {

        String empCode = utils.ConfigReader.get("username").split("@")[0];

        // DB: today's attendance
        String dbInTime  = db.getSingleValue(
            "SELECT TIME_FORMAT(in_time,'%H:%i') FROM attendance " +
            "WHERE emp_code='" + empCode + "' AND att_date=CURDATE() LIMIT 1");
        String dbOutTime = db.getSingleValue(
            "SELECT TIME_FORMAT(out_time,'%H:%i') FROM attendance " +
            "WHERE emp_code='" + empCode + "' AND att_date=CURDATE() LIMIT 1");

        // APK: navigate to Attendance
        menu.goToAttendance();
        WaitUtils.sleep(2000);

        AttendancePage ap = new AttendancePage(driver);
        String apkInTime  = "";
        String apkOutTime = "";
        try { apkInTime  = ap.intime.getText();  } catch (Exception ignored) {}
        try { apkOutTime = ap.outtime.getText(); } catch (Exception ignored) {}

        HRMSDataExcelManager.writeComparisonResult(
            "Attendance", "Today In Time",
            dbInTime  != null ? dbInTime  : "NULL",
            apkInTime.isEmpty() ? "(not shown)" : apkInTime, "");

        HRMSDataExcelManager.writeComparisonResult(
            "Attendance", "Today Out Time",
            dbOutTime != null ? dbOutTime : "NULL",
            apkOutTime.isEmpty() ? "(not shown)" : apkOutTime, "");

        driver.navigate().back();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  TC-DB-04  Salary – compare DB net salary vs APK
    // ═══════════════════════════════════════════════════════════════════════
    @Test(priority = 4)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Compare net salary from DB with salary shown in APK")
    public void validateSalary() throws Exception {

        String empCode = utils.ConfigReader.get("username").split("@")[0];

        String dbGross  = db.getSingleValue(
            "SELECT gross_salary FROM salary_details " +
            "WHERE emp_code='" + empCode + "' ORDER BY year DESC, month DESC LIMIT 1");
        String dbDeduct = db.getSingleValue(
            "SELECT total_deduction FROM salary_details " +
            "WHERE emp_code='" + empCode + "' ORDER BY year DESC, month DESC LIMIT 1");
        String dbNet    = db.getSingleValue(
            "SELECT net_salary FROM salary_details " +
            "WHERE emp_code='" + empCode + "' ORDER BY year DESC, month DESC LIMIT 1");

        // APK
        menu.goToSalary();
        WaitUtils.sleep(2000);

        SalaryPage sp = new SalaryPage(driver);
        String apkNet    = "";
        String apkGross  = "";
        String apkDeduct = "";
        try { apkNet    = sp.getNetSalary();     } catch (Exception ignored) {}
        try { apkGross  = sp.getGrossSalary();   } catch (Exception ignored) {}
        try { apkDeduct = sp.getTotalDeduction(); } catch (Exception ignored) {}

        HRMSDataExcelManager.writeComparisonResult("Salary", "Gross Salary",
            dbGross  != null ? dbGross  : "NULL", apkGross,  "");
        HRMSDataExcelManager.writeComparisonResult("Salary", "Total Deduction",
            dbDeduct != null ? dbDeduct : "NULL", apkDeduct, "");
        HRMSDataExcelManager.writeComparisonResult("Salary", "Net Salary",
            dbNet    != null ? dbNet    : "NULL", apkNet,    "");

        boolean match = dbNet != null && apkNet.contains(dbNet);
        Assert.assertTrue(match,
            "Salary mismatch! DB=" + dbNet + " APK=" + apkNet);

        driver.navigate().back();
    }


    // ═══════════════════════════════════════════════════════════════════════
    //  TC-DB-05  Approvals – compare pending count DB vs APK badge
    // ═══════════════════════════════════════════════════════════════════════
    @Test(priority = 5)
    @Severity(SeverityLevel.NORMAL)
    @Description("Compare pending approval counts from DB with APK badge numbers")
    public void validateApprovalCounts() throws Exception {

        // DB counts
        String dbLeaveCount = db.getSingleValue(
            "SELECT COUNT(*) FROM leave_applications WHERE status='Pending'");
        String dbRegCount   = db.getSingleValue(
            "SELECT COUNT(*) FROM attendance_regularization WHERE status='Pending'");
        String dbClaimCount = db.getSingleValue(
            "SELECT COUNT(*) FROM claim_applications WHERE status='Pending'");

        // APK
        menu.goToApprovals();
        WaitUtils.sleep(2000);

        ApprovalsPage ap = new ApprovalsPage(driver);
        String apkLeave = "";
        String apkReg   = "";
        String apkClaim = "";
        try { apkLeave = ap.getApprovalCount(ap.leaveApprovalsCount);          } catch (Exception ignored) {}
        try { apkReg   = ap.getApprovalCount(ap.attendanceRegularizationCount); } catch (Exception ignored) {}
        try { apkClaim = ap.getApprovalCount(ap.claimApprovalCount);            } catch (Exception ignored) {}

        HRMSDataExcelManager.writeComparisonResult("Approvals_Leave",
            "Pending Leave Approvals",
            dbLeaveCount != null ? dbLeaveCount : "NULL", apkLeave, "");
        HRMSDataExcelManager.writeComparisonResult("Approvals_Leave",
            "Pending Attendance Regularization",
            dbRegCount   != null ? dbRegCount   : "NULL", apkReg,   "");
        HRMSDataExcelManager.writeComparisonResult("Approvals_Leave",
            "Pending Claim Approvals",
            dbClaimCount != null ? dbClaimCount : "NULL", apkClaim, "");

        driver.navigate().back();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  TC-DB-06  Claims – compare DB claim list vs APK
    // ═══════════════════════════════════════════════════════════════════════
    @Test(priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Description("Compare claim records from DB with APK claim list")
    public void validateClaims() throws Exception {

        String empCode = utils.ConfigReader.get("username").split("@")[0];
        List<Map<String, String>> dbRows = db.executeQuery(
            "SELECT claim_id, claim_type, amount, status " +
            "FROM claim_applications " +
            "WHERE emp_code='" + empCode + "' " +
            "ORDER BY claim_date DESC LIMIT 5"
        );

        menu.goToClaim();
        WaitUtils.sleep(2000);
        String pageSource = driver.getPageSource();

        for (Map<String, String> row : dbRows) {
            String claimId   = row.get("claim_id");
            String claimType = row.get("claim_type");
            String amount    = row.get("amount");
            String status    = row.get("status");

            boolean found = pageSource.contains(claimType) || pageSource.contains(amount);

            HRMSDataExcelManager.writeComparisonResult(
                "Claims",
                "claim_id=" + claimId + " | " + claimType,
                claimType + " | ₹" + amount + " | " + status,
                found ? claimType + " | " + amount : "(not visible)",
                found ? "" : "Claim not found in APK list"
            );
        }

        driver.navigate().back();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  TC-DB-07  Holidays – compare DB holiday list vs APK
    // ═══════════════════════════════════════════════════════════════════════
    @Test(priority = 7)
    @Severity(SeverityLevel.MINOR)
    @Description("Compare holiday list from DB with APK holiday screen")
    public void validateHolidays() throws Exception {

        List<Map<String, String>> dbRows = db.executeQuery(
            "SELECT holiday_name, holiday_date FROM holidays " +
            "ORDER BY holiday_date ASC LIMIT 10"
        );

        menu.goToHoliday();
        WaitUtils.sleep(2000);
        String pageSource = driver.getPageSource();

        for (Map<String, String> row : dbRows) {
            String name = row.get("holiday_name");
            String date = row.get("holiday_date");
            boolean found = pageSource.contains(name);

            HRMSDataExcelManager.writeComparisonResult(
                "Holidays", name,
                name + " | " + date,
                found ? name : "(not visible)",
                found ? "" : "Holiday not shown in APK"
            );
        }

        driver.navigate().back();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  TC-DB-08  Travel – compare DB travel requests vs APK
    // ═══════════════════════════════════════════════════════════════════════
    @Test(priority = 8)
    @Severity(SeverityLevel.NORMAL)
    @Description("Compare travel requests from DB with APK travel list")
    public void validateTravel() throws Exception {

        String empCode = utils.ConfigReader.get("username").split("@")[0];
        List<Map<String, String>> dbRows = db.executeQuery(
            "SELECT travel_id, place_to_visit, departure_date, status " +
            "FROM travel_requests " +
            "WHERE emp_code='" + empCode + "' " +
            "ORDER BY departure_date DESC LIMIT 5"
        );

        menu.goToTravel();
        WaitUtils.sleep(2000);
        String pageSource = driver.getPageSource();

        for (Map<String, String> row : dbRows) {
            String travelId = row.get("travel_id");
            String place    = row.get("place_to_visit");
            String date     = row.get("departure_date");
            String status   = row.get("status");
            boolean found   = pageSource.contains(place) || pageSource.contains(date);

            HRMSDataExcelManager.writeComparisonResult(
                "Travel",
                "travel_id=" + travelId + " | " + place,
                place + " | " + date + " | " + status,
                found ? place : "(not visible)",
                found ? "" : "Travel record not found in APK"
            );
        }

        driver.navigate().back();
    }
}
