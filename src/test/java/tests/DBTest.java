package tests;

import utils.ConfigReader;

import java.sql.*;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 *  DBTest  –  SQL Server Connection Tester
 * ═══════════════════════════════════════════════════════════════════════════
 *
 *  HOW TO RUN
 *  ──────────
 *  Right-click this file → Run 'DBTest.main()'
 *  No Appium / device needed.
 *
 *  WHAT IT DOES
 *  ────────────
 *  1. Reads credentials from config/config.properties
 *  2. Loads the SQL Server JDBC driver
 *  3. Opens a connection to the DB
 *  4. Prints server version, database name, current user
 *  5. Lists all user tables in the connected database
 *  6. Runs a quick row-count on each table
 *  7. Closes the connection cleanly
 *
 *  CONNECTION DETAILS (from config.properties)
 *  ────────────────────────────────────────────
 *  db.url      = jdbc:sqlserver://192.168.1.200:1433;
 *                databaseName=March_2026_Suraj;
 *                encrypt=true;trustServerCertificate=true
 *  db.username = sa
 *  db.password = orange505
 *  db.driver   = com.microsoft.sqlserver.jdbc.SQLServerDriver
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class DBTest {

    // ── Connection details – read from config.properties ────────────────────
    private static final String URL      = ConfigReader.get("db.url");
    private static final String USERNAME = ConfigReader.get("db.username");
    private static final String PASSWORD = ConfigReader.get("db.password");
    private static final String DRIVER   = ConfigReader.get("db.driver");

    // ════════════════════════════════════════════════════════════════════════
    //  MAIN
    // ════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {

        printBanner("HRMS SQL Server – Connection Test");
        System.out.println("  Host    : " + URL);
        System.out.println("  User    : " + USERNAME);
        System.out.println("  Driver  : " + DRIVER);
        System.out.println();

        Connection con = openConnection();
        if (con == null) return;   // error already printed

        try {
            // ── 1. Server info ───────────────────────────────────────────
            printSection("Server Information");
            printServerInfo(con);

            // ── 2. List all user tables ──────────────────────────────────
            printSection("Tables in Database");
            listTables(con);

            // ── 3. Quick sample query ────────────────────────────────────
            printSection("Sample Query  (TOP 5 rows from first available table)");
            runSampleQuery(con);

        } catch (Exception e) {
            System.err.println("❌ Error during test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(con);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Step 1 – Open connection
    // ════════════════════════════════════════════════════════════════════════
    private static Connection openConnection() {
        try {
            Class.forName(DRIVER);
            System.out.println("✅ Driver loaded  : " + DRIVER);

            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Connected       : " + URL);
            System.out.println();
            return con;

        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver not found: " + DRIVER);
            System.err.println("   Make sure mssql-jdbc is in pom.xml dependencies.");
            e.printStackTrace();
            return null;

        } catch (SQLException e) {
            System.err.println("❌ Connection FAILED");
            System.err.println("   SQL State : " + e.getSQLState());
            System.err.println("   Error Code: " + e.getErrorCode());
            System.err.println("   Message   : " + e.getMessage());
            System.err.println();
            System.err.println("   Common causes:");
            System.err.println("   • SQL Server not reachable at 192.168.1.200:1433");
            System.err.println("   • SQL Server Browser service not running");
            System.err.println("   • TCP/IP not enabled in SQL Server Configuration Manager");
            System.err.println("   • Firewall blocking port 1433");
            System.err.println("   • Wrong username / password");
            System.err.println("   • Database 'March_2026_Suraj' does not exist");
            e.printStackTrace();
            return null;
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Step 2 – Print server info
    // ════════════════════════════════════════════════════════════════════════
    private static void printServerInfo(Connection con) throws SQLException {
        DatabaseMetaData meta = con.getMetaData();
        System.out.println("  DB Product  : " + meta.getDatabaseProductName());
        System.out.println("  DB Version  : " + meta.getDatabaseProductVersion());
        System.out.println("  Driver Name : " + meta.getDriverName());
        System.out.println("  Driver Ver  : " + meta.getDriverVersion());

        // Current database and user via SQL
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT DB_NAME() AS db_name, SYSTEM_USER AS login_user, " +
                 "USER_NAME() AS db_user, @@SERVERNAME AS server_name")) {
            if (rs.next()) {
                System.out.println("  Database    : " + rs.getString("db_name"));
                System.out.println("  Login User  : " + rs.getString("login_user"));
                System.out.println("  DB User     : " + rs.getString("db_user"));
                System.out.println("  Server Name : " + rs.getString("server_name"));
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Step 3 – List all user tables with row counts
    // ════════════════════════════════════════════════════════════════════════
    private static void listTables(Connection con) throws SQLException {

        String sql =
            "SELECT t.TABLE_NAME, " +
            "       p.rows AS row_count " +
            "FROM   INFORMATION_SCHEMA.TABLES t " +
            "LEFT JOIN sys.tables st ON st.name = t.TABLE_NAME " +
            "LEFT JOIN sys.partitions p ON p.object_id = st.object_id " +
            "                          AND p.index_id IN (0,1) " +
            "WHERE  t.TABLE_TYPE = 'BASE TABLE' " +
            "ORDER  BY t.TABLE_NAME ASC";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            int count = 0;
            System.out.printf("  %-50s %s%n", "Table Name", "Row Count");
            System.out.println("  " + "─".repeat(65));

            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String rowCount  = rs.getString("row_count");
                System.out.printf("  %-50s %s%n",
                    tableName,
                    rowCount != null ? rowCount : "N/A");
                count++;
            }

            System.out.println("  " + "─".repeat(65));
            System.out.println("  Total tables: " + count);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Step 4 – Run a sample SELECT on the first available table
    // ════════════════════════════════════════════════════════════════════════
    private static void runSampleQuery(Connection con) throws SQLException {

        // Get first table name
        String firstTable = null;
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT TOP 1 TABLE_NAME FROM INFORMATION_SCHEMA.TABLES " +
                 "WHERE TABLE_TYPE='BASE TABLE' ORDER BY TABLE_NAME")) {
            if (rs.next()) firstTable = rs.getString(1);
        }

        if (firstTable == null) {
            System.out.println("  No tables found in database.");
            return;
        }

        System.out.println("  Running: SELECT TOP 5 * FROM [" + firstTable + "]");
        System.out.println();

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT TOP 5 * FROM [" + firstTable + "]")) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            // Print column headers
            StringBuilder header = new StringBuilder("  ");
            for (int i = 1; i <= colCount; i++) {
                header.append(String.format("%-20s", meta.getColumnName(i)));
            }
            System.out.println(header);
            System.out.println("  " + "─".repeat(Math.min(colCount * 20, 120)));

            // Print rows
            int rowNum = 0;
            while (rs.next()) {
                StringBuilder row = new StringBuilder("  ");
                for (int i = 1; i <= colCount; i++) {
                    String val = rs.getString(i);
                    if (val == null) val = "NULL";
                    if (val.length() > 18) val = val.substring(0, 15) + "...";
                    row.append(String.format("%-20s", val));
                }
                System.out.println(row);
                rowNum++;
            }
            System.out.println();
            System.out.println("  Rows shown: " + rowNum);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Close connection
    // ════════════════════════════════════════════════════════════════════════
    private static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                System.out.println();
                System.out.println("✅ Connection closed cleanly.");
            } catch (SQLException e) {
                System.err.println("⚠️  Error closing connection: " + e.getMessage());
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Helpers
    // ════════════════════════════════════════════════════════════════════════
    private static void printBanner(String title) {
        String line = "═".repeat(title.length() + 4);
        System.out.println("╔" + line + "╗");
        System.out.println("║  " + title + "  ║");
        System.out.println("╚" + line + "╝");
        System.out.println();
    }

    private static void printSection(String title) {
        System.out.println();
        System.out.println("┌─ " + title + " " + "─".repeat(Math.max(0, 60 - title.length())));
    }
}
