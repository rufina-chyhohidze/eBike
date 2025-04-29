package be.kdg.integration4.controller.api;

import be.kdg.integration4.config.security.annotations.StaffOnly;
import be.kdg.integration4.controller.api.dtos.BikeDto;
import be.kdg.integration4.controller.api.dtos.CustomerDto;
import be.kdg.integration4.controller.api.dtos.mappers.BikeDtoMapper;
import be.kdg.integration4.controller.api.dtos.mappers.CustomerDtoMapper;
import be.kdg.integration4.domain.report.Bike;
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
    private final CustomerDtoMapper customMapper;
    private final BikeDtoMapper bikeMapper;
    private final BikeService bikeService;
    private final CustomerDtoMapper customerDtoMapper;

    @Autowired
    public CustomersController(CustomerService customerService, BikeService bikeService, CustomerDtoMapper customMapper, BikeDtoMapper bikeMapper) {
        this.customerService = customerService;
        this.customMapper = customMapper;
        this.bikeService = bikeService;
        this.bikeMapper = bikeMapper;
    }

    @GetMapping
    @StaffOnly
    public ResponseEntity<CustomerDto> getCustomerByEmail(@RequestParam String email) {
        return this.customerService.getByEmailIgnoreCase(email)
                .map(customer -> {
                    log.info("Found customer: {}", customer);
                    return ResponseEntity.ok(
                        customerDtoMapper.toCustomerDto(customer)
                    );
                }).orElseGet(() -> {
                    log.error("Customer with email {} not found", email);
                    return ResponseEntity.noContent().build();
                });
    }

    @GetMapping("{customerId}/bikes")
    @StaffOnly
    public ResponseEntity<List<BikeDto>> getCustomerBikes(@PathVariable Long customerId) {
        final Set<Bike> customerBikes = this.bikeService.getAllByOwnerId(customerId);
        List<Bike> customerBikesList = new LinkedList<>(customerBikes);
        if (customerBikes.isEmpty()) {
            log.debug("Found customer bikes: {}", customerBikes);
            return ResponseEntity.noContent().build();
        }
        Collections.sort(customerBikesList);
        log.debug("No bikes found for customer with Id: {}", customerId);
        return ResponseEntity.ok(bikeMapper.toBikeDtoList(customerBikesList.stream().toList()));
    }
}
