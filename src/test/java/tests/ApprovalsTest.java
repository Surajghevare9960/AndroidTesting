package tests;

import Base.BaseTest;
import Reusable_methods.Menu_drawer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.ApprovalsPage;
import pages.LoginPage;
import utils.ExcelUtils;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ApprovalsTest – validates all approval sub-modules and writes results to Excel.
 */
public class ApprovalsTest extends BaseTest {

    private LoginPage      loginPage;
    private Menu_drawer    menuDrawer;
    private ApprovalsPage  approvalsPage;
    private ExcelUtils     excel;

    @BeforeMethod
    public void initPages() {
        loginPage     = new LoginPage(driver);
        menuDrawer    = new Menu_drawer(driver);
        approvalsPage = new ApprovalsPage(driver);
        excel         = new ExcelUtils(utils.ConfigReader.get("excel.output.path"));
    }

    // -----------------------------------------------------------------------
    // TC-01 : Navigate to Approvals and verify all tiles are visible
    // -----------------------------------------------------------------------
    @Test(priority = 1, description = "Verify all approval tiles are visible")
    public void verifyApprovalTilesVisible() {

        menuDrawer.goToApprovals();

        List<String[]> results = new ArrayList<>();

        String[][] tiles = {
            {"Leave Approval",              approvalsPage.leaveApprovals.isDisplayed()           ? "PASS" : "FAIL"},
            {"Attendance Regularization",   approvalsPage.attendanceRegularization.isDisplayed() ? "PASS" : "FAIL"},
            {"Leave Cancellation",          approvalsPage.leaveCancellation.isDisplayed()        ? "PASS" : "FAIL"},
            {"Comp-Off",                    approvalsPage.compOffApproval.isDisplayed()           ? "PASS" : "FAIL"},
            {"Ticket Approval",             approvalsPage.ticketApproval.isDisplayed()            ? "PASS" : "FAIL"},
            {"Claim Approval",              approvalsPage.claimApproval.isDisplayed()             ? "PASS" : "FAIL"},
            {"Exit Approval",               approvalsPage.exitApproval.isDisplayed()              ? "PASS" : "FAIL"},
            {"Travel Approval",             approvalsPage.travelApproval.isDisplayed()            ? "PASS" : "FAIL"},
        };

        for (String[] tile : tiles) {
            results.add(tile);
            System.out.println(tile[0] + " → " + tile[1]);
        }

        excel.writeSheet("Approvals_Tiles",
                new String[]{"Approval Type", "Visibility Status"}, results);
        excel.save();

        for (String[] tile : tiles) {
            Assert.assertEquals(tile[1], "PASS", tile[0] + " tile not visible");
        }
    }

    // -----------------------------------------------------------------------
    // TC-02 : Open Leave Approvals and capture pending list
    // -----------------------------------------------------------------------
    @Test(priority = 2, description = "Capture leave approval pending list")
    public void captureLeaveApprovalList() {

        menuDrawer.goToApprovals();
        approvalsPage.openLeaveApprovals();

        WaitUtils.sleep(2000);

        List<String[]> rows = new ArrayList<>();
        int count = approvalsPage.leaveApplicantNames.size();

        for (int i = 0; i < count; i++) {
            String name      = safeText(approvalsPage.leaveApplicantNames, i);
            String fromDate  = safeText(approvalsPage.leaveFromDates, i);
            String toDate    = safeText(approvalsPage.leaveToDates, i);
            String type      = safeText(approvalsPage.leaveTypes, i);
            String days      = safeText(approvalsPage.leaveDays, i);
            rows.add(new String[]{name, fromDate, toDate, type, days});
            System.out.println("Leave: " + name + " | " + fromDate + " → " + toDate);
        }

        excel.writeSheet("Leave_Approvals",
                new String[]{"Employee", "From Date", "To Date", "Leave Type", "Days"}, rows);
        excel.save();

        excel.appendTestResult("Approvals", "captureLeaveApprovalList",
                count >= 0 ? "PASS" : "FAIL",
                "List loaded", count + " records", "");

        driver.navigate().back();
    }

    // -----------------------------------------------------------------------
    // TC-03 : Open Attendance Regularization approvals
    // -----------------------------------------------------------------------
    @Test(priority = 3, description = "Capture attendance regularization list")
    public void captureAttendanceRegularizationList() {

        menuDrawer.goToApprovals();
        approvalsPage.openAttendanceRegularization();

        WaitUtils.sleep(2000);

        List<String[]> rows = new ArrayList<>();
        int count = approvalsPage.empCodes.size();

        for (int i = 0; i < count; i++) {
            String empCode  = safeText(approvalsPage.empCodes, i);
            String applied  = safeText(approvalsPage.appliedOnDates, i);
            rows.add(new String[]{empCode, applied});
        }

        excel.writeSheet("Attendance_Regularization",
                new String[]{"Emp Code", "Applied On"}, rows);
        excel.save();

        driver.navigate().back();
    }

    // -----------------------------------------------------------------------
    // Helper
    // -----------------------------------------------------------------------
    private <T extends org.openqa.selenium.WebElement> String safeText(List<T> list, int idx) {
        try { return list.get(idx).getText(); } catch (Exception e) { return ""; }
    }
}
