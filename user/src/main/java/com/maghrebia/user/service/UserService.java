package com.maghrebia.user.service;

import com.maghrebia.user.dto.request.EmailRequest;
import com.maghrebia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public boolean existEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
