package com.onlineShop.bootcamp.security;

import com.onlineShop.bootcamp.entity.User;
import com.onlineShop.bootcamp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.*;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService  {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.onlineShop.bootcamp.entity.User> userRes = userRepository.findByUsername(username);

        if(userRes.isEmpty())
            throw new UsernameNotFoundException("No user found with this username "+username);
        User user = userRes.get();
        return new
                org.springframework.security.core.userdetails.User(
                username,
                user.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_USER")
                )
        );
    }



}
