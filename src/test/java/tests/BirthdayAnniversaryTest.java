package tests;

import Base.BaseTest;
import Reusable_methods.Menu_drawer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.BirthdayAnniversaryPage;
import utils.ExcelUtils;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * BirthdayAnniversaryTest – validates Birthday & Anniversary module.
 */
public class BirthdayAnniversaryTest extends BaseTest {

    private Menu_drawer             menuDrawer;
    private BirthdayAnniversaryPage baPage;
    private ExcelUtils              excel;

    @BeforeMethod
    public void initPages() {
        menuDrawer = new Menu_drawer(driver);
        baPage     = new BirthdayAnniversaryPage(driver);
        excel      = new ExcelUtils(utils.ConfigReader.get("excel.output.path"));
    }

    @Test(priority = 1, description = "Verify Birthday & Anniversary page loads")
    public void verifyPageVisible() {

        menuDrawer.goToBirthdayAnniversary();
        WaitUtils.sleep(2000);

        boolean visible = baPage.isPageVisible();
        excel.appendTestResult("Birthday & Anniversary", "verifyPageVisible",
                visible ? "PASS" : "FAIL",
                "Page title visible", String.valueOf(visible), "");
        excel.save();

        Assert.assertTrue(visible, "Birthday & Anniversary page did not load");
    }

    @Test(priority = 2, description = "Capture birthday list")
    public void captureBirthdayList() {

        menuDrawer.goToBirthdayAnniversary();
        WaitUtils.sleep(2000);

        baPage.switchToBirthday();
        WaitUtils.sleep(1500);

        int count = baPage.getBirthdayCount();
        List<String[]> rows = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String name = safeText(baPage.employeeNames, i);
            String dob  = safeText(baPage.employeeDOBs, i);
            String dept = safeText(baPage.employeeDepartments, i);
            rows.add(new String[]{name, dob, dept});
            System.out.println("Birthday: " + name + " | " + dob);
        }

        excel.writeSheet("Birthday_List",
                new String[]{"Employee Name", "Date of Birth", "Department"}, rows);
        excel.appendTestResult("Birthday & Anniversary", "captureBirthdayList",
                "PASS", "List loaded", count + " records", "");
        excel.save();

        driver.navigate().back();
    }

    @Test(priority = 3, description = "Capture anniversary list")
    public void captureAnniversaryList() {

        menuDrawer.goToBirthdayAnniversary();
        WaitUtils.sleep(2000);

        baPage.switchToAnniversary();
        WaitUtils.sleep(1500);

        List<String[]> rows = new ArrayList<>();
        int count = baPage.employeeNames.size();

        for (int i = 0; i < count; i++) {
            String name  = safeText(baPage.employeeNames, i);
            String date  = safeText(baPage.anniversaryDates, i);
            String years = safeText(baPage.yearsCompleted, i);
            rows.add(new String[]{name, date, years});
        }

        excel.writeSheet("Anniversary_List",
                new String[]{"Employee Name", "Anniversary Date", "Years Completed"}, rows);
        excel.save();

        driver.navigate().back();
    }

    private <T extends org.openqa.selenium.WebElement> String safeText(List<T> list, int idx) {
        try { return list.get(idx).getText(); } catch (Exception e) { return ""; }
    }
}
