package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.config.security.annotations.StaffOnly;
import be.kdg.integration4.config.security.annotations.TechnicianOnly;
import be.kdg.integration4.domain.enums.*;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.report.ReportSetting;
import be.kdg.integration4.service.implementations.ReportSettingServiceImpl;
import be.kdg.integration4.service.implementations.TechnicianServiceImpl;
import be.kdg.integration4.service.interfaces.BikeReportService;
import be.kdg.integration4.service.interfaces.CustomerService;
import be.kdg.integration4.service.interfaces.ReportSettingService;
import be.kdg.integration4.service.interfaces.TechnicianService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/technician")
public class TechnicianController {
    List<BikeSize> sizes = Arrays.stream(BikeSize.values()).toList();
    List<TestType> testTypes = Arrays.stream(TestType.values()).toList();
    List<InspectionCondition> conditions = Arrays.stream(InspectionCondition.values()).toList();

    private final TechnicianService technicianService;
    private final CustomerService customerService;
    private final BikeReportService bikeReportService;
    private final ReportSettingService reportSettingService;

    public TechnicianController(TechnicianService technicianService, CustomerService customerService, BikeReportService bikeReportService, ReportSettingService reportSettingService) {
        this.technicianService = technicianService;
        this.customerService = customerService;
        this.bikeReportService = bikeReportService;
        this.reportSettingService = reportSettingService;
    }

    @GetMapping("/dashboard")
    @TechnicianOnly
    public String dashboard(@RequestParam(required = false) String filter, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();

        Technician technician = technicianService.getByEmail(loggedInEmail);
        int totalReports = technicianService.getTotalReportsByTechnician(technician.getId());

        List<Customer> customers = customerService.getAll();
        if (filter != null && !filter.isBlank()) {
            customers = customers.stream()
                    .filter(c -> String.valueOf(c.getId()).contains(filter) ||
                            c.getName().toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
        }

        List<BikeReport> bikeReports = bikeReportService.getAll()
                .stream()
                .filter(report -> report.getTechnician().getId().equals(technician.getId()))
                .collect(Collectors.toList());

        model.addAttribute("totalReports", totalReports);
        model.addAttribute("totalClients", customers.size());
        model.addAttribute("technician", technician);
        model.addAttribute("customers", customers);
        model.addAttribute("bikeReports", bikeReports);
        model.addAttribute("filter", filter);
        return "technician";
    }

    @GetMapping("/start-test")
    @TechnicianOnly
    public String startTest(Model model) {
        model.addAttribute("bikeSizes", sizes);
        model.addAttribute("testTypes", testTypes);
        model.addAttribute("conditions", conditions);
        model.addAttribute("visualComponents", VisualInspectionComponents.values());
        model.addAttribute("functionalComponents", FunctionalTestComponents.values());
        return "start-test";
    }

    @GetMapping("/test/success/{id}")
    @TechnicianOnly
    public String testSuccess(@PathVariable long id, Model model) {
        model.addAttribute("id",id);
        return "test-success";
    }

    @GetMapping("/report-settings")
    public String getReportSettingsPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String loggedInEmail = authentication.getName();

        Technician technician = technicianService.getByEmail(loggedInEmail);

        ReportSetting reportSetting = reportSettingService.getReportSettingsForTechnician(technician);

        model.addAttribute("reportSetting", reportSetting);

        return "report-settings"; // This is the Thymeleaf template for the settings page
    }

    @GetMapping("/register-customer")
    @TechnicianOnly
    public String testRegisterCustomer(Model model) {
        return "register-customer";
    }
}
