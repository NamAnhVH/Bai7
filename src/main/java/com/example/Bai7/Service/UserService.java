package com.example.Bai7.Service;

import com.example.Bai7.FormRequest.AccountRequest;
import com.example.Bai7.Model.User;
import com.example.Bai7.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUser(AccountRequest accountRequest) {
        Optional<User> userOptional = userRepository.findByUsername(accountRequest.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (new BCryptPasswordEncoder().matches(accountRequest.getPassword(), user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public void addUser(AccountRequest registerRequest){
        String hashedPassword = new BCryptPasswordEncoder().encode(registerRequest.getPassword());
        userRepository.addUser(new User(registerRequest.getUsername(), hashedPassword));
    }

}
