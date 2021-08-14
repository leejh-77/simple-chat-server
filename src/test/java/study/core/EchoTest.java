package study.core;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class EchoTest {

    static final String TEST_HOST = "localhost";
    static final int TEST_PORT = 8901;

    @Test
    public void testEcho() throws IOException {
        TestClient client = new TestClient();
        client.connect(TEST_HOST, TEST_PORT);

        int cnt = 0;
        while (cnt++ < 5) {
            String msg = "message (" + cnt + ")";
            client.writeMessage(msg);
        }
    }

    public static class TestClient {

        private Socket socket;

        void connect(String host, int port) throws IOException {
            this.socket = new Socket(host, port);
        }

        void writeMessage(String message) throws IOException {
            OutputStream output = this.socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output);
            writer.write(message);
            writer.flush();

            InputStream input = this.socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            char[] buffer = new char[4096];
            int ret = reader.read(buffer);
            System.out.println(new String(buffer, 0, ret));
        }
    }
}
