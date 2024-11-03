package com.example.api.product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.example.Utils.DBConnection;
import com.example.Model.Category.Categories;
import com.google.gson.Gson;
import com.example.Model.Product_buy.Product;;

@WebServlet("/api/products/*") // Ánh xạ servlet
public class ProductServlet extends HttpServlet {
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "GET,");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        List<Product> products = new ArrayList<>();
        String pathInfo = request.getPathInfo(); // Lấy thông tin đường dẫn
        String productIdParam = (pathInfo != null && pathInfo.length() > 1) ? pathInfo.substring(1) : null;

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            String sql;

            if (productIdParam != null) {
                int productId = Integer.parseInt(productIdParam);
                // Include 'sold' column in the query
                sql = "SELECT p.product_id, p.name, p.description, p.image, p.price, p.stock, p.sold, c.category_name "
                        +
                        "FROM products p " +
                        "LEFT JOIN categories c ON p.category_id = c.category_id " +
                        "WHERE p.product_id = " + productId;
            } else {
                // Include 'sold' column in the query
                sql = "SELECT p.product_id, p.name, p.description, p.image, p.price, p.stock, p.sold, c.category_name "
                        +
                        "FROM products p " +
                        "LEFT JOIN categories c ON p.category_id = c.category_id";
            }

            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setImage(rs.getString("image"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setStock(rs.getInt("stock"));
                product.setSold(rs.getInt("sold")); // Set the 'sold' value
                product.setCategoryName(rs.getString("category_name"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng kết nối
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(products);
        out.print(json);
        out.flush();
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "PUT");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Đọc dữ liệu từ request
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        String json = sb.toString();
        Product product = new Gson().fromJson(json, Product.class);

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE products SET name = ?, description = ?, image = ?, price = ?, stock = ?, category_id=? WHERE product_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setString(3, product.getImage());
            pstmt.setBigDecimal(4, product.getPrice());
            pstmt.setInt(5, product.getStock());

            pstmt.setInt(6, product.getCategories());
            pstmt.setInt(7, product.getProductId());

            pstmt.executeUpdate();

            response.setStatus(HttpServletResponse.SC_OK);
            out.print("{\"message\": \"Product updated successfully.\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"message\": \"Error updating product.\"}");
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng kết nối
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        out.flush();
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String pathInfo = request.getPathInfo();
        String productIdParam = (pathInfo != null && pathInfo.length() > 1) ? pathInfo.substring(1) : null;

        Connection conn = null;
        PreparedStatement pstmt = null;

        if (productIdParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"message\": \"Product ID is required.\"}");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);
            conn = DBConnection.getConnection();
            String sql = "DELETE FROM products WHERE product_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"message\": \"Product deleted successfully.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"message\": \"Product not found.\"}");
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"message\": \"Error deleting product.\"}");
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng kết nối
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Đọc dữ liệu từ request
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        String json = sb.toString();
        Product product = new Gson().fromJson(json, Product.class);

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO products (name, description, image, price, stock, sold, category_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setString(3, product.getImage());
            pstmt.setBigDecimal(4, product.getPrice());
            pstmt.setInt(5, product.getStock());
            pstmt.setInt(6, product.getSold());
            pstmt.setString(7, product.getCategoryName()); // Giả sử bạn có categoryId trong lớp Product

            pstmt.executeUpdate();

            response.setStatus(HttpServletResponse.SC_CREATED);
            out.print("{\"message\": \"Product added successfully.\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"message\": \"Error adding product.\"}");
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng kết nối
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        out.flush();
    }
    // Lớp Product

}
