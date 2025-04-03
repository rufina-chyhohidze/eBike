package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.report.ApiRequest;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.enums.TestStatus;
import be.kdg.integration4.domain.enums.TestType;
import be.kdg.integration4.repository.ApiRequestRepository;
import be.kdg.integration4.service.dtos.StartTestDto;
import be.kdg.integration4.service.dtos.TestDto;
import be.kdg.integration4.service.interfaces.TestbenchApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class TestbenchApiServiceImpl implements TestbenchApiService {

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    @Value("${workbench.api.url}")
    private String baseUrl;

    private final ApiRequestRepository apiRequestRepository;

    public TestbenchApiServiceImpl(RestTemplate restTemplate, HttpHeaders headers, ApiRequestRepository apiRequestRepository) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.apiRequestRepository = apiRequestRepository;
    }

    @Override
    public TestDto sendStartRequest(TestType testType, int batteryCapacity, int maxSupport, int enginePowerMax, int enginePowerNominal, int engineTorque) {
        String startTestUrl = baseUrl + "/api/test";
        StartTestDto startTestDto = new StartTestDto(
                testType.toString(),
                batteryCapacity,
                maxSupport,
                enginePowerMax,
                enginePowerNominal,
                engineTorque
        );


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

    @Override
    public String sendReportRequest(String id) {
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
}
