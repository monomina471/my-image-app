package com.my_app.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.my_app.demo.entity.ImageEntity;
import com.my_app.demo.repository.ImageRepository;
import com.my_app.demo.storage.ImageStorageProvider;

@Service // Beanに登録される
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageStorageProvider storageProvider;

    public ImageService(
            ImageRepository imageRepository,
            ImageStorageProvider storageProvider) {
        this.imageRepository = imageRepository;
        this.storageProvider = storageProvider;
    }

    public List<ImageEntity> findByUserId(Long userId) {
        return imageRepository.findByUserId(userId);
    }

    public List<ImageEntity> searchAndSortImages(Long userId, String keyword, String sortOrder) {

        Sort sort = Sort.by("createTime");
        if ("asc".equalsIgnoreCase(sortOrder)) {
            sort = sort.ascending(); // 古い順
        } else {
            sort = sort.descending(); // 新しい順(こっちをデフォルトに)
        }

        if (keyword != null && !keyword.isEmpty()) {
            return imageRepository.findByUserIdAndTagsContaining(userId, keyword, sort); // タグ検索と並び替えを同時に
        } else {
            return imageRepository.findByUserId(userId, sort); // 全件取得と並び替えを同時に
        }
    }

    public ImageEntity saveImage(MultipartFile file, String tags, Long userId) throws IOException {

        // 保存処理はproviderに任せる
        String url = storageProvider.save(file);

        ImageEntity entity = new ImageEntity();
        entity.setUrl(url);
        entity.setTags(tags);
        entity.setUserId(userId);
        return imageRepository.save(entity);

    }

    public void deleteImage(Long id) throws IOException {

        // 削除処理もproviderに任せる
        ImageEntity image = imageRepository.findById(id).orElseThrow();
        
        storageProvider.delete(image.getUrl());
        imageRepository.deleteById(id);
    }
}
