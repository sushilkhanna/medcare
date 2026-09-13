package com.medcare.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Enables real-time push updates over STOMP/WebSocket (with SockJS fallback
 * for browsers/networks that block raw WebSocket).
 *
 * Clients connect to /ws and subscribe to:
 *   /topic/medicines  -> stock/availability changes (add, edit, delete, restock, checkout deduction)
 *   /topic/orders/{userId} -> order status changes for a specific patient (admin updates order status)
 *
 * This is one-way server -> client push (broadcast), which is all the
 * medicine-management "real-time" requirement needs: every connected client
 * sees stock levels update live instead of needing a manual page refresh.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String[] origins = java.util.Arrays.stream(allowedOrigins.split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).toArray(String[]::new);
        registry.addEndpoint("/ws")
                .setAllowedOrigins(origins)
                .withSockJS();
    }
}
