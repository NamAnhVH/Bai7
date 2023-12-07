package com.example.Bai7.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessToken{
    private String token;
    private String username;
    private long createdTime;
    private long expirationTime;
}
