package com.example.json.service;

import com.example.json.model.UserProfile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {

    private final List<UserProfile> profiles = new ArrayList<>();
    private int nextId = 1;

    public UserProfile create(UserProfile profile) {
        profile.setId(nextId++);
        profiles.add(profile);
        return profile;
    }

    public UserProfile findByUserId(int userId) {
        for (UserProfile p : profiles) {
            if (p.getUserId() == userId) return p;
        }
        return null;
    }

    public UserProfile update(int userId, UserProfile updated) {
        for (UserProfile p : profiles) {
            if (p.getUserId() == userId) {
                if (updated.getName() != null) p.setName(updated.getName());
                if (updated.getEmail() != null) p.setEmail(updated.getEmail());
                if (updated.getBio() != null) p.setBio(updated.getBio());
                if (updated.getPhone() != null) p.setPhone(updated.getPhone());
                if (updated.getLocation() != null) p.setLocation(updated.getLocation());
                if (updated.getWebsite() != null) p.setWebsite(updated.getWebsite());
                return p;
            }
        }
        return null;
    }

    public boolean delete(int userId) {
        return profiles.removeIf(p -> p.getUserId() == userId);
    }

    public List<UserProfile> findAll() {
        return profiles;
    }
}