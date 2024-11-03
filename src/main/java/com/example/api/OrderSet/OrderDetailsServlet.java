package com.example.api.OrderSet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.example.api.OrderSet.OrderDetailsServlet.OrderDetail;;;
import com.example.Utils.DBConnection;;
import java.math.BigDecimal;
import java.io.PrintWriter;
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

@WebServlet("/api/detailorder")
public class OrderDetailsServlet extends HttpServlet {
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        List<OrderDetail> orderDetails = new ArrayList<>();

        String pathInfo = request.getPathInfo();
        String[] params = (pathInfo != null && pathInfo.length() > 1) ? pathInfo.substring(1).split("/") : null;

        if (params == null || params.length < 2) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"message\": \"User ID and Order ID are required.\"}");
            out.flush();
            return;
        }

        int userId = Integer.parseInt(params[0]);
        int orderId = Integer.parseInt(params[1]);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT o.order_id, o.date_order, o.total_price, o.status, o.shipping_address, "
                    + "oi.quantity, oi.price, p.product_id, p.image, p.name AS product_name, "
                    + "c.category_name "
                    + "FROM orders o "
                    + "JOIN order_items oi ON o.order_id = oi.order_id "
                    + "JOIN products p ON oi.product_id = p.product_id "
                    + "JOIN categories c ON p.category_id = c.category_id "
                    + "WHERE o.user_id = ? AND o.order_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, orderId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderId(rs.getInt("order_id"));
                detail.setDateOrder(rs.getString("date_order"));
                detail.setTotalPrice(rs.getBigDecimal("total_price"));
                detail.setStatus(rs.getString("status"));
                detail.setShippingAddress(rs.getString("shipping_address"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPrice(rs.getBigDecimal("price"));
                detail.setProductId(rs.getInt("product_id"));
                detail.setImage(rs.getString("image"));
                detail.setProductName(rs.getString("product_name"));
                detail.setCategoryName(rs.getString("category_name"));
                orderDetails.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"message\": \"Error retrieving order details.\"}");
            out.flush();
            return;
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(orderDetails);
        out.print(json);
        out.flush();
    }

    public static class OrderDetail {
        private int orderId;
        private String dateOrder;
        private BigDecimal totalPrice;
        private String status;
        private String shippingAddress;
        private int quantity;
        private BigDecimal price;
        private int productId;
        private String image;
        private String productName;
        private String categoryName;

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

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(BigDecimal totalPrice) {
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

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }
}