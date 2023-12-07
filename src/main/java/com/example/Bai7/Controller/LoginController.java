package com.example.Bai7.Controller;

import com.example.Bai7.FormRequest.AccountRequest;
import com.example.Bai7.Model.AccessToken;
import com.example.Bai7.Model.User;
import com.example.Bai7.Service.AccessTokenService;
import com.example.Bai7.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private AccessTokenService accessTokenService;

    @PostMapping("login")
    public ResponseEntity<?> login(@ModelAttribute AccountRequest loginRequest) {
        Optional<User> userOptional = userService.getUser(loginRequest);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            AccessToken accessToken = accessTokenService.generateAccessToken(user.getUsername());
            return ResponseEntity.ok(accessToken);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản không hợp lệ");
    }
}

