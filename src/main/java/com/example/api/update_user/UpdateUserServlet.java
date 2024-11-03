package com.example.api.update_user;

import com.example.Utils.DBConnection;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/api/updateUser/*")
public class UpdateUserServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);
        response.setCharacterEncoding("UTF-8");

        String userIdParam = request.getPathInfo();
        PrintWriter out = response.getWriter();

        if (userIdParam == null || userIdParam.length() < 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdParam.substring(1)); // Remove leading "/"
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid User ID format");
            return;
        }

        UserUpdateRequest updateRequest;
        try {
            updateRequest = parseRequestBody(request);
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request body");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (getCurrentUserRole(conn, userId) == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }

            int rowsAffected = updateUser(conn, userId, updateRequest);
            if (rowsAffected > 0) {
                out.write("{\"message\": \"User updated successfully, role unchanged\"}");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update user");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } finally {
            out.close(); // Ensure PrintWriter is closed
        }
    }

    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    private UserUpdateRequest parseRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return new Gson().fromJson(sb.toString(), UserUpdateRequest.class);
    }

    private String getCurrentUserRole(Connection conn, int userId) throws SQLException {
        String roleSql = "SELECT role FROM users WHERE user_id = ?";
        try (PreparedStatement roleStmt = conn.prepareStatement(roleSql)) {
            roleStmt.setInt(1, userId);
            try (ResultSet roleRs = roleStmt.executeQuery()) {
                if (roleRs.next()) {
                    return roleRs.getString("role");
                }
            }
        }
        return null;
    }

    private int updateUser(Connection conn, int userId, UserUpdateRequest updateRequest) throws SQLException {
        String sql = "UPDATE users SET name = ?, address = ?, email = ?, username = ?, img = ? WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, updateRequest.getName());
            stmt.setString(2, updateRequest.getAddress());
            stmt.setString(3, updateRequest.getEmail());
            stmt.setString(4, updateRequest.getUsername());
            stmt.setString(5, updateRequest.getImg());
            stmt.setInt(6, userId);
            return stmt.executeUpdate();
        }
    }

    private static class UserUpdateRequest {
        private String name;
        private String address;
        private String email;
        private String username;
        private String img;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}