package be.kdg.integration4.config;

import be.kdg.integration4.service.TestbenchApiService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final TestbenchApiService testbenchApiService;

    public WebSocketConfig(TestbenchApiService testbenchApiService) {
        this.testbenchApiService = testbenchApiService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new be.kdg.integration4.config.TestStatusWebSocketHandler(testbenchApiService), "/ws/status")
                .setAllowedOrigins("*"); // Allow all origins, for CORS issues
    }
}
