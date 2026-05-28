package utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;

/**
 * ScrollUtils – UiScrollable helpers for Android.
 */
public class ScrollUtils {

    private final AndroidDriver driver;

    public ScrollUtils(AndroidDriver driver) {
        this.driver = driver;
    }

    /**
     * Scroll to an element with the given visible text.
     * @param text exact text visible on screen
     * @return the WebElement once scrolled into view
     */
    public WebElement scrollToText(String text) {
        return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().text(\"" + text + "\"));"
        ));
    }

    /**
     * Scroll to an element whose text contains the given substring.
     */
    public WebElement scrollToTextContains(String text) {
        return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().textContains(\"" + text + "\"));"
        ));
    }

    /**
     * Scroll to an element by resource-id.
     */
    public WebElement scrollToResourceId(String resourceId) {
        return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().resourceId(\"" + resourceId + "\"));"
        ));
    }

    /**
     * Scroll down by a fixed swipe gesture.
     */
    public void scrollDown(AndroidDriver driver) {
        int height = driver.manage().window().getSize().getHeight();
        int width  = driver.manage().window().getSize().getWidth();
        driver.executeScript("mobile: swipeGesture", new java.util.HashMap<String, Object>() {{
            put("left",   width / 4);
            put("top",    (int)(height * 0.7));
            put("width",  width / 2);
            put("height", (int)(height * 0.3));
            put("direction", "up");
            put("percent", 0.75);
        }});
    }
}
