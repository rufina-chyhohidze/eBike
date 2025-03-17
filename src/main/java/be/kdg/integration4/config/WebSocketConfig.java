package be.kdg.integration4.config;

import be.kdg.integration4.service.BikeReportService;
import be.kdg.integration4.service.CsvProcessingService;
import be.kdg.integration4.service.TestbenchApiService;
import be.kdg.integration4.websocket.TestResultFetchWebSocketHandler;
import be.kdg.integration4.websocket.TestStatusWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final TestbenchApiService testbenchApiService;
    private final CsvProcessingService csvProcessingService;
    private final BikeReportService bikeReportService;

    public WebSocketConfig(TestbenchApiService testbenchApiService, CsvProcessingService csvProcessingService, BikeReportService bikeReportService) {
        this.testbenchApiService = testbenchApiService;
        this.csvProcessingService = csvProcessingService;
        this.bikeReportService = bikeReportService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new TestStatusWebSocketHandler(testbenchApiService), "/ws/status")
                .addHandler(new TestResultFetchWebSocketHandler(testbenchApiService,csvProcessingService, bikeReportService), "/ws/result")
                .setAllowedOrigins("*"); // Allow all origins, for CORS issues
    }
}
