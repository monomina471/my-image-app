package com.my_app.demo.config;

//リクエストが来るたびにヘッダーにトークンが含まれているか、トークンが正しいかをチェックする

import com.my_app.demo.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // ヘッダーからAuthorizationを取得
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // "Bearer"で始まっているか確認
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // "Bearer " の後ろを取得
            try {
                email = jwtUtil.extractEmail(token); // payload内のsubjectを抽出
            } catch (Exception e) {
                logger.error("JWTトークンの解析に失敗しました; " + e.getMessage());
            }
        }

        // トークンがあり、まだ認証されていない場合
        // SecurityContextHolderは認証ユーザーの詳細を格納している
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 実際のユーザーの権限などの情報をDBから読み込みUserDetailsのフォーマットに変換
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // トークンが有効ならセキュリティコンテキストに設定
            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                // 認証オブジェクト(承認ハンコ)を作って、SecurityContextに設定
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,  //ここでユーザーの詳細情報を入れる
                        null,  //パスワードは認証済みなので消去
                        userDetails.getAuthorities()); //権限を取得
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);

    }

}
