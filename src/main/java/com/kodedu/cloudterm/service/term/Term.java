package com.kodedu.cloudterm.service.term;

import io.termd.core.function.BiConsumer;
import io.termd.core.function.Consumer;
import io.termd.core.http.HttpTtyConnection;
import io.termd.core.readline.Keymap;
import io.termd.core.readline.Readline;
import io.termd.core.tty.TtyEvent;
import io.termd.core.util.Helper;

import java.io.Closeable;
import java.io.IOException;


public class Term implements BiConsumer<TtyEvent, Integer>, Closeable {

    public Readline readline;
    public final HttpTtyConnection conn;

    public final Consumer<String> inputHandler;

    public static final String WELCOME = "Welcome to you-get shell!\n";
    public static final String PROMPT = "[you-get] > ";

    public Term(final HttpTtyConnection conn, Consumer<String> inputHandler) {
        this.readline = getReadline();
        this.conn = conn;
        this.inputHandler = inputHandler;
        conn.setEventHandler(this);
    }

    public void start() {
        conn.write(WELCOME);
        readline.readline(conn, PROMPT, new ReadlineLooper());
    }

    private static Readline getReadline() {
        Readline readline = new Readline(Keymap.getDefault());
        for (io.termd.core.readline.Function function : Helper
                .loadServices(Thread.currentThread()
                                    .getContextClassLoader(),
                        io.termd.core.readline.Function.class)) {
            readline.addFunction(function);
        }
        return readline;
    }

    @Override
    public void accept(TtyEvent ttyEvent, Integer integer) {

    }

    public void receive(String content) {
        conn.writeToDecoder(content);
    }

    public void echo(String content) {
        conn.write(content);
    }

    @Override
    public void close() throws IOException {
        conn.close();
    }

    private class ReadlineLooper implements Consumer<String> {

        @Override
        public void accept(String s) {
            inputHandler.accept(s);
            readline.readline(conn, PROMPT, this);
        }
    }
}
