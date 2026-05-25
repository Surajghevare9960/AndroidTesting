package Setup_Mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.swing.plaf.TableHeaderUI;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public class Claim {
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

        // 🔧 Add these two lines for UiAutomator2 stability
        caps.setCapability("appium:uiautomator2ServerLaunchTimeout", 60000);
        caps.setCapability("appium:uiautomator2ServerInstallTimeout", 60000);
        // Connect to Appium server
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);

        Thread.sleep(3000);
    }

    @Test
    public void test() throws InterruptedException {

        Thread.sleep(3000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Open navigation drawer\"]")));
        menu.click();

        Thread.sleep(3000);

        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().text(\"Claim\"));"
        ));



        driver.findElement(By.xpath("//android.widget.CheckedTextView[@resource-id=\"com.orange.payroll:id/design_menu_item_text\" and @text=\"Claim\"]")).click();

        Thread.sleep(3000);

        driver.findElement(By.id("com.orange.payroll:id/addTicketImgView")).click();

        Thread.sleep(2000);

        driver.findElement(By.id("com.orange.payroll:id/addTicketImgView")).click();

        Thread.sleep(2000);

        driver.findElement(By.id("com.orange.payroll:id/et_date")).click();

        Thread.sleep(2000);

        WebElement date = driver.findElement(By.xpath("//android.view.View[@content-desc=\"04 December 2025 selected\"]"));
        date.isDisplayed();
        date.click();

        Thread.sleep(2000);

        driver.findElement(By.id("com.orange.payroll:id/mdtp_ok")).click();

        Thread.sleep(2000);

        driver.findElement(By.id("android:id/text1")).click();

        Thread.sleep(2000);

        driver.findElement(By.xpath("//android.widget.TextView[@text=\"Petrol Bike\"]")).click();

        Thread.sleep(2000);

        WebElement KiloMeter = driver.findElement(By.id("com.orange.payroll:id/et_kilometer"));
        String km = "25";
        KiloMeter.sendKeys(km);

        Thread.sleep(2000);

        WebElement Ammount = driver.findElement(By.id("com.orange.payroll:id/et_ammount"));
        String amt = "200";
        Ammount.sendKeys(amt);

        Thread.sleep(2000);

        WebElement RM = driver.findElement(By.id("com.orange.payroll:id/et_purpose"));
        String rm = "Test";
        RM.sendKeys(rm);

        driver.navigate().back();

        Thread.sleep(2000);

        driver.navigate().back();

        Thread.sleep(2000);

        driver.navigate().back();














    }
}
