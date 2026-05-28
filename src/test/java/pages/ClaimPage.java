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
 * ClaimPage – Page Object for the Claim module.
 */
public class ClaimPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='Claim']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/addTicketImgView")
    public WebElement addClaimBtn;

    // Claim form
    @FindBy(id = "com.orange.payroll:id/et_date")
    public WebElement claimDateField;

    @FindBy(id = "android:id/text1")
    public WebElement claimTypeDropdown;

    @FindBy(id = "com.orange.payroll:id/et_kilometer")
    public WebElement kilometerField;

    @FindBy(id = "com.orange.payroll:id/et_ammount")
    public WebElement amountField;

    @FindBy(id = "com.orange.payroll:id/et_purpose")
    public WebElement purposeField;

    @FindBy(id = "com.orange.payroll:id/buttonSave")
    public WebElement saveBtn;

    @FindBy(id = "com.orange.payroll:id/mdtp_ok")
    public WebElement datePickerOkBtn;

    // Claim list
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_claim_type']")
    public List<WebElement> claimTypes;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_claim_amount']")
    public List<WebElement> claimAmounts;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_claim_status']")
    public List<WebElement> claimStatuses;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_claim_date']")
    public List<WebElement> claimDates;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public ClaimPage(AndroidDriver driver) {
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

    public void tapAddClaim() {
        wait.until(ExpectedConditions.elementToBeClickable(addClaimBtn)).click();
    }

    public void tapClaimDate() {
        wait.until(ExpectedConditions.elementToBeClickable(claimDateField)).click();
    }

    public void selectClaimType() {
        wait.until(ExpectedConditions.elementToBeClickable(claimTypeDropdown)).click();
    }

    public void fillClaimDetails(String km, String amount, String purpose) {
        if (!km.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(kilometerField)).sendKeys(km);
        }
        amountField.sendKeys(amount);
        purposeField.sendKeys(purpose);
    }

    public void submitClaim() {
        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
    }
}
