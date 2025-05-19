package be.kdg.integration4.controller.api;

import be.kdg.integration4.config.security.annotations.CustomerOnly;
import be.kdg.integration4.config.security.annotations.StaffOnly;
import be.kdg.integration4.config.security.annotations.TechnicianOnly;
import be.kdg.integration4.controller.api.dtos.BikeDto;
import be.kdg.integration4.controller.api.dtos.CustomerDto;
import be.kdg.integration4.controller.api.dtos.UpdateRequest;
import be.kdg.integration4.controller.api.dtos.UserOutputDto;
import be.kdg.integration4.controller.api.dtos.mappers.BikeDtoMapper;
import be.kdg.integration4.controller.api.dtos.mappers.CustomerDtoMapper;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.profile.UserDetailsImpl;
import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.exception.UserAlreadyExistsException;
import be.kdg.integration4.service.interfaces.BikeService;
import be.kdg.integration4.service.interfaces.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/customers")
public class CustomersController {
    private final CustomerService customerService;
    private final CustomerDtoMapper customMapper;
    private final BikeDtoMapper bikeMapper;
    private final BikeService bikeService;

    @Autowired
    public CustomersController(CustomerService customerService, BikeService bikeService, CustomerDtoMapper customMapper, BikeDtoMapper bikeMapper) {
        this.customerService = customerService;
        this.customMapper = customMapper;
        this.bikeService = bikeService;
        this.bikeMapper = bikeMapper;
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<List<String>> handleCustomerDoesntExist(NoSuchElementException e) {
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<List<String>> handleIdMismatch(AccessDeniedException e) {
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }

    @PatchMapping("/{id}")
    @CustomerOnly
    public ResponseEntity<UserOutputDto> updateProfile(
            @RequestBody UpdateRequest request,
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Customer customer = null;
        if (request.phoneNumber() != null && request.password() == null) {
            customer = customerService.updatePhoneNumber(userDetails.getUserId(), id, request.phoneNumber());
        }
        else if (request.phoneNumber() == null && request.password() != null) {
            customer = customerService.updatePassword(userDetails.getUserId(), id, request.password());
        }
        if (customer == null) {
            log.error("Customer with id {} not found", id);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new UserOutputDto(customer.getId(),customer.getName(), customer.getEmail()));
    }

    @GetMapping("/{email}")
    @StaffOnly
    public ResponseEntity<CustomerDto> getCustomerByEmail(@PathVariable String email) {
        return this.customerService.getByEmailIgnoreCase(email)
                .map(customer -> {
                    log.info("Found customer: {}", customer);
                    return ResponseEntity.ok(
                        customMapper.toCustomerDto(customer)
                    );
                }).orElseGet(() -> {
                    log.error("Customer with email {} not found", email);
                    return ResponseEntity.noContent().build();
                });
    }

    @GetMapping("{customerId}/bikes")
    @StaffOnly
    public ResponseEntity<List<BikeDto>> getCustomerBikes(@PathVariable Long customerId) {
        final List<Bike> customerBikes = this.bikeService.getAllByOwnerId(customerId);
        List<Bike> customerBikesList = new LinkedList<>(customerBikes);
        if (customerBikes.isEmpty()) {
            log.debug("No bikes found for customer with Id: {}", customerId);
            return ResponseEntity.noContent().build();
        }
        log.debug("Found customer bikes: {}", customerBikes);
        Collections.sort(customerBikesList);
        return ResponseEntity.ok(bikeMapper.toBikeDtoList(customerBikesList.stream().toList()));
    }

    @GetMapping
    @StaffOnly
    public ResponseEntity<List<CustomerDto>> filterCustomers(@RequestParam(required = false) String name) {

        if (name != null && !name.isEmpty()) {
            List<Customer> customers = customerService.getByNameIgnoreCase(name);
            if (customers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(customers.stream().map(customMapper::toCustomerDto).toList());
        }
        List<Customer> customers = customerService.getAll();
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers.stream().map(customMapper::toCustomerDto).toList());
    }
}
