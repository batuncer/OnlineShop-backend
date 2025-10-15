package com.onlineShop.bootcamp.service.auth;

import com.onlineShop.bootcamp.dto.auth.AuthResponse;
import com.onlineShop.bootcamp.dto.auth.LoginRequest;
import com.onlineShop.bootcamp.dto.auth.RegisterRequest;
import com.onlineShop.bootcamp.entity.User;
import com.onlineShop.bootcamp.repository.UserRepository;
import com.onlineShop.bootcamp.security.JwtUtil;
import com.onlineShop.bootcamp.service.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<UsernamePasswordAuthenticationToken> authenticationTokenCaptor;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("alice");
        registerRequest.setEmail("alice@example.com");
        registerRequest.setPassword("secret");
    }

    @Test
    void registerThrowsWhenEmailAlreadyExists() {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));

        verify(userRepository, never()).save(any(User.class));
        verify(emailService, never()).sendRegistrationWelcome(any(User.class));
        verify(jwtUtil, never()).generateJwtToken(anyLong(), any());
    }

    @Test
    void registerEncodesPasswordAndReturnsToken() {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("hashed-secret");
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(42L);
            return user;
        }).when(userRepository).save(any(User.class));
        when(jwtUtil.generateJwtToken(42L, registerRequest.getUsername())).thenReturn("jwt-token");

        AuthResponse response = authService.register(registerRequest);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getPassword()).isEqualTo("hashed-secret");
        assertThat(savedUser.getUsername()).isEqualTo("alice");
        assertThat(savedUser.getEmail()).isEqualTo("alice@example.com");
        assertThat(savedUser.getRoles()).isEqualTo("ROLE_USER");
        assertThat(savedUser.getCreateDate()).isNotNull();

        assertThat(response.getUserId()).isEqualTo(42L);
        assertThat(response.getUsername()).isEqualTo("alice");
        assertThat(response.getEmail()).isEqualTo("alice@example.com");
        assertThat(response.getRole()).isEqualTo("ROLE_USER");
        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getCreatedDate()).isNotNull();

        verify(emailService).sendRegistrationWelcome(savedUser);
        verify(jwtUtil).generateJwtToken(42L, "alice");
    }

    @Test
    void loginAuthenticatesAndReturnsAuthResponse() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("bob");
        loginRequest.setPassword("password");

        LocalDateTime createdDate = LocalDateTime.of(2024, 1, 1, 12, 0);
        User user = User.builder()
                .id(7L)
                .username("bob")
                .email("bob@example.com")
                .password("encoded")
                .roles("ROLE_USER")
                .createDate(createdDate)
                .build();

        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(user));
        when(jwtUtil.generateJwtToken(7L, "bob")).thenReturn("jwt-login");

        AuthResponse response = authService.login(loginRequest);

        verify(authenticationManager).authenticate(authenticationTokenCaptor.capture());
        UsernamePasswordAuthenticationToken authenticationToken = authenticationTokenCaptor.getValue();
        assertThat(authenticationToken.getName()).isEqualTo("bob");
        assertThat(authenticationToken.getCredentials()).isEqualTo("password");
        assertThat(response.getUserId()).isEqualTo(7L);
        assertThat(response.getUsername()).isEqualTo("bob");
        assertThat(response.getEmail()).isEqualTo("bob@example.com");
        assertThat(response.getRole()).isEqualTo("ROLE_USER");
        assertThat(response.getCreatedDate()).isEqualTo(createdDate);
        assertThat(response.getToken()).isEqualTo("jwt-login");
    }
}
