package com.example.Bai7.Service;

import com.example.Bai7.Model.AccessToken;
import com.example.Bai7.Repository.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccessTokenService {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    private static final long EXPIRED_TIME = 10;

    public AccessToken generateAccessToken(String username) {
        AccessToken token = new AccessToken();
        token.setToken(generateToken());
        token.setUsername(username);
        token.setCreatedTime(System.currentTimeMillis());
        token.setExpirationTime(token.getCreatedTime() + EXPIRED_TIME * 60 * 1000);
        accessTokenRepository.save(token);
        return token;
    }

    private String generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public boolean isValidAccessToken(String accessTokenHeader) {
        Optional<AccessToken> tokenOptional = accessTokenRepository.findByToken(accessTokenHeader);
        if(tokenOptional.isPresent()){
            AccessToken token = tokenOptional.get();
            long current = System.currentTimeMillis();
            return current > token.getCreatedTime() && current < token.getExpirationTime();
        }
        return false;
    }

    public AccessToken getAccessToken(String accessTokenHeader) {
        Optional<AccessToken> tokenOptional = accessTokenRepository.findByToken(accessTokenHeader);
        if(tokenOptional.isPresent()){
            AccessToken token = tokenOptional.get();
            long current = System.currentTimeMillis();
            if(current > token.getCreatedTime() && current < token.getExpirationTime()){
                return token;
            } else {
                accessTokenRepository.delete(token);
            }
        }
        return null;
    }
}
