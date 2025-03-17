package be.kdg.integration4.websocket;

import be.kdg.integration4.domain.TestStatus;
import be.kdg.integration4.service.TestbenchApiService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestStatusWebSocketHandler extends TextWebSocketHandler {
    private final TestbenchApiService testbenchApiService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public TestStatusWebSocketHandler(TestbenchApiService testbenchApiService) {
        this.testbenchApiService = testbenchApiService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String testId = message.getPayload(); // Received test ID from client

        // Periodically check test status
        scheduler.scheduleAtFixedRate(() -> {
            try {
                TestStatus status = testbenchApiService.checkTestStatus(testId);
                session.sendMessage(new TextMessage("Status: " + status));

                // Stop polling if the test is complete
                if (status == TestStatus.COMPLETED) {
                    session.sendMessage(new TextMessage("Test completed. Retrieving report..."));
                    session.close(); // Close connection after completion
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.SECONDS); // Check status every 5 seconds
    }
}
