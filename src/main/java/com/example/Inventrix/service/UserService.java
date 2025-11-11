package com.example.Inventrix.service;

import com.example.Inventrix.model.User;
import com.example.Inventrix.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🔹 Inisialisasi user default (owner dan gudang)
    @PostConstruct
    public void initDefaultUsers() {
        if (userRepository.findByUsername("owner").isEmpty()) {
            User owner = new User();
            owner.setUsername("owner");
            owner.setPassword(encoder.encode("owner123"));
            owner.setRole("OWNER");
            userRepository.save(owner);
        }

        if (userRepository.findByUsername("gudang").isEmpty()) {
            User gudang = new User();
            gudang.setUsername("gudang");
            gudang.setPassword(encoder.encode("gudang123"));
            gudang.setRole("WAREHOUSE");
            userRepository.save(gudang);
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean checkPassword(String raw, String encoded) {
        return encoder.matches(raw, encoded);
    }
}

