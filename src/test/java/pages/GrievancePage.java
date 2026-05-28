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
 * GrievancePage – Page Object for the Grievance module.
 */
public class GrievancePage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='Grievance']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/addTicketImgView")
    public WebElement addGrievanceBtn;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    // Grievance form
    @FindBy(id = "com.orange.payroll:id/et_grievance_type")
    public WebElement grievanceTypeField;

    @FindBy(id = "com.orange.payroll:id/et_grievance_subject")
    public WebElement subjectField;

    @FindBy(id = "com.orange.payroll:id/et_grievance_description")
    public WebElement descriptionField;

    @FindBy(id = "com.orange.payroll:id/buttonSave")
    public WebElement saveBtn;

    // Grievance list
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_grievance_subject']")
    public List<WebElement> grievanceSubjects;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_grievance_status']")
    public List<WebElement> grievanceStatuses;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_grievance_date']")
    public List<WebElement> grievanceDates;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public GrievancePage(AndroidDriver driver) {
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

    public void tapAddGrievance() {
        wait.until(ExpectedConditions.elementToBeClickable(addGrievanceBtn)).click();
    }

    public void fillGrievance(String type, String subject, String description) {
        wait.until(ExpectedConditions.visibilityOf(grievanceTypeField)).sendKeys(type);
        subjectField.sendKeys(subject);
        descriptionField.sendKeys(description);
    }

    public void submitGrievance() {
        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
    }
}
