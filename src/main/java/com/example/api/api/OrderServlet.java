package com.example.api.api;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import com.example.Utils.DBConnection;
import java.sql.Statement;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

@WebServlet("/api/order")
public class OrderServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
           response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        JsonObject jsonObject = JsonParser.parseString(sb.toString()).getAsJsonObject();
        if (jsonObject == null || !jsonObject.isJsonObject()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid JSON input\"}");
            return;
        }

        int userId;
        String shippingAddress;
        JsonArray products;
        try {
            userId = jsonObject.get("user_id").getAsInt();
            shippingAddress = jsonObject.get("shipping_address").getAsString();
            products = jsonObject.getAsJsonArray("products");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing or invalid fields\"}");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            long orderId = createOrder(conn, userId, shippingAddress);
            if (orderId == -1) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Failed to create order\"}");
                return;
            }

            if (!insertOrderItems(conn, orderId, products)) {
                conn.rollback();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Failed to insert order items\"}");
                return;
            }

            conn.commit();
            response.getWriter().write("{\"message\": \"Order placed successfully!\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Database error: " + e.getMessage() + "\"}");
        }
    }

    private long createOrder(Connection conn, int userId, String shippingAddress) throws SQLException {
        String sql = "INSERT INTO orders (date_order, total_price, status, shipping_address, user_id) VALUES (?, 0, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new Date(System.currentTimeMillis()));
            stmt.setString(2, "Pending");
            stmt.setString(3, shippingAddress);
            stmt.setInt(4, userId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return -1; // Order creation failed
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    return -1; // No ID obtained
                }
            }
        }
    }

    private boolean insertOrderItems(Connection conn, long orderId, JsonArray products) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price, image, name) VALUES (?, ?, ?, ?, ?, ?)";
        double totalPrice = 0;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < products.size(); i++) {
                JsonObject product = products.get(i).getAsJsonObject();
                int quantity = product.get("quantity").getAsInt();
                double price = product.get("price").getAsDouble();
                String image = product.get("image").getAsString();
                String name = product.get("name").getAsString();

                stmt.setLong(1, orderId);
                stmt.setInt(2, product.get("product_id").getAsInt());
                stmt.setInt(3, quantity);
                stmt.setDouble(4, price);
                stmt.setString(5, image);
                stmt.setString(6, name);
                stmt.addBatch();

                // Calculate total price
                totalPrice += quantity * price;
            }

            int[] affectedRows = stmt.executeBatch();
            if (affectedRows.length == products.size()) {
                // Update total price after successfully inserting order items
                updateOrderTotalPrice(conn, orderId, totalPrice);
                return true;
            }
        }

        return false; // Return false if the batch execution failed
    }

    private void updateOrderTotalPrice(Connection conn, long orderId, double totalPrice) throws SQLException {
        String sql = "UPDATE orders SET total_price = ? WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, totalPrice);
            stmt.setLong(2, orderId);
            stmt.executeUpdate();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        String orderIdParam = request.getParameter("order_id");
        if (orderIdParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing order_id parameter\"}");
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(orderIdParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid order_id parameter\"}");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            JsonObject orderDetails = getOrderDetails(conn, orderId);
            if (orderDetails != null) {
                response.getWriter().write(orderDetails.toString());
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Order not found\"}");
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Database error: " + e.getMessage() + "\"}");
        }
    }

    private JsonObject getOrderDetails(Connection conn, int orderId) throws SQLException {
        String sql = "SELECT o.order_id, o.date_order, o.total_price, o.status, o.shipping_address, "
                + "oi.quantity, oi.price, p.product_id, p.name AS product_name "
                + "FROM orders o "
                + "JOIN order_items oi ON o.order_id = oi.order_id "
                + "JOIN products p ON oi.product_id = p.product_id "
                + "WHERE o.order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JsonObject order = new JsonObject();
                order.addProperty("order_id", rs.getInt("order_id"));
                order.addProperty("date_order", rs.getDate("date_order").toString());
                order.addProperty("total_price", rs.getDouble("total_price"));
                order.addProperty("status", rs.getString("status"));
                order.addProperty("shipping_address", rs.getString("shipping_address"));

                JsonArray products = new JsonArray();
                do {
                    JsonObject product = new JsonObject();
                    product.addProperty("product_id", rs.getInt("product_id"));
                    product.addProperty("product_name", rs.getString("product_name"));
                    product.addProperty("quantity", rs.getInt("quantity"));
                    product.addProperty("price", rs.getDouble("price"));
                    products.add(product);
                } while (rs.next());

                order.add("products", products);
                return order;
            }
            return null; // No order found
        }
    }
}