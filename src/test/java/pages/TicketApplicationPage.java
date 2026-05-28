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
 * TicketApplicationPage – Page Object for the Ticket Application module.
 */
public class TicketApplicationPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='Ticket Application']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/addTicketImgView")
    public WebElement addTicketBtn;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    // Ticket form
    @FindBy(id = "android:id/text1")
    public WebElement ticketTypeDropdown;

    @FindBy(id = "com.orange.payroll:id/et_ticket_subject")
    public WebElement subjectField;

    @FindBy(id = "com.orange.payroll:id/et_ticket_description")
    public WebElement descriptionField;

    @FindBy(id = "com.orange.payroll:id/et_ticket_priority")
    public WebElement priorityField;

    @FindBy(id = "com.orange.payroll:id/buttonSave")
    public WebElement saveBtn;

    // Ticket list
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_ticket_subject']")
    public List<WebElement> ticketSubjects;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_ticket_status']")
    public List<WebElement> ticketStatuses;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_ticket_date']")
    public List<WebElement> ticketDates;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_ticket_id']")
    public List<WebElement> ticketIds;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public TicketApplicationPage(AndroidDriver driver) {
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

    public void tapAddTicket() {
        wait.until(ExpectedConditions.elementToBeClickable(addTicketBtn)).click();
    }

    public void fillTicketDetails(String subject, String description) {
        wait.until(ExpectedConditions.visibilityOf(subjectField)).sendKeys(subject);
        descriptionField.sendKeys(description);
    }

    public void submitTicket() {
        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
    }

    public int getTicketCount() {
        return ticketSubjects.size();
    }
}
