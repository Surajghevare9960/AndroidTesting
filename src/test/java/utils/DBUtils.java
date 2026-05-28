package utils;

import java.sql.*;
import java.util.*;

/**
 * DBUtils – JDBC helper for running SELECT queries and inserting results
 * into Excel sheets via ExcelUtils.
 *
 * Usage:
 *   DBUtils db = new DBUtils();
 *   db.connect();
 *
 *   // Run a query → get data as list of maps
 *   List<Map<String,String>> rows = db.executeQuery("SELECT * FROM attendance WHERE emp_id = 1");
 *
 *   // Write to Excel
 *   ExcelUtils excel = new ExcelUtils("test-output/HRMS_TestReport.xlsx");
 *   db.queryToExcel(excel, "Attendance_DB", "SELECT * FROM attendance");
 *   excel.save();
 *
 *   db.disconnect();
 */
public class DBUtils {

    private Connection connection;

    // -----------------------------------------------------------------------
    // Connection management
    // -----------------------------------------------------------------------

    /** Opens a connection using config.properties values. */
    public void connect() throws SQLException, ClassNotFoundException {
        String url      = ConfigReader.get("db.url");
        String user     = ConfigReader.get("db.username");
        String password = ConfigReader.get("db.password");
        String driver   = ConfigReader.get("db.driver");

        Class.forName(driver);
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("✅ DB connected → " + url);
    }

    /** Opens a connection with explicit parameters. */
    public void connect(String url, String user, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("✅ DB connected → " + url);
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✅ DB disconnected.");
            } catch (SQLException ignored) {}
        }
    }

    // -----------------------------------------------------------------------
    // Query execution
    // -----------------------------------------------------------------------

    /**
     * Executes a SELECT query and returns results as a list of column→value maps.
     * Column names are used as keys (case-sensitive as returned by the DB).
     */
    public List<Map<String, String>> executeQuery(String sql) throws SQLException {
        List<Map<String, String>> results = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    String colName = meta.getColumnLabel(i);
                    String value   = rs.getString(i);
                    row.put(colName, value != null ? value : "NULL");
                }
                results.add(row);
            }
        }

        System.out.println("✅ Query returned " + results.size() + " rows.");
        return results;
    }

    /**
     * Returns the ordered list of column names for a given query.
     */
    public List<String> getColumnNames(String sql) throws SQLException {
        List<String> columns = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs   = stmt.executeQuery(sql + " LIMIT 0")) {
            ResultSetMetaData meta = rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                columns.add(meta.getColumnLabel(i));
            }
        }
        return columns;
    }

    // -----------------------------------------------------------------------
    // DB → Excel
    // -----------------------------------------------------------------------

    /**
     * Runs the given SQL query and writes the result into an Excel sheet.
     * The sheet name is derived from the sheetName parameter.
     *
     * @param excel     ExcelUtils instance (call excel.save() after all sheets are written)
     * @param sheetName name of the Excel sheet to create/replace
     * @param sql       SELECT query to execute
     */
    public void queryToExcel(ExcelUtils excel, String sheetName, String sql) throws SQLException {
        List<Map<String, String>> rows = executeQuery(sql);

        if (rows.isEmpty()) {
            System.out.println("⚠️  No data returned for sheet: " + sheetName);
            excel.writeSheet(sheetName, new String[]{"No Data"}, new ArrayList<>());
            return;
        }

        // Column names from first row keys (LinkedHashMap preserves order)
        List<String> columns = new ArrayList<>(rows.get(0).keySet());
        excel.writeSheetFromMaps(sheetName, columns, rows);
    }

    // -----------------------------------------------------------------------
    // Validation helpers
    // -----------------------------------------------------------------------

    /**
     * Returns a single String value from a query (first column of first row).
     * Useful for COUNT(*), MAX(), etc.
     */
    public String getSingleValue(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString(1);
            }
        }
        return null;
    }

    /**
     * Checks whether a row exists matching the given query.
     */
    public boolean recordExists(String sql) throws SQLException {
        String count = getSingleValue("SELECT COUNT(*) FROM (" + sql + ") AS t");
        return count != null && Integer.parseInt(count) > 0;
    }
}
