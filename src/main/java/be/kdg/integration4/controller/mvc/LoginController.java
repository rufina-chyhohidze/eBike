package be.kdg.integration4.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Loads login.html from templates
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "signup"; // Loads register.html from templates
    }
}
