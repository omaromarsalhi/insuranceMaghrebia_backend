package com.maghrebia.user.service;

import com.maghrebia.user.dto.request.EmailRequest;
import com.maghrebia.user.dto.request.EmployeeRegistrationRequest;
import com.maghrebia.user.entity.Role;
import com.maghrebia.user.entity.User;
import com.maghrebia.user.exception.EmailAlreadyExistsException;
import com.maghrebia.user.repository.RoleRepository;
import com.maghrebia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final SecureRandom random = new SecureRandom();
    private final RoleRepository roleRepository;
    public static String generatePassword() {
        int firstThree = random.nextInt(900) + 100;
        int lastFour = random.nextInt(9000) + 1000;
        return firstThree + "MA" + lastFour;
    }
    public boolean existEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    public User createUser(EmployeeRegistrationRequest employeeRegistrationRequest) {

        var userRole = roleRepository.findByName("admin")
                .orElseGet(() -> roleRepository.save(new Role("admin")));;
        User user = User.builder().email(employeeRegistrationRequest.getEmail())
                .firstname(employeeRegistrationRequest.getFirstname())
                .lastname(employeeRegistrationRequest.getLastname())
                .password(passwordEncoder.encode("aaa"))
                .roles(List.of(userRole))
                .accountLocked(false)
                .enabled(true)
                .build();
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("The email is already registered.");
        }
        return userRepository.save(user);
    }
}
