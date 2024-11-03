package com.example.api.product;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
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

import com.google.gson.Gson;
import com.example.Model.Product_buy.Product;;

@WebServlet("/api/products-top/*")
public class Product_top extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "GET");
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
                        "ORDER BY p.sold DESC " + // Sắp xếp theo số lượng 'sold' giảm dần
                        "LIMIT 6"; // Giới hạn chỉ 6 sản phẩm

            } else {
                // Include 'sold' column in the query
                sql = "SELECT p.product_id, p.name, p.description, p.image, p.price, p.stock, p.sold, c.category_name "
                        +
                        "FROM products p " +
                        "LEFT JOIN categories c ON p.category_id = c.category_id " +
                        "ORDER BY p.sold DESC " +
                        "LIMIT 5";

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
}
