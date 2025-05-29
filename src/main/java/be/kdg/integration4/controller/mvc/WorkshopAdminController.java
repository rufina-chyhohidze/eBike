package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.config.security.annotations.StaffOnly;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.UserDetailsImpl;
import be.kdg.integration4.domain.profile.WorkshopAdmin;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.service.interfaces.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/workshopadmin")
public class WorkshopAdminController {

    private final TechnicianService technicianService;
    private final CustomerService customerService;
    private final BikeReportService bikeReportService;
    private final WorkshopAdminService workshopAdminService;

    public WorkshopAdminController(TechnicianService technicianService, CustomerService customerService, BikeReportService bikeReportService, WorkshopAdminService workshopAdminService) {
        this.technicianService = technicianService;
        this.customerService = customerService;
        this.bikeReportService = bikeReportService;
        this.workshopAdminService = workshopAdminService;
    }

    @GetMapping("/dashboard")
    @StaffOnly
    public String dashboard(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        WorkshopAdmin workshopAdmin = workshopAdminService.getById(userDetails.getUserId());


        List<Customer> customers = customerService.getCustomersByWorkshop(workshopAdmin.getWorkshop().getWorkshopId());
        List<BikeReport> bikeReports = bikeReportService.getReportsByWorkshop(workshopAdmin.getWorkshop().getWorkshopId());
        int totalReports =bikeReports.size();
        model.addAttribute("totalReports", totalReports);
        model.addAttribute("totalClients", customers.size());
        model.addAttribute("admin", workshopAdmin);
        model.addAttribute("customers", customers);
        model.addAttribute("bikeReports", bikeReports);
        return "admin-dashboard";
    }

    @GetMapping("/dashboard/update")
    @StaffOnly
    public String updateDashboard(Model model, @AuthenticationPrincipal UserDetailsImpl principal) {
        model.addAttribute("accountId", principal.getUserId());
        return "password-change";
    }
}
