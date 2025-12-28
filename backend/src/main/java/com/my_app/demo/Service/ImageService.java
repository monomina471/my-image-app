package com.my_app.demo.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.my_app.demo.Entity.ImageEntity;
import com.my_app.demo.Repository.ImageRepository;

@Service // Beanに登録される
public class ImageService {
    private final ImageRepository imageRepository;

    // 画像を保存するフォルダの場所（プロジェクト直下の uploads フォルダなど）
    // 本番では application.properties で設定するのが一般的ですが、一旦直書きします
    // ここのコメントは後々消す
    //pom.xmlなどが置いてあるプロジェクトのルートフォルダが基準
    //本番はサーバーやPCの絶対パスを指定
    private final String UPLOAD_DIR = "../uploads/"; 

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<ImageEntity> findByUserId(Long userId) {
        return imageRepository.findByUserId(userId);
    }

    public List<ImageEntity> searchAndSortImages(Long userId, String keyword, String sortOrder) {

        Sort sort = Sort.by("createTime");
        if ("asc".equalsIgnoreCase(sortOrder)) {
            sort = sort.ascending(); //古い順
        } else {
            sort = sort.descending(); //新しい順(こっちをデフォルトに)
        }

        if (keyword != null && !keyword.isEmpty()) {
            return imageRepository.findByUserIdAndTagsContaining(userId, keyword, sort); //タグ検索と並び替えを同時に
        } else {
            return imageRepository.findByUserId(userId, sort); //全件取得と並び替えを同時に
        }
    }

    public ImageEntity saveImage(MultipartFile file, String tags, Long userId) throws IOException {

        // ファイルが無い場合は作成
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // ファイル名が被らないようにUUIDを付与
        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

        // フォルダのパスとファイル名をくっつけて保存先のパスを作成
        Path filePath = uploadPath.resolve(fileName);

        // ファイルを実際に保存
        Files.copy(file.getInputStream(), filePath);

        ImageEntity entity = new ImageEntity();

        // フロントエンドから画像を表示できるようにURL（パス）をセット
        // 実際の運用では http://localhost:8080/images/ファイル名 のようになります
        entity.setUrl("/uploaded/" + fileName);

        entity.setTags(tags);
        entity.setUserId(userId);

        return imageRepository.save(entity);
    }

    public void deleteImage(Long id) throws IOException {

        ImageEntity deleteImage = imageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("画像が見つかりません ID:" + id));

        //DBに保存されているURLからファイル名部分だけを抽出
        String fileName = deleteImage.getUrl().replace("/uploaded/", "");

        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);

        try {
            Files.deleteIfExists(filePath);
            System.out.println("ファイルを削除しました:" + filePath.toString());
        } catch (IOException e) {
            System.err.println("ファイルの削除に失敗しました:" + e.getMessage());
        }

        imageRepository.deleteById(id);
    }
}
