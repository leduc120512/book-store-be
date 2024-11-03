package com.example.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bookstore3"; // Thay thế bằng tên cơ sở dữ liệu của
                                                                                // bạn
    private static final String USER = "root"; // Thay thế bằng tên đăng nhập cơ sở dữ liệu của bạn
    private static final String PASSWORD = "12345678"; // Thay thế bằng mật khẩu cơ sở dữ liệu của bạn

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Đăng ký driver
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver not found", e);
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close(); // Đóng kết nối
            } catch (SQLException e) {
                e.printStackTrace(); // Xử lý lỗi khi đóng
            }
        }
    }
}