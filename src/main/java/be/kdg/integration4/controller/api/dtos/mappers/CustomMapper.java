package be.kdg.integration4.controller.api.dtos.mappers;

import be.kdg.integration4.controller.api.dtos.CustomerDto;
import be.kdg.integration4.controller.api.dtos.WorkShopDto;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.report.Workshop;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomMapper {
    CustomerDto toCustomerDto(Customer customer);

    WorkShopDto toWorkShopDto(Workshop workshop);
    List<WorkShopDto> toWorkShopDtoList(List<Workshop> workshops);
}