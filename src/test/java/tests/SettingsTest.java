package tests;

import Base.BaseTest;
import Reusable_methods.Menu_drawer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.SettingsPage;
import utils.ExcelUtils;
import utils.WaitUtils;

/**
 * SettingsTest – validates the Settings module.
 */
public class SettingsTest extends BaseTest {

    private Menu_drawer  menuDrawer;
    private SettingsPage settingsPage;
    private ExcelUtils   excel;

    @BeforeMethod
    public void initPages() {
        menuDrawer   = new Menu_drawer(driver);
        settingsPage = new SettingsPage(driver);
        excel        = new ExcelUtils(utils.ConfigReader.get("excel.output.path"));
    }

    @Test(priority = 1, description = "Verify Settings page loads")
    public void verifySettingsPageVisible() {

        menuDrawer.goToSettings();
        WaitUtils.sleep(2000);

        boolean visible = settingsPage.isPageVisible();
        excel.appendTestResult("Settings", "verifySettingsPageVisible",
                visible ? "PASS" : "FAIL",
                "Settings page title visible", String.valueOf(visible), "");
        excel.save();

        Assert.assertTrue(visible, "Settings page did not load");
    }

    @Test(priority = 2, description = "Capture app version from Settings")
    public void captureAppVersion() {

        menuDrawer.goToSettings();
        WaitUtils.sleep(2000);

        String version = settingsPage.getAppVersion();
        System.out.println("App Version: " + version);

        excel.appendTestResult("Settings", "captureAppVersion",
                !version.equals("N/A") ? "PASS" : "FAIL",
                "App version displayed", version, "");
        excel.save();

        driver.navigate().back();
    }
}
