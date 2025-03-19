package be.kdg.integration4.controller.api;

import be.kdg.integration4.controller.api.dtos.BikeDto;
import be.kdg.integration4.controller.api.dtos.CustomMapper;
import be.kdg.integration4.controller.api.dtos.CustomerDto;
import be.kdg.integration4.domain.Bike;
import be.kdg.integration4.service.interfaces.BikeReportService;
import be.kdg.integration4.service.interfaces.BikeService;
import be.kdg.integration4.service.interfaces.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/customers")
public class CustomersController {
    private final CustomerService customerService;
    private final CustomMapper customMapper;
    private final BikeReportService bikeReportService;
    private final BikeService bikeService;

    @Autowired
    public CustomersController(CustomerService customerService, BikeService bikeService, BikeReportService bikeReportService, CustomMapper customMapper) {
        this.customerService = customerService;
        this.customMapper = customMapper;
        this.bikeReportService = bikeReportService;
        this.bikeService = bikeService;
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
                    return ResponseEntity.noContent().build();
                });
    }

    @GetMapping("{customerId}/bikes")
    public ResponseEntity<List<BikeDto>> getCustomerBikes(@PathVariable Long customerId) {
        final Set<Bike> customerBikes = this.bikeService.getBikesByOwnerId(customerId);
        List<Bike> customerBikesList = new LinkedList<>(customerBikes);
        if (customerBikes.isEmpty()) {
            log.debug("Found customer bikes: {}", customerBikes);
            return ResponseEntity.noContent().build();
        }
        Collections.sort(customerBikesList);
        log.debug("No bikes found or customer with Id: {}", customerId);
        return ResponseEntity.ok(customMapper.toBikeDtoList(customerBikesList.stream().toList()));
    }
}
