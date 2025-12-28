package com.my_app.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity //DBのテーブルと対応付ける
@Table(name = "images")
@Data //GetterやSetterなどを自動生成
public class ImageEntity {

    @Id //主キーに設定
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String tags;
    private Long userId;
    private LocalDateTime createTime;

    @PrePersist
    public void prePersist() {
            this.createTime = LocalDateTime.now();
    }
}
