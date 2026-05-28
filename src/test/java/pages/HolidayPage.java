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
 * HolidayPage – Page Object for the Holiday module.
 */
public class HolidayPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='Holiday']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    // Year selector
    @FindBy(id = "com.orange.payroll:id/tv_holiday_year")
    public WebElement yearSelector;

    // Holiday list
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_holiday_name']")
    public List<WebElement> holidayNames;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_holiday_date']")
    public List<WebElement> holidayDates;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_holiday_day']")
    public List<WebElement> holidayDays;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_holiday_type']")
    public List<WebElement> holidayTypes;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public HolidayPage(AndroidDriver driver) {
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

    public int getHolidayCount() {
        return holidayNames.size();
    }

    public String getHolidayName(int index) {
        return holidayNames.get(index).getText();
    }

    public String getHolidayDate(int index) {
        return holidayDates.get(index).getText();
    }
}
