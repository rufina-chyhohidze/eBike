package be.kdg.integration4.controller.api;

import be.kdg.integration4.controller.api.dtos.BikeDto;
import be.kdg.integration4.controller.api.dtos.CustomMapper;
import be.kdg.integration4.controller.api.dtos.CustomerDto;
import be.kdg.integration4.domain.Bike;
import be.kdg.integration4.domain.Customer;
import be.kdg.integration4.service.BikeReportService;
import be.kdg.integration4.service.BikeService;
import be.kdg.integration4.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        List<String> frameNumbers = this.bikeReportService.getFrameNumbersByCustomerId(customerId);
        log.debug("Frame numbers of bikes of customer with Id {}:  {}", customerId, frameNumbers);
        Set<Bike> customerBikes = frameNumbers.stream()
                .map(this.bikeService::findByFrameNumber).collect(Collectors.toSet());
        log.debug("Found customer bikes: {}", customerBikes);


        if (customerBikes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(customMapper.toBikeDtoList(customerBikes.stream().toList()));
    }
}
