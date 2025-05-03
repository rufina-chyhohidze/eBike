package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.domain.enums.FunctionalTestComponents;
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

@Controller
public class TestReportController {

    BikeReportService bikeReportService;

    public TestReportController(BikeReportService bikeReportService) {
        this.bikeReportService = bikeReportService;
    }

    @GetMapping("/report/{id}")
    public String showReport(@PathVariable Long id, Model model) {
        BikeReport bikeReport = bikeReportService.findByIdWithTestlinesAndBike(id);
        model.addAttribute("bikeReport", bikeReport);
        model.addAttribute("bike", bikeReport.getBike());
        OverviewTestDTO overviewTest = bikeReportService.calculateOverviewTest(id);
        model.addAttribute("overviewTest", overviewTest);
        NominalLoadTestDTO nominalLoadTest = bikeReportService.calculateNominalLoadTest(id);
        model.addAttribute("nominalLoadTest", nominalLoadTest);
        BatteryTestDTO batteryTest = bikeReportService.calculateBatteryTest(id);
        model.addAttribute("batteryTest", batteryTest);
        BearingHealthDTO healthBearing = bikeReportService.calculateBearingHealth(id);
        model.addAttribute("bearingHealth", healthBearing);
        return "report";
    }

    @GetMapping("/report/{id}/detailed")
    public String showDetailedReport(@PathVariable Long id, Model model) {
        BikeReport bikeReport = bikeReportService.getByIdWithTestlines(id);
        model.addAttribute("report", bikeReport);
        model.addAttribute("bike", bikeReport.getBike());
        model.addAttribute("metrics", Metric.values());

        List<BikeReport> allReports = bikeReportService.getAll();
        model.addAttribute("allReports", allReports);

        return "detailed-report";
    }



}
