package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.domain.profile.UserDetailsImpl;
import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.repository.BikeRepository;
import be.kdg.integration4.service.interfaces.BikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
public class BikeManagementController {
    BikeService bikeService;

    public BikeManagementController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @GetMapping("/customer/bike-management")
    public String getBikeManagement(@AuthenticationPrincipal UserDetailsImpl customer, Model model) {
        System.out.println("HELLO");
        List<Bike> bikes = bikeService.getAllByOwnerId(customer.getUserId());
        System.out.println(bikes);
        model.addAttribute("bikes", bikes != null ? bikes : new ArrayList<>());
        return "bike-management";
    }


}
