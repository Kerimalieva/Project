package com.example.project.service;

import com.example.project.entities.User;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;  // Подключаем JwtService

    public User registerUser(String username, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует.");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        // Прочие параметры

        return userRepository.save(newUser);
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким email не найден."));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Неверный пароль.");
        }

        // Генерация JWT токена после успешного входа
        return jwtService.generateToken(email);
    }
}
