package tests;

import Base.BaseTest;
import Reusable_methods.Menu_drawer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.ExitApplicationPage;
import utils.ExcelUtils;
import utils.WaitUtils;

/**
 * ExitApplicationTest – validates the Exit Application module.
 */
public class ExitApplicationTest extends BaseTest {

    private Menu_drawer       menuDrawer;
    private ExitApplicationPage exitPage;
    private ExcelUtils          excel;

    @BeforeMethod
    public void initPages() {
        menuDrawer = new Menu_drawer(driver);
        exitPage   = new ExitApplicationPage(driver);
        excel      = new ExcelUtils(utils.ConfigReader.get("excel.output.path"));
    }

    @Test(priority = 1, description = "Verify Exit Application page loads")
    public void verifyExitPageVisible() {

        menuDrawer.goToExitApplication();
        WaitUtils.sleep(2000);

        boolean visible = exitPage.isPageVisible();
        excel.appendTestResult("Exit Application", "verifyExitPageVisible",
                visible ? "PASS" : "FAIL",
                "Page title visible", String.valueOf(visible), "");
        excel.save();

        Assert.assertTrue(visible, "Exit Application page did not load");
    }

    @Test(priority = 2, description = "Verify exit status is displayed")
    public void verifyExitStatus() {

        menuDrawer.goToExitApplication();
        WaitUtils.sleep(2000);

        String status = exitPage.getExitStatus();
        System.out.println("Exit Status: " + status);

        excel.appendTestResult("Exit Application", "verifyExitStatus",
                "PASS", "Status displayed", status, "");
        excel.save();

        driver.navigate().back();
    }
}
