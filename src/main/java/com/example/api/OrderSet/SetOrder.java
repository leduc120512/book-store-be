package com.example.api.OrderSet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.example.api.OrderSet.SetOrder.OrderItem;;
import com.example.Utils.DBConnection;;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/orders")
public class SetOrder extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        JsonObject jsonObject;
        try {
            jsonObject = JsonParser.parseString(sb.toString()).getAsJsonObject();
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON input");
            return;
        }

        int userId = jsonObject.get("user_id").getAsInt();
        int orderId = jsonObject.get("order_id").getAsInt();
        String newStatus = jsonObject.get("status").getAsString();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE orders SET status = ? WHERE user_id = ? AND order_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newStatus);
                stmt.setInt(2, userId);
                stmt.setInt(3, orderId);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    sendSuccessResponse(response, "Order status updated successfully");
                } else {
                    sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND,
                            "Order not found or user not authorized");
                }
            }
        } catch (SQLException e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);
        List<Order> orders = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM orders";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    String dateOrder = rs.getDate("date_order").toString();
                    double totalPrice = rs.getDouble("total_price");
                    String status = rs.getString("status");
                    String shippingAddress = rs.getString("shipping_address");
                    int user_id = rs.getInt("user_id");

                    // Khởi tạo một danh sách OrderItem rỗng
                    List<OrderItem> items = new ArrayList<>();

                    // Tạo một đối tượng Order với danh sách OrderItem rỗng
                    Order order = new Order(orderId, dateOrder, totalPrice, status, shippingAddress, user_id, items);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            return;
        }

        Gson gson = new Gson();
        String json = gson.toJson(orders);
        response.setContentType("application/json");
        response.getWriter().write(json);
    }

    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*"); // Hoặc URL cụ thể
        response.setHeader("Access-Control-Allow-Methods", "GET, PUT, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setCharacterEncoding("UTF-8");
    }

    private void sendSuccessResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"message\": \"" + message + "\"}");
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

    class Order {
        private int orderId;
        private String dateOrder;
        private double totalPrice;
        private String status;
        private String shippingAddress;
        private int user_id;
        private List<OrderItem> items;

        // Constructor
        public Order(int orderId, String dateOrder, double totalPrice, String status, String shippingAddress,
                int user_id, List<OrderItem> items) {
            this.orderId = orderId;
            this.dateOrder = dateOrder;
            this.totalPrice = totalPrice;
            this.status = status;
            this.shippingAddress = shippingAddress;
            this.user_id = user_id;
            this.items = items;
        }

        public int getuser_id() {
            return user_id;
        }

        public void setuser_id(int user_id) {
            this.user_id = user_id;
        }

        // Getters and Setters
        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getDateOrder() {
            return dateOrder;
        }

        public void setDateOrder(String dateOrder) {
            this.dateOrder = dateOrder;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
        }

        public List<OrderItem> getItems() {
            return items;
        }

        public void setItems(List<OrderItem> items) {
            this.items = items;
        }
    }

    class OrderItem {
        private int productId;
        private String productName;
        private int quantity;
        private double price;
        private String categoryName;
        private String image;

        // Constructor
        public OrderItem(int productId, String productName, int quantity, double price, String categoryName,
                String image) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
            this.categoryName = categoryName;
            this.image = image;
        }

        // Getters and Setters
        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}