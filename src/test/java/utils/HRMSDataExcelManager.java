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
 *  HRMSDataExcelManager
 *  ─────────────────────────────────────────────────────────────────────────
 *  PURPOSE
 *  ───────
 *  1. Creates  test-output/HRMS_DB_Data.xlsx  with one sheet per HRMS module.
 *  2. Each sheet has:
 *       • A "Settings" block  – key/value rows read from DB settings tables
 *       • A "Data" block      – actual records (leave apps, approvals, etc.)
 *       • A "Comparison" block– DB value | APK value | MATCH/MISMATCH columns
 *         (APK column is filled by DBValidationTest during Appium run)
 *
 *  HOW TO ADD A NEW QUERY / MODULE
 *  ────────────────────────────────
 *  Scroll to the QUERY REGISTRY section and add one entry:
 *
 *    registry.add(new ModuleSheet(
 *        "SheetName",                          // Excel tab name (max 31 chars)
 *        "SELECT … FROM settings_table …",     // Settings query  (or null)
 *        "SELECT … FROM data_table …"          // Data query
 *    ));
 *
 *  That's it. Run main() or call buildExcel() from DBValidationTest.
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class HRMSDataExcelManager {

    // ── output path ─────────────────────────────────────────────────────────
    public static final String EXCEL_PATH = "test-output/HRMS_DB_Data.xlsx";

    // ── colours ─────────────────────────────────────────────────────────────
    private static final short COL_HEADER_BG  = IndexedColors.DARK_BLUE.getIndex();
    private static final short COL_SETTINGS_BG= IndexedColors.DARK_TEAL.getIndex();
    private static final short COL_DATA_BG    = IndexedColors.DARK_GREEN.getIndex();
    private static final short COL_COMPARE_BG = IndexedColors.DARK_RED.getIndex();
    private static final short COL_MATCH_BG   = IndexedColors.LIGHT_GREEN.getIndex();
    private static final short COL_MISMATCH_BG= IndexedColors.ROSE.getIndex();
    private static final short COL_PENDING_BG = IndexedColors.LIGHT_YELLOW.getIndex();


    // ═══════════════════════════════════════════════════════════════════════
    //  QUERY REGISTRY  ──  ADD YOUR MODULES HERE
    // ═══════════════════════════════════════════════════════════════════════
    private static List<ModuleSheet> buildRegistry() {
        List<ModuleSheet> r = new ArrayList<>();

        // ── Leave Master Settings + Leave Applications ───────────────────DONE
        r.add(new ModuleSheet(
            "Leave",
            // Settings query: leave types, max days, carry-forward rules
                // Settings Query
                "SELECT Leave_ID, Leave_Name, Leave_Code, Leave_Type, " +
                        "Max_Leave, Is_Carry_Forward, Is_Active " +
                        "FROM T0040_LEAVE_MASTER " +
                        "ORDER BY Leave_Name ASC",

                // Leave Application Stored Procedure Query
                "EXEC SP_GetPage " +
                        "@Items_Per_Page=15, " +
                        "@Page_No=1, " +
                        "@Select_Fields='Row_ID,Leave_ID,Emp_ID,Emp_Full_Name,Leave_Name," +
                        "Application_Code,From_Date,To_Date,Leave_Period,Application_Status," +
                        "Senior_Employee,Leave_Application_ID,Emp_first_name,Emp_Code," +
                        "Branch_Name,Desig_Name,Alpha_Emp_code,Leave_Reason,Application_Date," +
                        "is_backdated_application," +
                        "case when Apply_Hourly = 1 then ''hour(s)'' else ''day(s)'' end as Leave_Type," +
                        "'''' AS Approval_Comments', " +
                        "@From='V0110_Leave_Application_Detail', " +
                        "@Where='Cmp_ID = 3 AND Application_Status = ''P'' " +
                        "AND From_Date >= ''2026-01-01'' " +
                        "AND To_Date <= ''2027-12-31''', " +
                        "@OrderBy='From_Date DESC'"
        ));


        // ── Leave Closing as on date aaaaaaaaaaaaaaa ─────────────────────-----(Done)
        r.add(new ModuleSheet(
                "Leave Closing",

                // Settings Query
                "SELECT Leave_ID, Leave_Name, Leave_Code, Leave_Type " +
                        "FROM T0040_LEAVE_MASTER " +
                        "ORDER BY Leave_Name ASC",

                // Stored Procedure Query
                "EXEC SP_LEAVE_CLOSING_AS_ON_DATE_ALL " +
                        "@Cmp_ID='3', " +
                        "@Emp_Id='3574', " +
                        "@For_Date='2026-04-28 00:00:00'"
        ));

        // ── Approvals (Leave Pending) ────────────────────────────────────
        r.add(new ModuleSheet(
            "Approvals_Leave",
            null,   // no settings table for approvals
            "SELECT la.leave_id, e.emp_name, la.leave_type, " +
            "la.from_date, la.to_date, la.period, la.status, la.applied_on " +
            "FROM leave_applications la " +
            "JOIN employees e ON la.emp_code = e.emp_code " +
            "WHERE la.status IN ('Pending','Applied') " +
            "ORDER BY la.applied_on DESC LIMIT 100"
        ));

        // ── Salary ───────────────────────────────────────────────────────
        r.add(new ModuleSheet(
            "Salary",
            "SELECT setting_key, setting_value, description " +
            "FROM salary_settings ORDER BY setting_key ASC",
            "SELECT emp_code, emp_name, month, year, " +
            "gross_salary, total_deduction, net_salary " +
            "FROM salary_details ORDER BY year DESC, month DESC LIMIT 100"
        ));

        // ── Claims ───────────────────────────────────────────────────────
        r.add(new ModuleSheet(
            "Claims",
            "SELECT setting_key, setting_value, description " +
            "FROM claim_settings ORDER BY setting_key ASC",
            "SELECT claim_id, emp_code, emp_name, claim_type, " +
            "claim_date, amount, status, purpose " +
            "FROM claim_applications ORDER BY claim_date DESC LIMIT 200"
        ));

        // ── Travel ───────────────────────────────────────────────────────
        r.add(new ModuleSheet(
            "Travel",
            "SELECT setting_key, setting_value, description " +
            "FROM travel_settings ORDER BY setting_key ASC",
            "SELECT travel_id, emp_code, emp_name, state, city, " +
            "place_to_visit, purpose, departure_date, period, status " +
            "FROM travel_requests ORDER BY departure_date DESC LIMIT 200"
        ));

        // ── Holidays ─────────────────────────────────────────────────────
        r.add(new ModuleSheet(
            "Holidays",
            null,
            "SELECT holiday_name, holiday_date, day_name, holiday_type " +
            "FROM holidays ORDER BY holiday_date ASC"
        ));

        // ── Ticket Applications ──────────────────────────────────────────
        r.add(new ModuleSheet(
            "Ticket_Applications",
            "SELECT setting_key, setting_value, description " +
            "FROM ticket_settings ORDER BY setting_key ASC",
            "SELECT ticket_id, emp_code, emp_name, subject, " +
            "priority, status, created_date " +
            "FROM ticket_applications ORDER BY created_date DESC LIMIT 200"
        ));

        // ── Grievances ───────────────────────────────────────────────────
        r.add(new ModuleSheet(
            "Grievances",
            null,
            "SELECT grievance_id, emp_code, emp_name, subject, " +
            "status, created_date " +
            "FROM grievances ORDER BY created_date DESC LIMIT 200"
        ));

        // ── Employees Master ─────────────────────────────────────────────
        r.add(new ModuleSheet(
            "Employees",
            null,
            "SELECT emp_code, emp_name, department, designation, " +
            "joining_date, status " +
            "FROM employees ORDER BY emp_code ASC LIMIT 500"
        ));

        // ── ADD MORE MODULES HERE ────────────────────────────────────────
        // r.add(new ModuleSheet("MyModule",
        //     "SELECT setting_key, setting_value FROM my_settings",
        //     "SELECT * FROM my_data_table LIMIT 100"
        // ));

        return r;
    }


    // ═══════════════════════════════════════════════════════════════════════
    //  MAIN  –  run standalone, no Appium needed
    // ═══════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║  HRMS DB Data → Excel Builder (standalone)   ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        try {
            buildExcel();
        } catch (Exception e) {
            System.err.println("❌ Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  buildExcel()  –  called by main() AND by DBValidationTest
    //  Returns the path of the saved Excel file.
    // ═══════════════════════════════════════════════════════════════════════
    public static String buildExcel() throws Exception {

        String url  = ConfigReader.get("db.url");
        String user = ConfigReader.get("db.username");
        String pass = ConfigReader.get("db.password");

        new File("test-output").mkdirs();

        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ DB connected → " + url);
        } catch (Exception e) {
            System.err.println("❌ DB connection failed: " + e.getMessage());
            throw e;
        }

        try (Workbook wb = new XSSFWorkbook()) {

            // ── Summary sheet (first tab) ────────────────────────────────
            buildSummarySheet(wb);

            List<ModuleSheet> registry = buildRegistry();

            for (ModuleSheet m : registry) {
                System.out.println("\n▶ Building sheet: " + m.sheetName);
                buildModuleSheet(wb, conn, m);
            }

            // ── Save ─────────────────────────────────────────────────────
            try (FileOutputStream fos = new FileOutputStream(EXCEL_PATH)) {
                wb.write(fos);
            }
            System.out.println("\n✅ Excel saved → " + EXCEL_PATH);
            return EXCEL_PATH;

        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }


    // ═══════════════════════════════════════════════════════════════════════
    //  Sheet builders
    // ═══════════════════════════════════════════════════════════════════════

    /** First sheet – instructions + run timestamp */
    private static void buildSummarySheet(Workbook wb) {
        Sheet s = wb.createSheet("README");
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        String[][] info = {
            {"HRMS DB Data Workbook", ""},
            {"Generated", ts},
            {"Purpose",
             "Each sheet = one HRMS module. " +
             "Settings block shows DB config. " +
             "Data block shows actual records. " +
             "Comparison block is filled by Appium tests."},
            {"How to add a query",
             "Open HRMSDataExcelManager.java → buildRegistry() → add new ModuleSheet(...)"},
            {"How to run standalone",
             "run_db_query.bat  OR  right-click HRMSDataExcelManager → Run main()"},
            {"How to run with Appium",
             "DBValidationTest calls buildExcel() then compares APK values in Comparison columns"},
            {"Comparison column legend",
             "MATCH = DB value equals APK value | MISMATCH = values differ | PENDING = not yet tested"},
        };

        CellStyle titleStyle = sectionHeaderStyle(wb, COL_HEADER_BG);
        CellStyle bodyStyle  = bodyStyle(wb);
        s.setColumnWidth(0, 8000);
        s.setColumnWidth(1, 20000);

        for (int i = 0; i < info.length; i++) {
            Row r = s.createRow(i);
            Cell c0 = r.createCell(0);
            Cell c1 = r.createCell(1);
            c0.setCellValue(info[i][0]);
            c1.setCellValue(info[i][1]);
            if (i == 0) { c0.setCellStyle(titleStyle); c1.setCellStyle(titleStyle); }
            else        { c0.setCellStyle(bodyStyle);  c1.setCellStyle(bodyStyle); }
        }
    }

    /** Builds one module sheet: Settings block + Data block + Comparison block */
    private static void buildModuleSheet(Workbook wb, Connection conn, ModuleSheet m) {

        Sheet sheet = wb.createSheet(sanitize(m.sheetName));
        sheet.setColumnWidth(0, 500); // spacer col A

        int currentRow = 0;

        // ── SETTINGS BLOCK ───────────────────────────────────────────────
        if (m.settingsQuery != null) {
            currentRow = writeBlock(wb, sheet, conn, currentRow,
                "⚙  SETTINGS  –  " + m.sheetName,
                m.settingsQuery,
                COL_SETTINGS_BG,
                true   // isSettings = add "Enabled in APK?" column
            );
            currentRow += 2; // blank rows between blocks
        }

        // ── DATA BLOCK ───────────────────────────────────────────────────
        currentRow = writeBlock(wb, sheet, conn, currentRow,
            "📋  DATA  –  " + m.sheetName,
            m.dataQuery,
            COL_DATA_BG,
            false
        );
        currentRow += 2;

        // ── COMPARISON BLOCK header (filled by Appium tests) ─────────────
        currentRow = writeComparisonHeader(wb, sheet, conn, currentRow, m);

        System.out.println("  ✅ Sheet built: " + m.sheetName);
    }


    /**
     * Writes a titled block (settings or data) into the sheet.
     * Returns the next available row index.
     */
    private static int writeBlock(Workbook wb, Sheet sheet, Connection conn,
                                   int startRow, String title, String sql,
                                   short headerBg, boolean isSettings) {

        // Section title row
        Row titleRow = sheet.createRow(startRow++);
        Cell titleCell = titleRow.createCell(1);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(sectionHeaderStyle(wb, headerBg));

        List<String> cols = new ArrayList<>();
        List<List<String>> rows = new ArrayList<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            for (int i = 1; i <= colCount; i++) cols.add(meta.getColumnLabel(i));

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= colCount; i++) {
                    String v = rs.getString(i);
                    row.add(v != null ? v : "NULL");
                }
                rows.add(row);
            }

        } catch (SQLException e) {
            Row errRow = sheet.createRow(startRow++);
            errRow.createCell(1).setCellValue("⚠ Query error: " + e.getMessage());
            errRow.createCell(2).setCellValue(sql);
            return startRow;
        }

        if (cols.isEmpty()) {
            sheet.createRow(startRow++).createCell(1).setCellValue("(no data)");
            return startRow;
        }

        // If settings block, add extra columns for APK verification
        if (isSettings) {
            cols.add("Enabled in APK?");
            cols.add("APK Verified By Test");
        }

        // Column header row
        CellStyle colHdrStyle = columnHeaderStyle(wb, headerBg);
        Row hdrRow = sheet.createRow(startRow++);
        for (int i = 0; i < cols.size(); i++) {
            Cell c = hdrRow.createCell(i + 1);
            c.setCellValue(cols.get(i));
            c.setCellStyle(colHdrStyle);
            sheet.setColumnWidth(i + 1, 5500);
        }

        // Data rows
        CellStyle bodyS = bodyStyle(wb);
        CellStyle pendS = pendingStyle(wb);
        for (List<String> rowData : rows) {
            Row r = sheet.createRow(startRow++);
            for (int i = 0; i < rowData.size(); i++) {
                Cell c = r.createCell(i + 1);
                c.setCellValue(rowData.get(i));
                c.setCellStyle(bodyS);
            }
            // Extra columns for settings: leave blank (filled by Appium)
            if (isSettings) {
                Cell apkCell  = r.createCell(rowData.size() + 1);
                Cell testCell = r.createCell(rowData.size() + 2);
                apkCell.setCellValue("PENDING");
                testCell.setCellValue("Not tested yet");
                apkCell.setCellStyle(pendS);
                testCell.setCellStyle(pendS);
            }
        }

        return startRow;
    }


    /**
     * Writes the Comparison block header rows.
     * Columns: Field | DB Value | APK Value | Result | Remarks
     * Rows are pre-populated from the data query's first row as sample.
     */
    private static int writeComparisonHeader(Workbook wb, Sheet sheet,
                                              Connection conn, int startRow,
                                              ModuleSheet m) {

        Row titleRow = sheet.createRow(startRow++);
        Cell tc = titleRow.createCell(1);
        tc.setCellValue("🔍  COMPARISON  –  DB vs APK  (filled by Appium tests)");
        tc.setCellStyle(sectionHeaderStyle(wb, COL_COMPARE_BG));

        // Column headers
        String[] compCols = {"Field / Record ID", "DB Value", "APK Value", "Result", "Remarks", "Tested At"};
        CellStyle chStyle = columnHeaderStyle(wb, COL_COMPARE_BG);
        Row hdr = sheet.createRow(startRow++);
        for (int i = 0; i < compCols.length; i++) {
            Cell c = hdr.createCell(i + 1);
            c.setCellValue(compCols[i]);
            c.setCellStyle(chStyle);
            sheet.setColumnWidth(i + 1, 6000);
        }

        // Pre-fill sample rows from data query (first 5 rows, first 3 columns)
        CellStyle pendS = pendingStyle(wb);
        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(m.dataQuery + " LIMIT 5")) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = Math.min(meta.getColumnCount(), 3);

            while (rs.next()) {
                for (int col = 1; col <= colCount; col++) {
                    String colName = meta.getColumnLabel(col);
                    String dbVal   = rs.getString(col);
                    if (dbVal == null) dbVal = "NULL";

                    Row r = sheet.createRow(startRow++);
                    r.createCell(1).setCellValue(colName);
                    r.createCell(2).setCellValue(dbVal);

                    Cell apkCell    = r.createCell(3);
                    Cell resultCell = r.createCell(4);
                    Cell remCell    = r.createCell(5);
                    Cell tsCell     = r.createCell(6);

                    apkCell.setCellValue("(run Appium test)");
                    resultCell.setCellValue("PENDING");
                    remCell.setCellValue("");
                    tsCell.setCellValue("");

                    apkCell.setCellStyle(pendS);
                    resultCell.setCellStyle(pendS);
                }
            }
        } catch (SQLException e) {
            sheet.createRow(startRow++).createCell(1)
                 .setCellValue("⚠ Could not pre-fill comparison: " + e.getMessage());
        }

        return startRow;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  PUBLIC API  –  called by DBValidationTest to write one comparison row
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Opens the existing HRMS_DB_Data.xlsx and appends one comparison row
     * to the named module sheet's Comparison block.
     *
     * @param sheetName  module sheet name (e.g. "Leave")
     * @param field      field / record identifier (e.g. "leave_id=42 | leave_type")
     * @param dbValue    value from DB
     * @param apkValue   value seen in APK
     * @param remarks    any extra note
     */
    public static void writeComparisonResult(String sheetName, String field,
                                              String dbValue, String apkValue,
                                              String remarks) {
        File f = new File(EXCEL_PATH);
        if (!f.exists()) {
            System.err.println("⚠ HRMS_DB_Data.xlsx not found. Run buildExcel() first.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(f);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheet(sanitize(sheetName));
            if (sheet == null) {
                System.err.println("⚠ Sheet not found: " + sheetName);
                return;
            }

            // Find first empty row after the Comparison header
            int lastRow = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(lastRow);

            boolean match = dbValue != null && dbValue.trim().equalsIgnoreCase(
                            apkValue != null ? apkValue.trim() : "");

            CellStyle matchStyle   = matchStyle(wb, match);
            CellStyle bodyS        = bodyStyle(wb);

            row.createCell(1).setCellValue(field);
            row.createCell(2).setCellValue(dbValue  != null ? dbValue  : "NULL");
            row.createCell(3).setCellValue(apkValue != null ? apkValue : "(not found)");

            Cell resultCell = row.createCell(4);
            resultCell.setCellValue(match ? "MATCH" : "MISMATCH");
            resultCell.setCellStyle(matchStyle);

            row.createCell(5).setCellValue(remarks != null ? remarks : "");
            row.createCell(6).setCellValue(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

            row.getCell(1).setCellStyle(bodyS);
            row.getCell(2).setCellStyle(bodyS);
            row.getCell(3).setCellStyle(bodyS);
            row.getCell(5).setCellStyle(bodyS);
            row.getCell(6).setCellStyle(bodyS);

            try (FileOutputStream fos = new FileOutputStream(f)) {
                wb.write(fos);
            }

            System.out.println((match ? "✅ MATCH" : "❌ MISMATCH") +
                " | " + sheetName + " | " + field +
                " | DB=" + dbValue + " | APK=" + apkValue);

        } catch (IOException e) {
            System.err.println("❌ writeComparisonResult failed: " + e.getMessage());
        }
    }


    // ═══════════════════════════════════════════════════════════════════════
    //  Style helpers
    // ═══════════════════════════════════════════════════════════════════════

    private static CellStyle sectionHeaderStyle(Workbook wb, short bg) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont(); f.setBold(true); f.setFontHeightInPoints((short)12);
        f.setColor(IndexedColors.WHITE.getIndex());
        s.setFont(f);
        s.setFillForegroundColor(bg);
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        s.setAlignment(HorizontalAlignment.LEFT);
        return s;
    }

    private static CellStyle columnHeaderStyle(Workbook wb, short bg) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont(); f.setBold(true);
        f.setColor(IndexedColors.WHITE.getIndex());
        s.setFont(f);
        s.setFillForegroundColor(bg);
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setBorderBottom(BorderStyle.THIN); s.setBorderTop(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);  s.setBorderRight(BorderStyle.THIN);
        return s;
    }

    private static CellStyle bodyStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        s.setBorderBottom(BorderStyle.THIN); s.setBorderTop(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);  s.setBorderRight(BorderStyle.THIN);
        return s;
    }

    private static CellStyle pendingStyle(Workbook wb) {
        CellStyle s = bodyStyle(wb);
        s.setFillForegroundColor(COL_PENDING_BG);
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font f = wb.createFont(); f.setItalic(true);
        s.setFont(f);
        return s;
    }

    private static CellStyle matchStyle(Workbook wb, boolean match) {
        CellStyle s = bodyStyle(wb);
        s.setFillForegroundColor(match ? COL_MATCH_BG : COL_MISMATCH_BG);
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font f = wb.createFont(); f.setBold(true);
        s.setFont(f);
        return s;
    }

    private static String sanitize(String name) {
        String c = name.replaceAll("[\\\\/*?\\[\\]:]", "_");
        return c.length() > 31 ? c.substring(0, 31) : c;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  ModuleSheet – data holder
    // ═══════════════════════════════════════════════════════════════════════
    public static class ModuleSheet {
        public final String sheetName;
        public final String settingsQuery; // nullable
        public final String dataQuery;

        public ModuleSheet(String sheetName, String settingsQuery, String dataQuery) {
            this.sheetName     = sheetName;
            this.settingsQuery = settingsQuery;
            this.dataQuery     = dataQuery;
        }
    }
}
