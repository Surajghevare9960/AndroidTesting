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

    @FindBy (xpath= "//android.widget.Button[@resource-id=\"com.orange.payroll:id/buttonChange\"]")
    WebElement Applybtn ;

    public AttendancePage (AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

}
