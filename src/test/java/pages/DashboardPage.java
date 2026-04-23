package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {

    AndroidDriver driver;

    public DashboardPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//android.widget.ImageButton[@content-desc='Open navigation drawer']")
    WebElement menu;

    @FindBy(xpath = "//android.widget.CheckedTextView[@text='Dashboard']")
    WebElement dashboardMenu;

    @FindBy(xpath = "//android.widget.TextView[@text='Dashboard']")
    WebElement dashboardTitle;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    WebElement refresh;

    @FindBy(xpath = "//android.widget.TextView[@text='MY PROFILE']")
    WebElement myProfile;

    public void openDashboard() {
        menu.click();
        dashboardMenu.click();
    }

    public boolean isDashboardVisible() {
        return dashboardTitle.isDisplayed();
    }

    public boolean isRefreshVisible() {
        return refresh.isDisplayed();
    }

    public boolean isMyProfileVisible() {
        return myProfile.isDisplayed();
    }

    public void openDashboard(AndroidDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(ExpectedConditions.visibilityOf(menu)).click();

        wait.until(ExpectedConditions.visibilityOf(dashboardMenu)).click();

    }
}