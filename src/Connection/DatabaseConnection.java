
package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = System.getenv("DB_URL");

    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQL Server JDBC driver not found.", e);
        }
    }

    public static Connection getConnection() throws SQLException {

        if (USER == null || PASSWORD == null) {
            throw new IllegalStateException(
                    "Database credentials are not configured. " +
                            "Please set DB_USER and DB_PASSWORD environment variables.");
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
