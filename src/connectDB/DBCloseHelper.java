package connectDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCloseHelper {

    /**
     * Đóng ResultSet nếu không null và bắt mọi SQLException.
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng ResultSet:");
                e.printStackTrace();
            }
        }
    }

    /**
     * Đóng Statement (hoặc PreparedStatement, CallableStatement) nếu không null.
     */
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng Statement:");
                e.printStackTrace();
            }
        }
    }

    /**
     * Đóng Connection nếu không null.
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng Connection:");
                e.printStackTrace();
            }
        }
    }

    /**
     * Đóng đồng thời ResultSet, Statement và Connection.
     * Thứ tự: ResultSet → Statement → Connection.
     */
    public static void closeAll(ResultSet rs, Statement stmt, Connection conn) {
        close(rs);
        close(stmt);
        close(conn);
    }

    /**
     * Đóng Statement và Connection (nếu bạn chỉ dùng Statement).
     */
    public static void closeAll(Statement stmt, Connection conn) {
        close(stmt);
        close(conn);
    }
}
// DBCloseHelper.java 
