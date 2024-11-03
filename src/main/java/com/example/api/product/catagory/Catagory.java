package com.example.api.product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.example.Utils.DBConnection;
import com.google.gson.Gson;
import com.example.Model.Category.Categories;

@WebServlet("/api/Category/*") // Đảm bảo đường dẫn chính xác
public class Catagory extends HttpServlet {
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
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        List<Categories> categories = new ArrayList<>();
        String pathInfo = request.getPathInfo();
        String categoryParam = (pathInfo != null && pathInfo.length() > 1) ? pathInfo.substring(1) : null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql;

            if (categoryParam != null) {
                int categoryId = Integer.parseInt(categoryParam);
                sql = "SELECT categories.category_id, categories.category_name " +
                        "FROM categories " +
                        "WHERE categories.category_id = ?";

                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, categoryId);
            } else {
                // Truy vấn tất cả các danh mục nếu không có tham số
                sql = "SELECT categories.category_id, categories.category_name FROM categories";
                pstmt = conn.prepareStatement(sql);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                Categories category = new Categories();
                category.setCategories(rs.getInt("category_id"));
                category.setCategories_name(rs.getString("category_name"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng kết nối
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
        String json = gson.toJson(categories);
        out.print(json);
        out.flush();
    }
}