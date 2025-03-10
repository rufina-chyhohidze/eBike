package be.kdg.integration4.controller.api.dtos;

import be.kdg.integration4.domain.Workshop;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomMapper {
    WorkShopDto toWorkShopDto(Workshop workshop);
    List<WorkShopDto> toWorkShopDtoList(List<Workshop> workshops);
}
