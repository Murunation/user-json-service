package com.example.json.middleware;

import com.example.json.model.AuthInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthMiddleware extends OncePerRequestFilter {

    @Autowired
    private SoapAuthClient soapAuthClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // CORS headers нэмнэ
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");

        // OPTIONS preflight request-ийг шууд дамжуулна
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String path = request.getRequestURI();
        if (!path.startsWith("/users")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token шаардлагатай\"}");
            return;
        }

        String token = authHeader.substring(7);
        AuthInfo authInfo = soapAuthClient.validateToken(token);

        if (!authInfo.isValid()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token хүчингүй\"}");
            return;
        }

        request.setAttribute("authUserId", authInfo.getUserId());
        request.setAttribute("authUsername", authInfo.getUsername());
        request.setAttribute("authRole", authInfo.getRole());

        filterChain.doFilter(request, response);
    }
}