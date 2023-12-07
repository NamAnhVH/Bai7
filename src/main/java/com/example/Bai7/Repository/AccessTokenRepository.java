package com.example.Bai7.Repository;

import com.example.Bai7.Model.AccessToken;
import com.example.Bai7.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Repository;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AccessTokenRepository {

    private final String filePath;
    private final Gson gson;


    public AccessTokenRepository() {
        this.filePath = "src/main/resources/templates/token_db.json";
        this.gson = new GsonBuilder().setPrettyPrinting().create();

    }

    public List<AccessToken> getAllAccessTokens() throws IOException {
        try {
            String json = Files.readString(Path.of(filePath));
            List<AccessToken> list = gson.fromJson(json, new TypeToken<List<AccessToken>>() {}.getType());
            if(list != null){
                return list;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void save(AccessToken accessToken){
        try {
            List<AccessToken> tokens = getAllAccessTokens();

            tokens.add(accessToken);

            String json = gson.toJson(tokens);
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(json);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<AccessToken> findByToken(String token) {
        try {
            List<AccessToken> tokens = getAllAccessTokens();
            return tokens.stream()
                    .filter(t -> t.getToken().equals(token))
                    .findFirst();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void delete(AccessToken token) {
        try {
            List<AccessToken> tokens = getAllAccessTokens();

            tokens.removeIf(t -> t.getToken().equals(token.getToken()));

            String json = gson.toJson(tokens);
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(json);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
