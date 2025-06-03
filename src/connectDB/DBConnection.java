package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // URL kết nối tới SQL Server
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QLTHUOC;encrypt=false;";
    private static final String USER = "sa";
    private static final String PASS = "123123";

    static {
        try {
            // Nạp driver JDBC của Microsoft SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy driver JDBC cho SQL Server:");
            e.printStackTrace();
        }
    }

    /**
     * Trả về một Connection tới database QLTHUOC.
     * @return Connection đã mở
     * @throws SQLException nếu không thể kết nối
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
// DBConnection.java 
