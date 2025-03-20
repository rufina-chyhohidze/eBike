package be.kdg.integration4.websocket;

import be.kdg.integration4.domain.report.ApiRequest;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.domain.report.TestLine;
import be.kdg.integration4.service.interfaces.BikeReportService;
import be.kdg.integration4.service.interfaces.CsvProcessingService;
import be.kdg.integration4.service.interfaces.TestbenchApiService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;


public class TestResultFetchWebSocketHandler extends TextWebSocketHandler {
    private final TestbenchApiService testbenchApiService;
    private final CsvProcessingService csvProcessingService;
    private final BikeReportService bikeReportService;

    public TestResultFetchWebSocketHandler(TestbenchApiService testbenchApiService, CsvProcessingService csvProcessingService, BikeReportService bikeReportService) {
        this.testbenchApiService = testbenchApiService;
        this.csvProcessingService = csvProcessingService;
        this.bikeReportService = bikeReportService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String testId = message.getPayload(); // Received test ID from client
        String csvContent = testbenchApiService.sendReportRequest(testId);
        List<TestLine> testLines = csvProcessingService.processCsvString(csvContent);
        ApiRequest apiRequest = testbenchApiService.getApiRequest(testId);
        BikeReport report = apiRequest.getReport();
        bikeReportService.update(report.getId(),report.getBike().getFrameNumber(), report.getReportDate(),
                report.getScore(),
                report.getTechnician().getEmail(),
                report.getCustomer().getEmail(),
                testLines,
                report.getTestBench().getBenchId());
        session.sendMessage(new TextMessage(String.valueOf(report.getId())));
        session.close();
    }
}
