package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * TravelPage – Page Object for the Travel module.
 */
public class TravelPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='Travel']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/addTicketImgView")
    public WebElement addTravelBtn;

    @FindBy(id = "com.orange.payroll:id/tv_StateSearch")
    public WebElement stateField;

    @FindBy(id = "com.orange.payroll:id/tv_CitySearch")
    public WebElement cityField;

    @FindBy(id = "com.orange.payroll:id/et_Placevisit_travelDet")
    public WebElement placeVisitField;

    @FindBy(id = "com.orange.payroll:id/et_Purpose_travelDet")
    public WebElement purposeField;

    @FindBy(id = "com.orange.payroll:id/et_Departure_date_travelDet")
    public WebElement departureDateField;

    @FindBy(id = "com.orange.payroll:id/et_Period_travelDet")
    public WebElement periodField;

    @FindBy(id = "com.orange.payroll:id/et_Remarks_travelDet")
    public WebElement remarksField;

    @FindBy(id = "com.orange.payroll:id/buttonSave")
    public WebElement saveBtn;

    @FindBy(id = "android:id/button1")
    public WebElement okBtn;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    // List items
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_travel_destination']")
    public WebElement travelDestination;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_travel_status']")
    public WebElement travelStatus;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_travel_date']")
    public WebElement travelDate;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public TravelPage(AndroidDriver driver) {
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

    public void tapAddTravel() {
        wait.until(ExpectedConditions.elementToBeClickable(addTravelBtn)).click();
    }

    public void fillTravelDetails(String state, String city, String place,
                                   String purpose, String period, String remarks) {
        wait.until(ExpectedConditions.visibilityOf(stateField)).sendKeys(state);
        cityField.sendKeys(city);
        placeVisitField.sendKeys(place);
        purposeField.sendKeys(purpose);
        periodField.sendKeys(period);
        remarksField.sendKeys(remarks);
    }

    public void tapDepartureDate() {
        wait.until(ExpectedConditions.elementToBeClickable(departureDateField)).click();
    }

    public void submitTravel() {
        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
    }
}
