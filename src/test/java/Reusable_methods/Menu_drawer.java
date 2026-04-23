package Reusable_methods;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// Utils Class for the Menu Drawer Click

public class Menu_drawer {


    AndroidDriver driver;
   // WaitUtils waitUtils;
   WebDriverWait wait;
    @FindBy(xpath = "//android.widget.ImageButton[@content-desc='Open navigation drawer']")
    private WebElement menu;

    public Menu_drawer(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);  // IMPORTANT
       // waitUtils = new WaitUtils(driver);
    }



    public void clickMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(menu)).click();
    }
}
