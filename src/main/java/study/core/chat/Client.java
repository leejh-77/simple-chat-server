package study.core.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import study.core.base.Utils;
import study.core.model.Message;

import java.io.*;
import java.net.Socket;

class Client implements Runnable {

    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("Client Threads");

    private final long id;

    private final Socket socket;

    private Room room;
    private String name;

    Client(long id, Socket socket) {
        this.id = id;
        this.socket = socket;
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
        while (true) {
            Message message = Utils.getMapper().readValue(this.socket.getInputStream(), Message.class);
            switch (message.getType()) {
                case chat -> this.room.sendMessage(Message.Chat(this.name, message.getMessage()));
                case exit -> this.room.sendMessage(Message.Exit(this.name));
                case entrance -> {
                    this.name = message.getName();
                    Center.getInstance().onClientEntered(this, message.getRoom());
                }
            }
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
}
