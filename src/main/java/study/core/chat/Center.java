package study.core.chat;

import java.net.Socket;
import java.util.HashMap;

public class Center {

    private static final Center instance = new Center();
    private static long clientCnt = 0;

    private final HashMap<Integer, Room> rooms = new HashMap<>();

    public static Center getInstance() {
        return instance;
    }

    public Center() {
        this.rooms.put(1, new Room(1));
    }

    public void createClient(Socket socket) {
        new Client(++clientCnt, socket).start();
    }

    public void onClientEntered(Client client, Integer roomId) {
        Room room = rooms.get(roomId);
        room.addClient(client);
    }
}
