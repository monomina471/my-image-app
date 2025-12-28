package com.my_app.demo.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.my_app.demo.Entity.ImageEntity;
import com.my_app.demo.Service.ImageService;
import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    // @Autowiredは省略できる
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/user/{userId}/search")
    public List<ImageEntity> searchUserImages(
            @PathVariable Long userId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sort", defaultValue = "desc") String sort) {
        return imageService.searchAndSortImages(userId, keyword, sort);
    }

    @PostMapping("/save")
    public ImageEntity saveImage(
            @RequestParam("file") MultipartFile file, // formDataは区切られているデータのため、BodyではなくParamを使う
            @RequestParam("tags") String tags,
            @RequestParam("userId") Long userId) throws IOException { // ファイル操作時のエラー
        return imageService.saveImage(file, tags, userId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        try {
            imageService.deleteImage(id);
            return ResponseEntity.ok("削除しました");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ファイルの削除中にエラーが発生しました");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
