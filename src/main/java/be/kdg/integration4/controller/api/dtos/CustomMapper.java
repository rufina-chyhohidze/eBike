package be.kdg.integration4.controller.api.dtos;

import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.report.Workshop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomMapper {
    CustomerDto toCustomerDto(Customer customer);

    @Mapping(source = "bikeOwner.id", target = "bikeOwnerId")
    BikeDto toBikeDto(Bike bike);
    List<BikeDto> toBikeDtoList(List<Bike> bikeList);

    WorkShopDto toWorkShopDto(Workshop workshop);
    List<WorkShopDto> toWorkShopDtoList(List<Workshop> workshops);
}