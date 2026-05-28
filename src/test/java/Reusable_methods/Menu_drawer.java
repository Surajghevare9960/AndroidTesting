package Reusable_methods;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ScrollUtils;

import java.time.Duration;

/**
 * Menu_drawer – reusable navigation helper for the side-drawer menu.
 * Supports all HRMS modules including newly added ones.
 */
public class Menu_drawer {

    private final AndroidDriver driver;
    private final WebDriverWait wait;
    private final ScrollUtils scrollUtils;

    @FindBy(xpath = "//android.widget.ImageButton[@content-desc='Open navigation drawer']")
    private WebElement menuBtn;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public Menu_drawer(AndroidDriver driver) {
        this.driver      = driver;
        this.wait        = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.scrollUtils = new ScrollUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // -----------------------------------------------------------------------
    // Open drawer
    // -----------------------------------------------------------------------

    public void clickMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(menuBtn)).click();
    }

    // -----------------------------------------------------------------------
    // Navigate to any module by menu text
    // Scrolls if the item is not immediately visible
    // -----------------------------------------------------------------------

    public void navigateTo(String menuText) {
        clickMenu();
        try {
            // Try direct click first
            scrollUtils.scrollToText(menuText).click();
        } catch (Exception e) {
            System.out.println("⚠️  Could not navigate to: " + menuText + " → " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Convenience methods for every module
    // -----------------------------------------------------------------------

    public void goToDashboard()          { navigateTo("Dashboard"); }
    public void goToAttendance()         { navigateTo("Attendance"); }
    public void goToLeaves()             { navigateTo("Leaves"); }
    public void goToClocking()           { navigateTo("Clocking"); }
    public void goToApprovals()          { navigateTo("Approvals"); }
    public void goToChangeRequest()      { navigateTo("Change Request"); }
    public void goToTravel()             { navigateTo("Travel"); }
    public void goToSalary()             { navigateTo("Salary"); }
    public void goToPhotoGallery()       { navigateTo("Photo Gallery"); }
    public void goToGrievance()          { navigateTo("Grievance"); }
    public void goToCanteen()            { navigateTo("Canteen"); }
    public void goToCRM()                { navigateTo("CRM"); }
    public void goToBirthdayAnniversary(){ navigateTo("Birthday & Anniversary"); }
    public void goToHoliday()            { navigateTo("Holiday"); }
    public void goToTicketApplication()  { navigateTo("Ticket Application"); }
    public void goToSurvey()             { navigateTo("Survey"); }
    public void goToPolicyDocuments()    { navigateTo("Policy & Documents"); }
    public void goToClaim()              { navigateTo("Claim"); }
    public void goToMyTeamAttendance()   { navigateTo("My Team Attendance"); }
    public void goToExitApplication()    { navigateTo("Exit Application"); }
    public void goToSettings()           { navigateTo("Settings"); }
    public void goToMyProfile()          { navigateTo("My Profile"); }

    /** Sign out from the app. */
    public void signOut() {
        clickMenu();
        try {
            scrollUtils.scrollToText("Sign Out").click();
        } catch (Exception e) {
            // Some apps use "Logout"
            try {
                scrollUtils.scrollToText("Logout").click();
            } catch (Exception ex) {
                System.out.println("⚠️  Sign out option not found.");
            }
        }
    }
}
