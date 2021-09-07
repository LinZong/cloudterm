package com.kodedu.cloudterm.service;

import com.alibaba.fastjson.JSONObject;
import com.kodedu.cloudterm.service.term.Term;
import io.termd.core.http.HttpTtyConnection;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class TermdTerminalService implements TerminalLifecycle, WebSocketSessionAware {

    private WebSocketSession session;

    private Term term;

    @Override
    public void onTerminalInit() throws Exception {

    }

    @Override
    public void onTerminalReady() throws Exception {
        term = new Term(toHttpTtyConnection(), str -> term.echo(str + "\n"));
        term.start();
    }

    @Override
    public void onCommand(String command) throws Exception {
        JSONObject j = new JSONObject();
        j.put("action", "read");
        j.put("data", command);
        term.receive(j.toJSONString());
    }

    @Override
    public void onTerminalResize(String columns, String rows) throws Exception {
        JSONObject j = new JSONObject();
        j.put("action", "resize");
        j.put("cols", Integer.parseInt(columns));
        j.put("rows", Integer.parseInt(rows));
        term.receive(j.toJSONString());
    }

    @Override
    public void onTerminalClose() throws Exception {
        term.close();
    }


    private HttpTtyConnection toHttpTtyConnection() {
        return new HttpTtyConnection() {
            @Override
            protected void write(byte[] buffer) {
                try {
                    session.sendMessage(new TextMessage(buffer));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void close() {
                try {
                    term.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void execute(Runnable task) {
                task.run();
            }

            @Override
            public void schedule(Runnable task, long delay, TimeUnit unit) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        task.run();
                    }
                }, TimeUnit.MILLISECONDS.convert(delay, unit));
            }
        };
    }

    @Override
    public WebSocketSession getWebSocketSession() {
        return session;
    }

    @Override
    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.session = webSocketSession;
    }
}
