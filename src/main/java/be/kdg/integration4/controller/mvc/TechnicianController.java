package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.config.security.annotations.TechnicianOnly;
import be.kdg.integration4.domain.enums.BikeSize;
import be.kdg.integration4.domain.enums.TestType;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.service.interfaces.BikeReportService;
import be.kdg.integration4.service.interfaces.CustomerService;
import be.kdg.integration4.service.interfaces.TechnicianService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/technician")
public class TechnicianController {
    private final List<BikeSize> sizes = Arrays.stream(BikeSize.values()).toList();
    private final List<TestType> testTypes = Arrays.stream(TestType.values()).toList();
    private final TechnicianService technicianService;
    private final CustomerService customerService;
    private final BikeReportService bikeReportService;

    public TechnicianController(TechnicianService technicianService, CustomerService customerService, BikeReportService bikeReportService) {
        this.technicianService = technicianService;
        this.customerService = customerService;
        this.bikeReportService = bikeReportService;
    }

    @GetMapping("/dashboard")
    @TechnicianOnly
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String loggedInEmail = authentication.getName();

        Technician technician = technicianService.findByEmail(loggedInEmail);

        int totalReports = technicianService.getTotalReportsByTechnician(technician.getId());

        List<Customer> customers = customerService.findAll();
        List<BikeReport> bikeReports = bikeReportService.findAll()
                .stream()
                .filter(report -> report.getTechnician().getId().equals(technician.getId()))
                .collect(Collectors.toList());

        model.addAttribute("totalReports", totalReports);
        model.addAttribute("technician", technician);
        model.addAttribute("customers", customers);
        model.addAttribute("bikeReports", bikeReports);
        return "technician";
    }

    @GetMapping("/start-test")
    @TechnicianOnly
    public String startTest(Model model) {
        model.addAttribute("bikeSizes", sizes);
        model.addAttribute("testTypes", testTypes);
        return "start-test";
    }

    @GetMapping("/test/success/{id}")
    @TechnicianOnly
    public String testSuccess(@PathVariable long id, Model model) {
        model.addAttribute("id",id);
        return "test-success";
    }

}
