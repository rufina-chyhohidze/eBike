package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.config.security.annotations.StaffOnly;
import be.kdg.integration4.config.security.annotations.TechnicianOnly;
import be.kdg.integration4.domain.enums.Metric;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.service.interfaces.BikeReportService;
import be.kdg.integration4.service.interfaces.TechnicianService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TestReportController {

    private final BikeReportService bikeReportService;
    private final TechnicianService technicianService;

    public TestReportController(BikeReportService bikeReportService, TechnicianService technicianService) {
        this.bikeReportService = bikeReportService;
        this.technicianService = technicianService;
    }

    @GetMapping("/report-comparison/{id}")
    public String showReportComparison(@PathVariable Long id,
                             @RequestParam(value = "compareId", required = false) Long compareId,
                             Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String loggedInEmail = authentication.getName();

        Technician technician = technicianService.getByEmail(loggedInEmail);


        BikeReport bikeReport = bikeReportService.findByIdWithTestlinesAndBike(id);
        model.addAttribute("bikeReport", bikeReport);
        model.addAttribute("bike", bikeReport.getBike());
        model.addAttribute("id", id); // Needed for detailed report URL

        // Main report tests
        model.addAttribute("overviewTest", bikeReportService.calculateOverviewTest(id));
        model.addAttribute("nominalLoadTest", bikeReportService.calculateNominalLoadTest(id));
        model.addAttribute("batteryTest", bikeReportService.calculateBatteryTest(id));
        model.addAttribute("bearingHealth", bikeReportService.calculateBearingHealth(id, technician));

        if (compareId != null) {
            model.addAttribute("compareReport", bikeReportService.findByIdWithTestlinesAndBike(compareId));
            model.addAttribute("overviewTestCo", bikeReportService.calculateOverviewTest(compareId));
            model.addAttribute("nominalLoadTestCo", bikeReportService.calculateNominalLoadTest(compareId));
            model.addAttribute("batteryTestCo", bikeReportService.calculateBatteryTest(compareId));
            model.addAttribute("bearingHealthCo", bikeReportService.calculateBearingHealth(compareId, technician));
        }

        model.addAttribute("allReports", bikeReportService.getAll());

        return "report-comparison";
    }

    @GetMapping("/report/{id}")
    public String showReport(@PathVariable Long id,
                             Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String loggedInEmail = authentication.getName();

        Technician technician = technicianService.getByEmail(loggedInEmail);


        BikeReport bikeReport = bikeReportService.findByIdWithTestlinesAndBike(id);
        model.addAttribute("bikeReport", bikeReport);
        model.addAttribute("bike", bikeReport.getBike());
        model.addAttribute("id", id);

        // Main report tests
        model.addAttribute("overviewTest", bikeReportService.calculateOverviewTest(id));
        model.addAttribute("nominalLoadTest", bikeReportService.calculateNominalLoadTest(id));
        model.addAttribute("batteryTest", bikeReportService.calculateBatteryTest(id));
        model.addAttribute("bearingHealth", bikeReportService.calculateBearingHealth(id, technician));

        return "report";
    }


    @GetMapping("/report/{id}/detailed")
    @StaffOnly
    public String showDetailedReport(@PathVariable Long id,
                                     Model model) {
        BikeReport bikeReport = bikeReportService.findByIdWithTestlinesAndBike(id);
        model.addAttribute("report", bikeReport);
        model.addAttribute("bike", bikeReport.getBike());
        model.addAttribute("metrics", Metric.values());

        List<BikeReport> allReports = bikeReportService.getAll();
        model.addAttribute("allReports", allReports);

        return "detailed-report";
    }



}
