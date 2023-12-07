package com.example.Bai7.Controller;

import com.example.Bai7.FormRequest.MessageDto;
import com.example.Bai7.Model.AccessToken;
import com.example.Bai7.Model.Message;
import com.example.Bai7.Service.AccessTokenService;
import com.example.Bai7.Service.FileService;
import com.example.Bai7.Service.FriendService;
import com.example.Bai7.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
public class MessageController {

    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private FileService fileService;

    private static final Map<String, CompletableFuture<Message>> completableFutureMap = new HashMap<>();

    @PostMapping(path = "/sendMessage", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> sendMessage(@RequestHeader("AccessToken") String accessTokenHeader, @ModelAttribute MessageDto messageDto) {
        AccessToken accessToken = accessTokenService.getAccessToken(accessTokenHeader);
        if (accessToken != null) {
            if (friendService.isFriend(accessToken.getUsername(), messageDto.getUsername())) {
                Message message = messageDto.getMessage();
                if (message.getContent() instanceof MultipartFile file) {
                    String originalFilename = file.getOriginalFilename();
                    String storageDirectory = "C:/Users/naman/OneDrive/Desktop/BaiTapPhan2/Bai7/src/main/resources/static/storage";
                    try {
                        File destFile = new File(storageDirectory + File.separator + originalFilename);
                        file.transferTo(destFile);
                        String urlDownload = "http://localhost:8080/file?filename=" + originalFilename;
                        message.setContent(urlDownload);
                        fileService.addPermission(accessToken.getUsername(), messageDto.getUsername(), originalFilename);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                message.setSender(accessToken.getUsername());
                message.setTime(System.currentTimeMillis());
                if (completableFutureMap.containsKey(messageDto.getUsername())) {
                    completableFutureMap.get(messageDto.getUsername()).complete(messageDto.getMessage());
                    return ResponseEntity.ok(1);
                } else {
                    LinkedList<Message> messageQueue = messageService.getMessageQueue(messageDto.getUsername());
                    messageQueue.add(message);
                    messageService.saveMessageQueue(messageDto.getUsername(), messageQueue);
                    return ResponseEntity.ok(2);
                }
            } else {
                return ResponseEntity.ok(3);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/getNewMessage")
    public ResponseEntity<?> getNewMessage(@RequestHeader("AccessToken") String accessTokenHeader) {
        AccessToken accessToken = accessTokenService.getAccessToken(accessTokenHeader);
        if (accessToken != null) {
            LinkedList<Message> messageQueue = messageService.getMessageQueue(accessToken.getUsername());
            if (!messageQueue.isEmpty()) {
                messageService.removeMessageQueue(accessToken.getUsername());
                return ResponseEntity.ok(messageQueue);
            } else {
                CompletableFuture<Message> completableFuture = completableFutureMap.computeIfAbsent(
                        accessToken.getUsername(), k -> new CompletableFuture<>()
                );
                try {
                    Message message = completableFuture.get(10, TimeUnit.SECONDS);
                    completableFutureMap.remove(accessToken.getUsername());
                    return ResponseEntity.ok(message);
                } catch (TimeoutException e) {
                    return ResponseEntity.ok(List.of());
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/file")
    public ResponseEntity<?> getFile(@RequestHeader("AccessToken") String accessTokenHeader, @RequestParam String filename) {
        AccessToken accessToken = accessTokenService.getAccessToken(accessTokenHeader);
        if (accessToken != null) {
            if (fileService.isExistFile(filename)) {
                if(fileService.hasPermission(accessToken.getUsername(), filename)){
                    File file = new File("src/main/resources/static/storage/" + filename);
                    Resource resource = new FileSystemResource(file);
                    return ResponseEntity.ok(resource);
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
