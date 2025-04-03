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

    public TestResultFetchWebSocketHandler(TestbenchApiService testbenchApiService, CsvProcessingService csvProcessingService) {
        this.testbenchApiService = testbenchApiService;
        this.csvProcessingService = csvProcessingService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String testId = message.getPayload(); // Received test ID from client
        String csvContent = testbenchApiService.fetchCsv(testId);
        List<TestLine> testLines = csvProcessingService.processCsvString(csvContent);
        Long reportId = testbenchApiService.fetchReportId(testId, testLines);
        session.sendMessage(new TextMessage(String.valueOf(reportId)));
        session.close();
    }
}
