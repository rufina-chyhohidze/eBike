package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.report.ApiRequest;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.enums.TestStatus;
import be.kdg.integration4.domain.enums.TestType;
import be.kdg.integration4.domain.report.TestLine;
import be.kdg.integration4.repository.ApiRequestRepository;
import be.kdg.integration4.repository.BikeReportRepository;
import be.kdg.integration4.repository.TestLineRepository;
import be.kdg.integration4.service.dtos.StartTestDto;
import be.kdg.integration4.service.dtos.TestDto;
import be.kdg.integration4.service.interfaces.TestbenchApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class TestbenchApiServiceImpl implements TestbenchApiService {

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final BikeReportRepository bikeReportRepository;
    private final TestLineRepository testLineRepository;

    @Value("${workbench.api.url}")
    private String baseUrl;

    private final ApiRequestRepository apiRequestRepository;

    public TestbenchApiServiceImpl(RestTemplate restTemplate, HttpHeaders headers, BikeReportRepository bikeReportRepository, TestLineRepository testLineRepository, ApiRequestRepository apiRequestRepository) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.bikeReportRepository = bikeReportRepository;
        this.testLineRepository = testLineRepository;
        this.apiRequestRepository = apiRequestRepository;
    }

    @Override
    public TestDto startTest(TestType testType, int batteryCapacity, int maxSupport, int enginePowerMax, int enginePowerNominal, int engineTorque) {
        String startTestUrl = baseUrl + "/api/test";
        StartTestDto startTestDto = new StartTestDto(
                testType.toString(),
                batteryCapacity,
                maxSupport,
                enginePowerMax,
                enginePowerNominal,
                engineTorque);


        HttpEntity<StartTestDto> requestEntity = new HttpEntity<>(startTestDto, headers);

        // Send POST request with headers
        ResponseEntity<TestDto> response = restTemplate.exchange(startTestUrl, HttpMethod.POST, requestEntity, TestDto.class);


        return response.getBody();
    }

    @Override
    public TestStatus checkTestStatus(String id) {

        String statusUrl = baseUrl + "/api/test/" + id;

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TestDto> response = restTemplate.exchange(
                statusUrl, HttpMethod.GET, requestEntity, TestDto.class
        );

        return Objects.requireNonNull(response.getBody()).state();
    }

    public String fetchCsv(String id) {
        String reportUrl = baseUrl + "/api/test/" + id + "/report";
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(reportUrl, HttpMethod.GET, requestEntity, String.class);
        return Objects.requireNonNull(response.getBody());
    }



    @Override
    public ApiRequest saveApiRequest(BikeReport bikeReport, String requestId) {
        return apiRequestRepository.save(new ApiRequest(bikeReport, requestId));
    }

    @Override
    public void deleteApiRequest(String requestId) {
        apiRequestRepository.deleteApiRequestByTestId(requestId);
    }

    @Override
    public ApiRequest getApiRequest(String requestId) {
        return apiRequestRepository.findApiRequestByTestId(requestId).orElseThrow();
    }

    @Override
    public Long fetchReportId(String testId, List<TestLine> testLines) {
        ApiRequest apiRequest = this.getApiRequest(testId);
        BikeReport report = apiRequest.getReport();
        testLineRepository.saveAll(testLines);
        report.setTestLines(testLines);
        bikeReportRepository.save(report);
        return report.getId();
    }
}
