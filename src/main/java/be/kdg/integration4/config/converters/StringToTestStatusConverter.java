package be.kdg.integration4.config.converters;

import be.kdg.integration4.domain.TestStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToTestStatusConverter implements Converter<String, TestStatus> {
    @Override
    public TestStatus convert(String source) {
        for (TestStatus type : TestStatus.values()) {
            if (type.toString().equalsIgnoreCase(source)) {
                return type;
            }
        }
        return null;
    }
}
