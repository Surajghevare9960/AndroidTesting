package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 *  DBQueryRunner  –  Standalone DB → Excel extractor
 * ═══════════════════════════════════════════════════════════════════════════
 *
 *  HOW TO USE
 *  ──────────
 *  1. Fill in your DB credentials in the CONFIGURATION section below
 *     (or keep them in config.properties and they will be read automatically).
 *
 *  2. Add / edit queries in the QUERIES section.
 *     Each entry is:   new Query("SheetName", "SELECT …")
 *
 *  3. Run this class as a plain Java main() – no Appium / device needed.
 *     Right-click → Run 'DBQueryRunner.main()'
 *
 *  4. Open the generated Excel file:
 *       test-output/DB_Query_Results_<timestamp>.xlsx
 *
 *  Each query gets its own sheet.
 *  A "Query Log" sheet records every query, row count, and run time.
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class DBQueryRunner {

    // ═══════════════════════════════════════════════════════════════════════
    //  CONFIGURATION  –  edit these or leave blank to read from config.properties
    // ═══════════════════════════════════════════════════════════════════════
    private static final String DB_URL      = "";   // e.g. "jdbc:mysql://localhost:3306/hrms_db"
    private static final String DB_USER     = "";   // e.g. "root"
    private static final String DB_PASSWORD = "";   // e.g. "root"

    // Output file location
    private static final String OUTPUT_DIR  = "test-output";

    // ═══════════════════════════════════════════════════════════════════════
    //  QUERIES  –  add as many as you need
    //  Format:  new Query("Excel Sheet Name", "SQL query")
    // ═══════════════════════════════════════════════════════════════════════
    private static final List<Query> QUERIES = Arrays.asList(

        new Query("Attendance",
            "SELECT emp_code, emp_name, att_date, in_time, out_time, status " +
            "FROM attendance ORDER BY att_date DESC LIMIT 200"),

        new Query("Leave_Applications",
            "SELECT leave_id, emp_code, emp_name, leave_type, from_date, to_date, " +
            "period, reason, status, applied_on " +
            "FROM leave_applications ORDER BY applied_on DESC LIMIT 200"),

        new Query("Salary",
            "SELECT emp_code, emp_name, month, year, gross_salary, " +
            "total_deduction, net_salary " +
            "FROM salary_details ORDER BY year DESC, month DESC LIMIT 100"),

        new Query("Holidays",
            "SELECT holiday_name, holiday_date, day_name, holiday_type " +
            "FROM holidays ORDER BY holiday_date ASC"),

        new Query("Claims",
            "SELECT claim_id, emp_code, emp_name, claim_type, claim_date, " +
            "amount, status, purpose " +
            "FROM claim_applications ORDER BY claim_date DESC LIMIT 200"),

        new Query("Travel",
            "SELECT travel_id, emp_code, emp_name, state, city, place_to_visit, " +
            "purpose, departure_date, period, status " +
            "FROM travel_requests ORDER BY departure_date DESC LIMIT 200"),

        new Query("Employees",
            "SELECT emp_code, emp_name, department, designation, joining_date, status " +
            "FROM employees ORDER BY emp_code ASC LIMIT 500"),

        new Query("Approvals_Leave",
            "SELECT la.leave_id, e.emp_name, la.leave_type, la.from_date, la.to_date, " +
            "la.period, la.status, la.applied_on " +
            "FROM leave_applications la " +
            "JOIN employees e ON la.emp_code = e.emp_code " +
            "WHERE la.status = 'Pending' ORDER BY la.applied_on DESC LIMIT 100"),

        new Query("Ticket_Applications",
            "SELECT ticket_id, emp_code, emp_name, subject, description, " +
            "priority, status, created_date " +
            "FROM ticket_applications ORDER BY created_date DESC LIMIT 200"),

        new Query("Grievances",
            "SELECT grievance_id, emp_code, emp_name, subject, description, " +
            "status, created_date " +
            "FROM grievances ORDER BY created_date DESC LIMIT 200")

        // ── ADD YOUR OWN QUERIES HERE ──────────────────────────────────────
        // new Query("My_Custom_Sheet", "SELECT * FROM my_table WHERE condition = 'value'"),
    );

    // ═══════════════════════════════════════════════════════════════════════
    //  MAIN
    // ═══════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   HRMS DB Query Runner → Excel Exporter  ║");
        System.out.println("╚══════════════════════════════════════════╝");

        // Resolve credentials
        String url  = resolve(DB_URL,      "db.url");
        String user = resolve(DB_USER,     "db.username");
        String pass = resolve(DB_PASSWORD, "db.password");

        if (url.isEmpty()) {
            System.err.println("❌ DB URL not set. Edit DB_URL in DBQueryRunner.java " +
                               "or set db.url in config/config.properties");
            return;
        }

        // Output file
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String outputFile = OUTPUT_DIR + "/DB_Query_Results_" + timestamp + ".xlsx";
        new File(OUTPUT_DIR).mkdirs();

        // Connect
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Connected → " + url);
        } catch (Exception e) {
            System.err.println("❌ DB connection failed: " + e.getMessage());
            return;
        }

        // Build workbook
        try (Workbook wb = new XSSFWorkbook()) {

            Sheet logSheet = createLogSheet(wb);
            int logRow = 1;

            for (Query q : QUERIES) {
                System.out.println("\n▶ Running query for sheet: " + q.sheetName);
                System.out.println("  SQL: " + q.sql.substring(0, Math.min(80, q.sql.length())) + "…");

                long start = System.currentTimeMillis();
                int rowCount = 0;
                String errorMsg = "";

                try (Statement stmt = conn.createStatement();
                     ResultSet rs   = stmt.executeQuery(q.sql)) {

                    ResultSetMetaData meta = rs.getMetaData();
                    int colCount = meta.getColumnCount();

                    // Collect column names
                    List<String> cols = new ArrayList<>();
                    for (int i = 1; i <= colCount; i++) cols.add(meta.getColumnLabel(i));

                    // Create data sheet
                    Sheet sheet = wb.createSheet(sanitizeSheetName(q.sheetName));
                    writeDataHeader(wb, sheet, cols);

                    // Write rows
                    int rIdx = 1;
                    while (rs.next()) {
                        Row row = sheet.createRow(rIdx++);
                        for (int i = 0; i < cols.size(); i++) {
                            String val = rs.getString(i + 1);
                            row.createCell(i).setCellValue(val != null ? val : "NULL");
                        }
                    }
                    rowCount = rIdx - 1;

                    // Auto-size columns (up to 20 cols for performance)
                    for (int i = 0; i < Math.min(cols.size(), 20); i++) {
                        sheet.autoSizeColumn(i);
                    }

                    System.out.println("  ✅ " + rowCount + " rows written to sheet: " + q.sheetName);

                } catch (SQLException e) {
                    errorMsg = e.getMessage();
                    System.err.println("  ❌ Query failed: " + errorMsg);
                    // Write error sheet
                    Sheet errSheet = wb.createSheet(sanitizeSheetName(q.sheetName));
                    errSheet.createRow(0).createCell(0).setCellValue("ERROR: " + errorMsg);
                    errSheet.createRow(1).createCell(0).setCellValue("SQL: " + q.sql);
                }

                long elapsed = System.currentTimeMillis() - start;

                // Log entry
                Row lr = logSheet.createRow(logRow++);
                lr.createCell(0).setCellValue(q.sheetName);
                lr.createCell(1).setCellValue(q.sql);
                lr.createCell(2).setCellValue(rowCount);
                lr.createCell(3).setCellValue(elapsed + " ms");
                lr.createCell(4).setCellValue(errorMsg.isEmpty() ? "SUCCESS" : "FAILED");
                lr.createCell(5).setCellValue(LocalDateTime.now().toString());
                if (!errorMsg.isEmpty()) lr.createCell(6).setCellValue(errorMsg);
            }

            // Save
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                wb.write(fos);
            }
            System.out.println("\n╔══════════════════════════════════════════════════╗");
            System.out.println("║  ✅ Excel saved → " + outputFile);
            System.out.println("╚══════════════════════════════════════════════════╝");

        } catch (IOException e) {
            System.err.println("❌ Failed to write Excel: " + e.getMessage());
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  Helpers
    // ═══════════════════════════════════════════════════════════════════════

    private static String resolve(String hardcoded, String configKey) {
        if (hardcoded != null && !hardcoded.trim().isEmpty()) return hardcoded.trim();
        try { return ConfigReader.get(configKey); } catch (Exception e) { return ""; }
    }

    private static Sheet createLogSheet(Workbook wb) {
        Sheet s = wb.createSheet("Query Log");
        CellStyle hs = headerStyle(wb);
        Row h = s.createRow(0);
        String[] cols = {"Sheet Name", "SQL Query", "Row Count", "Duration", "Status", "Run Time", "Error"};
        for (int i = 0; i < cols.length; i++) {
            Cell c = h.createCell(i);
            c.setCellValue(cols[i]);
            c.setCellStyle(hs);
            s.setColumnWidth(i, i == 1 ? 15000 : 5000);
        }
        return s;
    }

    private static void writeDataHeader(Workbook wb, Sheet sheet, List<String> cols) {
        CellStyle hs = headerStyle(wb);
        Row h = sheet.createRow(0);
        for (int i = 0; i < cols.size(); i++) {
            Cell c = h.createCell(i);
            c.setCellValue(cols.get(i));
            c.setCellStyle(hs);
        }
    }

    private static CellStyle headerStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        f.setColor(IndexedColors.WHITE.getIndex());
        s.setFont(f);
        s.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setBorderBottom(BorderStyle.THIN);
        s.setBorderTop(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);
        s.setBorderRight(BorderStyle.THIN);
        return s;
    }

    /** Excel sheet names max 31 chars, no special chars */
    private static String sanitizeSheetName(String name) {
        String clean = name.replaceAll("[\\\\/*?\\[\\]:]", "_");
        return clean.length() > 31 ? clean.substring(0, 31) : clean;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  Inner class – Query holder
    // ═══════════════════════════════════════════════════════════════════════
    public static class Query {
        public final String sheetName;
        public final String sql;
        public Query(String sheetName, String sql) {
            this.sheetName = sheetName;
            this.sql       = sql;
        }
    }
}
