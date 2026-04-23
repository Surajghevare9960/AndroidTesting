    package pages;

    import io.appium.java_client.android.AndroidDriver;
    import io.appium.java_client.android.nativekey.AndroidKey;
    import io.appium.java_client.android.nativekey.KeyEvent;
    import org.openqa.selenium.By;
    import org.openqa.selenium.WebElement;
    import org.openqa.selenium.support.FindBy;
    import org.openqa.selenium.support.PageFactory;
    import org.openqa.selenium.support.ui.ExpectedCondition;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;
    import java.time.Duration;

    import static java.awt.SystemColor.menu;

    public class Clocking {

        AndroidDriver driver;
        WebDriverWait wait;

        public Clocking(AndroidDriver driver) {
            this.driver = driver;
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            PageFactory.initElements(driver, this);
        }

        // ==============================
        // ELEMENTS
        // ==============================
        @FindBy(xpath = "//android.widget.ImageButton[@content-desc=\"Open navigation drawer\"]")
        WebElement Menu;

        @FindBy(xpath= "//android.widget.CheckedTextView[@resource-id=\"com.orange.payroll:id/design_menu_item_text\" and @text=\"Clocking\"]")
        WebElement Clockingmodule;


        @FindBy(id = "com.orange.payroll:id/clockingTypeLayout")
        WebElement clockingType;

        @FindBy(id = "com.orange.payroll:id/btnIn")
        WebElement clockInBtn;

        @FindBy(id = "com.orange.payroll:id/btnOut")
        WebElement clockOutBtn;

        @FindBy(id = "com.orange.payroll:id/lastClockingLayout")
        WebElement lastClocking;

        @FindBy(id = "com.orange.payroll:id/addressLayout")
        WebElement clockStatusText;

        @FindBy(xpath = "//android.widget.ImageView[@content-desc='Done']")
        WebElement doneBtn;

        @FindBy(id = "com.orange.payroll:id/dashboard") //  update from inspector
        WebElement dashboardScreen;



        // ==============================
        // ACTION METHODS
        // ==============================
        public void navigateToClocking() {  try {
            // Wait for dashboard to load first
            //wait.until(ExpectedConditions.visibilityOf(dashboardScreen));
            Thread.sleep(2000);

            // Wait and click Menu
            wait.until(ExpectedConditions.elementToBeClickable(Menu)).click();

            // Wait and click Clocking
            wait.until(ExpectedConditions.elementToBeClickable(Clockingmodule)).click();

        } catch (Exception e) {
            System.out.println("Navigation failed: " + e.getMessage());
        }
        }
        public void performClockingLoop(int maxAttempts) {

            for (int i = 0; i < maxAttempts; i++) {

                try {
                    // Get current status
                    String status = getClockStatus().toLowerCase();
                    System.out.println("Current Status: " + status);

                    // ==========================
                    // CASE 1: CLOCKED OUT → CLOCK IN
                    // ==========================
                    if (status.contains("CLOCKED-OUT")) {

                        System.out.println("Performing Clock IN...");

                        openClockingType();
                        selectClockingType("Clock In");

                        wait.until(ExpectedConditions.elementToBeClickable(clockInBtn)).click();

                        // Wait for status update
                        Thread.sleep(3000);

                        String updatedStatus = getClockStatus().toLowerCase();
                        if (updatedStatus.contains("CLOCKED-IN")) {
                            System.out.println("Successfully Clocked IN");
                        } else {
                            System.out.println("Clock IN failed, retrying...");
                        }
                    }

                    // ==========================
                    // CASE 2: CLOCKED IN → CLOCK OUT
                    // ==========================
                    else if (status.contains("CLOCKED-IN")) {

                        System.out.println("Performing Clock OUT...");

                        openClockingType();
                        selectClockingType("Clock Out");

                        wait.until(ExpectedConditions.elementToBeClickable(clockOutBtn)).click();

                        // Wait for status update
                        Thread.sleep(3000);

                        String updatedStatus = getClockStatus().toLowerCase();
                        if (updatedStatus.contains("clocked out")) {
                            System.out.println("Successfully Clocked OUT");
                        } else {
                            System.out.println("Clock OUT failed, retrying...");
                        }
                    }

                    // ==========================
                    // UNKNOWN STATE
                    // ==========================
                    else {
                        System.out.println("Unknown status detected: " + status);
                        break;
                    }

                } catch (Exception e) {
                    System.out.println("Error in loop: " + e.getMessage());
                }
            }
        }

//-------------------------------Different Method

        public void openClockingType() {
            wait.until(ExpectedConditions.elementToBeClickable(clockingType)).click();
        }

        public void selectClockingType(String typeName) {

            By option = By.xpath(
                    "//android.widget.TextView[@resource-id='com.orange.payroll:id/txtvwReason' and @text='" + typeName + "']"
            );

            wait.until(ExpectedConditions.visibilityOfElementLocated(option));
            wait.until(ExpectedConditions.elementToBeClickable(option)).click();
        }

        public void clockIn(String typeName) {

            openClockingType();
            selectClockingType(typeName);

            wait.until(ExpectedConditions.elementToBeClickable(clockInBtn)).click();
        }

        public void clockOut(String typeName) {

            openClockingType();
            selectClockingType(typeName);

            wait.until(ExpectedConditions.elementToBeClickable(clockOutBtn)).click();
        }

        // ==============================
        // STATUS METHODS
        // ==============================

        public String getClockStatus() {
            return wait.until(ExpectedConditions.visibilityOf(clockStatusText)).getText();
        }

        // ==============================
        // CAMERA HANDLING
        // ==============================

        /*public void handleCamera() /*{

            try {
                // Wait for camera popup
                Thread.sleep(3000);

                // Handle popup if present
                try {
                    WebElement popup = driver.findElement(
                            By.xpath("//android.widget.FrameLayout[@resource-id='com.android.camera:id/bottom_popup_tips']")
                    );
                    popup.click();
                } catch (Exception e) {
                    System.out.println("Camera popup not present");
                }

                // Capture photo
                driver.pressKey(new KeyEvent(AndroidKey.CAMERA));

                Thread.sleep(2000);

                // Click Done
                wait.until(ExpectedConditions.elementToBeClickable(doneBtn)).click();

            } catch (Exception e) {
                System.out.println("Camera handling failed: " + e.getMessage());
            //}
        }

        // ==============================
        // VALIDATION
        // ==============================

        //public boolean isLastClockingVisible() {
          //  return wait.until(ExpectedConditions.visibilityOf(lastClocking)).isDisplayed();
        }*/
    }