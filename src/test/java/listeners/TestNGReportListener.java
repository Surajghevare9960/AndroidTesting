package listeners;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * TestNGReportListener – generates a clean, self-contained HTML report
 * from TestNG results without any external dependencies.
 *
 * Output → test-output/TestNGReport/HRMS_TestNG_Report.html
 */
public class TestNGReportListener implements IReporter {

    private static final String REPORT_DIR  = "test-output/TestNGReport";
    private static final String REPORT_FILE = REPORT_DIR + "/HRMS_TestNG_Report.html";

    @Override
    public void generateReport(List<XmlSuite> xmlSuites,
                               List<ISuite>   suites,
                               String         outputDirectory) {

        new File(REPORT_DIR).mkdirs();

        // Collect all results
        List<ITestResult> passed  = new ArrayList<>();
        List<ITestResult> failed  = new ArrayList<>();
        List<ITestResult> skipped = new ArrayList<>();

        for (ISuite suite : suites) {
            for (ISuiteResult sr : suite.getResults().values()) {
                ITestContext ctx = sr.getTestContext();
                passed .addAll(ctx.getPassedTests() .getAllResults());
                failed .addAll(ctx.getFailedTests()  .getAllResults());
                skipped.addAll(ctx.getSkippedTests() .getAllResults());
            }
        }

        int total = passed.size() + failed.size() + skipped.size();
        int passP = total > 0 ? (passed.size()  * 100 / total) : 0;
        int failP = total > 0 ? (failed.size()  * 100 / total) : 0;
        int skipP = total > 0 ? (skipped.size() * 100 / total) : 0;

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang='en'><head>")
            .append("<meta charset='UTF-8'>")
            .append("<meta name='viewport' content='width=device-width,initial-scale=1'>")
            .append("<title>HRMS TestNG Report</title>")
            .append("<style>")
            .append("body{font-family:Segoe UI,sans-serif;background:#1a1a2e;color:#eee;margin:0;padding:20px}")
            .append("h1{color:#e94560;text-align:center;margin-bottom:4px}")
            .append(".subtitle{text-align:center;color:#aaa;margin-bottom:20px;font-size:13px}")
            .append(".summary{display:flex;gap:16px;justify-content:center;margin-bottom:24px}")
            .append(".card{background:#16213e;border-radius:10px;padding:20px 30px;text-align:center;min-width:120px}")
            .append(".card .num{font-size:36px;font-weight:bold}")
            .append(".card .lbl{font-size:13px;color:#aaa;margin-top:4px}")
            .append(".pass .num{color:#4caf50} .fail .num{color:#f44336} .skip .num{color:#ff9800} .total .num{color:#2196f3}")
            .append(".progress{background:#0f3460;border-radius:6px;height:18px;margin-bottom:24px;overflow:hidden;display:flex}")
            .append(".p-pass{background:#4caf50;height:100%} .p-fail{background:#f44336;height:100%} .p-skip{background:#ff9800;height:100%}")
            .append("table{width:100%;border-collapse:collapse;background:#16213e;border-radius:10px;overflow:hidden}")
            .append("th{background:#0f3460;padding:10px 14px;text-align:left;font-size:13px;color:#90caf9}")
            .append("td{padding:9px 14px;font-size:13px;border-bottom:1px solid #0f3460}")
            .append("tr:hover td{background:#1a2a4a}")
            .append(".badge{display:inline-block;padding:2px 10px;border-radius:12px;font-size:12px;font-weight:bold}")
            .append(".PASS{background:#1b5e20;color:#a5d6a7} .FAIL{background:#b71c1c;color:#ef9a9a} .SKIP{background:#e65100;color:#ffcc80}")
            .append(".err{color:#ef9a9a;font-size:11px;max-width:400px;word-break:break-word}")
            .append("</style></head><body>")
            .append("<h1>🧪 HRMS Automation – TestNG Report</h1>")
            .append("<div class='subtitle'>Generated: ").append(timestamp).append("</div>")

            // Summary cards
            .append("<div class='summary'>")
            .append("<div class='card total'><div class='num'>").append(total).append("</div><div class='lbl'>Total</div></div>")
            .append("<div class='card pass'><div class='num'>").append(passed.size()).append("</div><div class='lbl'>Passed</div></div>")
            .append("<div class='card fail'><div class='num'>").append(failed.size()).append("</div><div class='lbl'>Failed</div></div>")
            .append("<div class='card skip'><div class='num'>").append(skipped.size()).append("</div><div class='lbl'>Skipped</div></div>")
            .append("</div>")

            // Progress bar
            .append("<div class='progress'>")
            .append("<div class='p-pass' style='width:").append(passP).append("%'></div>")
            .append("<div class='p-fail' style='width:").append(failP).append("%'></div>")
            .append("<div class='p-skip' style='width:").append(skipP).append("%'></div>")
            .append("</div>")

            // Table
            .append("<table><thead><tr>")
            .append("<th>#</th><th>Module</th><th>Test Method</th><th>Status</th><th>Duration (s)</th><th>Error</th>")
            .append("</tr></thead><tbody>");

        // All results combined
        List<ITestResult> all = new ArrayList<>();
        all.addAll(passed); all.addAll(failed); all.addAll(skipped);
        all.sort(Comparator.comparingLong(ITestResult::getStartMillis));

        int idx = 1;
        for (ITestResult r : all) {
            String status  = statusLabel(r.getStatus());
            double elapsed = (r.getEndMillis() - r.getStartMillis()) / 1000.0;
            String error   = "";
            if (r.getThrowable() != null) {
                String msg = r.getThrowable().getMessage();
                error = msg != null ? escHtml(msg.length() > 200 ? msg.substring(0, 200) + "…" : msg) : "";
            }
            html.append("<tr>")
                .append("<td>").append(idx++).append("</td>")
                .append("<td>").append(escHtml(r.getTestClass().getRealClass().getSimpleName())).append("</td>")
                .append("<td>").append(escHtml(r.getMethod().getMethodName())).append("</td>")
                .append("<td><span class='badge ").append(status).append("'>").append(status).append("</span></td>")
                .append("<td>").append(String.format("%.2f", elapsed)).append("</td>")
                .append("<td class='err'>").append(error).append("</td>")
                .append("</tr>");
        }

        html.append("</tbody></table></body></html>");

        try (PrintWriter pw = new PrintWriter(new FileWriter(REPORT_FILE))) {
            pw.write(html.toString());
            System.out.println("✅ TestNG HTML Report → " + REPORT_FILE);
        } catch (IOException e) {
            System.err.println("❌ Failed to write TestNG report: " + e.getMessage());
        }
    }

    private String statusLabel(int status) {
        switch (status) {
            case ITestResult.SUCCESS: return "PASS";
            case ITestResult.FAILURE: return "FAIL";
            default:                  return "SKIP";
        }
    }

    private String escHtml(String s) {
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;");
    }
}
