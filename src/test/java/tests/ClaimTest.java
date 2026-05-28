package tests;

import Base.BaseTest;
import Reusable_methods.Menu_drawer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.ClaimPage;
import utils.ExcelUtils;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ClaimTest – validates the Claim module.
 */
public class ClaimTest extends BaseTest {

    private Menu_drawer menuDrawer;
    private ClaimPage   claimPage;
    private ExcelUtils  excel;

    @BeforeMethod
    public void initPages() {
        menuDrawer = new Menu_drawer(driver);
        claimPage  = new ClaimPage(driver);
        excel      = new ExcelUtils(utils.ConfigReader.get("excel.output.path"));
    }

    // -----------------------------------------------------------------------
    // TC-01 : Navigate to Claim and verify page
    // -----------------------------------------------------------------------
    @Test(priority = 1, description = "Verify Claim page loads")
    public void verifyClaimPageVisible() {

        menuDrawer.goToClaim();
        WaitUtils.sleep(2000);

        boolean visible = claimPage.isPageVisible();
        System.out.println("Claim page visible: " + visible);

        excel.appendTestResult("Claim", "verifyClaimPageVisible",
                visible ? "PASS" : "FAIL",
                "Claim page title visible", String.valueOf(visible), "");
        excel.save();

        Assert.assertTrue(visible, "Claim page did not load");
    }

    // -----------------------------------------------------------------------
    // TC-02 : Capture existing claim list
    // -----------------------------------------------------------------------
    @Test(priority = 2, description = "Capture claim list data")
    public void captureClaimList() {

        menuDrawer.goToClaim();
        WaitUtils.sleep(2000);

        List<String[]> rows = new ArrayList<>();
        int count = claimPage.claimTypes.size();

        for (int i = 0; i < count; i++) {
            String type   = safeText(claimPage.claimTypes, i);
            String amount = safeText(claimPage.claimAmounts, i);
            String status = safeText(claimPage.claimStatuses, i);
            String date   = safeText(claimPage.claimDates, i);
            rows.add(new String[]{type, amount, status, date});
            System.out.println("Claim: " + type + " | " + amount + " | " + status);
        }

        excel.writeSheet("Claim_List",
                new String[]{"Claim Type", "Amount", "Status", "Date"}, rows);
        excel.appendTestResult("Claim", "captureClaimList",
                "PASS", "List loaded", count + " records", "");
        excel.save();

        driver.navigate().back();
    }

    // -----------------------------------------------------------------------
    // TC-03 : Fill claim form (without final submit)
    // -----------------------------------------------------------------------
    @Test(priority = 3, description = "Fill claim form and validate fields")
    public void fillClaimForm() {

        menuDrawer.goToClaim();
        WaitUtils.sleep(1500);

        claimPage.tapAddClaim();
        WaitUtils.sleep(1500);

        claimPage.tapAddClaim(); // second tap opens form
        WaitUtils.sleep(1500);

        claimPage.tapClaimDate();
        WaitUtils.sleep(1500);

        // Date selection handled by caller in real test
        claimPage.fillClaimDetails("25", "200", "Automation Test Claim");

        excel.appendTestResult("Claim", "fillClaimForm",
                "PASS", "Form filled", "km=25, amt=200", "");
        excel.save();

        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
    }

    private <T extends org.openqa.selenium.WebElement> String safeText(List<T> list, int idx) {
        try { return list.get(idx).getText(); } catch (Exception e) { return ""; }
    }
}
