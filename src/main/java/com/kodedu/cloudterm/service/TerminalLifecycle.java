package com.kodedu.cloudterm.service;

public interface TerminalLifecycle {

    void onTerminalInit() throws Exception;

    void onTerminalReady() throws Exception;

    void onCommand(String command) throws Exception;

    void onTerminalResize(String columns, String rows) throws Exception;

    void onTerminalClose() throws Exception;

    ;
}
