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
 * CanteenPage – Page Object for the Canteen module.
 */
public class CanteenPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='Canteen']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    // Menu items
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_canteen_item_name']")
    public List<WebElement> menuItemNames;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_canteen_item_price']")
    public List<WebElement> menuItemPrices;

    // Order form
    @FindBy(id = "com.orange.payroll:id/et_canteen_quantity")
    public WebElement quantityField;

    @FindBy(id = "com.orange.payroll:id/btn_canteen_order")
    public WebElement orderBtn;

    // Order history
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_order_date']")
    public List<WebElement> orderDates;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_order_total']")
    public List<WebElement> orderTotals;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public CanteenPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    // -----------------------------------------------------------------------
    // Actions
    // -----------------------------------------------------------------------

    public boolean isPageVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(pageTitle)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getMenuItemCount() {
        return menuItemNames.size();
    }
}
