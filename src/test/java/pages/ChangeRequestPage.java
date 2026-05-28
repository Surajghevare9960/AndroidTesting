package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * ChangeRequestPage – Page Object for the Change Request module.
 */
public class ChangeRequestPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='Change Request']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/addTicketImgView")
    public WebElement addRequestBtn;

    @FindBy(id = "com.orange.payroll:id/et_change_request_type")
    public WebElement requestTypeField;

    @FindBy(id = "com.orange.payroll:id/et_change_request_description")
    public WebElement descriptionField;

    @FindBy(id = "com.orange.payroll:id/et_change_request_date")
    public WebElement requestDateField;

    @FindBy(id = "com.orange.payroll:id/buttonSave")
    public WebElement saveBtn;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    // List items
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_request_type']")
    public WebElement requestTypeText;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_request_status']")
    public WebElement requestStatusText;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_request_date']")
    public WebElement requestDateText;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public ChangeRequestPage(AndroidDriver driver) {
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

    public void tapAddRequest() {
        wait.until(ExpectedConditions.elementToBeClickable(addRequestBtn)).click();
    }

    public void fillChangeRequest(String type, String description) {
        wait.until(ExpectedConditions.visibilityOf(requestTypeField)).sendKeys(type);
        descriptionField.sendKeys(description);
    }

    public void submitRequest() {
        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
    }
}
