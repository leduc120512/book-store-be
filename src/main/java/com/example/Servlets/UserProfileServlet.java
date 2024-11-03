package com.example.Servlets;

import com.example.Model.Order.Order;
import com.example.Model.User.User;
import com.example.Utils.DBConnection;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/profile")
public class UserProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCORSHeaders(response);

        String userId = request.getParameter("user_id");
        if (userId == null || userId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is missing");
            return;
        }

        String sqlUser = "SELECT * FROM users WHERE user_id = ?";
        String sqlOrders = "SELECT o.order_id, o.total_price, o.status, o.created_at, oi.product_id, oi.quantity, oi.unit_price "
                + "FROM orders o "
                + "JOIN order_items oi ON o.order_id = oi.order_id "
                + "WHERE o.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
                PreparedStatement stmtOrders = conn.prepareStatement(sqlOrders)) {

            // Get user information
            stmtUser.setInt(1, Integer.parseInt(userId));
            ResultSet rsUser = stmtUser.executeQuery();

            User user = null;
            if (rsUser.next()) {
                user = new User(
                        rsUser.getInt("user_id"),
                        rsUser.getString("name_user"),
                        rsUser.getString("username"),
                        rsUser.getString("email"),
                        rsUser.getString("role"),
                        rsUser.getString("gender"),
                        rsUser.getDate("birthdate"),
                        rsUser.getString("avatar_url"));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }

            // Get user order history
            stmtOrders.setInt(1, Integer.parseInt(userId));
            ResultSet rsOrders = stmtOrders.executeQuery();

            List<Order> orderList = new ArrayList<>();
            while (rsOrders.next()) {
                Order order = new Order(
                        rsOrders.getInt("order_id"),
                        rsOrders.getBigDecimal("total_price"),
                        rsOrders.getString("status"),
                        rsOrders.getTimestamp("created_at"),
                        rsOrders.getInt("product_id"),
                        rsOrders.getInt("quantity"),
                        rsOrders.getBigDecimal("unit_price"));
                orderList.add(order);
            }

            // Create UserProfileWithOrders object
            UserProfileWithOrders profile = new UserProfileWithOrders(user, orderList);

            // Return JSON response
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(profile);
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    // Lớp đại diện cho phản hồi với thông tin người dùng và đơn hàng
    class UserProfileWithOrders {
        private User user;
        private List<Order> orders;

        public UserProfileWithOrders(User user, List<Order> orders) {
            this.user = user;
            this.orders = orders;
        }
    }
}
