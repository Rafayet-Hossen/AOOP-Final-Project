package com.zcare.finalproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatUIController {
    @FXML
    private ListView<String> chatList;

    @FXML
    private TextArea messageField;

    private ChatClient chatClient;
    private String userName;
    public void initializeChat(String name) {
        this.userName = name;
        this.chatClient = new ChatClient(this);
        chatClient.start(name);
    }

    public void addMessage(String message) {
        Platform.runLater(() -> chatList.getItems().add(message));
    }

    @FXML
    private void sendMessage() {
        String msg = messageField.getText().trim();
        if (!msg.isEmpty()) {
            chatClient.sendMessage(msg);
            messageField.clear();
        }
    }
}
