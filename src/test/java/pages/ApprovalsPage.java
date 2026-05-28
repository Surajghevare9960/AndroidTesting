package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * ApprovalsPage – Page Object for the Approvals module.
 * Covers: Leave, Attendance Regularization, Leave Cancellation,
 *         Comp-Off, Ticket, Claim, Exit, Travel approvals.
 */
public class ApprovalsPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Approval type tiles
    // -----------------------------------------------------------------------

    // Approval Page menu Navigation//

    @FindBy(xpath = "//android.widget.CheckedTextView[@resource-id=\"com.orange.payroll:id/design_menu_item_text\" and @text=\"Approvals\"]")
    public WebElement ApprovalModule;





    @FindBy(xpath = "//android.widget.TextView[@text=\"Leave Approvals\"]")
    public WebElement leaveApprovals;

    @FindBy(id = "com.orange.payroll:id/tv_leave_approval")
    public WebElement leaveApprovalsCount;


    @FindBy(xpath = "//android.widget.TextView[@text=\"Attendance Regularization Approvals\"]")
    public WebElement attendanceRegularization;

    @FindBy(id = "/com.orange.payroll:id/tv_attendanceregularization")
    public WebElement attendanceRegularizationCount;


    @FindBy(xpath = "//android.widget.TextView[@text=\"Leave Cancellation Aprovals\"]")
    public WebElement leaveCancellation;

    @FindBy(id = "com.orange.payroll:id/tv_leave_cancleapproval")
    public WebElement leaveCancellationCount;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Comp-Off Approvals\"]")
    public WebElement compOffApproval;

    @FindBy(id = "com.orange.payroll:id/tv_compoff_approval")
    public WebElement compOffApprovalCount;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Ticket Approvals\"]")
    public WebElement ticketApproval;

    @FindBy(id = "com.orange.payroll:id/tv_ticket_approval")
    public WebElement ticketApprovalCount;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Claim Approvals\"]")
    public WebElement claimApproval;

    @FindBy(id = "com.orange.payroll:id/tv_claim_approval")
    public WebElement claimApprovalCount;


    @FindBy(xpath = "//android.widget.TextView[@text=\"Exit Approvals\"]\n")
    public WebElement exitApproval;

    @FindBy(id = "com.orange.payroll:id/tv_exit_approval")
    public WebElement exitApprovalCount;


    @FindBy(xpath = "//android.widget.TextView[@text=\"Travel Approvals\"]")
    public WebElement travelApproval;

    @FindBy(xpath = "com.orange.payroll:id/tv_travel_approval")
    public WebElement travelApprovalCount;

    // -----------------------------------------------------------------------
    // Leave approval list items
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/textViewUserName']")
    public List<WebElement> leaveApplicantNames;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/textViewFromDate']")
    public List<WebElement> leaveFromDates;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/textViewToDate']")
    public List<WebElement> leaveToDates;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/textViewLeaveType']")
    public List<WebElement> leaveTypes;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/textViewLeaveDays']")
    public List<WebElement> leaveDays;

    // -----------------------------------------------------------------------
    // Attendance regularization list items
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/txtvwEmpCode']")
    public List<WebElement> empCodes;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/txtvwAppliedOn']")
    public List<WebElement> appliedOnDates;

    // -----------------------------------------------------------------------
    // Approve / Reject buttons (detail screen)
    // -----------------------------------------------------------------------

    @FindBy(id = "com.orange.payroll:id/btnApprove")
    public WebElement approveBtn;

    @FindBy(id = "com.orange.payroll:id/btnReject")
    public WebElement rejectBtn;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public ApprovalsPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    // -----------------------------------------------------------------------
    // Actions
    // -----------------------------------------------------------------------

    public void openLeaveApprovals() {
        wait.until(ExpectedConditions.elementToBeClickable(leaveApprovals)).click();
    }

    public void openAttendanceRegularization() {
        wait.until(ExpectedConditions.elementToBeClickable(attendanceRegularization)).click();
    }

    public void openLeaveCancellation() {
        wait.until(ExpectedConditions.elementToBeClickable(leaveCancellation)).click();
    }

    public void openCompOff() {
        wait.until(ExpectedConditions.elementToBeClickable(compOffApproval)).click();
    }

    public void openTicketApprovals() {
        wait.until(ExpectedConditions.elementToBeClickable(ticketApproval)).click();
    }

    public void openClaimApprovals() {
        wait.until(ExpectedConditions.elementToBeClickable(claimApproval)).click();
    }

    public void openExitApprovals() {
        wait.until(ExpectedConditions.elementToBeClickable(exitApproval)).click();
    }

    public void openTravelApprovals() {
        wait.until(ExpectedConditions.elementToBeClickable(travelApproval)).click();
    }

    /** Returns the badge count text of a given approval tile. */
    public String getApprovalCount(WebElement tile) {
        return wait.until(ExpectedConditions.visibilityOf(tile)).getText();
    }
}
