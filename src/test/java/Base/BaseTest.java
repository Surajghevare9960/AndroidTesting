package Base;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.time.Duration;

/**
 * BaseTest – single driver instance shared across all test classes.
 * Wires Allure screenshot-on-failure and ExtentReport logging.
 */
public class BaseTest {

    public static AndroidDriver driver;
    public static WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Suite setup
    // -----------------------------------------------------------------------
    @BeforeSuite
    public void setup() throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName",              "Android");
        caps.setCapability("appium:deviceName",         utils.ConfigReader.get("deviceName"));
        caps.setCapability("appium:automationName",     "UiAutomator2");
        caps.setCapability("appium:appPackage",         utils.ConfigReader.get("appPackage"));
        caps.setCapability("appium:appActivity",        utils.ConfigReader.get("appActivity"));
        caps.setCapability("appium:noReset",            true);
        caps.setCapability("appium:fullReset",          false);
        caps.setCapability("appium:autoGrantPermissions", true);
        caps.setCapability("appium:newCommandTimeout",  300);
        caps.setCapability("appium:adbExecTimeout",     30000);
        caps.setCapability("appium:ignoreHiddenApiPolicyError", true);
        caps.setCapability("appium:uiautomator2ServerLaunchTimeout", 60000);
        caps.setCapability("appium:uiautomator2ServerInstallTimeout", 60000);

        String appiumUrl = utils.ConfigReader.get("appiumUrl");
        driver = new AndroidDriver(new URL(appiumUrl), caps);
        wait   = new WebDriverWait(driver, Duration.ofSeconds(30));

        System.out.println("✅ Driver initialised → " + appiumUrl);
    }

    // -----------------------------------------------------------------------
    // After each test – attach screenshot to Allure on failure
    // -----------------------------------------------------------------------
    @AfterMethod
    public void captureScreenshotOnFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);
                // Attach to Allure report
                Allure.addAttachment(
                        "Screenshot – " + result.getMethod().getMethodName(),
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        "png"
                );
                System.out.println("📸 Screenshot attached to Allure for: "
                        + result.getMethod().getMethodName());
            } catch (Exception e) {
                System.err.println("⚠️  Screenshot failed: " + e.getMessage());
            }
        }
    }

    // -----------------------------------------------------------------------
    // Suite teardown
    // -----------------------------------------------------------------------
    @AfterSuite
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✅ Driver closed.");
        }
    }
}
