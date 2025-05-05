package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.config.security.annotations.CustomerOnly;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.service.interfaces.BikeReportService;
import be.kdg.integration4.service.interfaces.BikeService;
import be.kdg.integration4.service.interfaces.CustomerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/customer")
public class CustomerDashboardController {

    private final CustomerService customerService;
    private final BikeReportService bikeReportService;
    private final BikeService bikeService;

    public CustomerDashboardController(CustomerService customerService, BikeReportService bikeReportService, BikeService bikeService) {
        this.customerService = customerService;
        this.bikeReportService = bikeReportService;
        this.bikeService = bikeService;
    }

    @GetMapping("/dashboard")
    @CustomerOnly
    public String dashboard(Model model) {
        // Get the logged-in customer
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();
        Customer customer = customerService.getByEmailIgnoreCase(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Get the customer's bike reports
        List<BikeReport> bikeReports = bikeReportService.getAll()
                .stream()
                .filter(report -> report.getCustomer().getId().equals(customer.getId()))
                .collect(Collectors.toList());

        // Get the customer's bikes
        Set<Bike> customerBikes = bikeService.getAllByOwnerId(customer.getId());

        // Get the latest report date
        String latestReportDate = bikeReports.stream()
                .max(Comparator.comparing(BikeReport::getReportDate))
                .map(report -> report.getReportDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .orElse("N/A");

        // Add attributes to the model
        model.addAttribute("customer", customer);
        model.addAttribute("totalReports", bikeReports.size());
        model.addAttribute("totalBikes", customerBikes.size());
        model.addAttribute("latestReportDate", latestReportDate);
        model.addAttribute("bikeReports", bikeReports);

        return "customer-dashboard";
    }

    @GetMapping("/reports")
    @CustomerOnly
    public String reports(Model model) {
        // Get the logged-in customer
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();
        Customer customer = customerService.getByEmailIgnoreCase(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Get the customer's bike reports
        List<BikeReport> bikeReports = bikeReportService.getAll()
                .stream()
                .filter(report -> report.getCustomer().getId().equals(customer.getId()))
                .collect(Collectors.toList());

        // Add attributes to the model
        model.addAttribute("customer", customer);
        model.addAttribute("bikeReports", bikeReports);

        return "customer-reports";
    }
}
