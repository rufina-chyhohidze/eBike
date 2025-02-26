package be.kdg.integration4.controller.mvc;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {
//    @GetMapping("/")
//    public String home(Model model) {
//        return "home";
//    }
@GetMapping("/")
public String home(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String userRole = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", "")) // Remove "ROLE_" prefix
                .collect(Collectors.joining(", "));

        System.out.println("User Role: " + userRole); // Debugging
        model.addAttribute("userRole", userRole);
    } else {
        System.out.println("No authentication found!"); // Debugging
        model.addAttribute("userRole", "GUEST");
    }

    return "home"; // Loads home.html
}
}

