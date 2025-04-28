package be.kdg.integration4.controller.api.dtos.mappers;

import be.kdg.integration4.controller.api.dtos.CustomerDto;
import be.kdg.integration4.domain.profile.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDtoMapper {
    public CustomerDto toCustomerDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerDto(
                Math.toIntExact(customer.getId()),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber()
        );
    }
}
