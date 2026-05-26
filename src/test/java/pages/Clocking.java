package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Clocking {

    AndroidDriver driver;
    static WebDriverWait wait;

    public Clocking(AndroidDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    // ==============================
    // ELEMENTS
    // ==============================

    @FindBy(xpath = "//android.widget.ImageButton[@content-desc='Open navigation drawer']")
    static WebElement Menu;

    @FindBy(xpath = "//android.widget.CheckedTextView[@resource-id='com.orange.payroll:id/design_menu_item_text' and @text='Clocking']")
    static WebElement Clockingmodule;

    @FindBy(id = "com.orange.payroll:id/txtvwClockingType")
    static WebElement clockingType;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/txtvwReason\" and @text=\"Clock in\"]")
    static WebElement Clockin;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.orange.payroll:id/txtvwReason\" and @text=\"Clock out\"]")
    static WebElement Clockout;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"android:id/message\"]")
    static WebElement ClockMessage;

    @FindBy(xpath = "//android.widget.Button[@resource-id=\"android:id/button1\"]")
    static WebElement CloseMessage;

    @FindBy(id = "com.orange.payroll:id/btnIn")
    static WebElement clockInBtn;

    @FindBy(id = "com.orange.payroll:id/btnOut")
    static WebElement clockOutBtn;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/txtvwCheckStatus']")
    static WebElement clockStatusText;

    @FindBy(xpath = "//android.widget.ImageView[@content-desc='Done']")
    WebElement doneBtn;

    // ==============================
    // NAVIGATION
    // ==============================

    public static void navigateToClocking() {

        try {

            Thread.sleep(2000);

            wait.until(ExpectedConditions
                            .elementToBeClickable(Menu))
                    .click();

            wait.until(ExpectedConditions
                            .elementToBeClickable(Clockingmodule))
                    .click();

        } catch (Exception e) {

            System.out.println("Navigation failed: "
                    + e.getMessage());
        }
    }

    // ==============================
    // CLOCKING LOOP
    // ==============================

    public static void performClockingLoop(int maxAttempts) {

        for (int i = 0; i < maxAttempts; i++) {

            try {

                String status =
                        getClockStatus().toLowerCase();

                System.out.println("Current Status: "
                        + status);

                // ==========================
                // CLOCK IN
                // ==========================

                if (status.contains("clocked-out")) {

                    System.out.println(
                            "Performing Clock IN...");

                    openClockingType();

                    wait.until(ExpectedConditions
                                    .elementToBeClickable(Clockin))
                            .click();

                    wait.until(ExpectedConditions
                                    .elementToBeClickable(clockInBtn))
                            .click();
                    String message = wait.until(
                                    ExpectedConditions.visibilityOf(ClockMessage))
                            .getText();
                    System.out.println(message);

                    wait.until(ExpectedConditions
                                    .elementToBeClickable(CloseMessage))
                            .click();


                    Thread.sleep(3000);

                    String updatedStatus =
                            getClockStatus().toLowerCase();

                    if (updatedStatus.contains("clocked-in")) {

                        System.out.println(
                                "Successfully Clocked IN");

                    } else {

                        System.out.println(
                                "Clock IN failed");
                    }
                }

                // ==========================
                // CLOCK OUT
                // ==========================

                else if (status.contains("clocked-in")) {

                    System.out.println(
                            "Performing Clock OUT...");

                    openClockingType();

                    wait.until(ExpectedConditions
                                    .elementToBeClickable(Clockout))
                            .click();

                    wait.until(ExpectedConditions
                                    .elementToBeClickable(clockOutBtn))
                            .click();
                    String message = wait.until(
                                    ExpectedConditions.visibilityOf(ClockMessage))
                            .getText();
                    System.out.println(message);

                    wait.until(ExpectedConditions
                                    .elementToBeClickable(CloseMessage))
                            .click();

                    Thread.sleep(3000);

                    String updatedStatus =
                            getClockStatus().toLowerCase();

                    if (updatedStatus.contains("clocked-out")) {

                        System.out.println(
                                "Successfully Clocked OUT");

                    } else {

                        System.out.println(
                                "Clock OUT failed");
                    }
                }

                // ==========================
                // UNKNOWN STATUS
                // ==========================

                else {

                    System.out.println(
                            "Unknown status detected: "
                                    + status);

                    break;
                }

            } catch (Exception e) {

                System.out.println(
                        "Error in loop: "
                                + e.getMessage());
            }
        }
    }

    // ==============================
    // CLOCKING METHODS
    // ==============================

    public static void openClockingType() {

        wait.until(ExpectedConditions
                        .elementToBeClickable(clockingType))
                .click();
    }

//    public static void selectClockingType(String typeName) {
//
//        By option = By.xpath(
//                "//*[@resource-id='com.orange.payroll:id/txtvwReason' and @text='"
//                        + typeName + "']");
//
//        wait.until(ExpectedConditions
//                .visibilityOfElementLocated(option));
//
//        wait.until(ExpectedConditions
//                        .elementToBeClickable(option))
//                .click();
//    }
//
//    public void clockIn(String typeName) {
//
//        openClockingType();
//
//        selectClockingType(typeName);
//
//        wait.until(ExpectedConditions
//                        .elementToBeClickable(clockInBtn))
//                .click();
//    }
//
//    public void clockOut(String typeName) {
//
//        openClockingType();
//
//        selectClockingType(typeName);
//
//        wait.until(ExpectedConditions
//                        .elementToBeClickable(clockOutBtn))
//                .click();
//    }

    // ==============================
    // STATUS
    // ==============================

    public static String getClockStatus() {

        return wait.until(ExpectedConditions
                        .visibilityOf(clockStatusText))
                .getText();
    }
}