package com.example.project;

import com.example.project.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Логируем запрос
        System.out.println("Request URI: " + request.getRequestURI());

        // Пропускаем запросы на регистрацию и логин
        String path = request.getRequestURI();
        if (path.contains("/api/users/register") || path.contains("/api/users/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Проверка токена для других маршрутов
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // Извлекаем токен из "Bearer <token>"
            System.out.println("Extracted Token: " + token);

            String email = jwtService.extractUsername(token);
            if (email != null) {
                System.out.println("Extracted Email: " + email);
            }

            if (email != null && jwtService.validateToken(token, email)) {
                // Токен валиден, извлекаем ID пользователя
                String userId = jwtService.extractUserId(token);  // Добавьте метод в JwtService для извлечения ID пользователя

                // Помещаем userId в атрибуты запроса для дальнейшего использования
                request.setAttribute("userId", userId);

                // Токен валиден, продолжаем выполнение запроса
                filterChain.doFilter(request, response);
            } else {
                // Если токен невалиден, возвращаем ошибку с пояснением
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Unauthorized, token is invalid or expired\"}");
            }
        } else {
            // Если токен не найден в заголовках, возвращаем ошибку
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized, token is missing\"}");
        }
    }
}
