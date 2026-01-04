package com.my_app.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Profile("prod") // 本番プロファイルの時だけこの設定を読み込む
public class S3Config {

    // S3クライアントをBeanに登録
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_1) // 東京リージョンなど
                .build();
    }
}