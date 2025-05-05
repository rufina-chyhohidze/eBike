package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.config.security.annotations.StaffOnly;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.service.interfaces.BikeReportService;
import be.kdg.integration4.service.interfaces.CustomerService;
import be.kdg.integration4.service.interfaces.ReportSettingService;
import be.kdg.integration4.service.interfaces.TechnicianService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/workshopadmin")
public class WorkshopAdminController {

    private final TechnicianService technicianService;
    private final CustomerService customerService;
    private final BikeReportService bikeReportService;

    public WorkshopAdminController(TechnicianService technicianService, CustomerService customerService, BikeReportService bikeReportService) {
        this.technicianService = technicianService;
        this.customerService = customerService;
        this.bikeReportService = bikeReportService;
    }

    @GetMapping("/dashboard")
    @StaffOnly
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String loggedInEmail = authentication.getName();

        Technician technician = technicianService.getByEmail(loggedInEmail);


        int totalReports = technicianService.getTotalReportsByTechnician(technician.getId());

        List<Customer> customers = customerService.getAll();
        List<BikeReport> bikeReports = bikeReportService.getAll()
                .stream()
                .filter(report -> report.getTechnician().getId().equals(technician.getId()))
                .collect(Collectors.toList());

        model.addAttribute("totalReports", totalReports);
        model.addAttribute("totalClients", customers.size());
        model.addAttribute("technician", technician);
        model.addAttribute("customers", customers);
        model.addAttribute("bikeReports", bikeReports);
        return "admin";
    }
}
