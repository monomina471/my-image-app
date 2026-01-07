package com.my_app.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // 追加
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // 追加
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // 追加
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ★ 1. JWTフィルターを使うために宣言
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // コンストラクタで注入
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        System.out.println("SecurityConfig (JWT版) の設定が読み込まれました");

        http
            // CSRF無効化
            .csrf(csrf -> csrf.disable())

            // CORS設定
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // ★ 2. セッションを無効化 (JWTはステートレスなのでセッションを作らない)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                // (A) プリフライトリクエスト(OPTIONS)を全て許可
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // (B) 画像ファイルそのものへのアクセスは許可（表示用）
                .requestMatchers("/uploaded/**").permitAll()

                // (C) ログイン・登録のエンドポイントを許可
                .requestMatchers("/api/users/signup", "/api/users/login", "/api/users/refresh").permitAll()
                
                // (D) 画像のリスト取得(GET)だけは許可したい場合（お好みで）
                // .requestMatchers(HttpMethod.GET, "/api/images/**").permitAll()

                // requestMatchersで指定したもの以外全てのリクエストが認証を必要とする
                .anyRequest().authenticated()
            )

            // ★ 3. JWTフィルターを、標準の認証フィルターの「前」に追加
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ★ 4. ログインAPIで使うために AuthenticationManager を公開
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("http://localhost:5173", "http://localhost:3000")); // 3000も念のため追加
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
