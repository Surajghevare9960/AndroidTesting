package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * ExcelUtils – write test data / DB query results into an Excel workbook.
 *
 * Usage:
 *   ExcelUtils excel = new ExcelUtils("test-output/HRMS_TestReport.xlsx");
 *   excel.writeSheet("Attendance", headers, rows);
 *   excel.save();
 */
public class ExcelUtils {

    private final String filePath;
    private Workbook workbook;

    public ExcelUtils(String filePath) {
        this.filePath = filePath;
        // Load existing workbook or create new
        File file = new File(filePath);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(fis);
            } catch (IOException e) {
                workbook = new XSSFWorkbook();
            }
        } else {
            workbook = new XSSFWorkbook();
        }
    }

    // -----------------------------------------------------------------------
    // Write a sheet from a list of String arrays
    // -----------------------------------------------------------------------

    /**
     * Creates (or replaces) a sheet with the given name and writes headers + rows.
     *
     * @param sheetName name of the Excel sheet
     * @param headers   column header names
     * @param rows      each row is a String[]
     */
    public void writeSheet(String sheetName, String[] headers, List<String[]> rows) {

        // Remove existing sheet if present
        int existingIdx = workbook.getSheetIndex(sheetName);
        if (existingIdx >= 0) {
            workbook.removeSheetAt(existingIdx);
        }

        Sheet sheet = workbook.createSheet(sheetName);

        // Header style
        CellStyle headerStyle = createHeaderStyle();

        // Write header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 6000);
        }

        // Write data rows
        int rowNum = 1;
        for (String[] rowData : rows) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < rowData.length; i++) {
                row.createCell(i).setCellValue(rowData[i] != null ? rowData[i] : "");
            }
        }

        System.out.println("✅ Sheet written: " + sheetName + " (" + rows.size() + " rows)");
    }

    // -----------------------------------------------------------------------
    // Write a sheet from a List<Map<String,String>> (DB result set style)
    // -----------------------------------------------------------------------

    /**
     * Writes DB result-set style data (list of column→value maps) into a sheet.
     *
     * @param sheetName sheet name
     * @param columns   ordered list of column names (used as headers)
     * @param data      list of row maps
     */
    public void writeSheetFromMaps(String sheetName, List<String> columns, List<Map<String, String>> data) {

        int existingIdx = workbook.getSheetIndex(sheetName);
        if (existingIdx >= 0) {
            workbook.removeSheetAt(existingIdx);
        }

        Sheet sheet = workbook.createSheet(sheetName);
        CellStyle headerStyle = createHeaderStyle();

        // Header
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns.get(i));
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 6000);
        }

        // Data
        int rowNum = 1;
        for (Map<String, String> rowMap : data) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < columns.size(); i++) {
                String val = rowMap.getOrDefault(columns.get(i), "");
                row.createCell(i).setCellValue(val != null ? val : "");
            }
        }

        System.out.println("✅ Sheet written: " + sheetName + " (" + data.size() + " rows)");
    }

    // -----------------------------------------------------------------------
    // Append a single result row to a "Test Results" sheet
    // -----------------------------------------------------------------------

    /**
     * Appends a test result row to the "Test Results" sheet.
     * Creates the sheet with headers if it doesn't exist.
     */
    public void appendTestResult(String moduleName, String testCase,
                                  String status, String expected,
                                  String actual, String remarks) {

        String sheetName = "Test Results";
        Sheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
            CellStyle hs = createHeaderStyle();
            Row h = sheet.createRow(0);
            String[] headers = {"Module", "Test Case", "Status", "Expected", "Actual", "Remarks", "Timestamp"};
            for (int i = 0; i < headers.length; i++) {
                Cell c = h.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(hs);
                sheet.setColumnWidth(i, 6000);
            }
        }

        int lastRow = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(lastRow);
        row.createCell(0).setCellValue(moduleName);
        row.createCell(1).setCellValue(testCase);
        row.createCell(2).setCellValue(status);
        row.createCell(3).setCellValue(expected);
        row.createCell(4).setCellValue(actual);
        row.createCell(5).setCellValue(remarks);
        row.createCell(6).setCellValue(java.time.LocalDateTime.now().toString());

        // Colour the status cell
        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(row.getCell(2).getCellStyle());
        if ("PASS".equalsIgnoreCase(status)) {
            style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        } else {
            style.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        }
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        row.getCell(2).setCellStyle(style);
    }

    // -----------------------------------------------------------------------
    // Save workbook to disk
    // -----------------------------------------------------------------------

    public void save() {
        // Ensure parent directories exist
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
            System.out.println("✅ Excel saved → " + filePath);
        } catch (IOException e) {
            System.err.println("❌ Failed to save Excel: " + e.getMessage());
        }
    }

    public void close() {
        try {
            workbook.close();
        } catch (IOException ignored) {}
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    private CellStyle createHeaderStyle() {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}
