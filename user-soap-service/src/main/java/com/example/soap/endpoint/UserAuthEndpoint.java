package com.example.soap.endpoint;

import com.example.soap.model.AuthUser;
import com.example.soap.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import jakarta.xml.bind.annotation.*;

@Endpoint
public class UserAuthEndpoint {

    private static final String NS = "http://num.edu.mn/users/auth";

    @Autowired
    private AuthService authService;

    @PayloadRoot(namespace = NS, localPart = "RegisterUserRequest")
    @ResponsePayload
    public RegisterUserResponse register(@RequestPayload RegisterUserRequest req) {
        System.out.println("=== REGISTER called: " + req.username);
        AuthUser user = authService.register(req.username, req.password, req.email);
        System.out.println("=== REGISTER result: " + (user != null ? "SUCCESS" : "FAILED"));
        RegisterUserResponse res = new RegisterUserResponse();
        if (user != null) {
            res.success = true;
            res.message = "Бүртгэл амжилттай!";
            res.userId  = user.getId();
        } else {
            res.success = false;
            res.message = "Username эсвэл email аль хэдийн бүртгэгдсэн.";
        }
        return res;
    }

    @PayloadRoot(namespace = NS, localPart = "LoginUserRequest")
    @ResponsePayload
    public LoginUserResponse login(@RequestPayload LoginUserRequest req) {
        System.out.println("=== LOGIN called: " + req.username);
        AuthUser user = authService.login(req.username, req.password);
        LoginUserResponse res = new LoginUserResponse();
        if (user != null) {
            res.success = true;
            res.message = "Нэвтрэлт амжилттай!";
            res.token   = user.getToken();
            res.userId  = user.getId();
            res.role    = user.getRole();
        } else {
            res.success = false;
            res.message = "Нэр эсвэл нууц үг буруу.";
        }
        return res;
    }

    @PayloadRoot(namespace = NS, localPart = "ValidateTokenRequest")
    @ResponsePayload
    public ValidateTokenResponse validate(@RequestPayload ValidateTokenRequest req) {
        System.out.println("=== VALIDATE called: " + req.token);
        AuthUser user = authService.validateToken(req.token);
        ValidateTokenResponse res = new ValidateTokenResponse();
        if (user != null) {
            res.valid    = true;
            res.userId   = user.getId();
            res.username = user.getUsername();
            res.role     = user.getRole();
            res.message  = "Token хүчинтэй.";
        } else {
            res.valid   = false;
            res.message = "Token хүчингүй.";
        }
        return res;
    }

    // ── Inner classes ─────────────────────────────────────────────────

    @XmlRootElement(namespace = NS, name = "RegisterUserRequest")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RegisterUserRequest {
        @XmlElement(namespace = NS)
        public String username;
        @XmlElement(namespace = NS)
        public String password;
        @XmlElement(namespace = NS)
        public String email;
    }

    @XmlRootElement(namespace = NS, name = "RegisterUserResponse")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RegisterUserResponse {
        public boolean success;
        public String message;
        public int userId;
    }

    @XmlRootElement(namespace = NS, name = "LoginUserRequest")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class LoginUserRequest {
        @XmlElement(namespace = NS)
        public String username;
        @XmlElement(namespace = NS)
        public String password;
    }

    @XmlRootElement(namespace = NS, name = "LoginUserResponse")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class LoginUserResponse {
        public boolean success;
        public String message;
        public String token;
        public String role;
        public int userId;
    }

    @XmlRootElement(namespace = NS, name = "ValidateTokenRequest")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ValidateTokenRequest {
        @XmlElement(namespace = NS)
        public String token;
    }

    @XmlRootElement(namespace = NS, name = "ValidateTokenResponse")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ValidateTokenResponse {
        public boolean valid;
        public int userId;
        public String username;
        public String role;
        public String message;
    }
}