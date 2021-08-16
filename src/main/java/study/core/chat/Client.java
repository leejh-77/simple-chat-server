package study.core.chat;

import study.core.base.Utils;
import study.core.model.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

class Client implements Runnable {

    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("Client Threads");

    private final Socket socket;

    private MessageHandler handler;
    private boolean isAlive;
    private long lastResponse;

    Client(Socket socket) {
        this.isAlive = true;
        this.socket = socket;
        this.lastResponse = System.currentTimeMillis();
    }

    void start() {
        new Thread(THREAD_GROUP, this).start();
    }

    @Override
    public void run() {
        try {
            this.readPipe();
        } catch (Exception e) {
            this.close();
        } finally {
            this.close();
        }
    }

    private void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readPipe() throws IOException {
        while (this.isAlive) {
            Message message = Utils.getMapper().readValue(this.socket.getInputStream(), Message.class);
            this.handler.handleMessage(this, message);
            this.lastResponse = System.currentTimeMillis();
        }
    }

    void setMessageHandler(MessageHandler handler) {
        this.handler = handler;
    }

    OutputStream getOutputStream() throws IOException {
        return this.socket.getOutputStream();
    }

    long getLastResponse() {
        return this.lastResponse;
    }

    void terminate() {
        this.isAlive = false;
    }
}
