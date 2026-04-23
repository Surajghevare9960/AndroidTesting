import Setup_Mobile.Android_Setup;
import com.google.errorprone.annotations.ThreadSafe;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;

public class Attendance  {

        AndroidDriver driver;

    @BeforeTest
    public void befortest() throws Exception {
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

        Thread.sleep(3000);


    }


    @Test

    public void attendance() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Open navigation drawer\"]")));
        menu.click();

        wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//android.widget.CheckedTextView[@resource-id='com.orange.payroll:id/design_menu_item_text' and @text='Attendance']")))
                .click();

        // Month And Year Selection

       /* wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//android.widget.TextView[@text=\"March\"]"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//android.widget.CheckedTextView[@resource-id=\"android:id/text1\" and @text=\"March\"]"))).click(); // Add Month Name Here

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//android.widget.TextView[@text=\"2026\"]"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//android.widget.CheckedTextView[@resource-id=\"android:id/text1\" and @text=\"2026\"]"))).click(); // Add Year Name Here

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("com.orange.payroll:id/buttonChange"))).click();*/

        // Present Month By Default

       /* driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().resourceId(\"com.orange.payroll:id/imgvwDownArrow\"));"
        ));*/

        /*___________________________________Request-1_________________________________________________*/

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//android.widget.ImageView[@resource-id='com.orange.payroll:id/imgvwDownArrow'])[1]"))).click(); // Date Number



        wait.until(ExpectedConditions.elementToBeClickable(
                        By.id("com.orange.payroll:id/spinnerRegReason"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//android.widget.TextView[@text=\"TEST\"]"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("com.orange.payroll:id/edttxtReason"))).sendKeys("Test"); // Reason Text

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("com.orange.payroll:id/buttonSubmit"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("android:id/button1"))).click();

        /*___________________________________Request-2_________________________________________________*/


        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//android.widget.ImageView[@resource-id='com.orange.payroll:id/imgvwDownArrow'])[2]"))).click(); // Date Number



        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("com.orange.payroll:id/spinnerRegReason"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//android.widget.TextView[@text=\"TEST\"]"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("com.orange.payroll:id/edttxtReason"))).sendKeys("Test 2"); // Reason Text

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("com.orange.payroll:id/buttonSubmit"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("android:id/button1"))).click();

        /*___________________________________Request-3_________________________________________________*/


        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//android.widget.ImageView[@resource-id='com.orange.payroll:id/imgvwDownArrow'])[3]"))).click(); // Date Number



        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("com.orange.payroll:id/spinnerRegReason"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//android.widget.TextView[@text=\"TEST\"]"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("com.orange.payroll:id/edttxtReason"))).sendKeys("Test 3"); // Reason Text

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("com.orange.payroll:id/buttonSubmit"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("android:id/button1"))).click();






       /* driver.navigate().back();

        Thread.sleep(5000);

        driver.navigate().back();*/







    }




}
