package tests;

import Base.BaseTest;
import Reusable_methods.Menu_drawer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.Clocking;
import pages.LoginPage;

public class LeaveTest extends BaseTest {

    LoginPage loginPage;
    Menu_drawer menuDrawer;

    @BeforeMethod
    public void initPages() {
        loginPage = new LoginPage(driver);
        menuDrawer = new Menu_drawer(driver);

    }

    @Test
    public void verifyLeaveNavigation() throws Exception {

        // =============================
        // LOGIN
        // =============================
        loginPage.login("qamobile", "0068@suraj", "m5o0t5");

        // =============================
        // NAVIGATION
        // =============================
        Clocking.navigateToClocking();

        // =============================
        // LOOP FLOW (SMART TOGGLE)
        // =============================
        Clocking.performClockingLoop(5);

        // =============================
        // FINAL VALIDATION
        // =============================
        String finalStatus = Clocking.getClockStatus().toLowerCase();
        System.out.println("Final Status: " + finalStatus);

        Assert.assertTrue(
                finalStatus.contains("clocked in") || finalStatus.contains("clocked out"),
                "Clocking flow failed - Invalid final status"
        );
    }
}
