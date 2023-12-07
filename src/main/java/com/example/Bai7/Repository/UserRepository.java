package com.example.Bai7.Repository;

import com.example.Bai7.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Repository;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository
public class UserRepository {
    private final String filePath;
    private final Gson gson;


    public UserRepository() {
        this.filePath = "src/main/resources/templates/user_db.json";
        this.gson = new GsonBuilder().setPrettyPrinting().create();

    }

    public List<User> getAllUsers() throws IOException {
        try {
            String json = Files.readString(Path.of(filePath));
            List<User> list = gson.fromJson(json, new TypeToken<List<User>>() {}.getType());
            if(list != null){
                return list;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void addUser(User user){
        try {
            List<User> users = getAllUsers();
            users.add(user);

            String json = gson.toJson(users);
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(json);
            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public Optional<User> findByUsername(String username) {
        try {
            List<User> users = getAllUsers();
            return users.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void save(User user) {
        try {
            List<User> users = getAllUsers();

            Optional<User> existingUser = users.stream()
                    .filter(u -> u.getUsername().equals(user.getUsername()))
                    .findFirst();

            existingUser.ifPresent(users::remove);
            users.add(user);

            String json = gson.toJson(users);
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(json);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getFriends(String username) {
        Optional<User> userOptional = findByUsername(username);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return user.getFriends();
        }
        return List.of();
    }
}
