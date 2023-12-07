package com.example.Bai7.Service;

import com.example.Bai7.Model.AccessToken;
import com.example.Bai7.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FriendService {

    @Autowired
    private UserRepository userRepository;


    public List<String> getFriends(String username) {
        return userRepository.getFriends(username);
    }

    public boolean isFriend(String username, String friendname) {
        return userRepository.getFriends(username)
                .stream()
                .anyMatch(friend -> friend.equals(friendname));
    }
}
