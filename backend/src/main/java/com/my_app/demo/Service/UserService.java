package com.my_app.demo.service;

import org.springframework.stereotype.Service;

import com.my_app.demo.entity.UserEntity;
import com.my_app.demo.repository.UserRepository;

import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); 
    }

    public UserEntity registerUser(UserEntity user) { // 新規登録
        Optional<UserEntity> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) { // Optional.isPresent() 値が存在する場合はtrue 存在しない場合は false
            throw new RuntimeException("このメールアドレスは既に使われています");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // パスワードのハッシュ化

        return userRepository.save(user);
    }

    public UserEntity login(String email, String rawPassword) { //ログイン
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);

        UserEntity user = userOpt.orElseThrow( //Optionalオブジェクトが保持する値を返し、値が無いなら例外スロー
            () -> new RuntimeException("メールアドレスが登録されていません"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) { //DBに保存されているハッシュと同じ計算式でハッシュ化し照合
            throw new RuntimeException("パスワードが違います");
        }

        return user; // 認証成功
    }
}