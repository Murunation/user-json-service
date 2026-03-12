package com.example.json.controller;

import com.example.json.model.UserProfile;
import com.example.json.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UserProfile> users = profileService.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<?> createProfile(
            @RequestBody UserProfile profile,
            HttpServletRequest request) {
        Integer authUserId = (Integer) request.getAttribute("authUserId");
        String authUsername = (String) request.getAttribute("authUsername");

        if (authUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Token шаардлагатай"));
        }

        profile.setUserId(authUserId);
        profile.setUsername(authUsername);

        UserProfile created = profileService.create(profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable int id) {
        UserProfile profile = profileService.findByUserId(id);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Профайл олдсонгүй"));
        }
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable int id,
            @RequestBody UserProfile profile,
            HttpServletRequest request) {
        Integer authUserId = (Integer) request.getAttribute("authUserId");
        String authRole = (String) request.getAttribute("authRole");

        if (authUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Token шаардлагатай"));
        }

        if (authUserId != id && !"admin".equals(authRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "Зөвшөөрөлгүй"));
        }

        UserProfile updated = profileService.update(id, profile);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Профайл олдсонгүй"));
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(
            @PathVariable int id,
            HttpServletRequest request) {
        Integer authUserId = (Integer) request.getAttribute("authUserId");
        String authRole = (String) request.getAttribute("authRole");

        if (authUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Token шаардлагатай"));
        }

        if (authUserId != id && !"admin".equals(authRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "Зөвшөөрөлгүй"));
        }

        boolean deleted = profileService.delete(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Профайл олдсонгүй"));
        }
        return ResponseEntity.noContent().build();
    }
}