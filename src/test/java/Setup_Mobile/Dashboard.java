package Setup_Mobile;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;
import java.util.List;

public class Dashboard {

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

    // Reusable Method
    public boolean isElementVisible(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(locator)
            );
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void Login() throws InterruptedException {

        String SN = "qa9999";
        String Username = "0079@mobile";
        String Password = "admin";

        // Wait until login OR home screen appears
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.id("com.orange.payroll:id/input_email")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc='Open navigation drawer']"))
        ));

        // Check Login Screen
        boolean isLoginScreen = driver.findElements(
                By.id("com.orange.payroll:id/input_email")).size() > 0;

        if (isLoginScreen) {

            System.out.println(" Not logged in → Performing Login");

            // Open Server Connection
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("com.orange.payroll:id/buttonServerConnection"))).click();

            // Enter Server Name
            WebElement serverName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("com.orange.payroll:id/serverName")));

            serverName.clear();
            serverName.sendKeys(SN);

            // Connect
            driver.findElement(By.id("com.orange.payroll:id/btn_connect")).click();

            // Handle Optional Popup
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

        // Wait for Home Screen
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//android.widget.ImageButton[@content-desc='Open navigation drawer']")));

        menu.click();

        // Navigate to Dashboard
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//android.widget.CheckedTextView[@resource-id='com.orange.payroll:id/design_menu_item_text' and @text='Dashboard']")
        )).click();

        // Optional wait for screen load
        Thread.sleep(2000);

        // Dashboard UI Validation

        By dashboardTitle = By.xpath("//android.widget.TextView[@text='Dashboard']");
        By refreshButton = By.xpath("//android.widget.ImageView[@resource-id='com.orange.payroll:id/refreshImgView']");
        By notificationBell = By.xpath("//android.widget.RelativeLayout[@resource-id='com.orange.payroll:id/newsArea']/android.widget.ImageView");

// New Elements
        By myProfile = By.xpath("//android.widget.TextView[@text='MY PROFILE']");
        By myAttendance = By.xpath("//android.widget.RelativeLayout[@resource-id='com.orange.payroll:id/myAttendanceLayout']");

        By present = By.xpath("//android.widget.TextView[@text='Present']");
        By leave = By.xpath("//android.widget.TextView[@text='Leave']");
        By absent = By.xpath("//android.widget.TextView[@text='Absent']");
        By onDuty = By.xpath("//android.widget.TextView[@text='On Duty']");
        By holiday = By.xpath("//android.widget.TextView[@text='Holiday']");
        By weekOff = By.xpath("//android.widget.TextView[@text='Week Off']");

        By leaveApproval = By.xpath("//android.widget.LinearLayout[@resource-id='com.orange.payroll:id/leaveApprovalBadgeLayout']");
        By regularizationApproval = By.xpath("//android.widget.LinearLayout[@resource-id='com.orange.payroll:id/regularizationApprovalBadgeLayout']");
        By leaveCancelApproval = By.xpath("//android.widget.LinearLayout[@resource-id='com.orange.payroll:id/leaveCancelApprovalBadgeLayout']");

        By arrowButton = By.xpath("//android.widget.ImageButton[@resource-id='com.orange.payroll:id/arrowImg']");

// Visibility Checks
        boolean isDashboardVisible = isElementVisible(dashboardTitle);
        boolean isRefreshVisible = isElementVisible(refreshButton);
        boolean isNotificationIconVisible = isElementVisible(notificationBell);

        boolean isMyProfileVisible = isElementVisible(myProfile);
        boolean isAttendanceVisible = isElementVisible(myAttendance);

        boolean isPresentVisible = isElementVisible(present);
        boolean isLeaveVisible = isElementVisible(leave);
        boolean isAbsentVisible = isElementVisible(absent);
        boolean isOnDutyVisible = isElementVisible(onDuty);
        boolean isHolidayVisible = isElementVisible(holiday);
        boolean isWeekOffVisible = isElementVisible(weekOff);

        boolean isLeaveApprovalVisible = isElementVisible(leaveApproval);
        boolean isRegularizationVisible = isElementVisible(regularizationApproval);
        boolean isLeaveCancelVisible = isElementVisible(leaveCancelApproval);

        boolean isArrowVisible = isElementVisible(arrowButton);

// Final Validation
        if (isDashboardVisible && isRefreshVisible && isNotificationIconVisible &&
                isMyProfileVisible && isAttendanceVisible &&
                isPresentVisible && isLeaveVisible && isAbsentVisible &&
                isOnDutyVisible && isHolidayVisible && isWeekOffVisible &&
                isLeaveApprovalVisible && isRegularizationVisible && isLeaveCancelVisible &&
                isArrowVisible) {

            System.out.println(" Test Case Passed - Dashboard UI is displayed correctly");

        } else {

            System.out.println(" Test Case Failed - Dashboard UI is NOT correct");

            if (!isDashboardVisible) System.out.println(" Dashboard title not visible");
            if (!isRefreshVisible) System.out.println(" Refresh button not visible");
            if (!isNotificationIconVisible) System.out.println(" Notification icon not visible");

            if (!isMyProfileVisible) System.out.println(" MY PROFILE not visible");
            if (!isAttendanceVisible) System.out.println(" My Attendance section not visible");

            if (!isPresentVisible) System.out.println(" Present not visible");
            if (!isLeaveVisible) System.out.println(" Leave not visible");
            if (!isAbsentVisible) System.out.println(" Absent not visible");
            if (!isOnDutyVisible) System.out.println(" On Duty not visible");
            if (!isHolidayVisible) System.out.println(" Holiday not visible");
            if (!isWeekOffVisible) System.out.println(" Week Off not visible");

            if (!isLeaveApprovalVisible) System.out.println(" Leave Approval not visible");
            if (!isRegularizationVisible) System.out.println(" Regularization Approval not visible");
            if (!isLeaveCancelVisible) System.out.println(" Leave Cancel Approval not visible");

            if (!isArrowVisible) System.out.println(" Arrow button not visible");
        }
    }
}