package fr.utc.sr03.chat.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * https://programming.vip/docs/four-ways-of-integrating-websocket-with-spring-boot.html
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpoint() {
        return new ServerEndpointExporter();
    }
}



/**
 * 这段代码是一个用于配置和启用 Spring Boot WebSocket 的配置类。
 *
 * 在 Spring Boot 中，使用 WebSocket 需要进行一些配置，以便正确地处理 WebSocket 连接。WebSocketConfig 类使用了 @Configuration 和 @EnableWebSocket 注解，将其标记为一个配置类并启用 WebSocket 功能。
 *
 * 在该配置类中，使用 @Bean 注解创建了一个 ServerEndpointExporter 对象，并将其返回。ServerEndpointExporter 是 Spring 提供的一个用于注册和管理 WebSocket 端点的类。通过将 ServerEndpointExporter 注册为一个 Bean，Spring Boot 将自动扫描和注册带有 @ServerEndpoint 注解的 WebSocket 端点类。
 *
 * 简而言之，这个配置类的作用是启用 Spring Boot 的 WebSocket 功能，并注册 WebSocket 端点以供使用。
 * */