package tests;

import Base.BaseTest;
import Reusable_methods.Menu_drawer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HolidayPage;
import utils.ExcelUtils;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * HolidayTest – validates the Holiday module and exports holiday list to Excel.
 */
public class HolidayTest extends BaseTest {

    private Menu_drawer menuDrawer;
    private HolidayPage holidayPage;
    private ExcelUtils  excel;

    @BeforeMethod
    public void initPages() {
        menuDrawer  = new Menu_drawer(driver);
        holidayPage = new HolidayPage(driver);
        excel       = new ExcelUtils(utils.ConfigReader.get("excel.output.path"));
    }

    // -----------------------------------------------------------------------
    // TC-01 : Navigate to Holiday and verify page
    // -----------------------------------------------------------------------
    @Test(priority = 1, description = "Verify Holiday page loads")
    public void verifyHolidayPageVisible() {

        menuDrawer.goToHoliday();
        WaitUtils.sleep(2000);

        boolean visible = holidayPage.isPageVisible();
        System.out.println("Holiday page visible: " + visible);

        excel.appendTestResult("Holiday", "verifyHolidayPageVisible",
                visible ? "PASS" : "FAIL",
                "Holiday page title visible", String.valueOf(visible), "");
        excel.save();

        Assert.assertTrue(visible, "Holiday page did not load");
    }

    // -----------------------------------------------------------------------
    // TC-02 : Capture full holiday list and write to Excel
    // -----------------------------------------------------------------------
    @Test(priority = 2, description = "Capture holiday list and export to Excel")
    public void captureHolidayList() {

        menuDrawer.goToHoliday();
        WaitUtils.sleep(2000);

        int count = holidayPage.getHolidayCount();
        System.out.println("Total holidays found: " + count);

        List<String[]> rows = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String name = safeText(holidayPage.holidayNames, i);
            String date = safeText(holidayPage.holidayDates, i);
            String day  = safeText(holidayPage.holidayDays, i);
            String type = safeText(holidayPage.holidayTypes, i);
            rows.add(new String[]{name, date, day, type});
            System.out.println("Holiday: " + name + " | " + date + " | " + day);
        }

        excel.writeSheet("Holiday_List",
                new String[]{"Holiday Name", "Date", "Day", "Type"}, rows);
        excel.appendTestResult("Holiday", "captureHolidayList",
                count > 0 ? "PASS" : "FAIL",
                "Holiday list loaded", count + " holidays", "");
        excel.save();

        Assert.assertTrue(count > 0, "No holidays found");
        driver.navigate().back();
    }

    private <T extends org.openqa.selenium.WebElement> String safeText(List<T> list, int idx) {
        try { return list.get(idx).getText(); } catch (Exception e) { return ""; }
    }
}
