package Setup_Mobile;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;
import java.util.List;

public class MyProfile {

    AndroidDriver driver;
    WebDriverWait wait;

    @BeforeTest
    public void beforeTest() throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:deviceName", "dd50591");
        caps.setCapability("appium:automationName", "UiAutomator2");

        caps.setCapability("appium:appPackage", "com.orange.payroll");
        caps.setCapability("appium:appActivity", "com.orange.payroll.Activity.SplashActivity");

        caps.setCapability("appium:noReset", true);
        caps.setCapability("appium:fullReset", false);
        caps.setCapability("appium:autoGrantPermissions", true);
        caps.setCapability("appium:newCommandTimeout", 300);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // Reusable Method
    public boolean isElementVisible(By locator) {
        try {
            WebElement element = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(locator)
            );
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void loginAndOpenProfile() {

        String SN = "qa9999";
        String Username = "0079@mobile";
        String Password = "m5o0t5";

        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.id("com.orange.payroll:id/input_email")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc='Open navigation drawer']"))
        ));

        boolean isLoginScreen = driver.findElements(
                By.id("com.orange.payroll:id/input_email")).size() > 0;

        if (isLoginScreen) {

            System.out.println("Not logged in → Performing Login");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("com.orange.payroll:id/buttonServerConnection"))).click();

            WebElement serverName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("com.orange.payroll:id/serverName")));

            serverName.clear();
            serverName.sendKeys(SN);

            driver.findElement(By.id("com.orange.payroll:id/btn_connect")).click();

            List<WebElement> okPopup = driver.findElements(By.id("android:id/button3"));
            if (okPopup.size() > 0) {
                okPopup.get(0).click();
            }

            WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("com.orange.payroll:id/input_email")));
            username.clear();
            username.sendKeys(Username);

            WebElement password = driver.findElement(
                    By.id("com.orange.payroll:id/input_password"));
            password.clear();
            password.sendKeys(Password);

            driver.findElement(By.id("com.orange.payroll:id/checkBoxRememberMe")).click();
            driver.findElement(By.id("com.orange.payroll:id/buttonLogin")).click();

        } else {
            System.out.println("Already logged in → Skipping Login");
        }

        // 👇 Wait for Dashboard before clicking profile
        By myProfile = By.xpath("//android.widget.TextView[@text='MY PROFILE']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(myProfile));

        // 👇 Call profile method
        profile();
    }

    // Profile Click Method
    public void profile() {
        try {
            By myProfile = By.xpath("//android.widget.TextView[@text='MY PROFILE']");

            WebElement element = wait.until(
                    ExpectedConditions.elementToBeClickable(myProfile)
            );

            element.click();
            System.out.println("Clicked on MY PROFILE");

        } catch (Exception e) {
            System.out.println("Failed to click MY PROFILE");
            e.printStackTrace();
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}