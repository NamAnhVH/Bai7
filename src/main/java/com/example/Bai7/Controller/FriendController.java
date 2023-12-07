package com.example.Bai7.Controller;

import com.example.Bai7.Model.AccessToken;
import com.example.Bai7.Service.AccessTokenService;
import com.example.Bai7.Service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class FriendController {

    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private FriendService friendService;

    @GetMapping("/friends")
    public ResponseEntity<?> getFriends(@RequestHeader("AccessToken") String accessTokenHeader){
        AccessToken accessToken = accessTokenService.getAccessToken(accessTokenHeader);
        if(accessToken != null){
            return ResponseEntity.ok(friendService.getFriends(accessToken.getUsername()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
