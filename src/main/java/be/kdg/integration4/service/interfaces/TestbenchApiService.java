package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.report.ApiRequest;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.enums.TestStatus;
import be.kdg.integration4.domain.enums.TestType;
import be.kdg.integration4.domain.report.TestLine;
import be.kdg.integration4.service.dtos.TestDto;

import java.util.List;

public interface TestbenchApiService {
    TestDto startTest(TestType testType, int batteryCapacity, int maxSupport, int enginePowerMax,
                      int enginePowerNominal, int engineTorque);
    TestStatus checkTestStatus(String id);

    ApiRequest saveApiRequest(BikeReport bikeReport, String requestId);

    void deleteApiRequest(String requestId);

    ApiRequest getApiRequest(String requestId);

    String fetchCsv(String id);

    Long fetchReportId(String testId, List<TestLine> testLines);

}
