import java.sql.*;
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/bankmanagement";
    private static final String USER = "postgres";  // change as per your MySQL username
    private static final String PASSWORD = "Arn@b123";  // change as per your MySQL password

    // Get a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    // Close the resources
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
