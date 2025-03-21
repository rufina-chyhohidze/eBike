package be.kdg.integration4.controller.api.dtos;

import be.kdg.integration4.domain.report.TestLine;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TestLineMapper {
    TestLineDto toTestLineDto(TestLine testLine);

    List<TestLineDto> toTestLineDtoList(List<TestLine> testLines);

}
