package ru.anotherworld.server.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = (InMemoryUserDetailsManager)userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping(value = "/register", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String username = request.get("username");
        String password = request.get("password");

        if (username == null || username.trim().isEmpty()) {
            response.put("message", "Имя пользователя не может быть пустым");
            return ResponseEntity.badRequest().body(response);
        }
        if (password == null || password.trim().isEmpty()) {
            response.put("message", "Пароль не может быть пустым");
            return ResponseEntity.badRequest().body(response);
        }
        if (password.length() < 6) {
            response.put("message", "Пароль должен содержать не менее 6 символов");
            return ResponseEntity.badRequest().body(response);
        }
        if (((InMemoryUserDetailsManager)userDetailsService).userExists(username.trim())) {
            response.put("message", "Пользователь с таким именем уже существует");
            return ResponseEntity.badRequest().body(response);
        }

        var newUser = User.withUsername(username.trim())
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build();
        ((InMemoryUserDetailsManager)userDetailsService).createUser(newUser);

        response.put("success", true);
        response.put("redirect", "/login");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> loginUser() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("redirect", "/");
        return ResponseEntity.ok(response);
    }
}
