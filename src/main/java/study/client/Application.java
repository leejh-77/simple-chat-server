package study.client;

import study.core.base.Utils;
import study.core.model.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Application {

    private final Socket socket;
    private String name;
    private int roomId;

    public static void main(String[] args) throws IOException {
        Application app = new Application("localhost", 8901);
        app.run();
    }

    Application(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    void run() throws IOException {
        this.prepare();
        this.connect();

        Scanner sc = new Scanner(System.in);
        while (true) {
            String msg = sc.nextLine();
            if (msg.equals("exit")) {
                this.write(Message.Exit(this.name));
                System.out.println("bye");
                break;
            }
            this.write(Message.Chat(this.name, msg));
        }
    }

    private void prepare() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter name : ");

        String name = sc.nextLine();

        System.out.println("Enter roomId : ");
        int room = sc.nextInt();

        this.name = name;
        this.roomId = room;
    }

    private void connect() throws IOException {
        new ChatReader().start();
        this.write(Message.Entrance(this.name, this.roomId));
    }

    void write(Message message) throws IOException {
        synchronized (this) {
            Utils.getMapper().writeValue(this.socket.getOutputStream(), message);
        }
    }

    private class ChatReader extends Thread {

        ChatReader() {
            super.setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Message message = Utils.getMapper().readValue(socket.getInputStream(), Message.class);
                    switch (message.getType()) {
                        case ping -> write(Message.Ping());
                        case entrance -> System.out.println(message.getName() + " has entered");
                        case exit -> System.out.println(message.getName() + " has left");
                        case chat -> System.out.println(message.getName() + " : " + message.getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
