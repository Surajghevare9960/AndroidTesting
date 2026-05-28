package utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitUtils – centralised explicit-wait helpers.
 */
public class WaitUtils {

    private final WebDriverWait wait;

    public WaitUtils(AndroidDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public WaitUtils(AndroidDriver driver, int timeoutSeconds) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /** Wait until element is visible and return it. */
    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /** Wait until element located by locator is visible. */
    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Wait until element is clickable and return it. */
    public WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /** Wait until element located by locator is clickable. */
    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Click element after waiting for it to be clickable. */
    public void clickWhenReady(WebElement element) {
        waitForClickable(element).click();
    }

    /** Click element located by locator after waiting. */
    public void clickWhenReady(By locator) {
        waitForClickable(locator).click();
    }

    /** Check if element is present without throwing. */
    public boolean isElementPresent(AndroidDriver driver, By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    /** Hard sleep – use sparingly. */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
