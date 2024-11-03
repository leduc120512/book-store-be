package com.example.Model.User;

import java.sql.Date;

public class User {
    private int id;
    private String nameUser;
    private String userName;
    private String email;
    private String password;
    private String role;
    private String gender;
    private Date birthday;
    private String avatar;
    private Date createdAt;
    private Date updatedAt;

    private String phone;
    private String Address;

    public User(String nameUser, String userName, String phone, String Adress, String password) {
        this.nameUser = nameUser;
        this.userName = userName;
        this.phone = phone;
        this.Address = Address;
        this.password = password;
    }

    // api get user
    public User(int id, String nameUser, String userName, String phone) {
        this.id = id;
        this.nameUser = nameUser;
        this.userName = userName;
        this.phone = phone;

    }

    public User(int id, String nameUser, String userName, String email, String role, String gender, Date birthday,
            String avatar) {
        this.id = id;
        this.nameUser = nameUser;
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.gender = gender;
        this.birthday = birthday;
        this.avatar = avatar;
    }

    // Constructor with all parameters
    public User(int id, String nameUser, String userName, String email, String password, String role, String gender,
            Date birthday, String avatar, Date createdAt, Date updatedAt) {
        this.id = id;
        this.nameUser = nameUser;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.gender = gender;
        this.birthday = birthday;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getPhone() {
        return phone;
    }

    public String getAdress() {
        return Address;
    }

    // Constructor for login scenario
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User() {
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
