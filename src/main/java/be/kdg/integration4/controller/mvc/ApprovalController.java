package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.domain.User;
import be.kdg.integration4.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/superadmin")
public class ApprovalController {

    private final UserService userService;

    public ApprovalController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/pending-approvals")
    public String viewPendingApprovals(Model model) {
        List<User> pendingUsers = userService.getUnapprovedUsers();
        model.addAttribute("pendingUsers", pendingUsers);
        return "pending-approvals";
    }

    @PostMapping("/approve/{id}")
    public String approveUser(@PathVariable Long id) {
        userService.approveUser(id);
        return "redirect:/superadmin/pending-approvals";
    }

    @PostMapping("/reject/{id}")
    public String rejectUser(@PathVariable Long id) {
        userService.rejectUser(id);
        return "redirect:/superadmin/pending-approvals";
    }
}
