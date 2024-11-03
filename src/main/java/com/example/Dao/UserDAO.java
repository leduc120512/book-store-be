package com.example.Dao;

import com.example.Model.User.User;
import com.example.Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public User authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?"; // Sử dụng mật khẩu đã băm trong sản
                                                                                // xuất
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // Trong sản xuất, sử dụng mật khẩu đã băm

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setNameUser(rs.getString("name_user"));
                user.setUserName(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setGender(rs.getString("gender"));
                user.setBirthday(rs.getDate("birthdate"));
                user.setAvatar(rs.getString("avatar_url"));
                return user;
            }
        }
        return null; // Trả về null nếu không tìm thấy người dùng
    }
}