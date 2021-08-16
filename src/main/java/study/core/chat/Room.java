package study.core.chat;

import study.core.model.Message;

import java.util.ArrayList;
import java.util.List;

public class Room implements MessageHandler {

    private final long id;
    private final BroadCaster caster = new BroadCaster(this);
    private final List<Client> clients = new ArrayList<>();

    Room(long id) {
        this.id = id;
    }

    void addClient(Client c) {
        System.out.println("client entered - room : " + this.id);
        c.setMessageHandler(this);
        synchronized (this.clients) {
            this.clients.add(c);
        }
    }

    void removeClient(Client c) {
        System.out.println("client left - room : " + this.id);
        synchronized (this.clients) {
            this.clients.remove(c);
            c.terminate();
        }
    }

    List<Client> getClients() {
        return this.clients;
    }

    public void handleMessage(Client c, Message message) {
        assert(message.getType() != Message.Type.entrance);
        this.caster.sendMessage(message);
    }

    void sendPing(long seconds) {
        long now = System.currentTimeMillis();
        Client[] clients = this.getClients().toArray(new Client[0]);
        ArrayList<Client> deletes = new ArrayList<>();
        for (Client client : clients) {
             long last = client.getLastResponse();
             if (now - last > seconds) {
                 deletes.add(client);
             }
        }

        for (Client c : deletes) {
            this.removeClient(c);
        }

        this.handleMessage(null, Message.Ping());
    }
}
