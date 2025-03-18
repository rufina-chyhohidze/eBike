package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.domain.User;
import be.kdg.integration4.domain.WorkshopAdmin;
import be.kdg.integration4.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/superadmin")
public class SystemAdminController {

    private final UserService userService;

    public SystemAdminController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/profile")
    public String adminDashboard(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        if (user instanceof WorkshopAdmin) {
            WorkshopAdmin admin = (WorkshopAdmin) user;
            model.addAttribute("workshopName", admin.getWorkshop() != null ? admin.getWorkshop().getWorkshopName() : "No Workshop Assigned");
        } else {
            model.addAttribute("workshopName", "N/A");
        }

        List<User> pendingUsers = userService.getUnapprovedUsers();
        model.addAttribute("pendingUsers", pendingUsers);

        model.addAttribute("user", user);
        return "super-admin";
    }

    @PostMapping("/approve/{id}")
    public String approveUser(@PathVariable Long id) {
        userService.approveUser(id);
        return "redirect:/superadmin/profile";
    }

    @PostMapping("/reject/{id}")
    public String rejectUser(@PathVariable Long id) {
        userService.rejectUser(id);
        return "redirect:/superadmin/profile";
    }
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return "redirect:/login?logout";
    }
}