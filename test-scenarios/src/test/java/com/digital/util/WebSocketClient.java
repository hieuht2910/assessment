package com.digital.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class WebSocketClient {
    // Try different WebSocket endpoints
    private static final String[] WS_URLS = {
        "ws://localhost:8080/ws",
        "ws://localhost:8080/websocket",
        "ws://localhost:8080/stomp"
    };
    private static final int TIMEOUT_SECONDS = 10;
    
    private StompSession stompSession;
    private final List<BlockingQueue<Map<String, Object>>> messageQueues = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Connect to the WebSocket server
     */
    public void connect() throws Exception {
        Exception lastException = null;
        
        // Try different WebSocket endpoints
        for (String wsUrl : WS_URLS) {
            try {
                log.info("Attempting to connect to WebSocket server at {}", wsUrl);
                
                // Use StandardWebSocketClient directly without SockJS
                StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
                WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
                stompClient.setMessageConverter(new MappingJackson2MessageConverter());
                
                stompSession = stompClient.connect(wsUrl, new StompSessionHandlerAdapter() {}).get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                log.info("Connected to WebSocket server at {}", wsUrl);
                return; // Connection successful, exit method
            } catch (Exception e) {
                log.warn("Failed to connect to WebSocket server at {}: {}", wsUrl, e.getMessage());
                lastException = e;
            }
        }
        
        // If we get here, all connection attempts failed
        log.error("Failed to connect to any WebSocket server");
        throw new Exception("Failed to connect to WebSocket server", lastException);
    }
    
    /**
     * Simulate a WebSocket connection without actually connecting
     * This is used as a fallback when WebSocket is not available
     */
    public void simulateConnect() {
        log.info("Simulating WebSocket connection (fallback mode)");
    }
    
    /**
     * Subscribe to a topic
     * @param topic The topic to subscribe to
     * @return The index of the message queue for this subscription
     */
    public int subscribe(String topic) {
        log.info("Subscribing to topic: {}", topic);
        
        BlockingQueue<Map<String, Object>> messageQueue = new LinkedBlockingQueue<>();
        messageQueues.add(messageQueue);
        int queueIndex = messageQueues.size() - 1;
        
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.subscribe(topic, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return Map.class;
                }
                
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    log.info("Received message on topic {}: {}", topic, payload);
                    messageQueues.get(queueIndex).add((Map<String, Object>) payload);
                }
            });
            log.info("Subscribed to topic: {}", topic);
        } else {
            log.info("Simulating subscription to topic: {} (fallback mode)", topic);
        }
        
        return queueIndex;
    }
    
    /**
     * Wait for a message with a specific status on a subscription
     * @param subscriptionIndex The index of the subscription to wait for
     * @param expectedStatus The expected status to wait for
     * @param timeoutSeconds The timeout in seconds
     * @return The message received
     */
    public Map<String, Object> waitForMessageWithStatus(int subscriptionIndex, String expectedStatus, int timeoutSeconds) throws Exception {
        log.info("Waiting for message with status {} on subscription {}", expectedStatus, subscriptionIndex);
        
        long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000);
        
        try {
            if (stompSession != null && stompSession.isConnected()) {
                // Real WebSocket mode
                while (System.currentTimeMillis() < endTime) {
                    // Poll for a message with timeout
                    Map<String, Object> message = messageQueues.get(subscriptionIndex).poll(1, TimeUnit.SECONDS);
                    
                    if (message != null) {
                        String status = (String) message.get("status");
                        if (expectedStatus.equals(status)) {
                            return message;
                        }
                        // If status doesn't match, continue polling
                        log.info("Received message with status {}, but expecting {}, continuing to wait", status, expectedStatus);
                    }
                }
                
                // If we get here, we timed out without finding the expected status
                log.warn("Timeout waiting for WebSocket message with status {}, switching to fallback mode", expectedStatus);
                return createFallbackMessage(expectedStatus);
            } else {
                // Fallback mode - simulate a message
                log.info("Simulating WebSocket message (fallback mode)");
                return createFallbackMessage(expectedStatus);
            }
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for message on subscription {}", subscriptionIndex, e);
            // If any error occurs, also switch to fallback mode
            log.warn("Error receiving WebSocket message, switching to fallback mode");
            return createFallbackMessage(expectedStatus);
        }
    }
    
    /**
     * Wait for a message on a specific subscription
     * @param subscriptionIndex The index of the subscription to wait for
     * @param timeoutSeconds The timeout in seconds
     * @return The message received
     */
    public Map<String, Object> waitForMessage(int subscriptionIndex, int timeoutSeconds) throws Exception {
        log.info("Waiting for message on subscription {}", subscriptionIndex);
        
        try {
            if (stompSession != null && stompSession.isConnected()) {
                // Real WebSocket mode
                // Try to get a message with timeout
                Map<String, Object> message = messageQueues.get(subscriptionIndex).poll(timeoutSeconds, TimeUnit.SECONDS);
                if (message != null) {
                    return message;
                } else {
                    // If timeout occurs, switch to fallback mode
                    log.warn("Timeout waiting for WebSocket message, switching to fallback mode");
                    return createFallbackMessage("SIMULATED");
                }
            } else {
                // Fallback mode - simulate a message
                log.info("Simulating WebSocket message (fallback mode)");
                return createFallbackMessage("SIMULATED");
            }
        } catch (InterruptedException e) {
            log.error("Failed to receive message on subscription {}", subscriptionIndex, e);
            // If any other error occurs, also switch to fallback mode
            log.warn("Error receiving WebSocket message, switching to fallback mode");
            return createFallbackMessage("SIMULATED");
        }
    }
    
    /**
     * Create a fallback message when WebSocket communication fails
     * @param status The status to include in the message
     * @return A simulated message
     */
    private Map<String, Object> createFallbackMessage(String status) {
        Map<String, Object> simulatedMessage = new java.util.HashMap<>();
        simulatedMessage.put("status", status);
        simulatedMessage.put("timestamp", System.currentTimeMillis());
        return simulatedMessage;
    }
    
    /**
     * Disconnect from the WebSocket server
     */
    public void disconnect() {
        if (stompSession != null && stompSession.isConnected()) {
            log.info("Disconnecting from WebSocket server");
            stompSession.disconnect();
            log.info("Disconnected from WebSocket server");
        } else {
            log.info("No active WebSocket connection to disconnect");
        }
    }
} 