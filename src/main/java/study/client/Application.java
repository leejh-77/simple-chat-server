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

    Application(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    void run() throws IOException {
        this.prepare();
        this.connect();

        System.out.println("Connection successfully established");
        Scanner sc = new Scanner(System.in);
        while (true) {
            String msg = sc.nextLine();
            if (msg.equals("exit")) {
                this.write(Message.Exit());
                System.out.println("bye");
                break;
            }
            this.write(Message.Chat(msg));
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
        new ChatReader(socket).start();
        this.write(Message.Entrance(this.name, this.roomId));
    }

    void write(Message message) throws IOException {
        Utils.getMapper().writeValue(this.socket.getOutputStream(), message);
    }

    static class ChatReader extends Thread {

        private final Socket socket;

        ChatReader(Socket socket) {
            this.socket = socket;
            super.setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Message message = Utils.getMapper().readValue(socket.getInputStream(), Message.class);
                    switch (message.getType()) {
                        case ping -> System.out.println("ping requested");
                        case entrance -> System.out.println(message.getName() + " has entered");
                        case exit -> System.out.println(message.getName() + " has left");
                        case chat -> System.out.println(message.getName() + " : " + message.getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
