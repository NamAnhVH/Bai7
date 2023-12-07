package com.example.Bai7.Repository;

import com.example.Bai7.Model.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Repository
public class MessageRepository {

    private final String filePath;
    private final Gson gson;

    public MessageRepository() {
        this.filePath = "src/main/resources/templates/message_queue_db.json";
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Map<String, LinkedList<Message>> getAllMessageQueue() {
        try {
            String json = Files.readString(Path.of(filePath));
            TypeToken<Map<String, LinkedList<Message>>> typeToken = new TypeToken<>() {};
            if(json.equals("")){
                return new HashMap<>();
            }
            return gson.fromJson(json, typeToken.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void save(String username,LinkedList<Message> messageQueue) {
        Map<String, LinkedList<Message>> messageQueueMap = getAllMessageQueue();
        messageQueueMap.put(username, messageQueue);
        try {
            String json = gson.toJson(messageQueueMap);
            Files.writeString(Path.of(filePath), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Message> getMessageQueue(String username) {
        Map<String, LinkedList<Message>> messageQueueMap = getAllMessageQueue();

        if (messageQueueMap.containsKey(username)) {
            return messageQueueMap.get(username);
        } else {
            return new LinkedList<>();
        }
    }

    public void remove(String username) {
        Map<String, LinkedList<Message>> messageQueueMap = getAllMessageQueue();
        messageQueueMap.remove(username);

        try {
            String json = gson.toJson(messageQueueMap);
            Files.writeString(Path.of(filePath), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
