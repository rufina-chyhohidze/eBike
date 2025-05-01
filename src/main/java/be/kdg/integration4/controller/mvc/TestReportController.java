package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.enums.Metric;
import be.kdg.integration4.service.interfaces.BikeReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class TestReportController {

    BikeReportService bikeReportService;

    public TestReportController(BikeReportService bikeReportService) {
        this.bikeReportService = bikeReportService;
    }

    @GetMapping("/report/{id}")
    public String showReport(@PathVariable Long id, Model model) {
        BikeReport bikeReport = bikeReportService.getByIdWithTestlines(id);
        Map<String, Double> averages = bikeReportService.calculateAverages(bikeReport.getTestLines());
        model.addAttribute("averages", averages);
        model.addAttribute("testLines", bikeReport.getTestLines());
        model.addAttribute("metrics", Metric.values());
        model.addAttribute("bike", bikeReport.getBike());
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
