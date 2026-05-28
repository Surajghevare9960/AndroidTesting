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
 * MyTeamAttendancePage – Page Object for the My Team Attendance module.
 */
public class MyTeamAttendancePage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='My Team Attendance']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    // Date filter
    @FindBy(xpath = "//android.widget.LinearLayout[@resource-id='com.orange.payroll:id/linearDate']/android.widget.LinearLayout[1]//android.widget.TextView")
    public WebElement monthSelector;

    @FindBy(xpath = "//android.widget.LinearLayout[@resource-id='com.orange.payroll:id/linearDate']/android.widget.LinearLayout[2]//android.widget.TextView")
    public WebElement yearSelector;

    @FindBy(id = "com.orange.payroll:id/buttonChange")
    public WebElement applyBtn;

    // Team member list
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/txtvwEmpName']")
    public List<WebElement> empNames;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/txtvwDeptName']")
    public List<WebElement> deptNames;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/txtvwPdays']")
    public List<WebElement> presentDays;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/txtvwInTime']")
    public List<WebElement> inTimes;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/txtvwOutTime']")
    public List<WebElement> outTimes;

    // Tab
    @FindBy(id = "com.orange.payroll:id/tabTeamAttendance")
    public WebElement teamAttendanceTab;

    @FindBy(id = "com.orange.payroll:id/tabMyAttendance")
    public WebElement myAttendanceTab;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public MyTeamAttendancePage(AndroidDriver driver) {
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

    public void switchToTeamTab() {
        wait.until(ExpectedConditions.elementToBeClickable(teamAttendanceTab)).click();
    }

    public int getTeamMemberCount() {
        return empNames.size();
    }

    public String getEmployeeName(int index) {
        return empNames.get(index).getText();
    }
}
