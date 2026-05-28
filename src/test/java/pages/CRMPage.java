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
 * CRMPage – Page Object for the CRM module.
 */
public class CRMPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='CRM']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    @FindBy(id = "com.orange.payroll:id/addTicketImgView")
    public WebElement addLeadBtn;

    // Lead list
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_crm_lead_name']")
    public List<WebElement> leadNames;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_crm_lead_status']")
    public List<WebElement> leadStatuses;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_crm_lead_date']")
    public List<WebElement> leadDates;

    // Lead form
    @FindBy(id = "com.orange.payroll:id/et_crm_lead_name")
    public WebElement leadNameField;

    @FindBy(id = "com.orange.payroll:id/et_crm_contact")
    public WebElement contactField;

    @FindBy(id = "com.orange.payroll:id/et_crm_remarks")
    public WebElement remarksField;

    @FindBy(id = "com.orange.payroll:id/buttonSave")
    public WebElement saveBtn;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public CRMPage(AndroidDriver driver) {
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

    public void tapAddLead() {
        wait.until(ExpectedConditions.elementToBeClickable(addLeadBtn)).click();
    }

    public void fillLeadDetails(String name, String contact, String remarks) {
        wait.until(ExpectedConditions.visibilityOf(leadNameField)).sendKeys(name);
        contactField.sendKeys(contact);
        remarksField.sendKeys(remarks);
    }

    public void saveLead() {
        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
    }
}
