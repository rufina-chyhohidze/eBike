package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.domain.enums.FunctionalTestComponents;
import be.kdg.integration4.domain.enums.Metric;
import be.kdg.integration4.domain.enums.VisualInspectionComponents;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.service.dtos.BatteryTestDTO;
import be.kdg.integration4.service.dtos.BearingHealthDTO;
import be.kdg.integration4.service.dtos.NominalLoadTestDTO;
import be.kdg.integration4.service.dtos.OverviewTestDTO;
import be.kdg.integration4.service.interfaces.BikeReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TestReportController {

    BikeReportService bikeReportService;

    public TestReportController(BikeReportService bikeReportService) {
        this.bikeReportService = bikeReportService;
    }

    @GetMapping("/report/{id}")
    public String showReport(@PathVariable Long id,
                             @RequestParam(value = "compareId", required = false) Long compareId,
                             Model model) {

        BikeReport bikeReport = bikeReportService.findByIdWithTestlinesAndBike(id);
        model.addAttribute("bikeReport", bikeReport);
        model.addAttribute("bike", bikeReport.getBike());
        model.addAttribute("id", id); // Needed for detailed report URL

        // Main report tests
        model.addAttribute("overviewTest", bikeReportService.calculateOverviewTest(id));
        model.addAttribute("nominalLoadTest", bikeReportService.calculateNominalLoadTest(id));
        model.addAttribute("batteryTest", bikeReportService.calculateBatteryTest(id));
        model.addAttribute("bearingHealth", bikeReportService.calculateBearingHealth(id));

        if (compareId != null) {
            model.addAttribute("compareReport", bikeReportService.findByIdWithTestlinesAndBike(compareId));
            model.addAttribute("overviewTestCo", bikeReportService.calculateOverviewTest(compareId));
            model.addAttribute("nominalLoadTestCo", bikeReportService.calculateNominalLoadTest(compareId));
            model.addAttribute("batteryTestCo", bikeReportService.calculateBatteryTest(compareId));
            model.addAttribute("bearingHealthCo", bikeReportService.calculateBearingHealth(compareId));
        }

        model.addAttribute("allReports", bikeReportService.getAll());

        return "report";
    }

    @GetMapping("/report/{id}/detailed")
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
