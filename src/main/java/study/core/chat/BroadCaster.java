package study.core.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import study.core.base.Utils;
import study.core.model.Message;

import java.io.IOException;
import java.io.Writer;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;


class BroadCaster extends Thread {

    private final ArrayList<Message> messages = new ArrayList<>();
    private final Room room;

    BroadCaster(Room room) {
        this.room = room;
    }

    void sendMessage(Message message) {
        synchronized (this.messages) {
            this.messages.add(message);
        }
        synchronized (this) {
            if (!this.isAlive()) {
                this.start();
            } else {
                this.notifyAll();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            while (this.messages.isEmpty()) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            Message[] messages;
            synchronized (this.messages) {
                messages = this.messages.toArray(new Message[0]);
                this.messages.clear();
            }
            this.broadCast(messages);
        }
    }

    private void broadCast(Message[] messages) {
        for (Message message : messages) {
            ArrayList<Client> clients = new ArrayList<>(this.room.getClients());
            for (Client client : clients) {
                try {
                    Utils.getMapper().writeValue(client.getOutputStream(), message);
                } catch (IOException e) {
                    e.printStackTrace();
                    this.room.removeClient(client);
                }
            }
        }
    }

}
