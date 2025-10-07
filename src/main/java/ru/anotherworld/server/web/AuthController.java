package ru.anotherworld.server.web;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
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

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        if (((InMemoryUserDetailsManager)userDetailsService).userExists(username)) {
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            return "register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "register";
        }

        if (password.length() < 6) {
            model.addAttribute("error", "Пароль должен содержать не менее 6 символов");
            return "register";
        }

        try {
            UserDetails user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .roles("USER")
                    .build();

            ((InMemoryUserDetailsManager)userDetailsService).createUser(user);
            model.addAttribute("success", true);
            return "register";

        } catch (Exception e) {
            model.addAttribute("error", "Произошла ошибка при регистрации. Пожалуйста, попробуйте снова.");
            return "register";
        }
    }
}
