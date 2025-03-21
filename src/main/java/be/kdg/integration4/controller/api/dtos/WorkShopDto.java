package be.kdg.integration4.controller.api.dtos;

import be.kdg.integration4.domain.enums.Location;

public record WorkShopDto(
    Long workshopId,
    String workshopName,
    Location workshopLocation
) {
}
