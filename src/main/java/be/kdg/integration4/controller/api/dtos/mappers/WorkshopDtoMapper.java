package be.kdg.integration4.controller.api.dtos.mappers;

import be.kdg.integration4.controller.api.dtos.WorkShopDto;
import be.kdg.integration4.domain.report.Workshop;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WorkshopDtoMapper {
    public WorkShopDto toWorkShopDto(Workshop workshop) {
        if (workshop == null) {
            return null;
        }
        return new WorkShopDto(
                workshop.getWorkshopId(),
                workshop.getWorkshopName(),
                workshop.getWorkshopLocation()
        );
    }

    public List<WorkShopDto> toWorkShopDtoList(List<Workshop> workshops) {
        if (workshops == null) {
            return null;
        }
        return workshops.stream()
                .map(this::toWorkShopDto)
                .collect(Collectors.toList());
    }
}
