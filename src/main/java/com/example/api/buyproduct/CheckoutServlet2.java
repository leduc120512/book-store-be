// package com.example.api.buyproduct;

// import com.google.gson.JsonObject;
// import com.google.gson.JsonParser;
// import com.example.Utils.DBConnection;

// import javax.servlet.annotation.WebServlet;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.BufferedReader;
// import java.io.IOException;
// import java.sql.*;

// @WebServlet("/api/order")
// public class OrderServlet extends HttpServlet {

//     @Override
//     protected void doOptions(HttpServletRequest request, HttpServletResponse response)
//             throws ServletException, IOException {
//         response.setHeader("Access-Control-Allow-Origin", "*");
//         response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
//         response.setHeader("Access-Control-Allow-Headers", "Content-Type");
//         response.setStatus(HttpServletResponse.SC_OK);
//     }

//     @Override
//     protected void doPost(HttpServletRequest request, HttpServletResponse response)
//             throws ServletException, IOException {

//         response.setContentType("application/json");
//         response.setCharacterEncoding("UTF-8");
//         response.setHeader("Access-Control-Allow-Origin", "*");

//         // Đọc nội dung JSON từ yêu cầu
//         StringBuilder sb = new StringBuilder();
//         String line;
//         try (BufferedReader reader = request.getReader()) {
//             while ((line = reader.readLine()) != null) {
//                 sb.append(line);
//             }
//         }

//         // Parse JSON body
//         JsonObject jsonObject = JsonParser.parseString(sb.toString()).getAsJsonObject();
//         if (jsonObject == null || !jsonObject.isJsonObject()) {
//             response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//             response.getWriter().write("{\"error\": \"Invalid JSON input\"}");
//             return;
//         }

//         int userId = jsonObject.get("user_id").getAsInt();
//         String shippingAddress = jsonObject.get("shipping_address").getAsString();
//         String status = "pending"; // Trạng thái mặc định cho đơn hàng mới
//         Date dateOrder = new Date(System.currentTimeMillis()); // Ngày tạo đơn hàng

//         Connection conn = null;
//         try {
//             conn = DBConnection.getConnection();
//             conn.setAutoCommit(false); // Bắt đầu transaction

//             // Tạo đơn hàng mới trong bảng `orders`
//             String createOrderSQL = "INSERT INTO orders (date_order, total_price, status, shipping_address, user_id) VALUES (?, 0, ?, ?, ?)";
//             PreparedStatement createOrderStmt = conn.prepareStatement(createOrderSQL, Statement.RETURN_GENERATED_KEYS);
//             createOrderStmt.setDate(1, dateOrder);
//             createOrderStmt.setString(2, status);
//             createOrderStmt.setString(3, shippingAddress);
//             createOrderStmt.setInt(4, userId);
//             createOrderStmt.executeUpdate();

//             ResultSet generatedKeys = createOrderStmt.getGeneratedKeys();
//             int orderId;
//             if (generatedKeys.next()) {
//                 orderId = generatedKeys.getInt(1);
//             } else {
//                 throw new SQLException("Creating order failed, no ID obtained.");
//             }

//             // Lấy giỏ hàng hiện tại của người dùng
//             String getCartSQL = "SELECT ci.product_id, ci.quantity, p.price FROM cart_items ci JOIN products p ON ci.product_id = p.product_id WHERE ci.cart_id = (SELECT cart_id FROM carts WHERE user_id = ? AND order_id IS NULL)";
//             PreparedStatement getCartStmt = conn.prepareStatement(getCartSQL);
//             getCartStmt.setInt(1, userId);
//             ResultSet cartItems = getCartStmt.executeQuery();

//             double totalPrice = 0;

//             // Cập nhật đơn hàng và tính tổng giá trị
//             while (cartItems.next()) {
//                 int productId = cartItems.getInt("product_id");
//                 int quantity = cartItems.getInt("quantity");
//                 double price = cartItems.getDouble("price");
//                 totalPrice += price * quantity;

//                 // Trừ hàng tồn kho
//                 String updateStockSQL = "UPDATE products SET stock = stock - ? WHERE product_id = ?";
//                 PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSQL);
//                 updateStockStmt.setInt(1, quantity);
//                 updateStockStmt.setInt(2, productId);
//                 updateStockStmt.executeUpdate();
//             }

//             // Cập nhật tổng giá trị đơn hàng
//             String updateOrderSQL = "UPDATE orders SET total_price = ? WHERE order_id = ?";
//             PreparedStatement updateOrderStmt = conn.prepareStatement(updateOrderSQL);
//             updateOrderStmt.setDouble(1, totalPrice);
//             updateOrderStmt.setInt(2, orderId);
//             updateOrderStmt.executeUpdate();

//             // Cập nhật `order_id` trong giỏ hàng
//             String updateCartSQL = "UPDATE carts SET order_id = ? WHERE user_id = ? AND order_id IS NULL";
//             PreparedStatement updateCartStmt = conn.prepareStatement(updateCartSQL);
//             updateCartStmt.setInt(1, orderId);
//             updateCartStmt.setInt(2, userId);
//             updateCartStmt.executeUpdate();

//             conn.commit(); // Hoàn thành transaction
//             response.setStatus(HttpServletResponse.SC_OK);
//             response.getWriter().write("{\"message\": \"Order placed successfully\", \"order_id\": " + orderId + "}");
//         } catch (SQLException e) {
//             if (conn != null) {
//                 try {
//                     conn.rollback(); // Rollback nếu có lỗi
//                 } catch (SQLException ex) {
//                     ex.printStackTrace();
//                 }
//             }
//             e.printStackTrace();
//             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
//         } finally {
//             if (conn != null) {
//                 try {
//                     conn.close(); // Đóng kết nối
//                 } catch (SQLException e) {
//                     e.printStackTrace();
//                 }
//             }
//         }
//     }
// }