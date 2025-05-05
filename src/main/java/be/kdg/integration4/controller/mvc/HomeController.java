package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.domain.enums.UserRole;
import be.kdg.integration4.domain.profile.UserDetailsImpl;
import be.kdg.integration4.service.interfaces.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl authentication) {

        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String userRole = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(role -> role.replace("ROLE_", "")) // Remove "ROLE_" prefix
                    .collect(Collectors.joining(", "));

            model.addAttribute("userRole", userRole);
//            model.addAttribute("user", userService.getById(authentication.getUserId()));
            if (authorities.contains(new SimpleGrantedAuthority("ROLE_" + UserRole.TECHNICIAN))) {
                return "redirect:/technician/dashboard";
            } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_" + UserRole.SYSTEMADMIN))) {
                return "redirect:/superadmin/profile";
            } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_" + UserRole.WORSHOPADMIN))) {
                return "redirect:/workshopadmin/dashboard";
            } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_" + UserRole.CUSTOMER))) {
                return "redirect:/customer/dashboard";
            }
        } else {
            model.addAttribute("userRole", "GUEST");
        }



        return "home"; // Loads home.html
    }
}

