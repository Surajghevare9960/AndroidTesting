package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.Clocking;

public class ClockingTest extends BaseTest {

    LoginPage loginPage;
    Clocking clocking;

    @BeforeMethod
    public void initPages() {
        loginPage = new LoginPage(driver);
        clocking = new Clocking(driver);
    }

    @Test
    public void verifyClockingFlow() throws Exception {

        // =============================
        // LOGIN
        // =============================
        loginPage.login("qamobile", "0065@suraj", "m5o0t5");

        // =============================
        // NAVIGATION
        // =============================
        clocking.navigateToClocking();

        // =============================
        // LOOP FLOW (SMART TOGGLE)
        // =============================
        clocking.performClockingLoop(5);

        // =============================
        // FINAL VALIDATION
        // =============================
        String finalStatus = clocking.getClockStatus().toLowerCase();
        System.out.println("Final Status: " + finalStatus);

        Assert.assertTrue(
                finalStatus.contains("clocked in") || finalStatus.contains("clocked out"),
                "Clocking flow failed - Invalid final status"
        );
    }
}