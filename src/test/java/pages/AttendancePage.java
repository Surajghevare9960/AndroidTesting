package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AttendancePage {


    AndroidDriver driver;
    WebDriverWait wait;

    //Locators for the attendance Module

    @FindBy(xpath = "//android.widget.CheckedTextView[@resource-id=\"com.orange.payroll:id/design_menu_item_text\" and @text=\"Attendance\"]")
    WebElement Attendance;

    @FindBy (id="com.orange.payroll:id/refreshImgView")
    WebElement Refreshbtn;

    @FindBy (id= "com.orange.payroll:id/tabMyAttendance")
    WebElement Myattendance;

    @FindBy (id = "com.orange.payroll:id/tabTeamAttendance")
    WebElement Teamattendance;

    @FindBy (xpath = "//android.widget.LinearLayout[@resource-id=\"com.orange.payroll:id/linearDate\"]/android.widget.LinearLayout[1]//android.widget.TextView")
    WebElement Monthselection;

    @FindBy (xpath = "//android.widget.LinearLayout[@resource-id=\"com.orange.payroll:id/linearDate\"]/android.widget.LinearLayout[2]//android.widget.TextView")
    WebElement Yearselection;

    @FindBy (xpath= "//android.widget.Button[@resource-id=\"com.orange.payroll:id/buttonChange\"]")
    WebElement Applybtn ;

    @FindBy (xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/txtvwEmpName\"]")
    WebElement empname;

    @FindBy (xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/txtvwDeptName\"]")
    WebElement deptname;

    @FindBy (xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/totalMonthlyHours_tv\"]")
    WebElement totalmonthlyhour;



    public AttendancePage (AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

}
