package com.example.soap.service;

import com.example.soap.model.AuthUser;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    private List<AuthUser> users;
    private int nextId;

    @PostConstruct
    public void init() {
        users = new ArrayList<>();
        nextId = 1;
        System.out.println("=== AuthService initialized. Users cleared. ===");
    }

    public AuthUser register(String username, String password, String email) {
        if (username == null || password == null || email == null) return null;

        for (AuthUser u : users) {
            if (username.equals(u.getUsername()) || email.equals(u.getEmail())) {
                return null;
            }
        }

        AuthUser user = new AuthUser(
            nextId++, username, hashPassword(password), email, "user"
        );
        users.add(user);
        System.out.println("=== Registered: " + username + " | Total: " + users.size() + " ===");
        return user;
    }

    public AuthUser login(String username, String password) {
        if (username == null || password == null) return null;
        String hash = hashPassword(password);
        for (AuthUser u : users) {
            if (u.getUsername() == null) continue;
            if (u.getUsername().equals(username) && u.getPasswordHash().equals(hash)) {
                String token = UUID.randomUUID().toString().replace("-", "");
                u.setToken(token);
                return u;
            }
        }
        return null;
    }

    public AuthUser validateToken(String token) {
        if (token == null || token.isEmpty()) return null;
        for (AuthUser u : users) {
            if (token.equals(u.getToken())) return u;
        }
        return null;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest((password + "lab06_salt").getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }
}