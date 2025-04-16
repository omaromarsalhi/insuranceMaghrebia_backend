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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final SecureRandom random = new SecureRandom();
    private final RoleRepository roleRepository;
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    public static String generatePassword() {
        int firstThree = random.nextInt(900) + 100;
        int lastFour = random.nextInt(9000) + 1000;
        return firstThree + "MA" + lastFour;
    }

    public List<User> createUser(EmployeeRegistrationRequest employeeRegistrationRequest, String creatorId) throws MessagingException {
        List<Role> listRole = new ArrayList<>();
        roleRepository.findAll().forEach(role -> {
            employeeRegistrationRequest.getRoles().forEach(role1 -> {
                if (role.getName().equals(role1.getName())) {
                    listRole.add(role);
                }
            });
        });
        String password = generatePassword();
        User user = User.builder().email(employeeRegistrationRequest.getEmail())
                .firstname(employeeRegistrationRequest.getFirstname())
                .lastname(employeeRegistrationRequest.getLastname())
                .password(passwordEncoder.encode(password))
                .roles(listRole)
                .accountLocked(false)
                .enabled(true)
                .canContinue(false)
                .build();
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("The email is already registered.");
        }
        sendLoginAccountEmail(user.getEmail(), password, user.fullName());
        userRepository.save(user);
        return userRepository.findAllExceptById(creatorId);
    }

    private void sendLoginAccountEmail(String email, String password, String username) throws MessagingException {
        emailService.sendLoginAccountEmail(email, username,
                "account-created",
                password,
                "Account Created");
    }

    public void createAdmin() {
        Role role = roleRepository.findByName("admin")
                .orElseGet(() -> roleRepository.save(new Role("admin")));
        User user = User.builder()
                .email("admin@admin.com")
                .firstname("admin")
                .lastname("admin")
                .password(passwordEncoder.encode("admin"))
                .roles(List.of(role))
                .accountLocked(false)
                .enabled(true)
                .canContinue(false)
                .build();
        userRepository.findByEmail("admin@admin.com").orElseGet(() -> userRepository.save(user));
    }

    public User getUserProfile(String id) {
        return this.userRepository.findById(id).orElseThrow(() -> new EmailNotFoundException("The user does not exist."));
    }

    public User updateProfile(UpdateProfileRequest user, String id) throws MessagingException {
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
                actualUser.setCanContinue(true);
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
        actualUser.setCanContinue(true);
        actualUser.setLastModifiedDate(LocalDateTime.now());
        return userRepository.save(actualUser);
    }

    public User changeProfilePassword(ChangePasswordRequest changePasswordRequest, String id) {
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

    public List<User> deleteUser(String id, String deleterId) {
        userRepository.deleteById(id);
        return userRepository.findAllExceptById(deleterId);
    }

    public List<User> banUserById(String id, String bannerId) {
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

    public void updateUserRoles(List<Role> roles, String id) {
        User actualUser = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Role> listRole = addRolesToList(roles);
        actualUser.setRoles(listRole);
        userRepository.save(actualUser);
    }

    public List<Role> getUserRoles(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getRoles();
    }

    private List<Role> addRolesToList(List<Role> roles) {
        List<Role> listRole = new ArrayList<>();
        roleRepository.findAll().forEach(role -> {
            roles.forEach(role1 -> {
                if (role.getName().equals(role1.getName())) {
                    listRole.add(role);
                }
            });
        });
        return listRole;
    }

    public Map<String, Boolean> checkUserCanContinue(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Map<String, Boolean> canContinue = new HashMap<>();
        canContinue.put(
                "canContinue", user.isCanContinue()
        );
        return canContinue;
    }
}
