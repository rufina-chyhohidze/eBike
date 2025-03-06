package be.kdg.integration4.controller.mvc;

import be.kdg.integration4.controller.mvc.dtos.RegistrationDto;
import be.kdg.integration4.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Slf4j
@RequestMapping("/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public String register(
            @ModelAttribute @Valid RegistrationDto registrationDto,
            BindingResult bindingResult,
            Model model
    ) throws Exception {
        if (bindingResult.hasErrors()) {
//         TODO: IMPLEMENT
            System.err.println("error");
            log.error("error");
            throw new Exception("Error registering");
        }

        log.info("GOT DETAILS: {}", registrationDto);
        registrationService.createUser(registrationDto.name(), registrationDto.email(), registrationDto.password(), registrationDto.userRole());

        return "login";
    }
}
