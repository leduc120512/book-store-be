package com.example.api.deleteAdmin2;

import javax.servlet.annotation.WebServlet;
import com.google.gson.Gson;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.example.Model.User.User;
import com.example.Utils.DBConnection;

@WebServlet("/api/admin/user2")
public class displayUser extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "GET, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT user_id, name, username, phone FROM users");
                ResultSet rs = stmt.executeQuery()) {

            List<User> userList = new ArrayList<>();

            while (rs.next()) {
                userList.add(new User(rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("phone")));
            }

            String userJson = new Gson().toJson(userList);
            response.getWriter().write(userJson);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get ID from URL
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"User ID is missing\"}");
            return;
        }
        String userId = pathInfo.substring(1);

        try (Connection conn = DBConnection.getConnection()) {
            // Disable auto-commit for transaction management
            conn.setAutoCommit(false);

            try {
                // Delete cart items
                String deleteCartItemsSQL = "DELETE FROM cart_items WHERE cart_id IN (SELECT cart_id FROM carts WHERE user_id = ?)";
                try (PreparedStatement stmt = conn.prepareStatement(deleteCartItemsSQL)) {
                    stmt.setInt(1, Integer.parseInt(userId));
                    stmt.executeUpdate();
                }

                // Delete orders and associated order items
                String deleteOrderItemsSQL = "DELETE FROM order_items WHERE order_id IN (SELECT order_id FROM orders WHERE user_id = ?)";
                try (PreparedStatement stmt = conn.prepareStatement(deleteOrderItemsSQL)) {
                    stmt.setInt(1, Integer.parseInt(userId));
                    stmt.executeUpdate();
                }

                String deleteOrdersSQL = "DELETE FROM orders WHERE user_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteOrdersSQL)) {
                    stmt.setInt(1, Integer.parseInt(userId));
                    stmt.executeUpdate();
                }

                // Delete carts
                String deleteCartsSQL = "DELETE FROM carts WHERE user_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteCartsSQL)) {
                    stmt.setInt(1, Integer.parseInt(userId));
                    stmt.executeUpdate();
                }

                // Finally, delete the user
                String deleteUserSQL = "DELETE FROM users WHERE user_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteUserSQL)) {
                    stmt.setInt(1, Integer.parseInt(userId));
                    int rowsAffected = stmt.executeUpdate();

                    if (rowsAffected > 0) {
                        // Commit transaction
                        conn.commit();
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("{\"message\": \"User deleted successfully\"}");
                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\": \"User not found\"}");
                    }
                }
            } catch (SQLException e) {
                conn.rollback(); // Rollback on error
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection error");
        }
    }
}