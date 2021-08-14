package study.client;

import study.core.model.Message;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Application app = new Application("localhost", 8901);
        app.run();
    }
}
