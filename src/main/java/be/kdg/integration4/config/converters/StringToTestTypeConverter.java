package be.kdg.integration4.config.converters;

import be.kdg.integration4.domain.enums.TestType;
import org.springframework.core.convert.converter.Converter;

public class StringToTestTypeConverter implements Converter<String, TestType> {
    @Override
    public TestType convert(String source) {
        for (TestType type : TestType.values()) {
            if (type.toString().equalsIgnoreCase(source)) {
                return type;
            }
        }
        return null;
    }
}
