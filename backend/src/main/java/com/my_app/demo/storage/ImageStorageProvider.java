package com.my_app.demo.storage;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageProvider {
    String save(MultipartFile file) throws IOException;
    void delete(String url);
}