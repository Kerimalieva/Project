package com.example.project.controller;

import com.example.project.LoginRequest;
import com.example.project.entities.User;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        // Здесь токен не требуется, просто создаем нового пользователя
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(null); // Email уже занят
        }

        User newUser = userService.save(user);  // Сохраняем нового пользователя
        return ResponseEntity.ok(newUser);  // Возвращаем нового пользователя
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginRequest loginRequest) {
        Map<String, String> tokens = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(tokens);  // Возвращаем токены после успешного логина
    }
}

