package study.core.chat;

import java.util.Collection;

public class PingScheduler extends Thread {

    private final Center center;
    private final long interval;

    PingScheduler(Center center, long interval) {
        this.center = center;
        this.interval = interval;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(this.interval);
                Collection<Room> rooms = this.center.getRooms();
                for (Room room : rooms) {
                    room.sendPing(this.interval);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
