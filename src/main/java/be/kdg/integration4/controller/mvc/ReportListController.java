package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.service.interfaces.BikeReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ReportListController {

    private final BikeReportService bikeReportService;

    public ReportListController(BikeReportService bikeReportService) {
        this.bikeReportService = bikeReportService;
    }

    @GetMapping("/report-list/{frameNumber}")
    public String viewReportsByFrame(@PathVariable String frameNumber, Model model) {
        List<BikeReport> reports = bikeReportService.getReportsByBikeFrameNumberWithDetails(frameNumber);
        model.addAttribute("frameNumber", frameNumber);
        model.addAttribute("reports", reports);

        return "report-list"; // Thymeleaf template name: report-list.html
    }
}
