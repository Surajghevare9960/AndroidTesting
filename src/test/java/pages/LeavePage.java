package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LeavePage {
    AndroidDriver driver;
    WebDriverWait wait;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Leaves\"]")
    public WebElement LeaveTitle;

    @FindBy(xpath = "//android.widget.ImageView[@resource-id=\"com.orange.payroll:id/compoffLeave\"]")
    public WebElement CompoffCalender;


    @FindBy(xpath = "//android.widget.ImageView[@resource-id=\"com.orange.payroll:id/addTicketImgView\"]")
    public WebElement AddLeave;

    @FindBy(xpath = "//android.widget.LinearLayout[@content-desc=\"Balance\"]")
    public WebElement LeaveBalance;

    @FindBy(xpath = "//android.widget.LinearLayout[@content-desc=\"Status\"]")
    public WebElement LeaveStatus;

    @FindBy(xpath = "//android.widget.TextView[@text=\"May\"]")
    public WebElement MonthSelection;

    @FindBy(xpath = "//android.widget.TextView[@text=\"2026\"]")
    public WebElement YearSelection;

    @FindBy(xpath = "//android.widget.Button[@resource-id=\"com.orange.payroll:id/buttonChange\"]")
    public WebElement ApplyButton;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/textViewDateRange']")
    public WebElement LeaveBalanceHeader;

    @FindBy(xpath = "//android.widget.TextView[@text='Code']")
    public WebElement CodeColumn;

    @FindBy(xpath = "//android.widget.TextView[@text='Opening']")
    public WebElement OpendingColumn;

    @FindBy(xpath = "//android.widget.TextView[@text='Used']")
    public WebElement UsedColumn;

    @FindBy(xpath = "//android.widget.TextView[@text='Balance']")
    public WebElement BalanceColumn;

    //---------------------------------------------------------------------------------------------//
          //Status //

    @FindBy(xpath = "//android.widget.TextView[@text=\"From Date\"]")
    public WebElement FromDate;

    @FindBy(xpath = "//android.widget.TextView[@text=\"To Date\"]")
    public WebElement ToDate;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/textviewFromDate\"]")
    public WebElement Fromdateselection;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/textviewToDate\"]")
    public WebElement Todateselection;

    //-------Leave information ---//

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/txtvwLeaveType\"]")
    public WebElement LeaveType;

    @FindBy(xpath = "//android.widget.ImageView[@resource-id=\"com.orange.payroll:id/imgvwLeaveStatus\"]")
    public WebElement LeaveStaus;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Applied On:\"]")
    public WebElement AppliedOnText;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/txtvwLeaveAppliedOn\"]")
    public WebElement LeaveApplieddate;

    @FindBy(xpath = "//android.widget.TextView[@text=\"ID:\"]")
    public WebElement Leaveidtext;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/txtvwLeaveId\"]")
    public WebElement Leaveidnumber;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Leave From\"]")
    public WebElement LeavefromText;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/txtvwLeaveFrom\"]")
    public WebElement LeavefromDate;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Leave To\"]")
    public WebElement LeavetoText;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/txtvwLeaveTo\"]")
    public WebElement LeavetoDate;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Period\"]")
    public WebElement LeaveperiodText;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/txtvwLeavePeriod\"]")
    public WebElement Leaveperiodnumber;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Reason\"]")
    public WebElement LeaveReasonText;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/txtvwLeaveReason\"]")
    public WebElement LeaveReason;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Assigned as: \"]")
    public WebElement AssignedasText;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/txtvwLeaveAssignedAs\"]")
    public WebElement Assignedas;

    @FindBy(xpath = "//android.widget.LinearLayout[@resource-id=\"com.orange.payroll:id/levelContainer\"]/android.widget.ImageView")
    public WebElement Levelcontainer;


    //------------------------Leave Leave Heirachy status -------//

    @FindBy(xpath = "//android.widget.ImageView[@resource-id=\"com.orange.payroll:id/iv_A\"]")
    public WebElement Applicant;

    @FindBy(xpath = "(//android.widget.ImageView[@resource-id=\"com.orange.payroll:id/img_level\"])[1]")
    public WebElement LevelOne;

    @FindBy(xpath = "(//android.widget.ImageView[@resource-id=\"com.orange.payroll:id/img_level\"])[2]")
    public WebElement LevelTwo;

    //--------------------Add Leave Application-------////































































}
