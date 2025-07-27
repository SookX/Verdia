package com.anastassow.server.webSockets.webSocketConfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.anastassow.server.jwt.AuthHandshakeInterceptor;
import com.anastassow.server.webSockets.MyWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired private MyWebSocketHandler myWebSocketHandler;
    @Autowired private AuthHandshakeInterceptor authInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler, "/ws/chat")
                .addInterceptors(authInterceptor)
                .setAllowedOrigins("*");
    }
}

