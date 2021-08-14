package study.core.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import study.core.base.Utils;
import study.core.model.Message;

import java.io.IOException;
import java.io.Writer;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;


class BroadCaster implements Runnable {

    private final ArrayList<Message> messages = new ArrayList<>();
    private final Room room;

    BroadCaster(Room room) {
        new Thread(this).start();
        this.room = room;
    }

    void sendMessage(Message message) {
        synchronized (this.messages) {
            this.messages.add(message);
        }
        synchronized (this) {
            this.notifyAll();
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
        ArrayList<Client> clients = new ArrayList<>(this.room.getClients());
        for (Message message : messages) {
            for (int i = clients.size() - 1; i >= 0; i--) {
                Client client = clients.get(i);
                try {
                    Utils.getMapper().writeValue(client.getOutputStream(), message);
                } catch (IOException e) {
                    e.printStackTrace();
                    clients.remove(i);
                    this.room.removeClient(client);
                }
            }
        }
    }

}
