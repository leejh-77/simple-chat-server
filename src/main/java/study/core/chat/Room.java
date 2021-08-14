package study.core.chat;

import study.core.model.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Room {

    private long id;
    private final BroadCaster caster = new BroadCaster(this);
    private final List<Client> clients = new ArrayList<>();

    Room(long id) {
        this.id = id;
    }

    void addClient(Client c) {
        System.out.println("client entered - " + c.getName() + ", room : " + this.id);
        c.setRoom(this);
        synchronized (this.clients) {
            this.clients.add(c);
        }
    }

    void removeClient(Client c) {
        System.out.println("client removed - " + c.getName() + ", room : " + this.id);
        synchronized (this.clients) {
            this.clients.remove(c);
        }
    }

    Collection<Client> getClients() {
        return this.clients;
    }

    void sendMessage(Message message) {
        this.caster.sendMessage(message);
    }
}
