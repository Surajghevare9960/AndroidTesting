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
 * SurveyPage – Page Object for the Survey module.
 */
public class SurveyPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='Survey']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    // Survey list
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_survey_title']")
    public List<WebElement> surveyTitles;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_survey_status']")
    public List<WebElement> surveyStatuses;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_survey_due_date']")
    public List<WebElement> surveyDueDates;

    // Survey detail / answer
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_question']")
    public List<WebElement> surveyQuestions;

    @FindBy(id = "com.orange.payroll:id/btn_submit_survey")
    public WebElement submitSurveyBtn;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public SurveyPage(AndroidDriver driver) {
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

    public int getSurveyCount() {
        return surveyTitles.size();
    }

    public void openSurvey(int index) {
        if (index < surveyTitles.size()) {
            wait.until(ExpectedConditions.elementToBeClickable(surveyTitles.get(index))).click();
        }
    }

    public void submitSurvey() {
        wait.until(ExpectedConditions.elementToBeClickable(submitSurveyBtn)).click();
    }
}
