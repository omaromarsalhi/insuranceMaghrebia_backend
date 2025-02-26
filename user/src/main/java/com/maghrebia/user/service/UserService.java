package com.maghrebia.user.service;

import com.maghrebia.user.dto.request.ChangePasswordRequest;
import com.maghrebia.user.dto.request.EmployeeRegistrationRequest;
import com.maghrebia.user.dto.request.UpdateProfileRequest;
import com.maghrebia.user.entity.Role;
import com.maghrebia.user.entity.User;
import com.maghrebia.user.exception.*;
import com.maghrebia.user.repository.RoleRepository;
import com.maghrebia.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final AuthenticationService authenticationService;

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
                .orElseGet(() -> roleRepository.save(new Role("admin")));
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

    public User getUserProfile(String id) {
        return this.userRepository.findById(id).orElseThrow(() -> new EmailNotFoundException("The user does not exist."));
    }

    public User updateUserProfile(UpdateProfileRequest user, String id) throws MessagingException {
        User actualUser = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!actualUser.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new EmailAlreadyExistsException("The email is already registered.");
            } else {
                actualUser.setFirstname(user.getFirstName());
                actualUser.setLastname(user.getLastName());
                actualUser.setEmail(user.getEmail());
                actualUser.setAddress(user.getAddress());
                actualUser.setPhone(user.getPhone());
                actualUser.setDateOfBirth(user.getDateOfBirth());
                actualUser.setGender(user.getGender());
                actualUser.setEnabled(false);
                actualUser.setLastModifiedDate(LocalDateTime.now());
                userRepository.save(actualUser);
                authenticationService.sendValidationEmail(actualUser);
                throw new EmailChangeRequiresVerificationException("Email change requires verification");
            }
        }
        actualUser.setFirstname(user.getFirstName());
        actualUser.setLastname(user.getLastName());
        actualUser.setAddress(user.getAddress());
        actualUser.setPhone(user.getPhone());
        actualUser.setDateOfBirth(user.getDateOfBirth());
        actualUser.setGender(user.getGender());
        actualUser.setLastModifiedDate(LocalDateTime.now());
        return userRepository.save(actualUser);
    }

    public User changeProfilePassword(@Valid ChangePasswordRequest changePasswordRequest, String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new CurrentPasswordDoesNotMatchException("current password does not match to the account password");
        }
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new PasswordDoesNotMatchException("New password does not match confirm password.");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return user;
    }

    public List<User> getUsers(String id) {
        return userRepository.findAllExceptById(id);
    }

    public List<User> deleteUser(String id,String deleterId) {
        userRepository.deleteById(id);
        return userRepository.findAllExceptById(deleterId);
    }

    public List<User> banUserById(String id,String bannerId) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setAccountLocked(true);
        userRepository.save(user);
        return userRepository.findAllExceptById(bannerId);
    }

    public List<User> unBanUserById(String id, String unBannerId) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setAccountLocked(false);
        userRepository.save(user);
        return userRepository.findAllExceptById(unBannerId);
    }
}
