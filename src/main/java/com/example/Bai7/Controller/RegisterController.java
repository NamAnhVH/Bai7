package com.example.Bai7.Controller;

import com.example.Bai7.FormRequest.AccountRequest;
import com.example.Bai7.Model.User;
import com.example.Bai7.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public ResponseEntity<?> register(@ModelAttribute AccountRequest registerRequest) {
        Optional<User> userOptional = userService.getUser(registerRequest);
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tài khoản đã tồn tại");
        } else {
            userService.addUser(registerRequest);
            return ResponseEntity.ok("Đăng ký thành công");
        }
    }
}
