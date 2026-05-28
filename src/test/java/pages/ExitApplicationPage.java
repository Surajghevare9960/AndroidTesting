package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * ExitApplicationPage – Page Object for the Exit Application module.
 */
public class ExitApplicationPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='Exit Application']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/addTicketImgView")
    public WebElement applyExitBtn;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    // Exit form
    @FindBy(id = "com.orange.payroll:id/et_exit_reason")
    public WebElement exitReasonField;

    @FindBy(id = "com.orange.payroll:id/et_exit_date")
    public WebElement exitDateField;

    @FindBy(id = "com.orange.payroll:id/et_exit_remarks")
    public WebElement remarksField;

    @FindBy(id = "com.orange.payroll:id/buttonSave")
    public WebElement saveBtn;

    // Status display
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_exit_status']")
    public WebElement exitStatus;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_exit_applied_date']")
    public WebElement exitAppliedDate;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_exit_reason']")
    public WebElement exitReasonText;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public ExitApplicationPage(AndroidDriver driver) {
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

    public void tapApplyExit() {
        wait.until(ExpectedConditions.elementToBeClickable(applyExitBtn)).click();
    }

    public void fillExitDetails(String reason, String remarks) {
        wait.until(ExpectedConditions.visibilityOf(exitReasonField)).sendKeys(reason);
        remarksField.sendKeys(remarks);
    }

    public void tapExitDate() {
        wait.until(ExpectedConditions.elementToBeClickable(exitDateField)).click();
    }

    public void submitExit() {
        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
    }

    public String getExitStatus() {
        try {
            return exitStatus.getText();
        } catch (Exception e) {
            return "N/A";
        }
    }
}
