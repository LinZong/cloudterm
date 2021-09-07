package com.kodedu.cloudterm.service;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketSessionAware {
    WebSocketSession getWebSocketSession();
    void setWebSocketSession(WebSocketSession webSocketSession);
}
