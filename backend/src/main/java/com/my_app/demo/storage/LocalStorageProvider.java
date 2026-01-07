package com.my_app.demo.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

// 開発環境用画像保存メソッド
// application.yamlにspring.profiles.active=devを記述

@Component
@Profile("dev")
public class LocalStorageProvider implements ImageStorageProvider{

    // 環境変数から画像保存用ディレクトリのパスを取得
    @Value("${api.uploaded-url}")
    private String uploadDir;

    @Override
    public String save(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        // 保存用ディレクトリが存在しないなら作成
        if (!Files.exists(uploadPath)) 
            Files.createDirectories(uploadPath);

        // ファイル名が被らないようにUUIDを付与
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        // フォルダのパスとファイル名をくっつけたパスの保存先にファイルを保存
        Files.copy(file.getInputStream(), uploadPath.resolve(fileName));

        return "/upload/" + fileName;
    }

    @Override
    public void delete(String url) {
        String fileName = url.replace("/upload/", "");
        try {
            Files.deleteIfExists(Paths.get(uploadDir).resolve(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
