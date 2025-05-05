package be.kdg.integration4.controller.api;

import be.kdg.integration4.config.security.annotations.StaffOnly;
import be.kdg.integration4.controller.api.dtos.UserOutputDto;
import be.kdg.integration4.controller.api.dtos.CustomerRegistrationDto;
import be.kdg.integration4.controller.api.dtos.StaffRegistrationDto;
import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.exception.UserAlreadyExistsException;
import be.kdg.integration4.service.dtos.CustomerAndPasswordServiceDto;
import be.kdg.integration4.service.email.EmailService;
import be.kdg.integration4.service.interfaces.RegistrationService;
import io.micrometer.common.lang.Nullable;
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
    private final EmailService emailService;

    public RegistrationController(RegistrationService registrationService, EmailService emailService) {
        this.registrationService = registrationService;
        this.emailService = emailService;
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

    @PostMapping("/customers")
    @StaffOnly // Because now customer can be registered only by technician
    //TODO is there a way to return ResponseEntity<UserOutputDto> and is it a bad practice to use ? sign.
    public ResponseEntity<?> registerCustomer(
            @Valid @RequestBody CustomerRegistrationDto customerRegistrationDto,
            BindingResult bindingResult
    ) {
        ResponseEntity<?> errorFields = getErrorFields(bindingResult);
        if (errorFields != null) return errorFields;

        log.info("Parameters received - Customer: {}", customerRegistrationDto);

        CustomerAndPasswordServiceDto customerAndPasswordServiceDto = registrationService.createCustomer(customerRegistrationDto.name(),
                customerRegistrationDto.email(),
                customerRegistrationDto.phoneNumber());

        emailService.sendCustomerRegistrationConfirmationEmail(customerAndPasswordServiceDto.customer().getEmail(), customerAndPasswordServiceDto.customer().getName(), customerAndPasswordServiceDto.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserOutputDto(customerAndPasswordServiceDto.customer().getId(), customerAndPasswordServiceDto.customer().getName(),
                customerAndPasswordServiceDto.customer().getEmail()));
    }

    @PostMapping("/staff")
    public ResponseEntity<?> registerStaffMember(
            @Valid @RequestBody StaffRegistrationDto staffRegistrationDto,
            BindingResult bindingResult
    ) {
        ResponseEntity<?> errorFields = getErrorFields(bindingResult);
        if (errorFields != null) return errorFields;

        log.info("Parameters received - Staff: {}", staffRegistrationDto);
        User user = registrationService.createStaff(staffRegistrationDto.name(),
                staffRegistrationDto.email(),
                staffRegistrationDto.password(),
                staffRegistrationDto.userRole(),
                staffRegistrationDto.workshopId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserOutputDto(user.getId(), user.getName(), user.getEmail()));
    }

    @Nullable
    private ResponseEntity<?> getErrorFields(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorFields = new ArrayList<>();
            bindingResult.getFieldErrors().forEach(error -> {
                log.info("Field: " + error.getField() + " - Error: " + error.getDefaultMessage());
                errorFields.add(error.getField());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorFields);
        }
        return null;
    }
}
