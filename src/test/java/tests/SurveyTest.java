package tests;

import Base.BaseTest;
import Reusable_methods.Menu_drawer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.SurveyPage;
import utils.ExcelUtils;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * SurveyTest – validates the Survey module.
 */
public class SurveyTest extends BaseTest {

    private Menu_drawer menuDrawer;
    private SurveyPage  surveyPage;
    private ExcelUtils  excel;

    @BeforeMethod
    public void initPages() {
        menuDrawer = new Menu_drawer(driver);
        surveyPage = new SurveyPage(driver);
        excel      = new ExcelUtils(utils.ConfigReader.get("excel.output.path"));
    }

    @Test(priority = 1, description = "Verify Survey page loads")
    public void verifySurveyPageVisible() {

        menuDrawer.goToSurvey();
        WaitUtils.sleep(2000);

        boolean visible = surveyPage.isPageVisible();
        excel.appendTestResult("Survey", "verifySurveyPageVisible",
                visible ? "PASS" : "FAIL",
                "Page title visible", String.valueOf(visible), "");
        excel.save();

        Assert.assertTrue(visible, "Survey page did not load");
    }

    @Test(priority = 2, description = "Capture survey list")
    public void captureSurveyList() {

        menuDrawer.goToSurvey();
        WaitUtils.sleep(2000);

        int count = surveyPage.getSurveyCount();
        List<String[]> rows = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String title   = safeText(surveyPage.surveyTitles, i);
            String status  = safeText(surveyPage.surveyStatuses, i);
            String dueDate = safeText(surveyPage.surveyDueDates, i);
            rows.add(new String[]{title, status, dueDate});
            System.out.println("Survey: " + title + " | " + status);
        }

        excel.writeSheet("Survey_List",
                new String[]{"Survey Title", "Status", "Due Date"}, rows);
        excel.appendTestResult("Survey", "captureSurveyList",
                "PASS", "List loaded", count + " surveys", "");
        excel.save();

        driver.navigate().back();
    }

    private <T extends org.openqa.selenium.WebElement> String safeText(List<T> list, int idx) {
        try { return list.get(idx).getText(); } catch (Exception e) { return ""; }
    }
}
