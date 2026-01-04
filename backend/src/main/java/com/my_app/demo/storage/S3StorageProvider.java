package com.my_app.demo.storage;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

// AWS SDK V2 関連
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

// 本番環境(S3)用画像保存メソッド
// 環境変数でSPRING_PROFILES_ACTIVE=prodを設定(application.yamlより優先される)

@Component
@Profile("prod")
public class S3StorageProvider implements ImageStorageProvider {

    // S3を操作するためのS3クライアントを用意
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${api.cloudfront.url}")
    private String cloudFrontUrl;

    // ConfigクラスでBeanとして登録したものがここで注入
    public S3StorageProvider(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // S3のどこにどのような設定でアップロードするか
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName) // 保存先のバケット名
                .key(fileName) // S3内でのファイル名(パス含む)
                .contentType(file.getContentType()) //画像の種類を指定
                .build(); 
        // ファイルをS3へアップロード
        s3Client.putObject(putObjectRequest, 
            // RequestBody.fromInputStreamはMultipartFileの中身をAWSが送信できる形式に変換
            // InputStreamからHTTPリクエストのボディを生成
            RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return cloudFrontUrl + "/" + fileName;
    }

    @Override
    public void delete(String url) {
        // urlからS3のキー(ファイル名)だけを抜き出す
        String fileName = url.replace(cloudFrontUrl + "/", "");

        //どのバケットのどのファイルを削除するかの設定
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        // S3クライアントを使って削除を実行
        s3Client.deleteObject(deleteObjectRequest);
    }

}
