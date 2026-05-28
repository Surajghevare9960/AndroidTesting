package tests;

import Base.BaseTest;
import Reusable_methods.Menu_drawer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.SalaryPage;
import utils.ExcelUtils;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * SalaryTest – validates the Salary / Payslip module.
 */
public class SalaryTest extends BaseTest {

    private Menu_drawer menuDrawer;
    private SalaryPage  salaryPage;
    private ExcelUtils  excel;

    @BeforeMethod
    public void initPages() {
        menuDrawer = new Menu_drawer(driver);
        salaryPage = new SalaryPage(driver);
        excel      = new ExcelUtils(utils.ConfigReader.get("excel.output.path"));
    }

    // -----------------------------------------------------------------------
    // TC-01 : Navigate to Salary and verify page
    // -----------------------------------------------------------------------
    @Test(priority = 1, description = "Verify Salary page loads")
    public void verifySalaryPageVisible() {

        menuDrawer.goToSalary();
        WaitUtils.sleep(2000);

        boolean visible = salaryPage.isPageVisible();
        System.out.println("Salary page visible: " + visible);

        excel.appendTestResult("Salary", "verifySalaryPageVisible",
                visible ? "PASS" : "FAIL",
                "Salary page title visible", String.valueOf(visible), "");
        excel.save();

        Assert.assertTrue(visible, "Salary page did not load");
    }

    // -----------------------------------------------------------------------
    // TC-02 : Capture net salary and write to Excel
    // -----------------------------------------------------------------------
    @Test(priority = 2, description = "Capture net salary amount")
    public void captureNetSalary() {

        menuDrawer.goToSalary();
        WaitUtils.sleep(2000);

        String netSalary   = salaryPage.getNetSalary();
        String gross       = salaryPage.getGrossSalary();
        String deductions  = salaryPage.getTotalDeduction();

        System.out.println("Net Salary:    " + netSalary);
        System.out.println("Gross Salary:  " + gross);
        System.out.println("Deductions:    " + deductions);

        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Gross Salary",    gross});
        rows.add(new String[]{"Total Deduction", deductions});
        rows.add(new String[]{"Net Salary",      netSalary});

        excel.writeSheet("Salary_Data",
                new String[]{"Component", "Amount"}, rows);

        excel.appendTestResult("Salary", "captureNetSalary",
                !netSalary.isEmpty() ? "PASS" : "FAIL",
                "Net salary displayed", netSalary, "");
        excel.save();

        Assert.assertFalse(netSalary.isEmpty(), "Net salary not displayed");

        driver.navigate().back();
    }
}
