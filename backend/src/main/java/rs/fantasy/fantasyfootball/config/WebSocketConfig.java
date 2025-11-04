package rs.fantasy.fantasyfootball.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enables a simple in-memory message broker
        // Destination prefixes: /topic for broadcasting, /queue for point-to-point
        config.enableSimpleBroker("/topic", "/queue");
        
        // Prefix for messages bound for @MessageMapping methods
        config.setApplicationDestinationPrefixes("/app");
        
        // Prefix for user-specific destinations
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the WebSocket endpoint
        // Client će se konektovati na ws://localhost:8080/ws
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Dozvoli sve origin-e (za dev)
                .setAllowedOrigins("http://localhost:4200")
                .withSockJS(); // Fallback opcija za browsere koji ne podržavaju WebSocket
    }
}

