package com.example.json.model;

public class AuthInfo {
    private boolean valid;
    private int userId;
    private String username;
    private String role;
    private String message;

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}