package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * SettingsPage – Page Object for the Settings module.
 */
public class SettingsPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='Settings']")
    public WebElement pageTitle;

    // Change Password
    @FindBy(id = "com.orange.payroll:id/tv_change_password")
    public WebElement changePasswordOption;

    @FindBy(id = "com.orange.payroll:id/et_old_password")
    public WebElement oldPasswordField;

    @FindBy(id = "com.orange.payroll:id/et_new_password")
    public WebElement newPasswordField;

    @FindBy(id = "com.orange.payroll:id/et_confirm_password")
    public WebElement confirmPasswordField;

    @FindBy(id = "com.orange.payroll:id/btn_update_password")
    public WebElement updatePasswordBtn;

    // Notification settings
    @FindBy(id = "com.orange.payroll:id/switch_notifications")
    public WebElement notificationSwitch;

    // Language settings
    @FindBy(id = "com.orange.payroll:id/tv_language")
    public WebElement languageOption;

    // App version
    @FindBy(id = "com.orange.payroll:id/tv_app_version")
    public WebElement appVersion;

    // Sign out
    @FindBy(id = "com.orange.payroll:id/tv_sign_out")
    public WebElement signOutBtn;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public SettingsPage(AndroidDriver driver) {
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

    public void tapChangePassword() {
        wait.until(ExpectedConditions.elementToBeClickable(changePasswordOption)).click();
    }

    public void changePassword(String oldPass, String newPass, String confirmPass) {
        wait.until(ExpectedConditions.visibilityOf(oldPasswordField)).sendKeys(oldPass);
        newPasswordField.sendKeys(newPass);
        confirmPasswordField.sendKeys(confirmPass);
        updatePasswordBtn.click();
    }

    public String getAppVersion() {
        try {
            return appVersion.getText();
        } catch (Exception e) {
            return "N/A";
        }
    }

    public void tapSignOut() {
        wait.until(ExpectedConditions.elementToBeClickable(signOutBtn)).click();
    }
}
