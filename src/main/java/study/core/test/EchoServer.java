package study.core.test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    private final ServerSocket serverSocket;

    EchoServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    void start() {
        while (true) {
            try {
                this.listenAndServe();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void listenAndServe() throws IOException {
        System.out.println("waiting for client ...");
        Socket socket = this.serverSocket.accept();
        System.out.println("client accepted");

        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output);

        char[] buffer = new char[4096];
        while (true) {
             int ret = reader.read(buffer);
             if (ret < 0) {
                 break;
             }
             writer.write(new String(buffer, 0, ret));
             writer.flush();
        }
    }
}
