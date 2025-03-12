package be.kdg.integration4.config;

import be.kdg.integration4.domain.TestStatus;
import be.kdg.integration4.domain.TestType;
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
