package com.my_app.demo.Service;

import org.springframework.stereotype.Service;
import com.my_app.demo.Entity.UserEntity;
import com.my_app.demo.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + email));

        return new org.springframework.security.core.userdetails.User( //UserDetailsクラスのオブジェクトを作成
            user.getEmail(),
            user.getPassword(),
            Collections.emptyList()
        );
    }
}
