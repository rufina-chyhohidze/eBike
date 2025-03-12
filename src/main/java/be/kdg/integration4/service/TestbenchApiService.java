package be.kdg.integration4.service;

import be.kdg.integration4.domain.TestStatus;
import be.kdg.integration4.domain.TestType;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface TestbenchApiService {
    TestDto sendStartRequest(TestType testType, int batteryCapacity, int maxSupport, int enginePowerMax,
                                                int enginePowerNominal, int engineTorque);
    TestStatus checkTestStatus(String id);
    String sendReportRequest(String id);

//    MultipartFile sendTestRequest(TestType testType, int batteryCapacity, int maxSupport, int enginePowerMax,
//                                  int enginePowerNominal, int engineTorque);
}
