package be.kdg.integration4.controller.api.dtos;

public record UpdateSettingsDto(
        String settingName,
        Double settingValue
) {

}
