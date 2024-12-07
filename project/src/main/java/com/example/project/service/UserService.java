package com.example.project.service;

import com.example.project.entities.User;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    // Регистрация пользователя
    public User save(User user) {
        return userRepository.save(user);  // Сохраняем пользователя в базе данных
    }

    // Логин пользователя и получение токенов
    public Map<String, String> loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким email не найден."));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Неверный пароль.");
        }

        // Генерация токенов
        String accessToken = jwtService.generateAccessToken(email);
        String refreshToken = jwtService.generateRefreshToken(email);

        // Возвращаем токены в ответе
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return tokens;
    }

    // Проверка существования пользователя по email
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
