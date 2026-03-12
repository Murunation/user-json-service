package com.example.soap.model;

public class AuthUser {
    private int id;
    private String username;
    private String passwordHash;
    private String email;
    private String role;
    private String token;

    public AuthUser() {}

    public AuthUser(int id, String username, String passwordHash,
                    String email, String role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}