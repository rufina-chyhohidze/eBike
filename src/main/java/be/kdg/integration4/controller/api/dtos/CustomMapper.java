package be.kdg.integration4.controller.api.dtos;

import be.kdg.integration4.domain.Customer;
import be.kdg.integration4.domain.Workshop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    CustomerDto toCustomerDto(Customer customer);
    WorkShopDto toWorkShopDto(Workshop workshop);
    List<WorkShopDto> toWorkShopDtoList(List<Workshop> workshops);
}