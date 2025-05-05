package be.kdg.integration4.controller.api.dtos;

import be.kdg.integration4.domain.enums.FunctionalTestComponents;
import be.kdg.integration4.domain.enums.InspectionCondition;
import be.kdg.integration4.domain.enums.TestType;
import be.kdg.integration4.domain.enums.VisualInspectionComponents;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {

    @NotNull(message = "Test bench number is required")
    @Min(value = 1, message = "Test bench number must be greater than 0")
    private int testBenchNumber;

    @NotNull(message = "Test type is required")
    private TestType testType;

    @NotBlank(message = "Bike owner email is required")
    @Email(message = "Invalid email format")
    private String emailBikeOwner;

    @NotBlank(message = "Frame number is required")
    @Size(min = 5, max = 20, message = "Frame number must be between 5 and 20 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Frame number must contain only alphanumeric characters")
    private String frameNumber;

    //@NotNull(message = "Visual inspection is required")
    private Map<VisualInspectionComponents, InspectionCondition> visualInspection;

    //@NotNull(message = "Visual inspection is required")
    private Map<FunctionalTestComponents, InspectionCondition> functionalTest;


}
