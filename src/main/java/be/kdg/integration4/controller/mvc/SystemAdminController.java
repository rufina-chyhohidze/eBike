package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.service.interfaces.BikeReportService;
import be.kdg.integration4.service.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/superadmin")
public class SystemAdminController {

    private final UserService userService;
    private final BikeReportService bikeReportService;

    public SystemAdminController(UserService userService, BikeReportService bikeReportService) {
        this.userService = userService;
        this.bikeReportService = bikeReportService;
    }
    @GetMapping("/profile")
    public String adminDashboard(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<User> pendingUsers = userService.getUnapprovedUsers();
        model.addAttribute("pendingUsers", pendingUsers);

        List<BikeReport> reports = bikeReportService.getAllWithDetails();
        model.addAttribute("reports", reports);
        model.addAttribute("user", user);
        return "super-admin";
    }

//    @GetMapping("/reports")
//    public String viewReports(@RequestParam(required = false) String frameNumber,
//                              @RequestParam(required = false) String customerName,
//                              Model model) {
//
//        List<BikeReport> reports = bikeReportService.searchReports(frameNumber, customerName);
//        model.addAttribute("reports", reports);
//
//        return "super-admin";
//    }

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

}