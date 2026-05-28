package tests;

import Base.BaseTest;
import Reusable_methods.Menu_drawer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.TicketApplicationPage;
import utils.ExcelUtils;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * TicketApplicationTest – validates the Ticket Application module.
 */
public class TicketApplicationTest extends BaseTest {

    private Menu_drawer          menuDrawer;
    private TicketApplicationPage ticketPage;
    private ExcelUtils            excel;

    @BeforeMethod
    public void initPages() {
        menuDrawer  = new Menu_drawer(driver);
        ticketPage  = new TicketApplicationPage(driver);
        excel       = new ExcelUtils(utils.ConfigReader.get("excel.output.path"));
    }

    @Test(priority = 1, description = "Verify Ticket Application page loads")
    public void verifyTicketPageVisible() {

        menuDrawer.goToTicketApplication();
        WaitUtils.sleep(2000);

        boolean visible = ticketPage.isPageVisible();
        excel.appendTestResult("Ticket Application", "verifyTicketPageVisible",
                visible ? "PASS" : "FAIL",
                "Page title visible", String.valueOf(visible), "");
        excel.save();

        Assert.assertTrue(visible, "Ticket Application page did not load");
    }

    @Test(priority = 2, description = "Capture existing ticket list")
    public void captureTicketList() {

        menuDrawer.goToTicketApplication();
        WaitUtils.sleep(2000);

        int count = ticketPage.getTicketCount();
        List<String[]> rows = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String id      = safeText(ticketPage.ticketIds, i);
            String subject = safeText(ticketPage.ticketSubjects, i);
            String status  = safeText(ticketPage.ticketStatuses, i);
            String date    = safeText(ticketPage.ticketDates, i);
            rows.add(new String[]{id, subject, status, date});
            System.out.println("Ticket: " + id + " | " + subject + " | " + status);
        }

        excel.writeSheet("Ticket_List",
                new String[]{"Ticket ID", "Subject", "Status", "Date"}, rows);
        excel.appendTestResult("Ticket Application", "captureTicketList",
                "PASS", "List loaded", count + " tickets", "");
        excel.save();

        driver.navigate().back();
    }

    private <T extends org.openqa.selenium.WebElement> String safeText(List<T> list, int idx) {
        try { return list.get(idx).getText(); } catch (Exception e) { return ""; }
    }
}
