package com.example.Inventrix.controller;

import com.example.Inventrix.model.User;
import com.example.Inventrix.service.UserService;
import com.example.Inventrix.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/inventrix/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // 🔹 Login untuk OWNER & GUDANG
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Optional<User> userOpt = userService.findByUsername(username);
        Map<String, Object> response = new HashMap<>();

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (userService.checkPassword(password, user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
                response.put("id", user.getId());
                response.put("pesan", "Login berhasil");
                response.put("token", token);
                response.put("role", user.getRole());
                response.put("username", user.getUsername());
                return response;
            }
        }

        response.put("pesan", "Username atau password salah");
        return response;
    }

    @PostMapping("/guest")
    public Map<String, Object> guestLogin() {
        Map<String, Object> response = new HashMap<>();

        // Anggap login sebagai karyawan toko
        String username = "karyawan";
        String role = "KARYAWAN";
        String token = jwtUtil.generateToken(username, role);

        response.put("id", 0);
        response.put("pesan", "Login sebagai karyawan toko (guest)");
        response.put("token", token);
        response.put("role", role);
        response.put("username", username);

        return response;
    }

}

