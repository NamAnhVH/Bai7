package com.example.Bai7.Service;

import com.example.Bai7.Repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    private final static String STORAGE_PATH = "src/main/resources/static/storage";

    public boolean isExistFile(String filename) {
        File file = new File(STORAGE_PATH + File.separator + filename);
        return file.exists() && file.isFile();
    }

    public void addPermission(String sender, String receiver, String originalFilename) {
        fileRepository.save(sender, receiver, originalFilename);
    }

    public boolean hasPermission(String username, String filename) {
        return fileRepository.hasPermission(username, filename);
    }
}
