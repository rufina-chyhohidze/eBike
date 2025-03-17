package be.kdg.integration4.controller.api.dtos;

import be.kdg.integration4.domain.Location;

public record WorkShopDto(
    Long workshopId,
    String workshopName,
    Location workshopLocation
) {
}
