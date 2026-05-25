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

public class Check_In_Out_Status  {


    AndroidDriver driver;


    @BeforeTest
    public void befortest() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:deviceName", "dd50591");
       // caps.setCapability("appium:deviceName", "emulator-5556");
       // caps.setCapability("appium:deviceName", "emulator-5558");
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
    public void test() throws InterruptedException {

        //Thread.sleep(3000);

        //driver.findElement(By.xpath("//android.widget.RelativeLayout[@resource-id=\"com.orange.payroll:id/rl_next_screen\"]/android.widget.ImageView")).click();

        Thread.sleep(3000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Open navigation drawer\"]")));
        menu.click();

        // WebDriverWait cl = new WebDriverWait(driver, Duration.ofSeconds(15));
        Thread.sleep(2000);
        driver.findElement(By.xpath("//android.widget.CheckedTextView[@resource-id=\"com.orange.payroll:id/design_menu_item_text\" and @text=\"Clocking\"]")).click();

        Thread.sleep(2000);

        // Clock In
        //driver.findElement(By.id("com.orange.payroll:id/btnIn")).click();



        //Clock Out
        //driver.findElement(By.id("com.orange.payroll:id/btnOut")).click();

        Thread.sleep(3000);

        // Ok

        //driver.findElement(By.id("android:id/button1")).click();






    }


}
