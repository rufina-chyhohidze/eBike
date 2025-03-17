package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.domain.TestType;
import be.kdg.integration4.domain.BikeSize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class TechnicianController {
    List<BikeSize> sizes = Arrays.stream(BikeSize.values()).toList();
    List<TestType> testTypes = Arrays.stream(TestType.values()).toList();

    @GetMapping("/start-test")
    public String startTest(Model model) {
        model.addAttribute("bikeSizes", sizes);
        model.addAttribute("testTypes", testTypes);
        return "start-test";
    }

    @GetMapping("/test/success")
    public String testSuccess(Model model) {
        return "test-success";
    }
}
