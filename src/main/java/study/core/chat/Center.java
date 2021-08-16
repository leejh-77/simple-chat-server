package study.core.chat;

import study.core.model.Message;

import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;

public class Center implements MessageHandler {

    private static final long PING_INTERVAL = 60 * 1000; // miilis

    private final HashMap<Integer, Room> rooms = new HashMap<>();

    public Center() {
        this.rooms.put(1, new Room(1));
        new PingScheduler(this, PING_INTERVAL).start();
    }

    public void createClient(Socket socket) {
        Client c = new Client(socket);
        c.setMessageHandler(this);
        c.start();
    }

    Collection<Room> getRooms() {
        return this.rooms.values();
    }

    @Override
    public void handleMessage(Client c, Message message) {
        assert(message.getType() == Message.Type.entrance);
        Room room = rooms.get(message.getRoom());
        room.addClient(c);
        room.handleMessage(c, message);
    }
}
