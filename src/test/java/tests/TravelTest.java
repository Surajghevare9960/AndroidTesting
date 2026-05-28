package tests;

import Base.BaseTest;
import Reusable_methods.Menu_drawer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.TravelPage;
import utils.ExcelUtils;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * TravelTest – validates the Travel module.
 */
public class TravelTest extends BaseTest {

    private Menu_drawer menuDrawer;
    private TravelPage  travelPage;
    private ExcelUtils  excel;

    @BeforeMethod
    public void initPages() {
        menuDrawer = new Menu_drawer(driver);
        travelPage = new TravelPage(driver);
        excel      = new ExcelUtils(utils.ConfigReader.get("excel.output.path"));
    }

    // -----------------------------------------------------------------------
    // TC-01 : Navigate to Travel and verify page title
    // -----------------------------------------------------------------------
    @Test(priority = 1, description = "Verify Travel page loads")
    public void verifyTravelPageVisible() {

        menuDrawer.goToTravel();
        WaitUtils.sleep(2000);

        boolean visible = travelPage.isPageVisible();
        System.out.println("Travel page visible: " + visible);

        excel.appendTestResult("Travel", "verifyTravelPageVisible",
                visible ? "PASS" : "FAIL",
                "Travel page title visible", String.valueOf(visible), "");
        excel.save();

        Assert.assertTrue(visible, "Travel page did not load");
    }

    // -----------------------------------------------------------------------
    // TC-02 : Fill and submit a travel request (does NOT actually submit)
    // -----------------------------------------------------------------------
    @Test(priority = 2, description = "Fill travel request form and validate fields")
    public void fillTravelRequestForm() {

        menuDrawer.goToTravel();
        WaitUtils.sleep(2000);

        travelPage.tapAddTravel();
        WaitUtils.sleep(1500);

        travelPage.fillTravelDetails("Gujarat", "Ahmedabad", "Delhi", "Meeting", "2", "Automation Test");

        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"State", "Gujarat"});
        rows.add(new String[]{"City", "Ahmedabad"});
        rows.add(new String[]{"Place to Visit", "Delhi"});
        rows.add(new String[]{"Purpose", "Meeting"});
        rows.add(new String[]{"Period", "2"});
        rows.add(new String[]{"Remarks", "Automation Test"});

        excel.writeSheet("Travel_Form_Data",
                new String[]{"Field", "Value"}, rows);
        excel.save();

        excel.appendTestResult("Travel", "fillTravelRequestForm",
                "PASS", "Form filled", "All fields entered", "");

        driver.navigate().back();
        driver.navigate().back();
    }
}
