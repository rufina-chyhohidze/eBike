package be.kdg.integration4.report;

import be.kdg.integration4.domain.TestLine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TestReportController {

    @GetMapping("/report")
    public String showReport(@RequestParam List<TestLine> testLines, Model model) {
        Map<String, Double> averages = calculateAverages(testLines);
        model.addAttribute("averages", averages);
        model.addAttribute("testLines", testLines);
        return "report";
    }

    private Map<String, Double> calculateAverages(List<TestLine> testLines) {
        return Map.of(
                "Battery Voltage (V)", testLines.stream().collect(Collectors.averagingDouble(TestLine::getBatteryVoltage)),
                "Battery Current (A)", testLines.stream().collect(Collectors.averagingDouble(TestLine::getBatteryCurrent)),
                "Battery Temperature (°C)", testLines.stream().collect(Collectors.averagingDouble(TestLine::getBatteryTemperature)),
                "Engine Power (W)", testLines.stream().collect(Collectors.averagingDouble(TestLine::getEnginePower)),
                "Wheel Power (W)", testLines.stream().collect(Collectors.averagingDouble(TestLine::getWheelPower))
        );
    }
}
