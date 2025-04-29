package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.enums.Metric;
import be.kdg.integration4.service.interfaces.BikeReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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




}
