package com.zcare.finalproject;

import java.io.*;
import java.net.Socket;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private final ChatUIController controller;

    public ChatClient(ChatUIController controller) {
        this.controller = controller;
    }

    public void start(String email) {
        try {
            socket = new Socket("localhost", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(email);

            new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        controller.addMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        if (out != null) {
            out.println(msg);
        }
    }
}
