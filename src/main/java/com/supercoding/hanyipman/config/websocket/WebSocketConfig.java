package com.supercoding.hanyipman.config.websocket;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WebSocketConfig  {
    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();

        config.setHostname("ec2-54-180-103-214.ap-northeast-2.compute.amazonaws.com");

        config.setPort(8088); // 포트를 필요에 따라 수정

        return new SocketIOServer(config);
    }
}
