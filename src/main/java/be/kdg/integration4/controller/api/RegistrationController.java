package be.kdg.integration4.controller.api;

import be.kdg.integration4.controller.api.dtos.RegistrationDto;
import be.kdg.integration4.controller.api.dtos.UserOutputDto;
import be.kdg.integration4.controller.api.dtos.CustomerRegistrationDto;
import be.kdg.integration4.controller.api.dtos.StaffRegistrationDto;
import be.kdg.integration4.domain.Customer;
import be.kdg.integration4.domain.User;
import be.kdg.integration4.exception.UserAlreadyExistsException;
import be.kdg.integration4.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Slf4j
@RestController
@RequestMapping("/api")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody @Valid RegistrationDto registrationDto
    ) {
        System.err.println("Parameters received: " + registrationDto);
        User userCreated;
        if (registrationDto.role().equalsIgnoreCase("CUSTOMER")) {
            userCreated = this.registrationService.createCustomer(registrationDto.name(), registrationDto.email(), registrationDto.phoneNumber(), registrationDto.phoneNumber());
        } else if (registrationDto.role().equalsIgnoreCase("TECHNICIAN") || registrationDto.role().equalsIgnoreCase("WORKSHOP_ADMIN")) {
            userCreated = this.registrationService.createStaff(registrationDto.name(), registrationDto.email(), registrationDto.password(), registrationDto.role(), registrationDto.workshopId());
        } else return null;

        System.err.println("User created: " + userCreated);


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationError(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(error.getField() + ": " + error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({UserAlreadyExistsException.class, IllegalArgumentException.class,NoSuchElementException.class})
    public ResponseEntity<List<String>> handleUserAlreadyExists(RuntimeException e) {
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @PostMapping("customers")
    public ResponseEntity<UserOutputDto> registerCustomer(
            @Valid @RequestBody CustomerRegistrationDto customerRegistrationDto
    ) {
        Customer customer = registrationService.createCustomer(customerRegistrationDto.name(),
                customerRegistrationDto.email(),
                customerRegistrationDto.password(),
                customerRegistrationDto.phoneNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserOutputDto(customer.getId(), customer.getName(), customer.getEmail()));
    }

    @PostMapping("staff")
    public ResponseEntity<UserOutputDto> registerStaffMember(
            @Valid @RequestBody StaffRegistrationDto staffRegistrationDto
    ) {
        User user = registrationService.createStaff(staffRegistrationDto.name(),
                staffRegistrationDto.email(),
                staffRegistrationDto.password(),
                staffRegistrationDto.userRole(),
                staffRegistrationDto.workshopId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserOutputDto(user.getId(), user.getName(), user.getEmail()));
    }
}
