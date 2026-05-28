package tests;

import Base.BaseTest;
import Reusable_methods.Menu_drawer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.PolicyDocumentsPage;
import utils.ExcelUtils;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * PolicyDocumentsTest – validates the Policy & Documents module.
 */
public class PolicyDocumentsTest extends BaseTest {

    private Menu_drawer        menuDrawer;
    private PolicyDocumentsPage policyPage;
    private ExcelUtils          excel;

    @BeforeMethod
    public void initPages() {
        menuDrawer = new Menu_drawer(driver);
        policyPage = new PolicyDocumentsPage(driver);
        excel      = new ExcelUtils(utils.ConfigReader.get("excel.output.path"));
    }

    @Test(priority = 1, description = "Verify Policy & Documents page loads")
    public void verifyPolicyPageVisible() {

        menuDrawer.goToPolicyDocuments();
        WaitUtils.sleep(2000);

        boolean visible = policyPage.isPageVisible();
        excel.appendTestResult("Policy & Documents", "verifyPolicyPageVisible",
                visible ? "PASS" : "FAIL",
                "Page title visible", String.valueOf(visible), "");
        excel.save();

        Assert.assertTrue(visible, "Policy & Documents page did not load");
    }

    @Test(priority = 2, description = "Capture document list")
    public void captureDocumentList() {

        menuDrawer.goToPolicyDocuments();
        WaitUtils.sleep(2000);

        int count = policyPage.getDocumentCount();
        List<String[]> rows = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String name = safeText(policyPage.documentNames, i);
            String type = safeText(policyPage.documentTypes, i);
            String date = safeText(policyPage.documentDates, i);
            rows.add(new String[]{name, type, date});
            System.out.println("Document: " + name + " | " + type);
        }

        excel.writeSheet("Policy_Documents",
                new String[]{"Document Name", "Type", "Date"}, rows);
        excel.appendTestResult("Policy & Documents", "captureDocumentList",
                "PASS", "List loaded", count + " documents", "");
        excel.save();

        driver.navigate().back();
    }

    private <T extends org.openqa.selenium.WebElement> String safeText(List<T> list, int idx) {
        try { return list.get(idx).getText(); } catch (Exception e) { return ""; }
    }
}
