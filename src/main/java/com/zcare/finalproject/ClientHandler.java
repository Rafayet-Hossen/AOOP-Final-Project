package com.zcare.finalproject;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final List<ClientHandler> clients;
    private PrintWriter out;
    private BufferedReader in;
    private String email;

    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            email = in.readLine();
            loadPreviousMessages();

            String msg;
            while ((msg = in.readLine()) != null) {
                String formattedMsg = email + ": " + msg;

                saveToDatabase(email, msg);
                broadcastMessage(formattedMsg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
            clients.remove(this);
        }
    }

    private void broadcastMessage(String message) {
        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("h:mma"));
        for (ClientHandler client : clients) {
            client.out.println("[" + timestamp + "] " + message);
        }
    }

    private void saveToDatabase(String sender, String message) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO chat_messages_group (sender_email, message) VALUES (?, ?)")) {
            ps.setString(1, sender);
            ps.setString(2, message);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPreviousMessages() {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT sender_email, message FROM chat_messages_group ORDER BY id ASC")) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String oldMsg = rs.getString("sender_email") + ": " + rs.getString("message");
                out.println(oldMsg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
