package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.config.security.annotations.SystemAdminOnly;
import be.kdg.integration4.controller.api.dtos.UserWithRolesDto;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.UserDetailsImpl;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.service.email.EmailService;
import be.kdg.integration4.service.interfaces.BikeReportService;
import be.kdg.integration4.service.interfaces.CustomerService;
import be.kdg.integration4.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/superadmin")
public class SystemAdminController {

    private final UserService userService;
    private final EmailService emailService;
    private final CustomerService customerService;
    private final BikeReportService bikeReportService;

    public SystemAdminController(UserService userService, EmailService emailService, CustomerService customerService, BikeReportService bikeReportService) {
        this.userService = userService;
        this.customerService = customerService;
        this.bikeReportService = bikeReportService;
        this.emailService = emailService;
    }

    @GetMapping("/profile")
    @SystemAdminOnly
    public String adminDashboard(Model model, @AuthenticationPrincipal UserDetailsImpl principal) {
        String email = principal.getUsername();
        User user = userService.getUserByEmail(email);
        List<User> pendingUsers = userService.getUnapprovedUsers();
        List<BikeReport> reports = bikeReportService.getAllReportsWithDetails();
        model.addAttribute("pendingUsers",
                pendingUsers.stream().map(
                        usr -> new UserWithRolesDto(
                                usr.getId(),
                                usr.getName(),
                                usr.getEmail(),
                                usr.getClass().getSimpleName().toUpperCase()
                        )
                ).toList()
        );
        model.addAttribute("reports", reports);
        model.addAttribute("user", new UserWithRolesDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getClass().getSimpleName().toUpperCase()
        ));
//        model.addAttribute("customers", userService.getAllWithoutLoggedInUser(principal.getUserId()).stream()
//                .map(usr ->
//                        new UserWithRolesDto(usr.getId(),usr.getName(),usr.getEmail(),
//                                usr.getClass().getSimpleName().toUpperCase()))
//                .toList());
        return "super-admin-dashboard";
    }

    @GetMapping("/profile/update")
    @SystemAdminOnly
    public String updateProfile(Model model, @AuthenticationPrincipal UserDetailsImpl principal) {
        model.addAttribute("accountId", principal.getUserId());
        return "password-change";
    }


    @PostMapping("/approve/{id}")
    @SystemAdminOnly
    public String approveUser(@PathVariable Long id) {
        userService.approveUser(id);
        String email = this.userService.getUserById(id).getEmail();
        try {
            this.emailService.sendUserApprovalEmail(email);
        } catch (Exception e) {
            log.error("Unable to send user approved email: {}", e.getMessage());
        }
        return "redirect:/superadmin/profile";
    }

    @PostMapping("/reject/{id}")
    @SystemAdminOnly
    public String rejectUser(@PathVariable Long id) {
        userService.rejectUser(id);
        String email = this.userService.getUserById(id).getEmail();
        try {
            this.emailService.sendUserRejectedEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("Unable to send user rejected email: " + e.getMessage());
        }
        return "redirect:/superadmin/profile";
    }

    @GetMapping("/reports")
    @SystemAdminOnly
    public String reports(@RequestParam Long customerId,Model model, @AuthenticationPrincipal UserDetailsImpl principal) {
        List<BikeReport> reports = bikeReportService.getAllReportsWithDetails().stream().filter(report ->
                report.getCustomer().getId().equals(customerId)).toList();
        Customer customer = customerService.getById(customerId);
        model.addAttribute("reports", reports);
        model.addAttribute("customer", customer);
        return "superadmin-reports";
    }
}