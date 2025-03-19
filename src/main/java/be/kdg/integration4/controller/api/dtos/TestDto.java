package be.kdg.integration4.controller.api.dtos;

import be.kdg.integration4.domain.TestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {
    private int testBenchNumber;
    private TestType testType;
    private String emailBikeOwner;
    private String frameNumber;
}