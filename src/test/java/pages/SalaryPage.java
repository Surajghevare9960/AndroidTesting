package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * SalaryPage – Page Object for the Salary / Payslip module.
 */
public class SalaryPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='Salary']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    // Month / Year selector
    @FindBy(xpath = "//android.widget.LinearLayout[@resource-id='com.orange.payroll:id/linearDate']/android.widget.LinearLayout[1]//android.widget.TextView")
    public WebElement monthSelector;

    @FindBy(xpath = "//android.widget.LinearLayout[@resource-id='com.orange.payroll:id/linearDate']/android.widget.LinearLayout[2]//android.widget.TextView")
    public WebElement yearSelector;

    @FindBy(id = "com.orange.payroll:id/buttonChange")
    public WebElement applyBtn;

    // Salary components
    @FindBy(id = "com.orange.payroll:id/TextView_value_net")
    public WebElement netSalary;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_salary_component_name']")
    public List<WebElement> componentNames;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_salary_component_amount']")
    public List<WebElement> componentAmounts;

    @FindBy(id = "com.orange.payroll:id/tv_gross_salary")
    public WebElement grossSalary;

    @FindBy(id = "com.orange.payroll:id/tv_total_deduction")
    public WebElement totalDeduction;

    @FindBy(id = "com.orange.payroll:id/tv_net_salary")
    public WebElement netSalaryLabel;

    // Download payslip
    @FindBy(id = "com.orange.payroll:id/imgvwDownload")
    public WebElement downloadBtn;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public SalaryPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    // -----------------------------------------------------------------------
    // Actions
    // -----------------------------------------------------------------------

    public boolean isPageVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(pageTitle)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getNetSalary() {
        return wait.until(ExpectedConditions.visibilityOf(netSalary)).getText();
    }

    public String getGrossSalary() {
        try {
            return grossSalary.getText();
        } catch (Exception e) {
            return "N/A";
        }
    }

    public String getTotalDeduction() {
        try {
            return totalDeduction.getText();
        } catch (Exception e) {
            return "N/A";
        }
    }

    public void selectMonthYear(String month, String year) {
        wait.until(ExpectedConditions.elementToBeClickable(monthSelector)).click();
        // Month picker handled by caller (date picker dialog)
    }

    public void tapApply() {
        wait.until(ExpectedConditions.elementToBeClickable(applyBtn)).click();
    }

    public void tapDownload() {
        wait.until(ExpectedConditions.elementToBeClickable(downloadBtn)).click();
    }
}
