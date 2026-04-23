import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;
import java.util.List;

public class OrangeLoginTest {

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

        // Keep session
        caps.setCapability("appium:noReset", true);
        caps.setCapability("appium:fullReset", false);

        caps.setCapability("appium:autoGrantPermissions", true);
        caps.setCapability("appium:newCommandTimeout", 300);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void test() throws InterruptedException {

        String SN = "qa9999";
        String Username = "0079@mobile";
        String Password = "admin";

        // Wait until either login screen OR home screen appears
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.id("com.orange.payroll:id/input_email")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc='Open navigation drawer']"))
        ));

        //  Check Login Screen
        boolean isLoginScreen = driver.findElements(
                By.id("com.orange.payroll:id/input_email")).size() > 0;

        if (isLoginScreen) {

            System.out.println("Not logged in → Performing Login");

            // Open Server Connection
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("com.orange.payroll:id/buttonServerConnection"))).click();

            // Enter Server Name
            WebElement serverName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("com.orange.payroll:id/serverName")));

            serverName.clear();   // IMPORTANT FIX
            serverName.sendKeys(SN);

            // Connect
            driver.findElement(By.id("com.orange.payroll:id/btn_connect")).click();
            Thread.sleep(2000);

            //  Handle Optional Popup
            List<WebElement> okPopup = driver.findElements(By.id("android:id/button3"));
            if (okPopup.size() > 0) {
                okPopup.get(0).click();
            }

            // Enter Username
            WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("com.orange.payroll:id/input_email")));
            username.clear();
            username.sendKeys(Username);

            // Enter Password
            WebElement password = driver.findElement(
                    By.id("com.orange.payroll:id/input_password"));
            password.clear();
            password.sendKeys(Password);

            driver.findElement(By.id("com.orange.payroll:id/checkBoxRememberMe")).click();



            // Login
            driver.findElement(By.id("com.orange.payroll:id/buttonLogin")).click();

        } else {
            System.out.println("Already logged in → Skipping Login");
        }

        //  Wait for Home Screen (Reliable)
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//android.widget.ImageButton[@content-desc='Open navigation drawer']")));

        menu.click();

        //  Navigate to Dashboard
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//android.widget.CheckedTextView[@resource-id=\"com.orange.payroll:id/design_menu_item_text\" and @text=\"Dashboard\"]\n"))).click();

        List<WebElement> dashboardList = driver.findElements(
                By.xpath("//android.widget.TextView[@text='Dashboard']")
        );

        if (dashboardList.size() > 0 && dashboardList.get(0).isDisplayed()) {
            System.out.println("Test Case Passed");
        } else {
            System.out.println("Test Case Failed");
        }


        System.out.println("Test Completed Successfully");
    }
}