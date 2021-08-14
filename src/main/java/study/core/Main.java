package study.core;

import study.core.chat.Center;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int SERVER_PORT = 8901;

    public static void main(String[] args) throws IOException {
        SocketHandler server = new SocketHandler(SERVER_PORT);
        server.start();
    }

    private static class SocketHandler {

        private final ServerSocket serverSocket;
        private final Center center = new Center();

        SocketHandler(int port) throws IOException {
            this.serverSocket = new ServerSocket(port);
        }

        void start() {
            while (true) {
                try {
                    this.listenAndServe();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        void listenAndServe() throws IOException {
            System.out.println("waiting for client...");
            Socket socket = this.serverSocket.accept();
            System.out.println("client accepted");

            this.center.createClient(socket);
        }
    }
}
