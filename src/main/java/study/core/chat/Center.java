package study.core.chat;

import study.core.model.Message;

import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;

public class Center implements MessageHandler {

    private static final long PING_INTERVAL = 30 * 1000; // millis

    private final HashMap<Integer, Room> rooms = new HashMap<>();

    public Center() {
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
        Room room = this.getRoom(message.getRoom());
        room.addClient(c);
        room.handleMessage(c, message);
    }

    private Room getRoom(int id) {
        Room room = this.rooms.get(id);
        if (room != null) {
            return room;
        }

        room = new Room(id);
        synchronized (this.rooms) {
            Room old = this.rooms.putIfAbsent(id, room);
            return old != null ? old : room;
        }
    }
}
