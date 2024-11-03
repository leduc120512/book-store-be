package com.example.api.buyproduct;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


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


@WebServlet("/api/cart")
public class AddToCartServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        // Đọc nội dung JSON từ yêu cầu
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        // Parse JSON body
        JsonObject jsonObject = JsonParser.parseString(sb.toString()).getAsJsonObject();
        if (jsonObject == null || !jsonObject.isJsonObject()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid JSON input\"}");
            return;
        }

        int userId = jsonObject.get("user_id").getAsInt();
        int productId = jsonObject.get("product_id").getAsInt();
        int quantity = jsonObject.get("quantity").getAsInt();

        if (quantity <= 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid quantity\"}");
            return;
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Kiểm tra giỏ hàng
            String checkCartSQL = "SELECT cart_id FROM carts WHERE user_id = ? AND order_id IS NULL";
            PreparedStatement checkCartStmt = conn.prepareStatement(checkCartSQL);
            checkCartStmt.setInt(1, userId);
            ResultSet cartRs = checkCartStmt.executeQuery();

            int cartId;
            if (cartRs.next()) {
                cartId = cartRs.getInt("cart_id");
            } else {
                // Tạo giỏ hàng mới
                String createCartSQL = "INSERT INTO carts (transaction_id_user, transaction_id_merchant, user_id) VALUES (?, ?, ?)";
                PreparedStatement createCartStmt = conn.prepareStatement(createCartSQL,
                        Statement.RETURN_GENERATED_KEYS);
                createCartStmt.setString(1, "TXN_NEW");
                createCartStmt.setString(2, "MERCHANT_NEW");
                createCartStmt.setInt(3, userId);
                createCartStmt.executeUpdate();

                ResultSet generatedKeys = createCartStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    cartId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating cart failed, no ID obtained.");
                }
            }

            // Kiểm tra sản phẩm trong giỏ hàng
            String checkItemSQL = "SELECT quantity FROM cart_items WHERE cart_id = ? AND product_id = ?";
            PreparedStatement checkItemStmt = conn.prepareStatement(checkItemSQL);
            checkItemStmt.setInt(1, cartId);
            checkItemStmt.setInt(2, productId);
            ResultSet itemRs = checkItemStmt.executeQuery();

            if (itemRs.next()) {
                // Cập nhật số lượng nếu sản phẩm đã có trong giỏ hàng
                int existingQuantity = itemRs.getInt("quantity");
                int newQuantity = existingQuantity + quantity;

                String updateCartSQL = "UPDATE cart_items SET quantity = ? WHERE cart_id = ? AND product_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateCartSQL);
                updateStmt.setInt(1, newQuantity);
                updateStmt.setInt(2, cartId);
                updateStmt.setInt(3, productId);
                updateStmt.executeUpdate();

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Product quantity updated in cart\"}");
            } else {
                // Thêm sản phẩm mới vào giỏ hàng
                String addToCartSQL = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";
                PreparedStatement addStmt = conn.prepareStatement(addToCartSQL);
                addStmt.setInt(1, cartId);
                addStmt.setInt(2, productId);
                addStmt.setInt(3, quantity);
                addStmt.executeUpdate();

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Product added to cart successfully\"}");
            }

            conn.commit(); // Hoàn thành transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn); // Đảm bảo đóng kết nối
        }
    }
}
