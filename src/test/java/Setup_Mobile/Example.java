package Setup_Mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;
import java.util.Set;

public class Example {

    AndroidDriver driver;
    WebDriverWait wait;

    // ================== SETUP ==================
    @BeforeTest
    public void setup() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:deviceName", "dd50591");
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("appium:appPackage", "com.orange.payroll");
        caps.setCapability("appium:appActivity", "com.orange.payroll.Activity.SplashActivity");
        caps.setCapability("appium:autoGrantPermissions", true);
        caps.setCapability("appium:noReset", true);
        caps.setCapability("appium:newCommandTimeout", 300);

        try {
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
            System.out.println("Driver initialized successfully!");
            wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Driver initialization failed!");
        }
    }


    // ================== TEST ==================
    @Test
    public void fullPayrollHybridTest() {

        // CLOCKING
        openMenu();
        click(By.xpath("//android.widget.CheckedTextView[@text='Clocking']"));
        driver.navigate().back();

        // ATTENDANCE
        openMenu();
        click(By.xpath("//android.widget.CheckedTextView[@text='Attendance']"));
        click(By.id("com.orange.payroll:id/imgvwArrow"));
        driver.navigate().back();
        driver.navigate().back();

        // LEAVES
        openMenu();
        click(By.xpath("//android.widget.CheckedTextView[@text='Leaves']"));
        click(By.id("com.orange.payroll:id/addTicketImgView"));

        click(By.id("android:id/text1"));
        click(By.xpath("//android.widget.TextView[@text='Casual Leave']"));

        click(By.id("com.orange.payroll:id/input_dateTime"));
        click(By.xpath("//android.view.View[@content-desc='11 December 2025']"));
        click(By.id("com.orange.payroll:id/mdtp_ok"));

        type(By.id("com.orange.payroll:id/input_days"), "1");
        type(By.id("com.orange.payroll:id/input_reason"), "Automation Test");

        driver.navigate().back();
        driver.navigate().back();

        // APPROVALS
        openMenu();
        click(By.xpath("//android.widget.CheckedTextView[@text='Approvals']"));
        click(By.id("com.orange.payroll:id/tv_leave_approval"));
        driver.navigate().back();

        // TRAVEL
        openMenu();
        click(By.xpath("//android.widget.CheckedTextView[@text='Travel']"));
        click(By.id("com.orange.payroll:id/addTicketImgView"));

        type(By.id("com.orange.payroll:id/tv_StateSearch"), "Gujarat");
        type(By.id("com.orange.payroll:id/tv_CitySearch"), "Ahmedabad");
        type(By.id("com.orange.payroll:id/et_Placevisit_travelDet"), "Delhi");
        type(By.id("com.orange.payroll:id/et_Purpose_travelDet"), "Meeting");

        driver.navigate().back();
        driver.navigate().back();

        // SALARY
        openMenu();
        click(By.xpath("//android.widget.CheckedTextView[@text='Salary']"));
        driver.navigate().back();

        // CLAIM (SCROLL)
        openMenu();
        scrollToText("Claim");
        click(By.xpath("//android.widget.CheckedTextView[@text='Claim']"));
        click(By.id("com.orange.payroll:id/addTicketImgView"));

        type(By.id("com.orange.payroll:id/et_kilometer"), "25");
        type(By.id("com.orange.payroll:id/et_ammount"), "200");

        driver.navigate().back();
        driver.navigate().back();
    }

    // ================== UTILS ==================

    private void openMenu() {
        click(By.xpath("//android.widget.ImageButton[@content-desc='Open navigation drawer']"));
    }

    private void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    private void type(By locator, String value) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(value);
    }

    private void scrollToText(String text) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().text(\"" + text + "\"))"));
    }

    // ================== HYBRID SUPPORT ==================
    private void switchToWebView() {
        Set<String> contexts = driver.getContextHandles();
        for (String context : contexts) {
            if (context.contains("WEBVIEW")) {
                driver.context(context);
                break;
            }
        }
    }

    private void switchToNative() {
        driver.context("NATIVE_APP");
    }

    // ================== TEARDOWN ==================
    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
