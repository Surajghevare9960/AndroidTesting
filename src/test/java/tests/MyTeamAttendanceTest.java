package tests;

import Base.BaseTest;
import Reusable_methods.Menu_drawer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.MyTeamAttendancePage;
import utils.ExcelUtils;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * MyTeamAttendanceTest – validates the My Team Attendance module.
 */
public class MyTeamAttendanceTest extends BaseTest {

    private Menu_drawer          menuDrawer;
    private MyTeamAttendancePage teamPage;
    private ExcelUtils           excel;

    @BeforeMethod
    public void initPages() {
        menuDrawer = new Menu_drawer(driver);
        teamPage   = new MyTeamAttendancePage(driver);
        excel      = new ExcelUtils(utils.ConfigReader.get("excel.output.path"));
    }

    @Test(priority = 1, description = "Verify My Team Attendance page loads")
    public void verifyTeamAttendancePageVisible() {

        menuDrawer.goToMyTeamAttendance();
        WaitUtils.sleep(2000);

        boolean visible = teamPage.isPageVisible();
        excel.appendTestResult("My Team Attendance", "verifyTeamAttendancePageVisible",
                visible ? "PASS" : "FAIL",
                "Page title visible", String.valueOf(visible), "");
        excel.save();

        Assert.assertTrue(visible, "My Team Attendance page did not load");
    }

    @Test(priority = 2, description = "Capture team attendance data")
    public void captureTeamAttendanceData() {

        menuDrawer.goToMyTeamAttendance();
        WaitUtils.sleep(2000);

        teamPage.switchToTeamTab();
        WaitUtils.sleep(1500);

        int count = teamPage.getTeamMemberCount();
        List<String[]> rows = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String name    = safeText(teamPage.empNames, i);
            String dept    = safeText(teamPage.deptNames, i);
            String present = safeText(teamPage.presentDays, i);
            String inTime  = safeText(teamPage.inTimes, i);
            String outTime = safeText(teamPage.outTimes, i);
            rows.add(new String[]{name, dept, present, inTime, outTime});
            System.out.println("Team: " + name + " | " + dept + " | P:" + present);
        }

        excel.writeSheet("Team_Attendance",
                new String[]{"Employee", "Department", "Present Days", "In Time", "Out Time"}, rows);
        excel.appendTestResult("My Team Attendance", "captureTeamAttendanceData",
                "PASS", "Data captured", count + " members", "");
        excel.save();

        driver.navigate().back();
    }

    private <T extends org.openqa.selenium.WebElement> String safeText(List<T> list, int idx) {
        try { return list.get(idx).getText(); } catch (Exception e) { return ""; }
    }
}
