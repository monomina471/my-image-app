package com.my_app.demo.controller;

import com.my_app.demo.entity.RefreshToken;
import com.my_app.demo.entity.UserEntity;
import com.my_app.demo.repository.UserRepository;
import com.my_app.demo.service.RefreshTokenService;
import com.my_app.demo.service.UserService;
import com.my_app.demo.util.JwtUtil;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil,
            UserRepository userRepository, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }

    // 新規登録
    @PostMapping("/signup")
    public UserEntity signup(@RequestBody UserEntity user) {
        return userService.registerUser(user);
    }

    // メールアドレス検索(ログインで使用)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Spring Securityの機能でユーザー認証を行う
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            // メールアドレスを使用してアクセストークンを作成
            String accessToken = jwtUtil.generateToken(loginRequest.getEmail());

            //リフレッシュトークンを生成
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());


            // DBからユーザー情報を取得
            UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

            // DTOクラスのオブジェクトを作成
            LoginResponse response = new LoginResponse(
                accessToken, 
                refreshToken.getToken(),  //Entityのgetterメソッド
                user.getUserId(), 
                user.getEmail());

            // トークンとユーザー情報をフロントエンドへ返す
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        // フロントから送られてきたリフレッシュトークン文字列
        String requestToken = request.getToken();
    
        return refreshTokenService.findByToken(requestToken) //DBにトークンがあるか検索
        .map(refreshTokenService::verifyExpiration) // 有効期限をチェック
        .map(RefreshToken::getUser) // トークンに紐づいたユーザー情報を取得
        .map(user -> {
            // ユーザーのメールアドレスを使って新しいアクセストークンを作成
            String newAccessToken = jwtUtil.generateToken(user.getEmail());

            return ResponseEntity.ok(new LoginResponse(
                newAccessToken, 
                requestToken, 
                user.getUserId(), 
                user.getEmail()
            ));
        })
        .orElseThrow(() -> new RuntimeException("リフレッシュトークンがデータベースに存在しません"));
    }

}

// ユーザーから送られてくる情報を保持するクラス
class LoginRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

// ログイン時にフロントエンドへ返す情報を保持するクラス
class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String email;

    public LoginResponse(String accessToken, String refreshToken, Long userId, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}

class RefreshTokenRequest {
    private String token; // フロントから送られてくるリフレッシュトークン

    // デフォルトコンストラクタ（JSON変換に必要）
    public RefreshTokenRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
