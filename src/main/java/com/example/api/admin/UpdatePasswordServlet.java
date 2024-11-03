package com.example.api.admin;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.example.Utils.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/api/admin/update-password")
public class UpdatePasswordServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "PUT");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        // Parse the request body to get user data
        String userId = request.getParameter("user_id");
        String currentPassword = request.getParameter("current_password");
        String newPassword = request.getParameter("new_password");

        if (userId == null || currentPassword == null || newPassword == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing parameters\"}");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Step 1: Get current password from database
            String getPasswordSQL = "SELECT password FROM users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(getPasswordSQL);
            stmt.setInt(1, Integer.parseInt(userId));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");

                // Step 2: Check if current password matches
                if (BCrypt.checkpw(currentPassword, hashedPassword)) {
                    // Step 3: Hash the new password
                    String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

                    // Step 4: Update the password in the database
                    String updatePasswordSQL = "UPDATE users SET password = ? WHERE user_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updatePasswordSQL);
                    updateStmt.setString(1, hashedNewPassword);
                    updateStmt.setInt(2, Integer.parseInt(userId));
                    int rowsAffected = updateStmt.executeUpdate();

                    if (rowsAffected > 0) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("{\"message\": \"Password updated successfully\"}");
                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\": \"Failed to update password\"}");
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"Incorrect current password\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"User not found\"}");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }
}