package com.example.Bai7.Repository;

import com.example.Bai7.Model.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
public class FileRepository {

    private final String filePath;
    private final Gson gson;



    public FileRepository() {
        this.filePath = "src/main/resources/templates/file_permission_db.json";
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void save(String sender, String receiver, String originalFilename) {
        Map<String, Set<String>> filePermissionMap = getAllFilePermission();
        Set<String> filePermission = filePermissionMap.getOrDefault(originalFilename, new HashSet<>());
        filePermission.add(sender);
        filePermission.add(receiver);
        filePermissionMap.put(originalFilename, filePermission);
        try {
            String json = gson.toJson(filePermissionMap);
            Files.writeString(Path.of(filePath), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Set<String>> getAllFilePermission() {
        try {
            String json = Files.readString(Path.of(filePath));
            TypeToken<Map<String, Set<String>>> typeToken = new TypeToken<>() {};
            if(json.equals("")){
                return new HashMap<>();
            }
            return gson.fromJson(json, typeToken.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public boolean hasPermission(String username, String filename) {
        Map<String, Set<String>> filePermissionMap = getAllFilePermission();
        if (filePermissionMap.containsKey(filename)) {
            Set<String> userSet = filePermissionMap.get(filename);
            return userSet.contains(username);
        }
        return false;
    }
}
