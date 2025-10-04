package com.onlineShop.bootcamp.service.user;

import com.onlineShop.bootcamp.dto.UserResponse;
import com.onlineShop.bootcamp.entity.User;
import com.onlineShop.bootcamp.mapper.UserMapper;
import com.onlineShop.bootcamp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserResponse).toList();
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
