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
 * PhotoGalleryPage – Page Object for the Photo Gallery module.
 */
public class PhotoGalleryPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='Photo Gallery']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    // Gallery grid items
    @FindBy(xpath = "//android.widget.ImageView[@resource-id='com.orange.payroll:id/iv_gallery_image']")
    public List<WebElement> galleryImages;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_gallery_title']")
    public List<WebElement> galleryTitles;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_gallery_date']")
    public List<WebElement> galleryDates;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public PhotoGalleryPage(AndroidDriver driver) {
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

    public int getImageCount() {
        return galleryImages.size();
    }

    public void openFirstImage() {
        if (!galleryImages.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(galleryImages.get(0))).click();
        }
    }
}
