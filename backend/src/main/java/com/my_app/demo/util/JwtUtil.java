package com.my_app.demo.util;

//トークンの作成や検証を行うメソッドを持ったクラス

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;

    //トークンの有効期間:24時間
    private static final long EXPIRATION_TIME = 86400000;
    
    public JwtUtil(@Value("${jwt.secret-key}") String secretKey) {
        // ここで Secrets Manager や環境変数から渡された文字列を Key オブジェクトに変換し、適切な長さの秘密鍵を生成
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    
    //トークンの作成
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //トークンからユーザー名を取得
    public String extractEmail(String token) {
        return getClaims(token).getSubject(); //JWTクレームの中のsub(ユーザーの件名)を取得
    }

    //トークンの有効性を検証
    public boolean validateToken(String token, String email) {
        String tokenEmail = extractEmail(token);
        return (email.equals(tokenEmail) && !isTokenExpired(token));
    }

    //クレームを取得するメソッド
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    //期限切れかを認証するメソッド
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date()); //クレームの有効期間と現在時刻を比較して検証
    }
}
