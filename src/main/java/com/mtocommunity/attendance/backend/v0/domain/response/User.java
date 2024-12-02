package com.mtocommunity.attendance.backend.v0.domain.response;

import org.json.JSONObject;

public class User {
    private String username;
    private UserRole role;
    private String name;
    private String last_name;
    private String email;
    private String phone_number;

    public User() {
    }

    public User(String username, UserRole role, String name, String last_name, String email, String phone_number) {
        this.username = username;
        this.role = role;
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public JSONObject toJSONObject() {
        return new JSONObject()
                .put("username", username)
                .put("role", role)
                .put("name", name)
                .put("last_name", last_name)
                .put("email", email)
                .put("phone_number", phone_number);
    }
}
