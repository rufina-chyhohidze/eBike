package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.service.interfaces.WorkshopService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private final WorkshopService workshopService;

    public LoginController(WorkshopService workshopService) {
        this.workshopService = workshopService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Loads login.html from templates
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("workshops", workshopService.getAll());
        return "signup"; // Loads register.html from templates
    }
}
