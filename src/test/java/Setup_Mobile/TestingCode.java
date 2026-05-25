package Setup_Mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;

public class TestingCode {

    AndroidDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setup() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:deviceName", "dd50591");
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("appium:appPackage", "com.orange.payroll");
        caps.setCapability("appium:appActivity", "com.orange.payroll.Activity.SplashActivity");
        caps.setCapability("appium:appWaitActivity", "com.orange.payroll.Activity.SplashActivity");
        caps.setCapability("appium:autoGrantPermissions", true);
        caps.setCapability("appium:noReset", true);
        caps.setCapability("appium:fullReset", false);
        caps.setCapability("appium:newCommandTimeout", 300);
        caps.setCapability("appium:adbExecTimeout", 30000);
        caps.setCapability("appium:ignoreHiddenApiPolicyError", true); // For Real Device Use

        // Connect to Appium server
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Wait until app fully loads
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//android.widget.ImageButton[@content-desc='Open navigation drawer']")));
    }

    @Test
    public void attendanceTest() {

        // Open Navigation Drawer
        wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//android.widget.ImageButton[@content-desc='Open navigation drawer']")))
                .click();

        // Click Attendance
        wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//android.widget.CheckedTextView[@resource-id='com.orange.payroll:id/design_menu_item_text' and @text='Attendance']")))
                .click();

        // Select Month
        wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//android.widget.TextView[@text='March']")))
                .click();

        wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//android.widget.CheckedTextView[@resource-id='android:id/text1' and @text='February']")))
                .click();

        // Select Year
        wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//android.widget.TextView[@text='2026']")))
                .click();

        wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//android.widget.CheckedTextView[@resource-id='android:id/text1' and @text='2026']")))
                .click();

        // Click Change Button
        wait.until(ExpectedConditions.elementToBeClickable(
                        By.id("com.orange.payroll:id/buttonChange")))
                .click();

        // 🔥 Click the 5th arrow (automatically scrolls if needed)
        clickArrowByIndex(5);

        // Example: Click the 10th arrow
        // clickArrowByNumber(10);
    }

    // ===============================
    // SCROLL + CLICK BY NUMBER (1-based)
    // ===============================
    public void clickArrowByIndex(int index) {
        By locator = By.xpath("(//android.widget.ImageView[@resource-id='com.orange.payroll:id/imgvwDownArrow'])[" + index + "]");
        int maxScrolls = 10;

        for (int i = 0; i < maxScrolls; i++) {
            if (!driver.findElements(locator).isEmpty() && driver.findElement(locator).isDisplayed()) {
                driver.findElement(locator).click();
                return;
            }

            // Scroll forward step by step
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollForward()"));
        }

        throw new RuntimeException("Element not found after scrolling: " + locator.toString());
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}