package com.my_app.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Elastic BeanStalkのヘルスチェック用コントローラー
@RestController
public class HealthCheckController {

    @GetMapping("/api/health") // EBの設定画面で指定するパス
    public String healthCheck() {
        return "ok";
    }
}
