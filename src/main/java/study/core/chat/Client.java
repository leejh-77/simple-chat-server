package study.core.chat;

import study.core.base.Utils;
import study.core.model.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

class Client implements Runnable {

    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("Client Threads");

    private final Socket socket;

    private long lastResponse;

    private Room room;
    private String name;

    private boolean isAlive;

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
            try {
                this.socket.close();
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }
    }

    private void readPipe() throws IOException {
        while (this.isAlive) {
            Message message = Utils.getMapper().readValue(this.socket.getInputStream(), Message.class);
            switch (message.getType()) {
                case chat -> this.room.sendMessage(Message.Chat(this.name, message.getMessage()));
                case exit -> this.room.sendMessage(Message.Exit(this.name));
                case entrance -> {
                    this.name = message.getName();
                    Center.getInstance().onClientEntered(this, message.getRoom());
                }
            }
            this.lastResponse = System.currentTimeMillis();
        }
    }

    void setRoom(Room room) {
        this.room = room;
    }

    public String getName() {
        return name;
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
