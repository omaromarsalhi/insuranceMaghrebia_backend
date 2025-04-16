package com.maghrebia.claim.service;

import com.maghrebia.claim.entity.User;
import com.maghrebia.claim.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
