package be.kdg.integration4.service;

import be.kdg.integration4.domain.ApiRequest;
import be.kdg.integration4.domain.BikeReport;
import be.kdg.integration4.domain.TestStatus;
import be.kdg.integration4.domain.TestType;
import be.kdg.integration4.service.dtos.TestDto;

public interface TestbenchApiService {
    TestDto sendStartRequest(TestType testType, int batteryCapacity, int maxSupport, int enginePowerMax,
                             int enginePowerNominal, int engineTorque);
    TestStatus checkTestStatus(String id);
    String sendReportRequest(String id);

    ApiRequest saveApiRequest(BikeReport bikeReport, String requestId);

    void deleteApiRequest(String requestId);

    ApiRequest getApiRequest(String requestId);

//    MultipartFile sendTestRequest(TestType testType, int batteryCapacity, int maxSupport, int enginePowerMax,
//                                  int enginePowerNominal, int engineTorque);
}
