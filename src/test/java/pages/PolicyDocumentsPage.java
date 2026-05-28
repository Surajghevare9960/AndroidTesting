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
 * PolicyDocumentsPage – Page Object for the Policy & Documents module.
 */
public class PolicyDocumentsPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // -----------------------------------------------------------------------
    // Locators
    // -----------------------------------------------------------------------

    @FindBy(xpath = "//android.widget.TextView[@text='Policy & Documents']")
    public WebElement pageTitle;

    @FindBy(id = "com.orange.payroll:id/refreshImgView")
    public WebElement refreshBtn;

    // Document list
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_document_name']")
    public List<WebElement> documentNames;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_document_type']")
    public List<WebElement> documentTypes;

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.orange.payroll:id/tv_document_date']")
    public List<WebElement> documentDates;

    // Download button per document
    @FindBy(xpath = "//android.widget.ImageView[@resource-id='com.orange.payroll:id/iv_download_doc']")
    public List<WebElement> downloadButtons;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    public PolicyDocumentsPage(AndroidDriver driver) {
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

    public int getDocumentCount() {
        return documentNames.size();
    }

    public void downloadDocument(int index) {
        if (index < downloadButtons.size()) {
            wait.until(ExpectedConditions.elementToBeClickable(downloadButtons.get(index))).click();
        }
    }

    public String getDocumentName(int index) {
        return documentNames.get(index).getText();
    }
}
