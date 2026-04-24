package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AttendancePage;
import Reusable_methods.Menu_drawer;
import pages.LoginPage;

public class AttendanceTest extends BaseTest {

    LoginPage loginPage;
    AttendancePage attendancePage;
    Menu_drawer menuDrawer;

    @BeforeMethod
    public void initPages() {
        loginPage = new LoginPage(driver);
        menuDrawer = new Menu_drawer(driver);
        attendancePage = new AttendancePage(driver);

    }
    @Test
    public void Initatetoattendace ()
    {
        try {
            loginPage.login("qamobile", "0065@suraj", "m5o0t5");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 2)
    public void verifyAttendanceNavigation() {

        // Step 1: Open Menu
        menuDrawer.clickMenu();

        // Step 2: Click Attendance
        attendancePage.Attendance.click();

        // Validation
        Assert.assertTrue(attendancePage.Myattendance.isDisplayed(),
                "Attendance page not opened");
    }

    @Test(priority = 3)
    public void verifyAttendanceData() {

        menuDrawer.clickMenu();
        attendancePage.Attendance.click();

        // Employee Info
        String empName = attendancePage.empname.getText();
        String dept = attendancePage.deptname.getText();

        System.out.println("Employee: " + empName);
        System.out.println("Department: " + dept);

        // Attendance Info
        String day = attendancePage.Attendday.getText();
        String date = attendancePage.Attenddate.getText();

        System.out.println("Day: " + day);
        System.out.println("Date: " + date);

        // In/Out Time
        String inTime = attendancePage.intime.getText();
        String outTime = attendancePage.outtime.getText();

        System.out.println("InTime: " + inTime);
        System.out.println("OutTime: " + outTime);

        // Assertions
        Assert.assertFalse(empName.isEmpty(), "Employee name missing");
        Assert.assertFalse(day.isEmpty(), "Day not displayed");
    }

    @Test(priority = 4)
    public void verifyRefreshAndRegularization() {

        menuDrawer.clickMenu();
        attendancePage.Attendance.click();

        // Refresh
        attendancePage.Refreshbtn.click();

        // Click Regularize Dropdown
        attendancePage.Regularizebtn.click();

        Assert.assertTrue(attendancePage.Presentday.isDisplayed(),
                "Present day not visible");
    }
}