package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.domain.TestType;
import be.kdg.integration4.domain.BikeSize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/technician")
public class TechnicianController {
    List<BikeSize> sizes = Arrays.stream(BikeSize.values()).toList();
    List<TestType> testTypes = Arrays.stream(TestType.values()).toList();

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "technician";
    }

    @GetMapping("/start-test")
    public String startTest(Model model) {
        model.addAttribute("bikeSizes", sizes);
        model.addAttribute("testTypes", testTypes);
        return "start-test";
    }

    @GetMapping("/test/success/{id}")
    public String testSuccess(@PathVariable long id, Model model) {
        model.addAttribute("id",id);
        return "test-success";
    }
}
