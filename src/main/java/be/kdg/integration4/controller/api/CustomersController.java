package be.kdg.integration4.controller.api;

import be.kdg.integration4.controller.api.dtos.BikeDto;
import be.kdg.integration4.controller.api.dtos.CustomMapper;
import be.kdg.integration4.controller.api.dtos.CustomerDto;
import be.kdg.integration4.domain.Customer;
import be.kdg.integration4.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/customers")
public class CustomersController {
    private final CustomerService customerService;
    private final CustomMapper customMapper;

    @Autowired
    public CustomersController(CustomerService customerService, CustomMapper customMapper) {
        this.customerService = customerService;
        this.customMapper = customMapper;
    }

    @GetMapping
    public ResponseEntity<CustomerDto> getCustomerByEmail(@RequestParam String email) {
        return this.customerService.findByEmailIgnoreCase(email)
                .map(customer -> {
                    log.info("Found customer: {}", customer);
                    return ResponseEntity.ok(
                        customMapper.toCustomerDto(customer)
                    );
                }).orElseGet(() -> {
                    log.error("Customer with email {} not found", email);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/bikes")
    public ResponseEntity<List<BikeDto>> getCustomerBikes(@RequestParam String customerEmail) {

        return null;
    }
}
