package com.onlineShop.bootcamp.service.auth;

import com.onlineShop.bootcamp.dto.auth.AuthResponse;
import com.onlineShop.bootcamp.dto.auth.LoginRequest;
import com.onlineShop.bootcamp.dto.auth.RegisterRequest;
import com.onlineShop.bootcamp.entity.User;
import com.onlineShop.bootcamp.repository.UserRepository;
import com.onlineShop.bootcamp.service.email.EmailService;
import com.onlineShop.bootcamp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new RuntimeException("Email is already registered with " + registerRequest.getEmail());
        }

        User user  = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles("ROLE_USER")
                .createDate(LocalDateTime.now())
                .build();

        userRepository.save(user);
        emailService.sendRegistrationWelcome(user);
        String token = jwtUtil.generateJwtToken(user.getId(), user.getUsername());

        return new AuthResponse(user.getId(), user.getUsername(), user.getEmail() , user.getRoles(), user.getCreateDate(), token);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtUtil.generateJwtToken(user.getId(),user.getUsername());

        return new AuthResponse(user.getId(), user.getUsername(), user.getEmail() , user.getRoles(), user.getCreateDate(), token);
    }

}
