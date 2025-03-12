package be.kdg.integration4.config;

import be.kdg.integration4.domain.TestStatus;
import be.kdg.integration4.domain.TestType;
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
