package be.kdg.integration4.controller.api.dtos;

import be.kdg.integration4.controller.api.dtos.WorkShopDto;
import be.kdg.integration4.domain.Workshop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkshopMapper {
    @Mappings({
            @Mapping(source = "workshopLocation", target = "location")
    })
    WorkShopDto toWorkShopDto(Workshop workshop);

    List<WorkShopDto> toWorkShopDtoList(List<Workshop> workshops);
}
