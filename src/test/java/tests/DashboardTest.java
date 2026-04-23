package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;

public class DashboardTest extends BaseTest {

    @Test
    public void testDashboard() throws InterruptedException {

        LoginPage login = new LoginPage(driver);
        DashboardPage dashboard = new DashboardPage(driver);

        login.login("otlpvt", "0001@demo.com", "m5o0t5");

        dashboard.openDashboard(driver);

        Assert.assertTrue(dashboard.isDashboardVisible());
        Assert.assertTrue(dashboard.isRefreshVisible());
        Assert.assertTrue(dashboard.isMyProfileVisible());
    }
}