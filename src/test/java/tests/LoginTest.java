package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test
    public void verifyLogin() throws Exception {

        LoginPage loginPage = new LoginPage(driver);

        String serverName = "otlpvt";
        String username = "0001@demo.com";
        String password = "m5o0t5";

        // Perform Login
        loginPage.login(serverName, username, password);

        // =============================
        // Validation (Dashboard)
        // =============================

        boolean isDashboardDisplayed = driver.getPageSource().contains("Dashboard");

        Assert.assertTrue(isDashboardDisplayed, "Login Failed → Dashboard not visible");

        System.out.println("Login Successful → Dashboard loaded");
    }
}