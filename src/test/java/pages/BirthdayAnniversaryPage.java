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
 * BirthdayAnniversaryPage – Page Object for Birthday & Anniversary module.
 */
public class BirthdayAnniversaryPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='Birthday & Anniversary']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    // Tab selectors
    @FindBy(xpath = "//android.widget.TextView[@text='Birthday']")
    public WebElement birthdayTab;

    @FindBy(xpath = "//android.widget.TextView[@text='Anniversary']")
    public WebElement anniversaryTab;

    // Birthday list
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_emp_name']")
    public List<WebElement> employeeNames;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_emp_dob']")
    public List<WebElement> employeeDOBs;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_emp_department']")
    public List<WebElement> employeeDepartments;

    // Anniversary list
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_anniversary_date']")
    public List<WebElement> anniversaryDates;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_years_completed']")
    public List<WebElement> yearsCompleted;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public BirthdayAnniversaryPage(AndroidDriver driver) {
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

    public void switchToBirthday() {
        wait.until(ExpectedConditions.elementToBeClickable(birthdayTab)).click();
    }

    public void switchToAnniversary() {
        wait.until(ExpectedConditions.elementToBeClickable(anniversaryTab)).click();
    }

    public int getBirthdayCount() {
        return employeeNames.size();
    }
}
