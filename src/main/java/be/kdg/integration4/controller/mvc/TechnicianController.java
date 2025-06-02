package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.config.security.annotations.TechnicianOnly;
import be.kdg.integration4.domain.enums.*;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.profile.UserDetailsImpl;
import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.report.ReportSetting;
import be.kdg.integration4.service.interfaces.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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
    private final BikeService bikeService;

    public TechnicianController(TechnicianService technicianService, CustomerService customerService, BikeReportService bikeReportService, ReportSettingService reportSettingService, BikeService bikeService) {
        this.technicianService = technicianService;
        this.customerService = customerService;
        this.bikeReportService = bikeReportService;
        this.reportSettingService = reportSettingService;
        this.bikeService = bikeService;
    }

    @GetMapping("/dashboard")
    @TechnicianOnly
    public String dashboard(@RequestParam(required = false) String filter, @RequestParam(required = false) String frameNumber,
                            @RequestParam(required = false) String engineType,Model model) {
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

        List<BikeReport> bikeReports = bikeReportService
                .getReportsWithDetailsForTechnician(technician.getId())
                .stream()
                .filter(report -> report.getTechnician().getId().equals(technician.getId()))
                .filter(report -> frameNumber == null || frameNumber.isBlank() ||
                        report.getBike().getFrameNumber().toLowerCase().contains(frameNumber.toLowerCase()))
                .filter(report -> engineType == null || engineType.isBlank() ||
                        report.getBike().getBikeModel().getEngineType().toLowerCase().contains(engineType.toLowerCase()))
                .toList();

        model.addAttribute("totalReports", totalReports);
        model.addAttribute("totalClients", customers.size());
        model.addAttribute("technician", technician);
        model.addAttribute("customers", customers);
        model.addAttribute("bikeReports", bikeReports);
        model.addAttribute("frameNumber", frameNumber);
        model.addAttribute("engineType", engineType);
        model.addAttribute("filter", filter);
        return "technician-dashboard";
    }

    @GetMapping("/start-test")
    @TechnicianOnly
    public String startTest(Model model) {
        model.addAttribute("bikeSizes", sizes);
        model.addAttribute("testTypes", testTypes);
        model.addAttribute("conditions", conditions);
        model.addAttribute("visualComponents", VisualInspectionComponents.values());
        model.addAttribute("functionalComponents", FunctionalTestComponents.values());
        return "technician-start-test";
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

        return "technician-report-settings";
    }

    @GetMapping("/register-customer")
    @TechnicianOnly
    public String testRegisterCustomer(Model model) {
        return "technician-register-customer";
    }

    @GetMapping("/bikes")
    @TechnicianOnly
    public String viewTechnicianBikes(@RequestParam(required = false) String search, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Technician technician = technicianService.getByEmail(email);

        model.addAttribute("technician", technician);
        return "technician-search-bikes";
    }

    @GetMapping("/bike-management/{id}")
    public String getBikeManagement(@PathVariable long id, Model model) {
        List<Bike> bikes = bikeService.getAllByOwnerId(id);
        model.addAttribute("bikes", bikes != null ? bikes : new ArrayList<>());
        return "bike-management";
    }

}
