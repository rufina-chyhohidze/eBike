package be.kdg.integration4.service;

import be.kdg.integration4.domain.TestStatus;
import be.kdg.integration4.domain.TestType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class TestbenchApiServiceImpl implements TestbenchApiService {

    private final RestTemplate restTemplate;

    @Value("${workbench.apikey}")
    private String apiKey;

    public TestbenchApiServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public TestDto sendStartRequest(TestType testType, int batteryCapacity, int maxSupport, int enginePowerMax, int enginePowerNominal, int engineTorque) {
        String startTestUrl = "https://testbench.raoul.dev/api/test";
        StartTestDto startTestDto = new StartTestDto(
                testType.toString(),
                batteryCapacity,
                maxSupport,
                enginePowerMax,
                enginePowerNominal,
                engineTorque
        );

        // Create headers with API key
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", apiKey); // Add API key to header

        HttpEntity<StartTestDto> requestEntity = new HttpEntity<>(startTestDto, headers);

        // Send POST request with headers
        ResponseEntity<TestDto> response = restTemplate.exchange(startTestUrl, HttpMethod.POST, requestEntity, TestDto.class);


        return response.getBody();
    }

    @Override
    public TestStatus checkTestStatus(String id) {

        String statusUrl = "https://testbench.raoul.dev/api/test/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TestDto> response = restTemplate.exchange(
                statusUrl, HttpMethod.GET, requestEntity, TestDto.class
        );

        return Objects.requireNonNull(response.getBody()).state();
    }

    @Override
    public String sendReportRequest(String id) {
        String reportUrl = "https://testbench.raoul.dev/api/test/" + id + "/report";
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(reportUrl, HttpMethod.GET, requestEntity, String.class);
        return Objects.requireNonNull(response.getBody());
    }
}
