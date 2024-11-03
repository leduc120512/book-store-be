package com.example.api.cart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.example.api.cart.CartItemDeleteRequest;
import com.example.Utils.DBConnection;
import com.google.gson.Gson;
import com.example.Model.Cart.Cart_items;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/carts")
public class CartApiServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);

        int userId = Integer.parseInt(request.getParameter("userId")); // Nhận userId từ tham số
        List<CartItem> cartItems = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT " +
                    "ci.product_id, " +
                    "p.name AS product_name, " +
                    "ci.quantity, " +
                    "p.price, " +
                    "cat.category_name, " +
                    "p.image " +
                    "FROM carts c " +
                    "LEFT JOIN cart_items ci ON c.cart_id = ci.cart_id " +
                    "LEFT JOIN products p ON ci.product_id = p.product_id " +
                    "LEFT JOIN categories cat ON p.category_id = cat.category_id " +
                    "WHERE c.user_id = ? " +
                    "AND ci.product_id IS NOT NULL";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cartItems.add(new CartItem(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("category_name"),
                        rs.getString("image"))); // Đảm bảo rằng điều này khớp với trường trong bảng của bạn
            }

            response.setStatus(HttpServletResponse.SC_OK);
            String cartItemsJson = new Gson().toJson(cartItems);
            response.getWriter().write(cartItemsJson);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi cơ sở dữ liệu");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);

        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String jsonInput = sb.toString();

        Gson gson = new Gson();
        CartItem cartItem = gson.fromJson(jsonInput, CartItem.class);

        try (Connection conn = DBConnection.getConnection()) {
            addCartItem(conn, cartItem);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Sản phẩm đã được thêm vào giỏ hàng\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi cơ sở dữ liệu");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);

        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String jsonInput = sb.toString();

        Gson gson = new Gson();
        CartItemDeleteRequest deleteRequest = gson.fromJson(jsonInput, CartItemDeleteRequest.class);
        int cartItemId = deleteRequest.getCartItemId();
        int userId = deleteRequest.getUserId();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE ci FROM cart_items ci " +
                    "JOIN carts c ON ci.cart_id = c.cart_id " +
                    "WHERE ci.cart_item_id = ? AND c.user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cartItemId);
            stmt.setInt(2, userId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Sản phẩm đã được xóa khỏi giỏ hàng\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Sản phẩm không tìm thấy\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi cơ sở dữ liệu");
        }
    }

    private void setCORSHeaders(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setCharacterEncoding("UTF-8");
    }

    private List<CartItem> getCartItems(Connection conn, int userId) throws SQLException {
        String query = "SELECT " +
                "ci.product_id, " +
                "p.name AS product_name, " +
                "ci.quantity, " +
                "p.price, " +
                "cat.category_name, " +
                "p.image " +
                "FROM carts c " +
                "LEFT JOIN cart_items ci ON c.cart_id = ci.cart_id " +
                "LEFT JOIN products p ON ci.product_id = p.product_id " +
                "LEFT JOIN categories cat ON p.category_id = cat.category_id " +
                "WHERE c.user_id = ? " +
                "AND ci.product_id IS NOT NULL";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        List<CartItem> cartItemList = new ArrayList<>();
        while (rs.next()) {
            cartItemList.add(new CartItem(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getString("category_name"),
                    rs.getString("image")));
        }
        return cartItemList;
    }

    private void addCartItem(Connection conn, CartItem cartItem) throws SQLException {
        String sql = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, cartItem.getCartId());
        stmt.setInt(2, cartItem.getProductId());
        stmt.setInt(3, cartItem.getQuantity());
        stmt.executeUpdate();
    }
}