package Setup_Mobile;

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

public class Approvals {
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
    public void test() throws InterruptedException {

        Thread.sleep(3000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement menu4 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Open navigation drawer\"]")));
        menu4.click();

        Thread.sleep(3000);

        driver.findElement(By.xpath("//android.widget.CheckedTextView[@resource-id=\"com.orange.payroll:id/design_menu_item_text\" and @text=\"Approvals\"]")).click();

        Thread.sleep(3000);

        WebElement LA = driver.findElement(By.id("com.orange.payroll:id/tv_leave_approval"));
        String text = LA.getText();
        System.out.println("Leave Approvals =  " + text);
        LA.click();

        Thread.sleep(3000);

        /*String tt = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"com.orange.payroll:id/textViewUserName\"]")).getText();
        System.out.println("Who Apply Leave = " + tt);*/

        List<WebElement> elements = driver.findElements(By.xpath(
                "//android.widget.TextView[@resource-id='com.orange.payroll:id/textViewUserName'] | " +
                        "//android.widget.TextView[@resource-id='com.orange.payroll:id/textViewFromDate'] | " +
                        "//android.widget.TextView[@resource-id='com.orange.payroll:id/textViewToDate'] | " +
                        "//android.widget.TextView[@resource-id='com.orange.payroll:id/textViewLeaveType'] | " +
                        "//android.widget.TextView[@resource-id='com.orange.payroll:id/textViewLeaveDays'] | " +
                        "//android.widget.TextView[@resource-id='com.orange.payroll:id/textViewDate']"
        ));



        System.out.println(elements.size());
        for (WebElement el : elements) {
            System.out.println(el.getText());
        }


        driver.navigate().back();

        Thread.sleep(3000);

        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*Attendance Regularization-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");

        WebElement AR = driver.findElement(By.id("com.orange.payroll:id/tv_attendanceregularization"));
        String text1 = AR.getText();
        System.out.println("Attendance Regularization Approvals =  " + text1);
        AR.click();

        List<WebElement> Ele = driver.findElements(By.xpath(
                "//android.widget.TextView[@resource-id='com.orange.payroll:id/txtvwEmpCode'] | " +
                        "//android.widget.TextView[@resource-id='com.orange.payroll:id/txtvwAppliedOn']"
        ));


        System.out.println(elements.size());
        for (WebElement t1 : Ele) {
            System.out.println(t1.getText());
        }


        Thread.sleep(3000);


        driver.navigate().back();

        Thread.sleep(3000);

        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-Leave Cancellation*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");

        WebElement LC = driver.findElement(By.id("com.orange.payroll:id/tv_leave_cancleapproval"));
        String text2 = LC.getText();
        System.out.println("Leave Cancellation =  " + text2);
        LC.click();

        Thread.sleep(3000);


        driver.navigate().back();

        Thread.sleep(3000);

        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-Comp-Off*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");

        WebElement C_Off = driver.findElement(By.id("com.orange.payroll:id/tv_compoff_approval"));
        String text3 = C_Off.getText();
        System.out.println("Comp-Off =  " + text3);
        C_Off.click();

        Thread.sleep(3000);


        driver.navigate().back();


        Thread.sleep(3000);

        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-Ticket_Approvals*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");

        WebElement Ticket_Approvals = driver.findElement(By.id("com.orange.payroll:id/tv_ticket_approval"));
        String text4 = Ticket_Approvals.getText();
        System.out.println("Ticket_Approvals =  " + text4);
        Ticket_Approvals.click();

        Thread.sleep(3000);


        driver.navigate().back();

        Thread.sleep(3000);

        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-Claim_Approvals*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");

        WebElement Claim_Approvals = driver.findElement(By.id("com.orange.payroll:id/tv_claim_approval"));
        String text5 = Claim_Approvals.getText();
        System.out.println("Claim_Approvals =  " + text5);
        Claim_Approvals.click();

        Thread.sleep(3000);


        driver.navigate().back();

        Thread.sleep(3000);

        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-Exit_Approvals*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");

        WebElement Exit_Approvals = driver.findElement(By.id("com.orange.payroll:id/tv_exit_approval"));
        String text6 = Exit_Approvals.getText();
        System.out.println("Exit_Approvals =  " + text6);
        Exit_Approvals.click();

        Thread.sleep(3000);


        driver.navigate().back();

        Thread.sleep(3000);

        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-Travel_Approvals*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");

        WebElement Travel_Approvals = driver.findElement(By.id("com.orange.payroll:id/tv_travel_approval"));
        String text7 = Exit_Approvals.getText();
        System.out.println("Travel_Approvals =  " + text7);
        Travel_Approvals.click();

        Thread.sleep(3000);

        driver.navigate().back();

        Thread.sleep(2000);

        driver.navigate().back();




    }
}
