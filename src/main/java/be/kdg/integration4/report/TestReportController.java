package be.kdg.integration4.report;

import be.kdg.integration4.domain.BikeReport;
import be.kdg.integration4.domain.TestLine;
import be.kdg.integration4.service.BikeReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TestReportController {

    BikeReportService bikeReportService;

    public TestReportController(BikeReportService bikeReportService) {
        this.bikeReportService = bikeReportService;
    }

    @GetMapping("/report/{id}")
    public String showReport(@PathVariable Long bikeReportId, Model model) {
        BikeReport bikeReport = bikeReportService.findById(bikeReportId);
        Map<String, Double> averages = bikeReportService.calculateAverages(bikeReport.getTestLines());
        model.addAttribute("averages", averages);
        model.addAttribute("testLines", bikeReport.getTestLines());
        return "report";
    }


}
