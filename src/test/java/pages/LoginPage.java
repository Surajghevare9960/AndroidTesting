package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    AndroidDriver driver;

    public LoginPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Next screen
    @FindBy(id = "com.orange.payroll:id/rl_next_screen")
    WebElement nextScreen;

    @FindBy(id = "com.orange.payroll:id/input_email")
    WebElement username;

    @FindBy(id = "com.orange.payroll:id/input_password")
    WebElement password;

    @FindBy(id = "com.orange.payroll:id/buttonLogin")
    WebElement loginBtn;

    @FindBy(id = "com.orange.payroll:id/buttonServerConnection")
    WebElement serverBtn;

    @FindBy(id = "com.orange.payroll:id/serverName")
    WebElement serverName;

    @FindBy(id = "android:id/button3")
    WebElement okbutton;

    @FindBy(id = "com.orange.payroll:id/btn_connect")
    WebElement connectBtn;

    // =============================
    // LOGIN FLOW
    // =============================

    public void login(String sn, String user, String pass) throws InterruptedException {

        // Check if Login screen is already visible
        boolean isLoginScreen = driver.findElements(
                By.id("com.orange.payroll:id/input_email")).size() > 0;

        // If NOT login screen → click next screen
        if (!isLoginScreen) {

            System.out.println("Next screen detected → navigating...");

            // Click next screen twice (if present)
            for (int i = 0; i < 2; i++) {
                if (driver.findElements(By.id("com.orange.payroll:id/rl_next_screen")).size() > 0) {
                    nextScreen.click();
                } else {
                    break;
                }
            }
        } else {
            System.out.println("Already on Login screen → skipping next screen");
        }

        // Server Connection
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        if (driver.findElements(By.id("com.orange.payroll:id/buttonServerConnection")).size() > 0) {

            wait.until(ExpectedConditions.elementToBeClickable(serverBtn)).click();

            wait.until(ExpectedConditions.visibilityOf(serverName)).sendKeys(sn);

            wait.until(ExpectedConditions.elementToBeClickable(connectBtn)).click();

            // WAIT for popup and click OK
            //By okBtn = By.id("android:id/button3");

            wait.until(ExpectedConditions.elementToBeClickable(okbutton)).click();
        }

        // Login
        username.sendKeys(user);
        password.sendKeys(pass);
        loginBtn.click();
    }
}