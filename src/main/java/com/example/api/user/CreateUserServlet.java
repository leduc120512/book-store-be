package com.example.api.user;

import com.example.Utils.DBConnection;
import org.mindrot.jbcrypt.BCrypt;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/create_user")
public class CreateUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set encoding and CORS headers
        request.setCharacterEncoding("UTF-8");
        setCORSHeaders(response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        StringBuilder sb = new StringBuilder();

        // Read JSON data from request body
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Unable to read request body\"}");
            return;
        }

        String requestBody = sb.toString();
        if (requestBody.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Request body cannot be empty\"}");
            return;
        }

        JsonObject jsonObject;
        try {
            jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Invalid JSON format\"}");
            return;
        }

        // Extract fields from JSON
        String name = jsonObject.get("name").getAsString();
        String email = jsonObject.get("email").getAsString();
        String phone = jsonObject.get("phone").getAsString();
        String address = jsonObject.get("address").getAsString();
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();

        // Validate required fields
        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"All required fields must be filled\"}");
            return;
        }

        // Hash the password
        // Keep the password as is
        String hashedPassword = password;
        String role = "customer";
        String img = "https://down-vn.img.susercontent.com/file/15bf25c1c7a8c2ba0f02239fcd976001_tn";
        // Insert user into the database
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO users (name, email, phone, address, username, password, role,img) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, phone);
                stmt.setString(4, address);
                stmt.setString(5, username);
                stmt.setString(6, hashedPassword);
                stmt.setString(7, role);
                stmt.setString(8, img);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"message\": \"User created successfully\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\": \"Failed to create user\"}");
                }
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Database error: " + e.getMessage() + "\"}");
        }
    }

    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setCharacterEncoding("UTF-8");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCORSHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}