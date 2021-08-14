package study.core.chat;

import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;

public class Center {

    private static final long PING_INTERVAL = 60 * 1000; // miilis
    private static final Center instance = new Center();

    private final HashMap<Integer, Room> rooms = new HashMap<>();

    public static Center getInstance() {
        return instance;
    }

    public Center() {
        this.rooms.put(1, new Room(1));
        new PingScheduler(this, PING_INTERVAL).start();
    }

    public void createClient(Socket socket) {
        new Client(socket).start();
    }

    void onClientEntered(Client client, Integer roomId) {
        Room room = rooms.get(roomId);
        room.addClient(client);
    }

    Collection<Room> getRooms() {
        return this.rooms.values();
    }

}
