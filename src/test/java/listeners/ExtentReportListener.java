package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReportListener – auto-generates a rich HTML Extent Report.
 *
 * Wired via testng.xml  <listeners> block.
 * Output → test-output/ExtentReport/HRMS_Report_<timestamp>.html
 */
public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;
    // ThreadLocal so parallel tests each get their own ExtentTest node
    private static final ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();

    // -----------------------------------------------------------------------
    // Suite start – create the reporter once
    // -----------------------------------------------------------------------
    @Override
    public void onStart(ITestContext context) {
        if (extent == null) {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportDir  = "test-output/ExtentReport";
            String reportPath = reportDir + "/HRMS_Report_" + timestamp + ".html";

            new File(reportDir).mkdirs();

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle("HRMS Automation Report");
            spark.config().setReportName("HRMS Mobile Test Results");
            spark.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Application",  "HRMS Mobile (com.orange.payroll)");
            extent.setSystemInfo("Platform",      "Android");
            extent.setSystemInfo("Automation",    "Appium + TestNG");
            extent.setSystemInfo("Tester",        System.getProperty("user.name"));
            extent.setSystemInfo("Report Time",   timestamp);

            System.out.println("✅ ExtentReport initialised → " + reportPath);
        }
    }

    // -----------------------------------------------------------------------
    // Each test
    // -----------------------------------------------------------------------
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getTestClass().getRealClass().getSimpleName()
                + " → " + result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(testName,
                result.getMethod().getDescription());
        testNode.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testNode.get().log(Status.PASS,
                "✅ PASSED in " + elapsedSec(result) + "s");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testNode.get().log(Status.FAIL,
                "❌ FAILED: " + result.getThrowable().getMessage());
        testNode.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testNode.get().log(Status.SKIP,
                "⚠️ SKIPPED: " + result.getThrowable().getMessage());
    }

    // -----------------------------------------------------------------------
    // Suite finish – flush to disk
    // -----------------------------------------------------------------------
    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
            System.out.println("✅ ExtentReport flushed.");
        }
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------
    private double elapsedSec(ITestResult r) {
        return (r.getEndMillis() - r.getStartMillis()) / 1000.0;
    }

    /** Called by tests to log a step inside the current test node. */
    public static void logStep(String message) {
        ExtentTest t = testNode.get();
        if (t != null) t.log(Status.INFO, message);
    }

    public static void logPass(String message) {
        ExtentTest t = testNode.get();
        if (t != null) t.log(Status.PASS, message);
    }

    public static void logFail(String message) {
        ExtentTest t = testNode.get();
        if (t != null) t.log(Status.FAIL, message);
    }
}
