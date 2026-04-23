package Setup_Mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;
import java.util.List;

public class Android_Full_test {

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

        //Thread.sleep(3000);

        //driver.findElement(By.xpath("//android.widget.RelativeLayout[@resource-id=\"com.orange.payroll:id/rl_next_screen\"]/android.widget.ImageView")).click();

        Thread.sleep(3000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Open navigation drawer\"]")));
        menu.click();

        // WebDriverWait cl = new WebDriverWait(driver, Duration.ofSeconds(15));
        Thread.sleep(2000);
        driver.findElement(By.xpath("//android.widget.CheckedTextView[@resource-id=\"com.orange.payroll:id/design_menu_item_text\" and @text=\"Clocking\"]")).click();

        Thread.sleep(4000);

        // Clock In
        //driver.findElement(By.id("com.orange.payroll:id/btnIn")).click();

        //Clock Out
        //driver.findElement(By.id("com.orange.payroll:id/btnOut")).click();

        Thread.sleep(3000);

        // Ok

        //driver.findElement(By.id("android:id/button1")).click();

        Thread.sleep(2000);

        driver.navigate().back();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Attendance Check Here

        WebElement menu1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Open navigation drawer\"]")));
        menu1.click();

        Thread.sleep(3000);

        driver.findElement(By.xpath("//android.widget.CheckedTextView[@resource-id=\"com.orange.payroll:id/design_menu_item_text\" and @text=\"Attendance\"]")).click();

        Thread.sleep(3000);

        driver.findElement(By.id("com.orange.payroll:id/imgvwArrow")).click();

        Thread.sleep(7000);

       /* String In = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"com.orange.payroll:id/txtvwInTime\" and @text=\"08:39\"]")).getText();
        System.out.println("Today In = " + In);

        Thread.sleep(1000);

        String Out = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"com.orange.payroll:id/txtvwOutTime\" and @text=\"16:50\"]")).getText();
        System.out.println("Today Out = " + Out); */

        driver.navigate().back();

        Thread.sleep(5000);

        driver.navigate().back();

        //driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc=\"Navigate up\"]")).click();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Leave Check Here

        WebElement menu2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Open navigation drawer\"]")));
        menu2.click();

        Thread.sleep(3000);

        driver.findElement(By.xpath("//android.widget.CheckedTextView[@resource-id=\"com.orange.payroll:id/design_menu_item_text\" and @text=\"Leaves\"]")).click();

        Thread.sleep(3000);

        driver.findElement(By.xpath("//android.widget.TextView[@text=\"STATUS\"]")).click();

        Thread.sleep(3000);

        driver.findElement(By.id("com.orange.payroll:id/addTicketImgView")).click();

        Thread.sleep(3000);

        WebElement Leave = driver.findElement(By.id("android:id/text1"));
        Leave.click();


        Thread.sleep(2000);

        driver.findElement(By.xpath("//android.widget.TextView[@text=\"Casual Leave\"]")).click();

        Thread.sleep(2000);

        driver.findElement(By.id("com.orange.payroll:id/input_dateTime")).click();

        Thread.sleep(2000);

        driver.findElement(By.xpath("//android.view.View[@content-desc=\"11 December 2025\"]")).click(); //નવેમ્બર  //December

        driver.findElement(By.id("com.orange.payroll:id/mdtp_ok")).click();

        Thread.sleep(2000);

        WebElement day = driver.findElement(By.id("com.orange.payroll:id/input_days"));
        String days = "1";
        day.sendKeys(days);

        Thread.sleep(7000);

        WebElement Reason = driver.findElement(By.id("com.orange.payroll:id/input_reason"));
        String reason = "Test";
        Reason.sendKeys(reason);

        //driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"com.orange.payroll:id/buttonSave\"]")).click();

        Thread.sleep(5000);

        driver.navigate().back();

        driver.findElement(By.id("com.orange.payroll:id/buttonChange")).click();

        Thread.sleep(2000);

        driver.navigate().back();

        Thread.sleep(2000);


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Approvals Check Here

        WebElement menu3 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Open navigation drawer\"]")));
        menu3.click();

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

        Thread.sleep(5000);

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

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Thread.sleep(2000);

        // Travel Module Start Here


        WebElement men4 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Open navigation drawer\"]")));
        men4.click();

        Thread.sleep(3000);

        driver.findElement(By.xpath("//android.widget.CheckedTextView[@resource-id=\"com.orange.payroll:id/design_menu_item_text\" and @text=\"Travel\"]")).click();

        Thread.sleep(3000);

        driver.findElement(By.id("com.orange.payroll:id/addTicketImgView")).click();

        Thread.sleep(3000);

        WebElement State = driver.findElement(By.id("com.orange.payroll:id/tv_StateSearch"));
        String state = "Gujarat";
        State.sendKeys(state);

        //Thread.sleep(2000);

        WebElement City = driver.findElement(By.id("com.orange.payroll:id/tv_CitySearch"));
        String city = "Ahmedabad";
        City.sendKeys(city);

        //Thread.sleep(2000);

        WebElement PV = driver.findElement(By.id("com.orange.payroll:id/et_Placevisit_travelDet"));
        String Visit ="Delhi";
        PV.sendKeys(Visit);

        //Thread.sleep(2000);

        WebElement Perpose = driver.findElement(By.id("com.orange.payroll:id/et_Purpose_travelDet"));
        String perpose ="Meeting";
        Perpose.sendKeys(perpose);

        //Thread.sleep(2000);

        WebElement Departure = driver.findElement(By.id("com.orange.payroll:id/et_Departure_date_travelDet"));
        Departure.click();

        // Depature Date Enter Here
        Thread.sleep(2000);
        driver.findElement(By.xpath("//android.view.View[@content-desc=\"17 December 2025\"]")).click();  //નવેમ્બર  //November
        Thread.sleep(2000);
        driver.findElement(By.id("android:id/button1")).click();

        Thread.sleep(2000);

        WebElement Period = driver.findElement(By.id("com.orange.payroll:id/et_Period_travelDet"));
        String period ="1";
        Period.sendKeys(period);

        Thread.sleep(2000);

        WebElement Remark = driver.findElement(By.id("com.orange.payroll:id/et_Remarks_travelDet"));
        String Test = "Testing";
        Remark.sendKeys(Test);

        Thread.sleep(2000);

        driver.navigate().back();

        Thread.sleep(2000);

        driver.navigate().back();

        Thread.sleep(2000);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Thread.sleep(2000);

        // Salary Module Start Here

        WebElement menu4 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Open navigation drawer\"]")));
        menu4.click();

        Thread.sleep(3000);

        driver.findElement(By.xpath("//android.widget.CheckedTextView[@resource-id=\"com.orange.payroll:id/design_menu_item_text\" and @text=\"Salary\"]")).click();

        Thread.sleep(3000);

        List<WebElement> salaryElements = driver.findElements(
                By.id("com.orange.payroll:id/TextView_value_net"));

        if (!salaryElements.isEmpty()) {
            String salary = salaryElements.get(0).getText();
            System.out.println("November Month Salary = " + salary);
        }


        driver.navigate().back();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Thread.sleep(2000);

        // Claim Application Start Here

        WebElement menu5 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Open navigation drawer\"]")));
        menu5.click();

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

        WebElement date = driver.findElement(By.xpath("//android.view.View[@content-desc=\"11 December 2025\"]"));  //નવેમ્બર  //December
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
